import java.io.*;

public class C {

    static long[][] a;

    private static void merge(int i) {
        int j1 = 2 * i + 1;
        int j2 = 2 * i + 2;
        a[0][i] = a[0][j1] + a[0][j2];
        a[1][i] = Math.max(a[1][j1], a[0][j1] + a[1][j2]);
        a[2][i] = Math.max(a[2][j2], a[2][j1] + a[0][j2]);
        a[3][i] = Math.max(Math.max(a[3][j1], a[3][j2]), a[2][j1] + a[1][j2]);
    }

    public static void main(String[] args) throws IOException {
        MyScanner in = new MyScanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int k = 0;
        while (Math.pow(2, k) < n) {
            k++;
        }
        int len = (int) Math.pow(2, k);
        a = new long[4][2 * len - 1];
        for (int i = len - 1; i < len - 1 + n; i++) {
            a[0][i] = in.nextInt();
            for (int j = 1; j < 4; j++) {
                a[j][i] = a[0][i];
            }
        }
        for (int i = len + n - 1; i < 2 * len - 1; i ++) {
            a[0][i] = 0;
            for (int j = 1; j < 4; j++) {
                a[j][i] = a[0][i];
            }
        }
        int len1 = len / 2;
        while (len1 > 0) {
            for (int i = len1 - 1; i < 2 * len1 - 1; i++) {
                merge(i);
            }
            len1 /= 2;
        }
        System.out.println(Math.max(a[3][0], 0));
        while (m > 0) {
            int i = len - 1 + in.nextInt();
            int u = in.nextInt();
            a[0][i] = u;
            for (int j = 1; j < 4; j++) {
                a[j][i] = a[0][i];
            }
            while (i > 0) {
                i = (i - 1) / 2;
                merge(i);
            }
            System.out.println(Math.max(a[3][0], 0));
            m--;
        }
        in.close();
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
