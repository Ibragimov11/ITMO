import java.util.Scanner;

public class G {
    static int n;
    static int[][] dp, p;
    static String s;

    private static void rec(int l, int r) {
//        System.out.println(l + " " + r);
        if (dp[l][r] == r - l + 1) {
            System.out.print("");
        } else if (dp[l][r] == 0) {
//            int pr = Math.min(r - l + 2, n);
//            if (l == 0 && r != n - 1) {
//                pr--;
//            }
            System.out.print(s.substring(l, r + 1));
        } else if (p[l][r] == -1) {
            System.out.print(s.charAt(l));
            rec(l + 1, r - 1);
            System.out.print(s.charAt(r));
        } else {
            rec(l, p[l][r]);
            rec(p[l][r] + 1, r);
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        s = in.next();
        n = s.length();
        dp = new int[n][n];
        p = new int[n][n];

        for (int r = 0; r < n; r++) {
            for (int l = r; l >= 0; l--) {
                if (l == r) {
                    dp[l][r] = 1;
                } else {
                    int m = (int) 1e7;
                    int t = -1;
                    if (s.charAt(l) == '(' && s.charAt(r) == ')'|| s.charAt(l) == '{' && s.charAt(r) == '}' || s.charAt(l) == '[' && s.charAt(r) == ']') {
                        m = dp[l + 1][r - 1];
                    }
                    for (int k = l; k < r; k++) {
                        if (dp[l][k] + dp[k + 1][r] < m) {
                            m = dp[l][k] + dp[k + 1][r];
                            t = k;
                        }
                    }
                    dp[l][r] = m;
                    p[l][r] = t;
//                    System.out.println(l + " " + r + " " + t);
                }
            }
        }

//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                System.out.print(dp[i][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                System.out.print(p[i][j] + " ");
//            }
//            System.out.println();
//        }

        rec(0, n - 1);

    }
}
