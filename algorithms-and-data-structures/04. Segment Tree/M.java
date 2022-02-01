import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class M {
    static long[][] tree; // max, add, index
    static long xx, yy;
    static long mx;

    private static void propagate(int i) {
        tree[1][2 * i + 1] += tree[1][i];
        tree[1][2 * i + 2] += tree[1][i];
        tree[0][2 * i + 1] += tree[1][i];
        tree[0][2 * i + 2] += tree[1][i];
        tree[1][i] = 0;
    }

    private static void add(int i, int lx, int rx, int val, long l, long r) {
        if (rx - lx == 1) {
            if (lx >= l && rx <= r) {
                tree[1][i] += val;
                tree[0][i] += val;
            }
        } else {
            if (lx >= l && rx <= r) {
                tree[1][i] += val;
                tree[0][i] += val;
            } else if (rx > l && lx < r) {
                int m = (lx + rx) / 2;
                propagate(i);
                add(2 * i + 1, lx, m, val, l, r);
                add(2 * i + 2, m, rx, val, l, r);
//                tree[0][i] = Math.max(tree[0][2 * i + 1], tree[0][2 * i + 2]);
                if (tree[0][2 * i + 1] >= tree[0][2 * i + 2]) {
                    tree[0][i] = tree[0][2 * i + 1];
                    tree[2][i] = tree[2][2 * i + 1];
                } else {
                    tree[0][i] = tree[0][2 * i + 2];
                    tree[2][i] = tree[2][2 * i + 2];
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MyScanner in = new MyScanner(System.in);
        PrintWriter out = new PrintWriter(System.out);
        int n = in.nextInt();
        int len = 1;
        long h = 2L * (int) Math.pow(10, 5);
        while (len < (2 * h + 1)) {
            len *= 2;
        }
        tree = new long[3][2 * len - 1];
        for (int i = 0; i < len - 1; i++) {
            tree[2][i + len - 1] = i - h;
        }
        int len1 = len / 2;
        while (len1 > 0) {
            for (int i = len1 - 1; i < 2 * len1 - 1; i++) {
                tree[2][i] = tree[2][2 * i + 1];
            }
            len1 /= 2;
        }
        long[] y = new long[2 * n];
        long[][] op = new long[n][3];
        long[][] cl = new long[n][3];
        for (int i = 0; i < n; i++) {
            op[i][1] = in.nextInt() + h;
            op[i][0] = in.nextInt() + h;
            op[i][2] = in.nextInt() + h;
            cl[i][0] = in.nextInt() + h;
            cl[i][1] = op[i][1];
            cl[i][2] = op[i][2];
            y[i] = op[i][0];
            y[2 * n - 1 - i] = cl[i][0];
        }
        Arrays.sort(op, Comparator.comparingLong(o -> o[0]));
        Arrays.sort(cl, Comparator.comparingLong(o -> o[0]));
        Arrays.sort(y);
        int i = 0, j = 0;
        for (int q = 0; q < 2 * n; q++) {
            if (i < n) {
                while (op[i][0] == y[q]) {
                    add(0, 0, len, 1, op[i][1], op[i][2]);
                    i++;
                    if (i == n) {
                        break;
                    }
                }
            }

            if (tree[0][0] >= mx) {
                mx = tree[0][0];
                yy = y[q];
                xx = tree[2][0];
            }

            if (j < n) {
                while (cl[j][0] == y[q]) {
                    add(0, 0, len, -1, cl[j][1], cl[j][2]);
                    j++;
                    if (j == n) {
                        break;
                    }
                }
            }
        }
        out.println(mx);
        out.println(xx + " " + (yy - h));
        in.close();
        out.close();
    }
}

class MyScanner {
    final private Reader reader;

    public MyScanner(InputStream input) throws IOException {
        reader = new BufferedReader(new InputStreamReader(input));
    }

    public int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    public String next() throws IOException {
        int read = reader.read();
        StringBuilder sb = new StringBuilder();
        while (Character.isWhitespace((char) read)) {
            read = reader.read();
        }
        while (!Character.isWhitespace((char) read) && read != -1) {
            sb.append((char) read);
            read = reader.read();
        }
        return sb.toString();
    }

    public void close() throws IOException {
        reader.close();
    }
}
