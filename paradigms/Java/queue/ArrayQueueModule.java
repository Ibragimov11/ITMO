package queue;

import java.util.Arrays;

/*
    Model:
        [..., a1, a2, ..., an, ...]
        n -- размер очереди

    Inv:
        n >= 0
        forall i = 1..n: a[i] != null

    Imm:
        n = n' && forall i = 1..n: a[i] == a'[i]
 */

public class ArrayQueueModule {
    private static int tail = -1; // index of last element in queue
    private static int head = 0; // index of first element in queue
    private static int size;
    private static Object[] elements = new Object[8];

    // Pred: e != null
    // Post: n = n' + 1 && a[n] = e && forall i = 1..n: a[i] == a'[i]
    public static void enqueue(Object element) {
        assert element != null;

        size++;
        ensureCapacity(size);
        elements[(++tail) % elements.length] = element;
    }

    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            Object[] array = new Object[elements.length];
            System.arraycopy(elements, head, array, 0, elements.length - head);
            System.arraycopy(elements, 0, array, elements.length - head, head);
            elements = Arrays.copyOf(array, 2 * capacity);
            head = 0;
            tail = capacity - 2;
        }
    }

    // Pred: n > 0
    // Post: R == a[0] && Imm
    public static Object element() {
        return elements[head];
    }

    // Pred: n > 0
    // Post: n == n' - 1 && forall i = 1..n: a[i] == a'[i] && R == a[n']
    public static Object dequeue() {
        Object ans = elements[head];
        size--;
        if (size == 0) {
            tail = -1;
            head = 0;
        } else {
            head = (head + 1) % elements.length;
        }
        return ans;
    }

    // Pred: True
    // Post: R == n && Imm
    public static int size() {
        return size;
    }

    // Pred: True
    // Post: R == (n == 0) && Imm
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pred: True
    // Post: n == 0
    public static void clear() {
        size = 0;
        tail = -1;
        head = 0;
    }

    // Pred: n > 0 && 0 <= j <= n - 1
    // Post: R == a[j] && Imm
    public static Object get(int j) {
        return elements[(head + j) % elements.length];
    }

    // Pred: n > 0 && 0 <= j <= n - 1 && e != null
    // Post: a[j] == e && n == n' && forall i = 0..j-1, j+1..n: a[i] == a'[i]
    public static void set(int j, Object element) {
        elements[(head + j) % elements.length] = element;
    }

}
