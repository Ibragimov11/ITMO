package PCMS;

import java.util.*;

public class M {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();

        for (int z = 0; z < t; z++) {
            int n = in.nextInt();
            Integer[] a = new Integer[n];
            for (int j = 0; j < n; j++) {
                a[j] = in.nextInt();
            }

            int count = 0;
            for (int i = 0; i < n - 1; i++) {
                Map<Integer, Integer> c = new HashMap<>();
                for (int j = n - 1; j > i; j--) {
                    Integer key = 2 * a[j] - a[i];
                    if (c.get(key) != null) {
                        count += c.get(key);
                    }
                    if (c.containsKey(a[j])) {
                        c.put(a[j], c.get(a[j]) + 1);
                    } else {
                        c.put(a[j], 1);
                    }
                }
            }

            System.out.println(count);

        }

    }
}
