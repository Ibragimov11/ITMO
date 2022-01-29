package queue;

public class LinkedQueue extends AbstractQueue implements Queue{
    private Node head;
    private Node tail;

    protected void enqueueImpl(Object element) {
        Node newNode = new Node(element);
        if (size == 1) {
            tail = newNode;
            head = tail;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }

    protected Object elementImpl() {
        return head.value;
    }

    protected void remove() {
        head = head.next;
    }

    protected void clearImpl() {
        head = null;
        tail = null;
    }

    protected Object getImpl(int i) {
        Node tmp = head;
        for (int j = 0; j < i; j++) {
            tmp = tmp.next;
        }
        return tmp.value;
    }

    protected void setImpl(int i, Object value) {
        Node tmp = head;
        for (int j = 0; j < i; j++) {
            tmp = tmp.next;
        }
        tmp.value = value;
    }

    protected Object[] toArrayImpl() {
        Object[] result = new Object[size];
        Node tmp = head;
        for (int j = 0; j < size; j++) {
            result[j] = tmp.value;
            tmp = tmp.next;
        }
        return result;
    }

    private static class Node {
        private Object value;
        private Node next;

        public Node(Object value) {
            assert value != null;

            this.value = value;
        }
    }
}
