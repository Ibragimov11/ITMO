package info.kgeorgiy.ja.ibragimov.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

/**
 * @author Said Ibragimov on 13.04.2022 22:42
 */
public class ParallelMapperImpl implements ParallelMapper {
    private final List<Thread> threads;
    private final Deque<RunnableWithId> tasks;
    private static final int QUEUE_SIZE_LIMIT = 500;
    private final Map<Integer, Integer> numberOfRemainingTasks;
    private int k = 0;

    public ParallelMapperImpl(final int threadNumber) {
        if (threadNumber < 1) {
            throw new IllegalArgumentException("The number of threads is not allowed to be less than 1");
        }

        threads = new ArrayList<>();
        tasks = new ArrayDeque<>();
        numberOfRemainingTasks = new HashMap<>();

        final Runnable runnable = () -> {
            try {
                while (!Thread.interrupted()) {
                    RunnableWithId task;
                    synchronized (tasks) {
                        while (tasks.isEmpty()) {
                            tasks.wait();
                        }
                        task = tasks.pollFirst();
                        tasks.notifyAll();
                    }
                    task.runnable.run();

                    synchronized (numberOfRemainingTasks) {
                        numberOfRemainingTasks.merge(task.id, -1, Integer::sum);
                        numberOfRemainingTasks.notifyAll();
                    }
                }
            } catch (InterruptedException ignored) {
            } finally {
                Thread.currentThread().interrupt();
            }
        };

        for (int i = 0; i < threadNumber; i++) {
            Thread t = new Thread(runnable);
            threads.add(t);
            t.start();
        }
    }

    @Override
    public <T, R> List<R> map(final Function<? super T, ? extends R> f, final List<? extends T> args) throws InterruptedException {
        final int size = args.size();
        if (size == 0) {
            return Collections.emptyList();
        }

        k++;
        numberOfRemainingTasks.put(k, size);
        final List<R> result = new ArrayList<>(Collections.nCopies(size, null));

        for (int i = 0; i < size; i++) {
            int finalI = i;
            synchronized (tasks) {
                final Runnable task = () -> result.set(finalI, f.apply(args.get(finalI)));
                while (tasks.size() == QUEUE_SIZE_LIMIT) {
                    tasks.wait();
                }

                tasks.add(new RunnableWithId(k, task));
                tasks.notifyAll();
            }
        }

        synchronized (numberOfRemainingTasks) {
            while (numberOfRemainingTasks.get(k) != 0) {
                numberOfRemainingTasks.wait();
            }
        }

        return result;
    }

    @Override
    public void close() {
        for (final Thread thread : threads) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        }
    }

    private record RunnableWithId(Integer id, Runnable runnable) {}
}
