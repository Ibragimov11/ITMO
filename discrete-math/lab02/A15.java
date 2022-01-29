import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class A15 {

    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("num2choose.in"));
        FileWriter out = new FileWriter(new File("num2choose.out"));
        int n = in.nextInt();
        int k = in.nextInt();
        long m = in.nextLong();
        long num = 0;
        int i = 1;
        int z = 0;
        long[][] tr = new long[31][31];
        for (int q = 0; q < 31; q++) {
            for (int w = 0; w <= q; w++) {
                if (w == 0 || w == q) {
                    tr[q][w] = 1;
                } else {
                    tr[q][w] = tr[q - 1][w - 1] + tr[q - 1][w];
                }
            }
        }
        while (z != k) {
            while (num + tr[n - i][k - z - 1] <= m) {
                num += tr[n - i][k - z - 1];
                i++;
            }
            out.write(i + " ");
            i++;
            z++;
        }
        out.close();
    }
}