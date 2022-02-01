import java.io.*;
import java.util.HashMap;

public class I {
    static int r;
    static int[] ans = new int[4];
    static int[][] a;
    static HashMap<int[], int[]> map = new HashMap<>();

    private static int[] mul(int[] a, int[] b) {
        int[] c = new int[4];
        c[0] = ((a[0] * b[0]) % r + (a[1] * b[2]) % r) % r;
        c[1] = ((a[0] * b[1]) % r + (a[1] * b[3]) % r) % r;
        c[2] = ((a[2] * b[0]) % r + (a[3] * b[2]) % r) % r;
        c[3] = ((a[2] * b[1]) % r + (a[3] * b[3]) % r) % r;
        return c;
    }

    private static void search(int x, int lx, int rx, int ll, int rr) {
        if (lx > rr || rx < ll) {
            return;
        }
        if (lx >= ll && rx <= rr) {
            ans = mul(ans, a[x]);
            return;
        }
        int m = (lx + rx) / 2;
        search(2 * x + 1, lx, m, ll, rr);
        search(2 * x + 2, m + 1, rx, ll, rr);
    }

    public static void main(String[] args) throws IOException {
        MyScanner in = new MyScanner(System.in);
        PrintWriter out = new PrintWriter(System.out);
        r = in.nextInt();
        int n = in.nextInt();
        int m = in.nextInt();
        int k = 0;
        while (Math.pow(2, k) < n) {
            k++;
        }
        int len = (int) Math.pow(2, k);
        a = new int[2 * len - 1][4];
        for (int i = len - 1; i < len - 1 + n; i++) {
            a[i][0] = in.nextInt();
            a[i][1] = in.nextInt();
            a[i][2] = in.nextInt();
            a[i][3] = in.nextInt();
        }
        for (int i = len + n - 1; i < 2 * len - 1; i ++) {
            a[i][0] = 1;
            a[i][1] = 0;
            a[i][2] = 0;
            a[i][3] = 1;
        }
        int len1 = len / 2;
        while (len1 > 0) {
            for (int i = len1 - 1; i < 2 * len1 - 1; i++) {
                a[i] = mul(a[2 * i + 1], a[2 * i + 2]);
            }
            len1 /= 2;
        }
        while (m > 0) {
            int ll = in.nextInt() - 1;
            int rr = in.nextInt() - 1;
            ans[0] = 1;
            ans[1] = 0;
            ans[2] = 0;
            ans[3] = 1;
            if (map.containsKey(new int[] {ll, rr})) {
                ans = map.get(new int[] {ll, rr});
            } else {
                search(0, 0, len - 1, ll, rr);
                map.put(new int[] {ll, rr}, ans);
            }
            out.println(ans[0] + " " + ans[1]);
            out.println(ans[2] + " " + ans[3]);
            out.println();
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
