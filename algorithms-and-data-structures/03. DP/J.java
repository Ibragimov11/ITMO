import java.util.Scanner;

public class J {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int aa = in.nextInt();
        int bb = in.nextInt();
        int n = Math.min(aa, bb);
        int m = Math.max(aa, bb);
        int k = (int) Math.pow(2, n);
        int all = 0;

        int[][] correct = new int[k][k];
        for (int i = 0; i < k; i++) {
            for (int j = i; j < k; j++) {
                int ad = i | j;
                int ml = i & j;
                String ads = String.format("%32s", Integer.toBinaryString(ad)).replace(' ', '0').substring(32 - n);
                String mls = String.format("%32s", Integer.toBinaryString(ml)).replace(' ', '0').substring(32 - n);
                if (n == 1) {
                    correct[i][j] = 1;
                    correct[j][i] = 1;
                } else {
                    for (int pos = 0; pos < n - 1; pos++) {
                        if (ads.charAt(pos) == ads.charAt(pos + 1) && mls.charAt(pos) == mls.charAt(pos + 1) && ads.charAt(pos) == mls.charAt(pos)) {
//                            System.out.println(i + "  " + j);
                            correct[i][j] = 0;
                            correct[j][i] = 0;
                            break;
                        }
                        correct[i][j] = 1;
                        correct[j][i] = 1;
                    }
                }
            }
        }

        int[][] a = new int[k][m];
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < k; i++) {
                if (j == 0) {
                    a[i][j] = 1;
                } else {
                    for (int v = 0; v < k; v++) {
                        if (correct[i][v] == 1) {
                            a[i][j] += a[v][j - 1];
                        }
                    }
                }
            }
        }

//        for (int i = 0; i < k; i++) {
//            for (int j = 0; j < k; j++) {
//                System.out.print(correct[i][j] + " ");
//            }
//            System.out.println();
//        }

//        for (int i = 0; i < k; i++) {
//            for (int j = 0; j < m; j++) {
//                System.out.print(a[i][j] + " ");
//            }
//            System.out.println();
//        }

        for (int i = 0; i < k; i++) {
            all += a[i][m - 1];
        }

        System.out.println(all);
    }
}
