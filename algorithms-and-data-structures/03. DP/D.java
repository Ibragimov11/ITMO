import java.util.Scanner;

public class D {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[][] a = new int[n][10];
        for (int i = 0; i < 10; i++) {
            a[0][i] = (i == 0 || i == 8) ? 0 : 1;
        }
        for (int i = 1; i < n; i++) {
            a[i][0] += (a[i - 1][4] + a[i - 1][6]) % 1e9;
            a[i][1] += (a[i - 1][6] + a[i - 1][8]) % 1e9;
            a[i][2] += (a[i - 1][7] + a[i - 1][9]) % 1e9;
            a[i][3] += (a[i - 1][4] + a[i - 1][8]) % 1e9;
            a[i][4] += (a[i - 1][3] + a[i - 1][9] + a[i - 1][0]) % 1e9;
            a[i][5] = 0;
            a[i][6] += (a[i - 1][1] + a[i - 1][7] + a[i - 1][0]) % 1e9;
            a[i][7] += (a[i - 1][2] + a[i - 1][6]) % 1e9;
            a[i][8] += (a[i - 1][1] + a[i - 1][3]) % 1e9;
            a[i][9] += (a[i - 1][2] + a[i - 1][4]) % 1e9;
        }
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += a[n - 1][i];
        }
        String str = Double.toString(sum % 1e9);
        System.out.println(str.substring(0, str.length() - 2));
    }
}
//на Python работает, здесь проблемы с делением