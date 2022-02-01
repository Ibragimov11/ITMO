import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class A23 {
    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("nextvector.in"));
        FileWriter out = new FileWriter(new File("nextvector.out"));
        String s = in.next();
        int n = s.length();
        boolean one = false;
        boolean zero = false;
        char[] a = new char[n];
        s.getChars(0, n, a, 0);

        for (int i = 0; i < n; i++) {
            if (a[i] == '0') {
                zero = true;
                break;
            }
        }

        for (int i = 0; i < n; i++) {
            if (a[i] == '1') {
                one = true;
                break;
            }
        }

        if (!zero) {
            a[n - 1] = '0';
            for (char x : a) {
                out.write(x);
            }
            out.write("\n-");
        } else if (!one) {
            out.write("-\n");
            for (int i = 0; i < n - 1; i++) {
                out.write("0");
            }
            out.write("1");
        } else {
            int j = n - 1;
            while (a[j] != '1') {
                j--;
            }
            for (int i = 0; i < j; i++) {
                out.write(a[i]);
            }
            for (int i = j; i < n; i++) {
                if (a[i] == '1') {
                    out.write("0");
                } else {
                    out.write("1");
                }
            }
            out.write('\n');
            j = n - 1;
            while (a[j] != '0') {
                j--;
            }
            for (int i = 0; i < j; i++) {
                out.write(a[i]);
            }
            for (int i = j; i < n; i++) {
                if (a[i] == '1') {
                    out.write("0");
                } else {
                    out.write("1");
                }
            }
        }
        out.close();
    }
}
