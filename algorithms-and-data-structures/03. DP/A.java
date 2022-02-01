import java.util.ArrayList;
import java.util.Scanner;

public class A {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int k = in.nextInt();
        int[] costs = new int[n];
        int[] start_costs = new int[n];
        for (int i = 1; i < n - 1; i++) {
            costs[i] = in.nextInt();
            start_costs[i] = costs[i];
        }
        start_costs[0] = 0; start_costs[n - 1] = 0;
        ArrayList<Integer> queue = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                queue.add(costs[i]);
            } else {
                costs[i] += queue.get(0);
                if (i >= k && costs[i - k] == queue.get(0)) {
                    queue.remove(0);
                }
                if (queue.size() > 0) {
                    while (queue.get(queue.size() - 1) < costs[i]) {
                        queue.remove(queue.size() - 1);
                        if (queue.size() == 0) {
                            break;
                        }
                    }
                }
                queue.add(costs[i]);
            }
        }
        int value = costs[n - 1];
        System.out.println(value);
        int j = n - 1;
        ArrayList<Integer> way = new ArrayList<>();
        while (j > 0) {
            if (costs[j] == value) {
                way.add(j + 1);
                value -= start_costs[j];
            }
            j--;
        }
        System.out.println(way.size());
        System.out.print(1 + " ");
        for (int i = way.size() - 1; i >= 0; i--) {
            System.out.print(way.get(i) + " ");
        }
    }
}
