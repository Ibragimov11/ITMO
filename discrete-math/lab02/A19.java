import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class A19 {
    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("num2brackets2.in"));
        FileWriter out = new FileWriter(new File("num2brackets2.out"));
        int n = in.nextInt();
        long k = in.nextLong();
        long[][] d = new long[2 * n + 1][n + 1];
        k++;
        for (int i = 0; i <= 2 * n; i++)
            for (int j = 0; j <= n; j++)
                d[i][j] = 0;
        d[0][0] = 1;
        for (int i = 1; i < 2 * n; i++) {
            for (int j = 0; j<n +1; j++) {
                if (j == 0)
                    d[i][j] = d[i - 1][j + 1];
                else if (j == n)
                    d[i][j] = d[i - 1][j - 1];
                else
                    d[i][j] = d[i - 1][j - 1] + d[i - 1][j + 1];
            }
        }
        String res = "";
        int depth = 0;
        String[] log = new String[2 * n];
        int s = 0;
        for (int i = 2 * n - 1; i >= 0; i--) {
            long c;
            if (depth + 1 <= n)
                c = d[i][depth + 1] << ((i - depth - 1) / 2);
            else
                c = 0;
            if (c >= k)
            {
                res += "(";
                log[s++] = "(";
                depth++;
                continue;
            }
            k -= c;
            if (s > 0 && log[s - 1] == "(" && depth > 0) {
                c = d[i][depth - 1] << ((i - depth + 1) / 2);
            } else {
                c = 0;
            }
            if (c >= k)
            {
                res += ")";
                --s;
                --depth;
                continue;
            }
            k -= c;

            if (depth < n)
                c = d[i][depth + 1] << ((i - depth - 1) / 2);
            else
                c = 0;
            if (c >= k)
            {
                res += "[";
                log[s++] = "[";
                ++depth;
                continue;
            }
            k -= c;
            res += "]";
            --s;
            depth--;
        }
        out.write(res);
        out.close();
    }
}
