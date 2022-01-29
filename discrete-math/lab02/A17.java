import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class A17 {

    static int n;
    static long k;
    static long[][] d;

    private static String get_sequence(int n, long k) {
        int depth = 0;
        String s = "";
        for (int i = 0; i < 2 * n; i++) {
            if (d[2 * n - (i + 1)][depth + 1] >= k) {
                s += "(";
                depth++;
            } else {
                k -= d[2 * n - (i + 1)][depth + 1];
                s += ")";
                depth--;
            }
        }
        return s;
    }

    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("num2brackets.in"));
        FileWriter out = new FileWriter(new File("num2brackets.out"));
        n = in.nextInt();
        k = in.nextLong();
        d = new long[2 * n + 1][2 * n + 1];
        for (int i = 0; i < 2 * n + 1; i++) {
            for (int j = 0; j < 2 * n + 1; j++) {
                if (i == j) {
                    d[i][j] = 1;
                } else if (j > i) {
                    d[i][j] = 0;
                } else if (j == 0) {
                    d[i][j] = d[i-1][j+1];
                } else {
                    d[i][j] = d[i-1][j+1] + d[i-1][j-1];
                }
            }
        }
        String s = get_sequence(n, k + 1);
        out.write(s);
        out.close();
    }
}
