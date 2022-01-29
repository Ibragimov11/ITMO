import java.io.*;
import java.util.Arrays;

public class J {
    static int count;
    static int[] a;

    private static void build(int i, int h) {
        a[i] = h;
        while (i > 0) {
            i = (i - 1) / 2;
            a[i] = Math.min(a[2 * i + 1], a[2 * i + 2]);
        }
    }


    private static void broke(int x, int lx, int rx, int l, int r, int p) {
        if (lx >= r || rx <= l || a[x] > p) {
            return;
        }
        if (rx - lx == 1) {
            if (a[x] <= p && a[x] != 0) {
                count++;
                a[x] = Integer.MAX_VALUE;
            }
            return;
        }
        int m = (lx + rx) / 2;
        broke(2 * x + 1, lx, m, l, r, p);
        broke(2 * x + 2, m, rx, l, r, p);
        a[x] = Math.min(a[2 * x + 1], a[2 * x + 2]);
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
        a = new int[2 * len - 1];
        Arrays.fill(a, Integer.MAX_VALUE);
        while (m > 0) {
            int t = in.nextInt();
            if (t == 1) {
                int i = in.nextInt() + len - 1;
                int h = in.nextInt();
                build(i, h);
            } else {
                int l = in.nextInt();
                int r = in.nextInt();
                int p = in.nextInt();
                count = 0;
                broke(0, 0, len, l, r, p);
                out.println(count);
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