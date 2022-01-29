import java.io.*;

public class H {
    static long[][] a;
    static long sum;

    private static void propagate(int i, int r) {
        if (a[1][i] == -1) {
            if (a[1][2 * i + 1] == -1) {
                a[2][2 * i + 1] += a[2][i];
            } else {
                a[1][2 * i + 1] += a[2][i];
            }
            a[0][2 * i + 1] += a[2][i] * r / 2;
            if (a[1][2 * i + 2] == -1) {
                a[2][2 * i + 2] += a[2][i];
            } else {
                a[1][2 * i + 2] += a[2][i];
            }
            a[0][2 * i + 2] += a[2][i] * r / 2;
        } else {
            a[1][2 * i + 1] = a[1][i];
            a[0][2 * i + 1] = a[0][i] / 2;
            a[1][2 * i + 2] = a[1][i];
            a[0][2 * i + 2] = a[0][i] / 2;
        }
        a[0][i] = 0;
        a[1][i] = -1;
        a[2][i] = 0;
    }

    private static void add (int i, int lx, int rx, int val, int l, int r) {
        if (rx - lx == 1) {
            if (lx >= l && rx <= r) {
                if (a[1][i] == -1) {
                    a[2][i] += val;
                } else {
                    a[1][i] += val;
                }
                a[0][i] += val;
            }
        } else {
            if (lx >= l && rx <= r) {
                if (a[1][i] == -1) {
                    a[2][i] += val;
                } else {
                    a[1][i] += val;
                }
                a[0][i] += (long) val * (rx - lx);
            } else if (rx > l && lx < r) {
                int m = (lx + rx) / 2;
                propagate(i, rx - lx);
                add(2 * i + 1, lx, m, val, l, r);
                add(2 * i + 2, m, rx, val, l, r);
                a[0][i] = a[0][2 * i + 1] + a[0][2 * i + 2];
            }
        }
    }

    private static void sset(int i, int lx, int rx, int val, int l, int r) {
        if (rx - lx == 1) {
            if (lx >= l && rx <= r) {
                a[0][i] = val;
                a[1][i] = val;
                a[2][i] = 0;
            }
        } else {
            if (lx >= l && rx <= r) {
                a[0][i] = (long) val * (rx - lx);
                a[1][i] = val;
                a[2][i] = 0;
            } else if (rx > l && lx < r) {
                int m = (lx + rx) / 2;
                propagate(i, rx - lx);
                sset(2 * i + 1, lx, m, val, l, r);
                sset(2 * i + 2, m, rx, val, l, r);
                a[0][i] = a[0][2 * i + 1] + a[0][2 * i + 2];
            }
        }
    }

    private static void search(int i, int lx, int rx, int l, int r, boolean wassset, long firstsset, int alladd) {
        if (rx - lx == 1) {
            if (lx >= l && rx <= r) {
                if (wassset) {
                    sum += firstsset + alladd;
                } else {
                    sum += a[0][i] + alladd;
                }
            }
        } else {
            if (lx >= l && rx <= r) {
                if (wassset) {
                    sum += (firstsset + alladd) * (rx - lx);
                } else {
                    sum += a[0][i] + (long) alladd * (rx - lx);
                }
            } else if (rx > l && lx < r) {
                if (!wassset) {
                    if (a[1][i] == -1) {
                        alladd += a[2][i];
                    } else {
                        wassset = true;
                        firstsset = a[1][i];
                    }
                }
                int m = (lx + rx) / 2;
                search(2 * i + 1, lx, m, l, r, wassset, firstsset, alladd);
                search(2 * i + 2, m, rx, l, r, wassset, firstsset, alladd);
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
        a = new long[3][2 * len - 1]; // 0 - sum; 1 - set; 2 - add
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
            } else if (t == 2) {
                int u = in.nextInt();
                add(0, 0, len, u, l, r);
            } else {
                sum = 0;
                search(0, 0, len, l, r, false, 0, 0);
                out.println(sum);
            }
            m--;
//            print();
        }
        in.close();
        out.close();
    }

//    private static void print() {
//        System.out.print("sum: ");
//        for (int i = 0; i < a[0].length; i++) {
//            System.out.print(a[0][i] + " ");
//        }
//        System.out.println();
//
//        System.out.print("set: ");
//        for (int i = 0; i < a[1].length; i++) {
//            System.out.print(a[1][i] + " ");
//        }
//        System.out.println();
//
//        System.out.print("add: ");
//        for (int i = 0; i < a[2].length; i++) {
//            System.out.print(a[2][i] + " ");
//        }
//        System.out.println();
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