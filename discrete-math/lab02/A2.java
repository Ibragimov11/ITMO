import java.io.*;
import java.util.Scanner;

public class A2 {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        //Scanner in = new Scanner(new File("gray.in"));
        FileWriter out = new FileWriter(new File("gray.out"));
        int n = in.nextInt();
        int m = (int) Math.pow(2, n);
        int[][] arr = new int[m][n];
        for (int i = 0; i < n; i++) {
            int k = (int) Math.pow(2, i);
            for (int j = k; j < k + k; j++) {
                for (int l = 0; l <= i; l++) {
                    int l1 = n - 1 - l;
                    if (l == i) {
                        arr[j][l1] = 1;
                    } else {
                        arr[j][l1] = arr[(int) Math.pow(2, i + 1) - 1 - j][l1];
                    }
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                //System.out.print(arr[i][j]);
                out.write(Integer.toString(arr[i][j]));
            }
            //System.out.println();
            out.write("\n");
        }
        out.close();
    }
}
