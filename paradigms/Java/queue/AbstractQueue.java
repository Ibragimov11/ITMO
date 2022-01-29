package queue;

public abstract class AbstractQueue {
    protected int size;

    public void enqueue(Object element) {
        assert element != null;

        size++;
        enqueueImpl(element);
    }

    protected abstract void enqueueImpl(Object element);

    public Object element() {
        assert size > 0;

        return elementImpl();
    }

    protected abstract Object elementImpl();

    public Object dequeue() {
        assert size > 0;

        Object result = element();
        size--;
        remove();
        return result;
    }

    protected abstract void remove();

    public Object get(int i) {
        return getImpl(i);
    }

    protected abstract Object getImpl(int i);

    public void set(int i, Object value) {
        setImpl(i, value);
    }

    protected abstract void setImpl(int i, Object value);

    public Object[] toArray() {
        return toArrayImpl();
    }

    protected abstract Object[] toArrayImpl();

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;

        clearImpl();
    }

    protected abstract void clearImpl();
}
