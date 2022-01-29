import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class A18 {

    static int n;
    static long[][] d;

    private static long get_number(String s) {
        long num = 0;
        int depth = 0;
        for (int i = 0; i < 2 * n - 1; i++) {
            if (s.charAt(i) == '(') {
                depth++;
            } else {
                num += d[2 * n - i - 1][depth + 1];
                depth--;
            }
        }
        return num;
    }

    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("brackets2num.in"));
        FileWriter out = new FileWriter(new File("brackets2num.out"));
        String s = in.next();
        n = s.length() / 2;
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
        long num = get_number(s);
        out.write(Long.toString(num));
        out.close();
    }
}
