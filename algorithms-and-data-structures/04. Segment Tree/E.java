import java.io.*;

public class E {
    static long[] a;
    static long mn;

    private static void search(int i, int x, int l, int lx, int rx) {
        if (rx <= l) {
            return;
        }
        if (rx - lx == 1) {
            if (a[i] >= x) {
                mn = Math.min(mn, i);
            }
            return;
        }
        int m = (lx + rx) / 2;
        if (a[2 * i + 1] >= x && m > l) {
            search(2 * i + 1, x, l, lx, m);
        }
        if (a[2 * i + 2] >= x && mn == Long.MAX_VALUE) {
            search(2 * i + 2, x, l, m, rx);
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
        a = new long[2 * len - 1];
        for (int i = len - 1; i < len - 1 + n; i++) {
            a[i] = in.nextInt();
        }
        for (int i = len + n - 1; i < 2 * len - 1; i ++) {
            a[i] = Long.MIN_VALUE;
        }
        int len1 = len / 2;
        while (len1 > 0) {
            for (int i = len1 - 1; i < 2 * len1 - 1; i++) {
                a[i] = Math.max(a[2 * i + 1], a[2 * i + 2]);
            }
            len1 /= 2;
        }
        while (m > 0) {
            int t = in.nextInt();
            if (t == 1) {
                int i = len - 1 + in.nextInt();
                a[i] = in.nextInt();
                while (i > 0) {
                    i = (i - 1) / 2;
                    a[i] = Math.max(a[2 * i + 1], a[2 * i + 2]);
                }
            } else {
                int x = in.nextInt();
                int l = /*len - 1 +*/ in.nextInt();
                mn = Long.MAX_VALUE;
                search(0, x, l, 0, len);
                if (mn == Long.MAX_VALUE) {
                    out.println(-1);
                } else {
                    out.println(mn - len + 1);
                }
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
