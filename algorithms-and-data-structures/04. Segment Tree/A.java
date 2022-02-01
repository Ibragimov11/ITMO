import java.io.*;

public class A {
    public static void main(String[] args) throws IOException, NumberFormatException {
        MyScanner in = new MyScanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int k = 0;
        while (Math.pow(2, k) < n) {
            k++;
        }
        int len = (int) Math.pow(2, k);
        long[] a = new long[2 * len - 1];
        for (int i = len - 1; i < len - 1 + n; i++) {
            a[i] = in.nextInt();
        }
        for (int i = len + n - 1; i < 2 * len - 1; i ++) {
            a[i] = 0;
        }
        int len1 = len / 2;
        while (len1 > 0) {
            for (int i = len1 - 1; i < 2 * len1 - 1; i++) {
                a[i] = a[2 * i + 1] + a[2 * i + 2];
            }
            len1 /= 2;
        }
        while (m > 0) {
            int t = in.nextInt();
            if (t == 1) {
                int i = len - 1 + in.nextInt();
                int u = in.nextInt();
                a[i] = u;
                while (i > 0) {
                    i = (i - 1) / 2;
                    a[i] = a[2 * i + 1] + a[2 * i + 2];
                }
            } else {
                int l = len - 1 + in.nextInt();
                int r = len - 1 + in.nextInt() - 1;
                long s = 0;
                while (r >= l) {
                    if (l % 2 == 0) {
                        s += a[l];
                    }
                    l /= 2;
                    if (r % 2 == 1) {
                        s += a[r];
                    }
                    r = r / 2 - 1;
                }
                System.out.println(s);
            }
            m--;
        }
        in.close();
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
