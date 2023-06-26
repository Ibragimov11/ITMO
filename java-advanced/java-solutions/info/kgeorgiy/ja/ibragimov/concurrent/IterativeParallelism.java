package info.kgeorgiy.ja.ibragimov.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Said Ibragimov on 30.03.2022 00:55
 */
public class IterativeParallelism implements ListIP {
    final private ParallelMapper parallelMapper;

    public IterativeParallelism() {
        this(null);
    }

    public IterativeParallelism(ParallelMapper parallelMapper) {
        this.parallelMapper = parallelMapper;
    }

    @Override
    public <T> T maximum(final int threads, final List<? extends T> values, final Comparator<? super T> comparator) throws InterruptedException {
        return doParallel(threads, values, stream -> stream.max(comparator).orElse(null),
                stream -> stream.filter(Objects::nonNull).max(comparator).orElse(null));
    }

    @Override
    public <T> T minimum(final int threads, final List<? extends T> values, final Comparator<? super T> comparator) throws InterruptedException {
        return doParallel(threads, values, stream -> stream.min(comparator).orElse(null),
                stream -> stream.filter(Objects::nonNull).min(comparator).orElse(null));
    }

    @Override
    public <T> boolean all(final int threads, final List<? extends T> values, final Predicate<? super T> predicate) throws InterruptedException {
        return doParallel(threads, values, stream -> stream.allMatch(predicate), stream -> stream.allMatch(b -> b));
    }

    @Override
    public <T> boolean any(final int threads, final List<? extends T> values, final Predicate<? super T> predicate) throws InterruptedException {
        return doParallel(threads, values, stream -> stream.anyMatch(predicate), stream -> stream.anyMatch(b -> b));
    }

    @Override
    public String join(final int threads, final List<?> values) throws InterruptedException {
        return doParallel(threads, values, stream -> stream.map(Objects::toString).collect(Collectors.joining()),
                stream -> stream.collect(Collectors.joining()));
    }

    @Override
    public <T> List<T> filter(final int threads, final List<? extends T> values, final Predicate<? super T> predicate) throws InterruptedException {
        return doParallel(threads, values, stream -> stream.filter(predicate).collect(Collectors.toList()),
                listStream -> listStream.flatMap(Collection::stream).collect(Collectors.toList()));
    }

    @Override
    public <T, U> List<U> map(final int threads, final List<? extends T> values, final Function<? super T, ? extends U> f) throws InterruptedException {
        return doParallel(threads, values, stream -> stream.map(f).collect(Collectors.toList()),
                listStream -> listStream.flatMap(Collection::stream).collect(Collectors.toList()));
    }

    private <T, U> U doParallel(
            final int threads,
            final List<? extends T> values,
            final Function<Stream<? extends T>, ? extends U> function1,
            final Function<Stream<? extends U>, ? extends U> function2
    ) throws InterruptedException {
        final List<? extends List<? extends T>> lists = splitList(values, threads);
        final List<U> results;

        if (parallelMapper != null) {
            results = parallelMapper.map(function1, lists.stream().map(Collection::stream).collect(Collectors.toList()));
        } else {
            results = new ArrayList<>(Collections.nCopies(threads, null));
            final List<Thread> threadList = new ArrayList<>();

            for (int i = 0; i < threads; i++) {
                final int finalI = i;
                final Thread t = new Thread(() -> results.set(finalI, function1.apply(lists.get(finalI).stream())));
                t.start();
                threadList.add(t);
            }

            for (final Thread thread : threadList) {
                thread.join();
            }
        }

        return function2.apply(results.stream());
    }

    private <T> List<List<T>> splitList(final List<T> list, final int threads) {
        final int listSize = list.size();
        final int partSize = listSize / threads;
        int rest = listSize % threads;
        final List<List<T>> splitedLists = new ArrayList<>();
        int border = 0;

        for (int i = 0; i < threads; i++) {
            final int rightBorder = border + partSize + (rest-- > 0 ? 1 : 0);
            splitedLists.add(list.subList(border, rightBorder));
            border = rightBorder;
        }

        return splitedLists;
    }
}
