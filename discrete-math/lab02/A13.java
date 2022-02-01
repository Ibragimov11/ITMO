import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
//перестановка по номеру
public class A13 {

    public static long getFactorial(long f) {
        long result = 1;
        for (int i = 1; i <= f; i++) {
            result = result * i;
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        //Scanner in = new Scanner(new File("num2perm.in"));
        FileWriter out = new FileWriter(new File("num2perm.out"));
        long n = in.nextLong();
        long k = in.nextLong();
        long p = 1;
        ArrayList<Long> arr = new ArrayList<>();
        while (arr.size() != n) {
            if (!arr.contains(p)) {
                if (k >= getFactorial(n - 1 - arr.size())) {
                    k -= getFactorial(n - 1 - arr.size());
                    p++;
                } else {
                    arr.add(p);
                    p = 1;
                }
            } else {
                p++;
            }
        }
        for (long x: arr) {
            out.write(x + " ");
        }
        out.close();
    }
}
