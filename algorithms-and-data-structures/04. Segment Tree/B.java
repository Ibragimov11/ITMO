import java.io.*;

public class B {
    public static void main(String[] args) throws IOException, NumberFormatException {
        MyScanner in = new MyScanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int k = 0;
        while (Math.pow(2, k) < n) {
            k++;
        }
        int len = (int) Math.pow(2, k);
        long[][] a = new long[2][2 * len - 1];
        for (int i = len - 1; i < len - 1 + n; i++) {
            a[0][i] = in.nextInt();
            a[1][i] = 1;
        }
        for (int i = len + n - 1; i < 2 * len - 1; i ++) {
            a[0][i] = Long.MAX_VALUE;
            a[1][i] = 1;
        }
        int len1 = len / 2;
        while (len1 > 0) {
            for (int i = len1 - 1; i < 2 * len1 - 1; i++) {
                a[0][i] = Math.min(a[0][2 * i + 1], a[0][2 * i + 2]);
                if (a[0][2 * i + 1] < a[0][2 * i + 2]) {
                    a[1][i] = a[1][2 * i + 1];
                } else if (a[0][2 * i + 1] > a[0][2 * i + 2]) {
                    a[1][i] = a[1][2 * i + 2];
                } else {
                    a[1][i] = a[1][2 * i + 1] + a[1][2 * i + 2];
                }
            }
            len1 /= 2;
        }
        while (m > 0) {
            int t = in.nextInt();
            if (t == 1) {
                int i = len - 1 + in.nextInt();
                int u = in.nextInt();
                a[0][i] = u;
                while (i > 0) {
                    i = (i - 1) / 2;
                    a[0][i] = Math.min(a[0][2 * i + 1], a[0][2 * i + 2]);
                    if (a[0][2 * i + 1] < a[0][2 * i + 2]) {
                        a[1][i] = a[1][2 * i + 1];
                    } else if (a[0][2 * i + 1] > a[0][2 * i + 2]) {
                        a[1][i] = a[1][2 * i + 2];
                    } else {
                        a[1][i] = a[1][2 * i + 1] + a[1][2 * i + 2];
                    }
                }
            } else {
                int l = len - 1 + in.nextInt();
                int r = len - 1 + in.nextInt() - 1;
                long s = Long.MAX_VALUE;
                long count = 0;
                while (r >= l) {
                    if (l % 2 == 0) {
                        if (a[0][l] == s) {
                            count += a[1][l];
                        } else if (a[0][l] < s) {
                            s = a[0][l];
                            count = a[1][l];
                        }
                    }
                    l /= 2;
                    if (r % 2 == 1) {
                        if (a[0][r] == s) {
                            count += a[1][r];
                        } else if (a[0][r] < s) {
                            s = a[0][r];
                            count = a[1][r];
                        }
                    }
                    r = r / 2 - 1;
                }
                System.out.println(s + " " + count);
            }
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
