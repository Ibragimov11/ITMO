import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class A5 {

    static int n, k, m;
    static FileWriter out;
    static int[][] arr;

    public static void telemetry(int i) {
        int l = (int) Math.pow(k, i);
        int b = 1;
        int j = 0;
        while (j < m) {
            if (b == 1) {
                for (int u = 0; u < k; u++) {
                    for (int v = 0; v < l; v++) {
                        arr[j][i] = u;
                        j++;
                    }
                }
            } else {
                for (int u = k - 1; u > -1; u--) {
                    for (int v = 0; v < l; v++) {
                        arr[j][i] = u;
                        j++;
                    }
                }
            }
            b = 1 - b;
        }
    }

    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("telemetry.in"));
        out = new FileWriter(new File("telemetry.out"));
        n = in.nextInt();
        k = in.nextInt();
        m = (int) Math.pow(k, n);
        arr = new int[m][n];
        for (int i = 0; i < n; i++) {
            System.out.println(i);
            telemetry(i);
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                out.write(Integer.toString(arr[i][j]));
            }
            out.write('\n');
        }
        out.close();
    }

}
