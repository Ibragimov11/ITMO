import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class A27 {

    static FileWriter out;

    private static void nextb(String s) throws IOException {
        int counter_close = 0;
        int counter_open = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '(') {
                counter_open++;
                if (counter_close > counter_open) {
                    break;
                }
            } else {
                counter_close++;
            }
        }
        s = s.substring(0, s.length() - counter_close - counter_open);
        if (s.length() == 0) {
            out.write("-");
        } else {
            s = s + ")";
            out.write(s);
            for (int j = 1; j <= counter_open; j++) {
                out.write("(");
            }
            for (int j = 1; j < counter_close; j++) {
                out.write(")");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("nextbrackets.in"));
        out = new FileWriter(new File("nextbrackets.out"));
        String s = in.next();
        nextb(s);
        out.close();
    }
}
