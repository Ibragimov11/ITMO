package info.kgeorgiy.ja.ibragimov.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Said Ibragimov on 01.05.2022 03:00
 */
public class WebCrawler implements Crawler {
    private final Downloader downloader;
    private final ExecutorService downloadersThreadPool;
    private final ExecutorService extractorsThreadPool;
    private final int perHost;
    private final Map<String, HostWorker> hosts;

    public WebCrawler(final Downloader downloader, final int downloaders, final int extractors, final int perHost) {
        this.downloader = downloader;
        this.downloadersThreadPool = Executors.newFixedThreadPool(downloaders);
        this.extractorsThreadPool = Executors.newFixedThreadPool(extractors);
        this.perHost = perHost;
        this.hosts = new ConcurrentHashMap<>();
    }

    private class HostWorker {
        private final Queue<Runnable> hostQueue = new ConcurrentLinkedQueue<>();
        private final int limit = perHost;
        private int working;

        public synchronized void submit(final Runnable task) {
            if (working < limit) {
                working++;
                downloadersThreadPool.submit(task);
            } else {
                hostQueue.add(task);
            }
        }

        public synchronized void executeWaiting() {
            final Runnable task = hostQueue.poll();

            if (task != null) {
                downloadersThreadPool.submit(task);
            } else {
                working--;
            }
        }
    }

    private record UrlWithDepth(String url, int depth) {
    }

    @Override
    public Result download(final String url, final int depth) {
        final Set<String> visited = ConcurrentHashMap.newKeySet();
        final Set<String> downloaded = ConcurrentHashMap.newKeySet();
        final Map<String, IOException> errors = new ConcurrentHashMap<>();

        final Phaser phaser = new Phaser(1);
        final ConcurrentLinkedQueue<UrlWithDepth> queue = new ConcurrentLinkedQueue<>();
        final Set<UrlWithDepth> linksFromCurrentStep = ConcurrentHashMap.newKeySet();
        queue.add(new UrlWithDepth(url, depth));

        while (!queue.isEmpty()) {
            while (!queue.isEmpty()) {
                final UrlWithDepth current = queue.poll();
                visited.add(current.url);

                try {
                    final String hostName = URLUtils.getHost(current.url);
                    final HostWorker hostWorker = hosts.computeIfAbsent(hostName, h -> new HostWorker());

                    phaser.register();
                    hostWorker.submit(() -> {
                        try {
                            Document document = downloader.download(current.url);
                            downloaded.add(current.url);

                            if (current.depth > 1) {
                                phaser.register();
                                extractorsThreadPool.submit(() -> {
                                    try {
                                        document.extractLinks().forEach(link ->
                                                linksFromCurrentStep.add(new UrlWithDepth(link, current.depth - 1)));
                                    } catch (IOException e) {
                                        System.err.println("failed to load links from " + current.url);
                                    } finally {
                                        phaser.arriveAndDeregister();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            errors.put(current.url, e);
                        } finally {
                            phaser.arriveAndDeregister();
                            hostWorker.executeWaiting();
                        }
                    });
                } catch (MalformedURLException e) {
                    errors.put(current.url, e);
                }
            }

            phaser.arriveAndAwaitAdvance();

            queue.addAll(linksFromCurrentStep.stream()
                    .filter(e -> !visited.contains(e.url))
                    .toList());
            linksFromCurrentStep.clear();
        }

        return new Result(List.copyOf(downloaded), errors);
    }

    @Override
    public void close() {
        closeExecutorServices(downloadersThreadPool, extractorsThreadPool);
    }

    private void closeExecutorServices(final ExecutorService... services) {
        for (final var executorService : services) {
            executorService.shutdown();
        }

        for (final var executorService : services) {
            try {
                if (!executorService.awaitTermination(15, TimeUnit.MINUTES)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    public static void main(String[] args) {
        if (args == null || Arrays.stream(args).anyMatch(Objects::isNull)) {
            System.err.println("Null is not allowed in args");
            return;
        }

        if (!(1 <= args.length && args.length <= 5)) {
            System.err.println("Incorrect number of args: expected 1-5");
            return;
        }

        if (!Arrays.stream(args).skip(1).allMatch(s -> s.matches("[1-9]+\\d*"))) {
            System.err.println("Invalid args: starting from the second its must be a positive number");
            return;
        }

        try (Crawler crawler = new WebCrawler(
                new CachingDownloader(),
                getArgument(args, 2, Integer.MAX_VALUE),
                getArgument(args, 3, Integer.MAX_VALUE),
                getArgument(args, 4, Integer.MAX_VALUE)
        )) {
            crawler.download(args[0], getArgument(args, 1, 1));
        } catch (NumberFormatException e) {
            System.err.println("Invalid number in args: it's absolute value is too large");
        } catch (IOException e) {
            System.err.println("Initialization error: failed to create Downloader");
        }
    }

    private static int getArgument(String[] args, int index, int defaultValue) {
        return index >= args.length ? defaultValue : Integer.parseInt(args[index]);
    }
}
