import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
//номер по сочетанию
public class A16 {

    private static Long Pascal(ArrayList<Long> a, long k, long n) {
        if (a.size() == n + 1) {
            return a.get((int) k);
        } else {
            ArrayList<Long> a1 = new ArrayList<>();
            a1.add(1L);
            for (int i = 0; i < a.size() - 1; i++) {
                a1.add(a.get(i) + a.get(i + 1));
            }
            a1.add(1L);
            return Pascal(a1, k, n);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        //Scanner in = new Scanner(new File("choose2num.in"));
        FileWriter out = new FileWriter(new File("choose2num.out"));
        long n = in.nextLong();
        long k = in.nextLong();
        ArrayList<Long> arr = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            arr.add(in.nextLong());
        }
        Collections.sort(arr);
        long num = 0;
        long i = 1;
        int j = 0;
        while (j < k) {
            while (i < arr.get(j)) {
                num += Pascal(new ArrayList<>(),k - j - 1, n - i);
                i++;
            }
            i++;
            j++;
        }
        out.write(Long.toString(num));
        out.close();
    }
}
