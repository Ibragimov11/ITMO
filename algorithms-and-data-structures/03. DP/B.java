import java.util.Scanner;

public class B {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int[][] costs = new int[n][m];
        int[][] start_costs = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                costs[i][j] = in.nextInt();
                start_costs[i][j] = costs[i][j];
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i + j == 0) {
                    continue;
                }
                if (i == 0) {
                    costs[i][j] += costs[i][j - 1];
                } else if (j == 0) {
                    costs[i][j] += costs[i - 1][j];
                } else {
                    costs[i][j] += Math.max(costs[i][j - 1], costs[i - 1][j]);
                }
            }
        }
        int value = costs[n - 1][m - 1];
        System.out.println(value);
        StringBuilder str = new StringBuilder();
        int i = n - 1;
        int j = m - 1;
        while (i > 0 || j > 0) {
            if (i == 0) {
                j--;
                str.insert(0, "R");
            } else if (j == 0) {
                i--;
                str.insert(0, "D");
            } else {
                value -= start_costs[i][j];
                if (costs[i - 1][j] == value) {
                    str.insert(0, "D");
                    i--;
                } else {
                    str.insert(0, "R");
                    j--;
                }
            }
        }
        System.out.println(str.toString());
    }
}
