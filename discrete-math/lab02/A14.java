import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class A14 {

    private static long getFactorial(long f) {
        long result = 1;
        for (int i = 1; i <= f; i++) {
            result = result * i;
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("perm2num.in"));
        FileWriter out = new FileWriter(new File("perm2num.out"));
        long num = 0;
        int n = in.nextInt();
        ArrayList<Long> a = new ArrayList<>();
        for (long i = 0; i <= n; i++) {
            a.add(i);
        }
        long[] b = new long[n];
        for (int i = 0; i < n; i ++) {
            b[i] = in.nextLong();
        }
        for (int i = 0; i < n; i++) {
            num += (a.indexOf(b[i]) - 1) * getFactorial(n - 1 - i);
            a.remove(b[i]);
        }
        out.write(Long.toString(num));
        out.close();
    }
}
