import java.io.*;

public class N {
    static long[][][] a; // x : y : z
    static long count;

    private static void add(int x, int y, int z, int d) {
        int xx, yy, zz;
        xx = x;
        int x0 = x == 0 ? 1 : 0;
        while (xx > 0 || x0 == 1) {
            int y0 = y == 0 ? 1 : 0;
            yy = y;
            while (yy > 0 || y0 == 1) {
                int z0 = z == 0 ? 1 : 0;
                zz = z;
                while (zz > 0 || z0 == 1) {
                    a[xx][yy][zz] += d;
                    zz = (zz - 1) / 2;
                    if (zz == 0) z0++;
                }
                yy = (yy - 1) / 2;
                if (yy == 0) y0++;
            }
            xx = (xx - 1) / 2;
            if (xx == 0) x0++;
        }
    }

    private static void search(int x1, int y1, int z1, int x2, int y2, int z2) {
        int xx1 = x1;
        int xx2 = x2;
        while (xx2 >= xx1) {
            if (xx1 % 2 == 0) {

                int yy1 = y1;
                int yy2 = y2;
                while (yy2 >= yy1) {
                    if (yy1 % 2 == 0) {

                        int zz1 = z1;
                        int zz2 = z2;
                        while (zz2 >= zz1) {
                            if (zz1 % 2 == 0) {
                                count += a[xx1][yy1][zz1];
                            }
                            zz1 /= 2;
                            if (zz2 % 2 == 1) {
                                count += a[xx1][yy1][zz2];
                            }
                            zz2 = zz2 / 2 - 1;
                        }

                    }
                    yy1 /= 2;
                    if (yy2 % 2 == 1) {

                        int zz1 = z1;
                        int zz2 = z2;
                        while (zz2 >= zz1) {
                            if (zz1 % 2 == 0) {
                                count += a[xx1][yy2][zz1];
                            }
                            zz1 /= 2;
                            if (zz2 % 2 == 1) {
                                count += a[xx1][yy2][zz2];
                            }
                            zz2 = zz2 / 2 - 1;
                        }

                    }
                    yy2 = yy2 / 2 - 1;
                }

            }
            xx1 /= 2;
            if (xx2 % 2 == 1) {

                int yy1 = y1;
                int yy2 = y2;
                while (yy2 >= yy1) {
                    if (yy1 % 2 == 0) {

                        int zz1 = z1;
                        int zz2 = z2;
                        while (zz2 >= zz1) {
                            if (zz1 % 2 == 0) {
                                count += a[xx2][yy1][zz1];
                            }
                            zz1 /= 2;
                            if (zz2 % 2 == 1) {
                                count += a[xx2][yy1][zz2];
                            }
                            zz2 = zz2 / 2 - 1;
                        }

                    }
                    yy1 /= 2;
                    if (yy2 % 2 == 1) {

                        int zz1 = z1;
                        int zz2 = z2;
                        while (zz2 >= zz1) {
                            if (zz1 % 2 == 0) {
                                count += a[xx2][yy2][zz1];
                            }
                            zz1 /= 2;
                            if (zz2 % 2 == 1) {
                                count += a[xx2][yy2][zz2];
                            }
                            zz2 = zz2 / 2 - 1;
                        }

                    }
                    yy2 = yy2 / 2 - 1;
                }

            }
            xx2 = xx2 / 2 - 1;
        }
    }

    public static void main(String[] args) throws IOException {
        MyScanner in = new MyScanner(System.in);
        PrintWriter out = new PrintWriter(System.out);
        int n = in.nextInt();
        int len = 1;
        while (len < n) {
            len *= 2;
        }
        a = new long[2 * len - 1][2 * len - 1][2 * len - 1];
        while (true) {
            int m = in.nextInt();
            if (m == 1) {
                int x = in.nextInt() + len - 1;
                int y = in.nextInt() + len - 1;
                int z = in.nextInt() + len - 1;
                int d = in.nextInt();
                add(x, y, z, d);
            } else if (m == 2) {
                int x1 = in.nextInt() + len - 1;
                int y1 = in.nextInt() + len - 1;
                int z1 = in.nextInt() + len - 1;
                int x2 = in.nextInt() + len - 1;
                int y2 = in.nextInt() + len - 1;
                int z2 = in.nextInt() + len - 1;
                count = 0;
                search(x1, y1, z1, x2, y2, z2);
                out.println(count);
            } else {
                break;
            }
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

/*
  2
  1 0 0 0 1
  1 0 1 0 3
  1 0 1 0 -2
  2 0 0 0 1 1 1
  3
 */