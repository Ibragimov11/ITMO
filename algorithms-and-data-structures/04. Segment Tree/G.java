import java.io.*;

public class G {
    static long[][] a; // a[0][i] - min, a[1][i] - sset
    static long mn;

    private static void propagate(int i) {
        if (a[1][i] != -1) {
            a[1][2 * i + 1] = a[1][i];
            a[1][2 * i + 2] = a[1][i];
            a[0][2 * i + 1] = a[1][i];
            a[0][2 * i + 2] = a[1][i];
            a[1][i] = -1;
        }
    }

    private static void sset(int i, int lx, int rx, int val, int l, int r) {
        if (rx - lx == 1) {
            if (lx >= l && rx <= r) {
                a[1][i] = val;
                a[0][i] = val;
            }
        } else {
            if (lx >= l && rx <= r) {
                a[1][i] = val;
                a[0][i] = val;
            } else if (rx > l && lx < r) {
                int m = (lx + rx) / 2;
                propagate(i);
                sset(2 * i + 1, lx, m, val, l, r);
                sset(2 * i + 2, m, rx, val, l, r);
                a[0][i] = Math.min(a[0][2 * i + 1], a[0][2 * i + 2]);
            }
        }
    }

    private static void search(int i, int lx, int rx, int l, int r, long w, int z) {
        if (rx - lx == 1) {
            if (lx >= l && rx <= r) {
                if (z == 1) {
                    mn = Math.min(w, mn);
                } else {
                    mn = Math.min(a[0][i], mn);
                }
            }
        } else {
            if (lx >= l && rx <= r) {
                if (z == 1) {
                    mn = Math.min(w, mn);
                } else {
                    mn = Math.min(a[0][i], mn);
                }
            } else if (rx > l && lx < r) {
                if (z != 1 && a[1][i] != -1) {
                    z = 1;
                    w = a[1][i];
                }
                int m = (lx + rx) / 2;
                search(2 * i + 1, lx, m, l, r, w, z);
                search(2 * i + 2, m, rx, l, r, w, z);
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
        for (int i = 0; i < 2 * len - 1; i++) {
            a[1][i] = -1;
        }
        while (m > 0) {
            int t = in.nextInt();
            int l = in.nextInt();
            int r = in.nextInt();
            if (t == 1) {
                int u = in.nextInt();
                sset(0, 0, len, u, l, r);
            } else {
                mn = Long.MAX_VALUE;
                search(0, 0, len, l, r, 0, 0);
                out.println(mn);
            }
            m--;
        }
        in.close();
        out.close();
    }
}

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