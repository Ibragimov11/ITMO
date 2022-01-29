import java.io.*;

public class L {
    static long[][] a;
    static int len;
    static long sum;

    private static long getSum(long first, long n) {
        return (2 * first + n - 1) * n / 2;
    }

    private static void propagate(int i) {
        a[2][2 * i + 1] += a[2][i];
        a[2][2 * i + 2] += a[2][i];
        a[0][2 * i + 1] += a[2][i] * a[4][2 * i + 1];
        a[0][2 * i + 2] += a[2][i] * a[4][2 * i + 2];
        a[1][2 * i + 1] += a[2][i] * getSum(a[3][2 * i + 1], a[4][2 * i + 1]);
        a[1][2 * i + 2] += a[2][i] * getSum(a[3][2 * i + 2], a[4][2 * i + 2]);
        a[2][i] = 0;
    }

    private static void add(int x, int lx, int rx, int l, int r, int d) {
        if (lx > r || rx < l) {
            return;
        }
        if (lx >= l && rx <= r) {
            a[0][x] += d * a[4][x];
            a[1][x] += d * getSum(a[3][x], a[4][x]);
            a[2][x] += d;
            return;
        }
        propagate(x);
        int m = (lx + rx) / 2;
        add(2 * x + 1, lx, m, l, r, d);
        add(2 * x + 2, m + 1, rx, l, r, d);
        a[0][x] = a[0][2 * x + 1] + a[0][2 * x + 2];
        a[1][x] = a[1][2 * x + 1] + a[1][2 * x + 2];
    }

    private static void search(int x, int lx, int rx, int l, int r, long alladd) {
        if (lx > r || rx < l) {
            return;
        }
        if (lx >= l && rx <= r) {
            sum += (a[1][x] + alladd * getSum(a[3][x], a[4][x])) - l * (a[0][x] + alladd * a[4][x]);
            return;
        }
        int m = (lx + rx) / 2;
        search(2 * x + 1, lx, m, l, r, alladd + a[2][x]);
        search(2 * x + 2, m + 1, rx, l, r, alladd + a[2][x]);
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
        len = (int) Math.pow(2, k);
        a = new long[5][2 * len - 1]; // 0 - сумма, 1 - спец. сумма, 2 - add, 3 - левая граница, 4 - количество
        for (int i = len - 1; i < len - 1 + n; i++) {
            a[0][i] = in.nextInt();
            a[1][i] = a[0][i] * (i - len + 2);
            a[2][i] = 0;
            a[3][i] = i - len + 2;
            a[4][i] = 1;
        }
        for (int i = len - 1 + n; i < 2 * len - 1; i++) {
            a[0][i] = 0;
            a[1][i] = 0;
            a[2][i] = 0;
            a[3][i] = i - len + 2;
            a[4][i] = 0;
        }
        int len1 = len / 2;
        while (len1 > 0) {
            for (int i = len1 - 1; i < 2 * len1 - 1; i++) {
                a[0][i] = a[0][2 * i + 1] + a[0][2 * i + 2];
                a[1][i] = a[1][2 * i + 1] + a[1][2 * i + 2];
                a[3][i] = a[3][2 * i + 1];
                a[4][i] = a[4][2 * i + 1] + a[4][2 * i + 2];
            }
            len1 /= 2;
        }
        while (m > 0) {
            int t = in.nextInt();
            int l = in.nextInt() - 1;
            int r = in.nextInt() - 1;
            if (t == 1) {
                int d = in.nextInt();
                add(0, 0, len - 1, l, r, d);
            } else {
                sum = 0;
                search(0, 0, len - 1, l, r, 0);
                out.println(sum);
            }
//            print();
            m--;
        }
        in.close();
        out.close();
    }

//    private static void print() {
//        System.out.println();
//        for (int i = 0; i < 2 * len - 1; i++) {
//            System.out.print(a[0][i] + " ");
//        }
//        System.out.println();
//        for (int i = 0; i < 2 * len - 1; i++) {
//            System.out.print(a[1][i] + " ");
//        }
//        System.out.println();
//    }

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
