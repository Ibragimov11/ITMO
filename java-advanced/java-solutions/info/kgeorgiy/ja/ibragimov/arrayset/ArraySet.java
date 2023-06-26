package info.kgeorgiy.ja.ibragimov.arrayset;

import java.util.*;

public class ArraySet<E> extends AbstractSet<E> implements NavigableSet<E> {
    private final Comparator<? super E> comparator;
    private final MyList<E> elements;

    public ArraySet(final Collection<? extends E> collection, final Comparator<? super E> comparator) {
        final NavigableSet<E> set = new TreeSet<>(comparator);
        set.addAll(collection);

        this.elements = new MyList<>(set);
        this.comparator = comparator;
    }

    public ArraySet(final Comparator<? super E> comparator) {
        this(List.of(), comparator);
    }

    public ArraySet(final Collection<? extends E> collection) {
        this(collection, null);
    }

    public ArraySet() {
        this(List.of(), null);
    }

    private ArraySet(final MyList<E> myList, final Comparator<? super E> comparator, final boolean reversed) {
        this.elements = new MyList<>(myList, reversed);
        this.comparator = comparator;
    }

    @Override
    public Iterator<E> iterator() {
        return elements.iterator();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(final Object o) {
        return binSearch((E) o) >= 0;
    }

    @Override
    public E lower(final E e) {
        return find(e, -1, -1);
    }

    @Override
    public E floor(final E e) {
        return find(e, 0, -1);
    }

    @Override
    public E ceiling(final E e) {
        return find(e, 0, 0);
    }

    @Override
    public E higher(final E e) {
        return find(e, 1, 0);
    }

    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public NavigableSet<E> descendingSet() {
        return new ArraySet<>(elements, Collections.reverseOrder(comparator), true);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return descendingSet().iterator();
    }

    @Override
    public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        if (compare(toElement, fromElement) < 0) {
            throw new IllegalArgumentException("Illegal arguments");
        }

        final var fromIndex = binSearch(fromElement);
        final var toIndex = binSearch(toElement);

        return subList(fromIndex, fromInclusive, toIndex, toInclusive);
    }

    @Override
    public NavigableSet<E> headSet(final E toElement, final boolean toInclusive) {
        final var toIndex = binSearch(toElement);

        return subList(0, true, toIndex, toInclusive);
    }

    @Override
    public NavigableSet<E> tailSet(final E fromElement, final boolean fromInclusive) {
        final var fromIndex = binSearch(fromElement);

        return subList(fromIndex, fromInclusive, size() - 1, true);
    }

    @Override
    public SortedSet<E> subSet(final E fromElement, final E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(final E toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(final E fromElement) {
        return tailSet(fromElement, true);
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException("No such element");
        }

        return elementByIndex(0);
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException("No such element");
        }

        return elementByIndex(size() - 1);
    }

    private E elementByIndex(final int i) {
        return elements.get(i);
    }

    private int binSearch(final E e) {
        return Collections.binarySearch(elements, e, comparator);
    }

    @SuppressWarnings("unchecked")
    private int compare(final E first, final E second) {
        if (comparator == null) {
            final Comparable<E> comparableFirst = (Comparable<E>) first;
            return comparableFirst.compareTo(second);
        } else {
            return comparator.compare(first, second);
        }
    }

    private E find(final E e, final int firstIndexDiff, final int secondIndexDiff) {
        if (isEmpty()) {
            return null;
        }

        var i = binSearch(e);
        i = 0 <= i ? i + firstIndexDiff : -1 - i + secondIndexDiff;

        return 0 <= i && i < size() ? elementByIndex(i) : null;

    }

    private ArraySet<E> subList(int fromIndex, final boolean fromInclusive, int toIndex, final boolean toInclusive) {
        if (isEmpty()) {
            return this;
        }

        if (fromIndex < 0) {
            fromIndex = -1 - fromIndex;
        } else if (!fromInclusive) {
            fromIndex++;
        }

        if (toIndex < 0) {
            toIndex = -1 - toIndex;
        } else if (toInclusive) {
            toIndex++;
        }

        return new ArraySet<>(elements.subList(fromIndex, toIndex), comparator, false);
    }

    private static class MyList<T> extends AbstractList<T> implements RandomAccess {
        private final List<T> list;
        private final boolean reversed;

        private MyList(final Collection<? extends T> collection) {
            this.list = List.copyOf(collection);
            this.reversed = false;
        }

        private MyList(final MyList<T> myList, final boolean reversed) {
            this.list = myList.list;
            this.reversed = myList.reversed ^ reversed;
        }

        private MyList(final boolean reversed, final List<T> list) {
            this.list = list;
            this.reversed = reversed;
        }

        @Override
        public T get(final int index) {
            return reversed ? list.get(size() - 1 - index) : list.get(index);
        }

        @Override
        public int size() {
            return list.size();
        }

        @Override
        public MyList<T> subList(final int fromIndex, final int toIndex) {
            if (reversed) {
                return new MyList<>(true, list.subList(size() - toIndex, size() - fromIndex));
            } else {
                return new MyList<>(false, list.subList(fromIndex, toIndex));
            }
        }
    }
}
