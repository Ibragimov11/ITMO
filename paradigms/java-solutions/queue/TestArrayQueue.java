package queue;

public class TestArrayQueue {
    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        System.out.println(queue.size());
        queue.enqueue("рыба");
        queue.enqueue(21);
        queue.enqueue("Hello");
        queue.enqueue(222);
        System.out.println(queue.size());
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
        System.out.println(queue.size());
        System.out.println(queue.element());
        System.out.println(queue.isEmpty());
        queue.clear();
        System.out.println(queue.isEmpty());
        for (int i = 1; i <= 10; i++) {
            queue.enqueue(i);
        }
        for (int i = 1; i <= 30; i++) {
            queue.enqueue(i);
        }
        System.out.println(queue.get(25));
        for (int i = 1; i <= 5; i++) {
            queue.dequeue();
        }
        System.out.println(queue.get(25));
        queue.set(25, "привет");
        System.out.println(queue.get(25));
    }
}
