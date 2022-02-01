import java.util.Arrays;
import java.util.Scanner;

public class E {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String word1 = in.next();
        String word2 = in.next();
        int n = word1.length() + 1;
        int m = word2.length() + 1;
        int[][] a = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || j == 0) {
                    a[i][j] = i + j;
                    continue;
                }
                if (word1.charAt(j - 1) == word2.charAt(i - 1)) {
                    a[i][j] = a[i - 1][j - 1];
                } else {
                    a[i][j] = Math.min(Math.min(a[i - 1][j - 1], a[i - 1][j]), a[i][j - 1])  + 1;
                }
            }
        }
        System.out.println(a[m - 1][n - 1]);
    }
}
