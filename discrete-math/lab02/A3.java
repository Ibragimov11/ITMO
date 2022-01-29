import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class A3 {
    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("antigray.in"));
        FileWriter out = new FileWriter(new File("antigray.out"));
        int n = in.nextInt();
        int k = (int) Math.pow(3, n);
        for (int i = 0; i < k / 3; i ++) {
            int q = i;
            int[] arr = new int[n];
            for (int j = 0; j < n; j++) {
                arr[n - 1 - j] = q % 3;
                q /= 3;
            }
            for (int j = 0; j < 3; j++) {
                for (int x: arr) {
                    //System.out.print((x + j) % 3);
                    out.write(Integer.toString((x + j) % 3));
                }
                //System.out.println();
                out.write('\n');
            }
        }
        out.close();
    }
}
