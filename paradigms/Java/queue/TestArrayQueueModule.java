package queue;

public class TestArrayQueueModule {
    public static void main(String[] args) {
        System.out.println(ArrayQueueModule.size());
        ArrayQueueModule.enqueue("рыба");
        ArrayQueueModule.enqueue(21);
        ArrayQueueModule.enqueue("Hello");
        ArrayQueueModule.enqueue(222);
        System.out.println(ArrayQueueModule.size());
        System.out.println(ArrayQueueModule.dequeue());
        System.out.println(ArrayQueueModule.dequeue());
        System.out.println(ArrayQueueModule.size());
        System.out.println(ArrayQueueModule.element());
        System.out.println(ArrayQueueModule.isEmpty());
        ArrayQueueModule.clear();
        System.out.println(ArrayQueueModule.isEmpty());
        for (int i = 1; i <= 10; i++) {
            ArrayQueueModule.enqueue(i);
        }
        for (int i = 1; i <= 30; i++) {
            ArrayQueueModule.enqueue(i);
        }
        System.out.println(ArrayQueueModule.get(25));
        for (int i = 1; i <= 5; i++) {
            ArrayQueueModule.dequeue();
        }
        System.out.println(ArrayQueueModule.get(25));
        ArrayQueueModule.set(25, "привет");
        System.out.println(ArrayQueueModule.get(25));
    }
}
