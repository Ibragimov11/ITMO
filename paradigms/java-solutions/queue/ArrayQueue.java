package queue;

public class ArrayQueue extends AbstractQueue implements Queue {
    private int tail = -1; // index of last element in queue
    private int head = 0; // index of first element in queue
    private Object[] elements = new Object[8];

    protected void enqueueImpl(Object element) {
        ensureCapacity(size);
        tail = (tail + 1) % elements.length;
        elements[tail] = element;
    }

    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            elements = makeSAC(elements.length * 2, elements.length);
            head = 0;
            tail = capacity - 2;
        }
    }

    protected Object elementImpl() {
        return elements[head];
    }

    protected void remove() {
        elements[head] = null;
        if (size == 0) {
            tail = -1;
            head = 0;
        } else {
            head = (head + 1) % elements.length;
        }
    }

    protected void clearImpl() {
        tail = -1;
        head = 0;
    }

    protected Object getImpl(int j) {
        return elements[(head + j) % elements.length];
    }

    protected void setImpl(int j, Object element) {
        elements[(head + j) % elements.length] = element;
    }

    protected Object[] toArrayImpl() {
        return makeSAC(size, size);
    }

    private Object[] makeSAC(int size, int count) {
        Object[] array = new Object[size];
        if (head <= tail) {
            System.arraycopy(elements, head, array, 0, count);
        } else {
            System.arraycopy(elements, head, array, 0, (elements.length - head) % elements.length);
            System.arraycopy(elements, 0, array, (elements.length - head) % elements.length, (tail + 1) % elements.length);
        }
        return array;
    }

//    public ArrayQueue makeCopy() {
//        final ArrayQueue copy = new ArrayQueue();
//        copy.size = size;
//        copy.elements = Arrays.copyOf(elements, size);
//        return copy;
//    }

}
