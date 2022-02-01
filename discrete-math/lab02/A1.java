import java.util.Scanner;
import java.io.*;

public class A1 {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        //Scanner in = new Scanner(new File("allvectors.in"));
        FileWriter out = new FileWriter(new File("allvectors.out"));
        int n = in.nextInt();
        char[] c = new char[n];
        int k = (int) Math.pow(2, n);
        for (int i = 0; i < k; i++) {
            String str = String.format("%16s", Integer.toBinaryString(i)).replace(' ', '0');
            str.getChars(16 - n, 16, c, 0);
            for (int j = 0; j < n; j ++) {
                out.write(c[j]);
                System.out.print(c[j]);
            }
            out.write("\n");
            System.out.println();
        }
        out.close();
    }
}
