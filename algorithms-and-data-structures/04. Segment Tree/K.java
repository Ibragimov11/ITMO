import java.io.*;

public class K {
    static int len = 1_048_576;
    static int[][] a = new int[4][2 * len - 1];

    private static void propagate(int i, int lx, int rx) {
        if (a[1][i] == rx - lx || a[1][i] == 0) {
            a[0][2 * i + 1] = a[0][i];
            a[0][2 * i + 2] = a[0][i];
            a[1][2 * i + 1] = a[1][i] / 2;
            a[1][2 * i + 2] = a[1][i] / 2;
            a[2][2 * i + 1] = a[2][i];
            a[2][2 * i + 2] = a[2][i];
            a[3][2 * i + 1] = a[3][i];
            a[3][2 * i + 2] = a[3][i];
        }
    }

    private static void draw(int x, int lx, int rx, int val, int l, int r) {
        if (lx >= r || rx <= l) {
            return;
        }
        if (lx >= l && rx <= r) {
            a[0][x] = val;
            a[1][x] = val * (rx - lx);
            a[2][x] = val;
            a[3][x] = val;
            return;
        }
        propagate(x, lx, rx);
        int m = (lx + rx) / 2;
        draw(2 * x + 1, lx, m, val, l, r);
        draw(2 * x + 2, m, rx, val, l, r);
        a[0][x] = a[0][2 * x + 1] + a[0][2 * x + 2] - a[3][2 * x + 1] * a[2][2 * x + 2];
        a[1][x] = a[1][2 * x + 1] + a[1][2 * x + 2];
        a[2][x] = a[2][2 * x + 1];
        a[3][x] = a[3][2 * x + 2];
    }

    public static void main(String[] args) throws IOException {
        MyScanner in = new MyScanner(System.in);
        PrintWriter out = new PrintWriter(System.out);
        int n = in.nextInt();
        for (int i = 0; i < n; i++) {
            String s = in.next();
            int l = in.nextInt() + 500000;
            int r = in.nextInt();
            draw(0, 0, len, s.equals("B") ? 1 : 0, l, l + r);
            out.println(a[0][0] + " " + a[1][0]);
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