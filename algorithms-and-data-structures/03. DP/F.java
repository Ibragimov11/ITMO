import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class F {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int k1 = 0;
        int k2 = 0;
        int[] costs = new int[n + 1];
        for (int i = 1; i < n + 1; i++) {
            costs[i] = in.nextInt();
        }
        int[][][] a = new int[n + 1][n + 1][2];
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < i; j++) {
                a[i][j][0] = (int) 1e8;
            }
        }
        for (int j = 1; j < n + 1; j++) {
            for (int i = 0; i <= j; i++) {
                if (i == 0) {
                    if (costs[j] > 100) {
                        a[i][j][0] = a[i + 1][j - 1][0];
                        if (a[i + 1][j - 1][0] <= 1e6) {
                            a[i][j][1] = a[i + 1][j - 1][1] + 1;
                        }
                    } else {
                        a[i][j][0] = Math.min(a[i + 1][j - 1][0], a[i][j - 1][0] + costs[j]);
                        if (a[i + 1][j - 1][0] < a[i][j - 1][0] + costs[j]) {
                            a[i][j][1] = a[i + 1][j - 1][1] + 1;
                        } else {
                            a[i][j][1] = a[i][j - 1][1];
                        }
                    }
                    continue;
                }
                if (i == j) {
                    if (costs[j] > 100 && a[i - 1][j - 1][0] < 1e6) {
                        a[i][j][0] = a[i - 1][j - 1][0] + costs[j];
                        a[i][j][1] = a[i - 1][j - 1][1];
                    } else {
                        a[i][j][0] = (int) 1e8;
                        a[i][j][1] = 0;
                    }
                } else {
                    if (costs[j] > 100) {
                        a[i][j][0] = Math.min(a[i + 1][j - 1][0], a[i - 1][j - 1][0] + costs[j]);
                        if (a[i + 1][j - 1][0] < a[i - 1][j - 1][0] + costs[j]) {
                            a[i][j][1] = a[i + 1][j - 1][1] + 1;
                        } else {
                            a[i][j][1] = a[i - 1][j - 1][1];
                        }
                    } else {
                        a[i][j][0] = Math.min(a[i + 1][j - 1][0], a[i][j - 1][0] + costs[j]);
                        if (a[i + 1][j - 1][0] < a[i][j - 1][0] + costs[j]) {
                            a[i][j][1] = a[i + 1][j - 1][1] + 1;
                        } else {
                            a[i][j][1] = a[i][j - 1][1];
                        }
                    }
                }
            }
        }

        int p = 0;
        int mn = (int) 1e5;
        for (int i = n; i >= 0; i--) {
            if (a[i][n][0] < mn) {
                p = i;
                mn = a[i][n][0];
                k1 = i;
                k2 = a[i][n][1];
            }
        }
        System.out.println(mn);
        System.out.println(k1 + " " + k2);
        ArrayList<Integer> w = new ArrayList<>();
        int q = n;
        while (q > 0) {
            if (p < n - 1 && a[p + 1][q - 1][0] == a[p][q][0]) {
                w.add(q);
                p++;
            } else {
                if (costs[q] > 100) {
                    p--;
                }
            }
            q--;
        }
        Collections.reverse(w);
        for (int x : w) {
            System.out.println(x);
        }
    }
}