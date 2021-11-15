package PCMS;

import java.util.*;

public class H {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int mx = Integer.MIN_VALUE;
        int[] a = new int[n];
        int[] p = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = in.nextInt();
            mx = Math.max(a[i], mx);
            if (i == 0) {
                p[i] = a[i];
            } else {
                p[i] = a[i] + p[i - 1];
            }
        }

        int[] f = new int[p[n - 1]];
        int r = 0;
        int sum1 = 0;
        for (int i = 0; i < n; i++) {
            sum1 += a[i];
            while (r < sum1) {
                f[r] = i;
                r++;
            }
        }

        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        int q = in.nextInt();
        for (int i = 0; i < q; i++) {
            int t = in.nextInt();
            if (t < mx) {
                System.out.println("Impossible");
            } else {
                if (map.containsKey(t)) {
                    System.out.println(map.get(t));
                } else {
                    int b = -1;
                    int count = 0;
                    while (b + t + 1 <= p[n - 1] - 1) {
                        int w = f[b + t + 1] - 1;
                        count++;
                        b = p[w] - 1;
                    }
                    count++;
                    map.put(t, count);
                    System.out.println(count);
                }
            }
        }

    }
}
