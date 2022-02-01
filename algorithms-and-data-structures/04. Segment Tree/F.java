import java.io.*;

public class F {
    static long[][] a; // a[0][i] - min, a[1][i] - add
    static long mn;

    private static void propagate(int i) {
        a[1][2 * i + 1] += a[1][i];
        a[1][2 * i + 2] += a[1][i];
        a[0][2 * i + 1] += a[1][i];
        a[0][2 * i + 2] += a[1][i];
        a[1][i] = 0;
    }

    private static void add(int i, int lx, int rx, int val, int l, int r) {
        if (rx - lx == 1) {
            if (lx >= l && rx <= r) {
                a[1][i] += val;
                a[0][i] += val;
            }
        } else {
            if (lx >= l && rx <= r) {
                a[1][i] += val;
                a[0][i] += val;
            } else if (rx > l && lx < r) {
                int m = (lx + rx) / 2;
                propagate(i);
                add(2 * i + 1, lx, m, val, l, r);
                add(2 * i + 2, m, rx, val, l, r);
                a[0][i] = Math.min(a[0][2 * i + 1], a[0][2 * i + 2]);
            }
        }
    }

    private static void search(int i, int lx, int rx, int l, int r, long w) {
        if (rx - lx == 1) {
            if (lx >= l && rx <= r) {
                mn = Math.min(a[0][i] + w, mn);
            }
        } else {
            if (lx > l && rx < r) {
                mn = Math.min(a[0][i] + w, mn);
            } else if (rx >= l && lx <= r) {
                int m = (lx + rx) / 2;
                search(2 * i + 1, lx, m, l, r, a[1][i] + w);
                search(2 * i + 2, m, rx, l, r, a[1][i] + w);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MyScanner in = new MyScanner(System.in);
        PrintWriter out = new PrintWriter(System.out);
        int n = in.nextInt();
        int m = in.nextInt();
        int k = 0;
        while (Math.pow(2, k) < n) {
            k++;
        }
        int len = (int) Math.pow(2, k);
        a = new long[2][2 * len - 1];
        for (int i = len + n - 1; i < 2 * len - 1; i ++) {
            a[0][i] = Long.MAX_VALUE;
        }
        while (m > 0) {
            int t = in.nextInt();
            if (t == 1) {
                int l = in.nextInt();
                int r = in.nextInt();
                int u = in.nextInt();
                add(0, 0, len, u, l, r);
            } else {
                int l = in.nextInt();
                int r = in.nextInt();
                mn = Long.MAX_VALUE;
                search(0, 0, len, l, r, 0);
                out.println(mn);
            }
            m--;
        }
        in.close();
        out.close();
    }
}

//
//class MyScanner {
//    final private Reader reader;
//
//    public MyScanner(InputStream input) throws IOException {
//        reader = new BufferedReader(new InputStreamReader(input));
//    }
//
//    public int nextInt() throws IOException {
//        return Integer.parseInt(next());
//    }
//
//    public String next() throws IOException {
//        int read = reader.read();
//        StringBuilder sb = new StringBuilder();
//        while (Character.isWhitespace((char) read)) {
//            read = reader.read();
//        }
//        while (!Character.isWhitespace((char) read) && read != -1) {
//            sb.append((char) read);
//            read = reader.read();
//        }
//        return sb.toString();
//    }
//
//    public void close() throws IOException {
//        reader.close();
//    }
//}