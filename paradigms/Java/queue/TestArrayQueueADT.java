package queue;

public class TestArrayQueueADT {
    public static void main(String[] args) {
        ArrayQueueADT queue = new ArrayQueueADT();
        System.out.println(ArrayQueueADT.size(queue));
        ArrayQueueADT.enqueue(queue, "рыба");
        ArrayQueueADT.enqueue(queue, 21);
        ArrayQueueADT.enqueue(queue, "Hello");
        ArrayQueueADT.enqueue(queue, 222);
        System.out.println(ArrayQueueADT.size(queue));
        System.out.println(ArrayQueueADT.dequeue(queue));
        System.out.println(ArrayQueueADT.dequeue(queue));
        System.out.println(ArrayQueueADT.size(queue));
        System.out.println(ArrayQueueADT.element(queue));
        System.out.println(ArrayQueueADT.isEmpty(queue));
        ArrayQueueADT.clear(queue);
        System.out.println(ArrayQueueADT.isEmpty(queue));
        for (int i = 1; i <= 10; i++) {
            ArrayQueueADT.enqueue(queue, i);
        }
        for (int i = 1; i <= 30; i++) {
            ArrayQueueADT.enqueue(queue, i);
        }
        System.out.println(ArrayQueueADT.get(queue, 25));
        for (int i = 1; i <= 5; i++) {
            ArrayQueueADT.dequeue(queue);
        }
        System.out.println(ArrayQueueADT.get(queue, 25));
        ArrayQueueADT.set(queue, 25, "привет");
        System.out.println(ArrayQueueADT.get(queue, 25));
    }
}
