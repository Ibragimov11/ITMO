import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class C {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[][] a = new int[3][n];
        for (int i = 0; i < n; i++) {
            a[0][i] = in.nextInt();
        }
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                a[1][i] = -1;
                a[2][i] = 1;
            } else {
                int v = -1;
                int mx = 0;
                for (int j = 0; j < i; j++) {
                    if (a[0][j] < a[0][i] && a[2][j] > mx) {
                        v = j;
                        mx = a[2][j];
                    }
                }
                a[1][i] = v;
                a[2][i] = mx + 1;
            }
        }
        int u = 0;
        for (int i = 0; i < n; i++) {
            if (a[2][i] > a[2][u]) {
                u = i;
            }
        }
        System.out.println(a[2][u]);
        ArrayList<Integer> b = new ArrayList<>();
        while (u != -1) {
            b.add(a[0][u]);
            u = a[1][u];
        }
        Collections.reverse(b);
        for (int x : b) {
            System.out.print(x + " ");
        }
    }
}
