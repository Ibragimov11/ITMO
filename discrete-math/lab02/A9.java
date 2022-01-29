import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class A9 {

    static int n;
    static FileWriter out;

    public static void brackets(int open, int close, String s) throws IOException {
        if (close == n) {
            out.write(s + '\n');
        } else {
            if (open < n) {
                brackets(open + 1, close, s + "(");
            }
            if (open > close) {
                brackets(open, close + 1, s + ")");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        //Scanner in = new Scanner(new File("brackets.in"));
        out = new FileWriter(new File("brackets.out"));
        n = in.nextInt();
        String s = "";
        brackets(0, 0, s);
        out.close();
    }
}
