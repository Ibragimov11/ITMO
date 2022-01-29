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

public class ArrayQueueADT {
    private int tail = -1; // index of last element in queue
    private int head = 0; // index of first element in queue
    private int size;
    private Object[] elements = new Object[8];

    // Pred: e != null && queue != null
    // Post: n = n' + 1 && a[n] = e && forall i = 1..n: a[i] == a'[i]
    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;

        queue.size++;
        ensureCapacity(queue, queue.size);
        queue.elements[(++queue.tail) % queue.elements.length] = element;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity > queue.elements.length) {
            Object[] array = new Object[queue.elements.length];
            System.arraycopy(queue.elements, queue.head, array, 0, queue.elements.length - queue.head);
            System.arraycopy(queue.elements, 0, array, queue.elements.length - queue.head, queue.head);
            queue.elements = Arrays.copyOf(array, 2 * capacity);
            queue.head = 0;
            queue.tail = capacity - 2;
        }
    }

    // Pred: n > 0 && queue != null
    // Post: R == a[0] && Imm
    public static Object element(ArrayQueueADT queue) {
        return queue.elements[queue.head];
    }

    // Pred: n > 0 && queue != null
    // Post: n == n' - 1 && forall i = 1..n: a[i] == a'[i] && R == a[n']
    public static Object dequeue(ArrayQueueADT queue) {
        Object ans = queue.elements[queue.head];
        queue.size--;
        if (queue.size == 0) {
            queue.tail = -1;
            queue.head = 0;
        } else {
            queue.head = (queue.head + 1) % queue.elements.length;
        }
        return ans;
    }

    // Pred: queue != null
    // Post: R == n && Imm
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pred: queue != null
    // Post: R == (n == 0) && Imm
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pred: queue != null
    // Post: n == 0
    public static void clear(ArrayQueueADT queue) {
        queue.size = 0;
        queue.tail = -1;
        queue.head = 0;
    }

    // Pred: n > 0 && 0 <= j <= n - 1 && queue != null
    // Post: R == a[j] && Imm
    public static Object get(ArrayQueueADT queue, int j) {
        return queue.elements[(queue.head + j) % queue.elements.length];
    }

    // Pred: n > 0 && 0 <= j <= n - 1 && e != null && queue != null
    // Post: a[j] == e && n == n' && forall i = 0..j-1, j+1..n: a[i] == a'[i]
    public static void set(ArrayQueueADT queue, int j, Object element) {
        queue.elements[(queue.head + j) % queue.elements.length] = element;
    }

}
