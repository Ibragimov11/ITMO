import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class A29 {

    static String s;
    static int n, l, count;
    static FileWriter out;

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        //Scanner in = new Scanner(new File("nextpartition.in"));
        out = new FileWriter(new File("nextpartition.out"));
        s = in.next();
        l = s.length();
        count = 0;
        for (int i = 0; i < l; i++) {
            if (s.charAt(i) == '+') {
                count++;
            }
        }
        if (count == 0) {
            out.write("No solution");
        } else {
            int[] a = new int[count + 1];
            int i = 0;
            int j = 0;
            while (s.charAt(j) != '=') {
                j++;
            }
            n = Integer.parseInt(s.substring(i, j));
            j++;
            i = j;
            for (int u = 0; u < count + 1; u++) {
                while (s.charAt(j) != '+') {
                    j++;
                    if (j == l) break;
                }
                a[u] = Integer.parseInt(s.substring(i, j));
                j++;
                i = j;
            }
            out.write(n + "=");
            int ost = 0;
            int w = -1;
            int q = -1;
            if (a[count] + a[count - 1] <= n) {
                q = count - 1;
            }
            for (int u = count; u >= 0; u--) {
                if (ost - 1 >= a[u] + 1) {
                    w = u;
                    break;
                } else {
                    ost += a[u];
                }
            }
            for (int v = 0; v < Math.max(q, w); v++) {
                out.write(a[v] + "+");
            }
            if (q > w || (q == w && a[q + 1] + a[q] < a[q] + 1)) {
                out.write(Integer.toString(a[q] + a[q + 1]));
            } else {
                a[w]++;
                out.write(Integer.toString(a[w]));
                ost--;
                if (ost > 0) {
                    out.write("+");
                }
                while(ost - a[w] >= a[w]) {
                    out.write(a[w] + "+");
                    ost -= a[w];
                }
                if (ost > 0) {
                    out.write(Integer.toString(ost));
                }
            }
        }
        out.close();
    }
}
