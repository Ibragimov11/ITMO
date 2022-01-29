import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class A12 {

    static int n, k;
    static FileWriter out;
    static int[][] a;

    private static void gen(int q, int p) throws IOException {
        if (q == k && p == n) {
            for (int i = 0; i < k; i++) {
                for (int j = 1; j <= a[i][0]; ++j) {
                    out.write(a[i][j] + " ");
                }
                out.write('\n');
            }
            out.write('\n');
        } else {
            if (k - q < n - p) {
                for (int i = 0; i < q; i++) {
                    a[i][0]++;
                    a[i][a[i][0]] = p + 1;
                    gen(q, p + 1);
                    a[i][0]--;
                }
            }
            if (q < k) {
                a[q][0]++;
                a[q][1] = p + 1;
                gen(q + 1, p + 1);
                a[q][0]--;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("part2sets.in"));
        out = new FileWriter(new File("part2sets.out"));
        n = in.nextInt();
        k = in.nextInt();
        a = new int[k][n + 1];
        gen(0, 0);
        out.close();
    }
}
