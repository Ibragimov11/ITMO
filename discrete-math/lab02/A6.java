import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class A6 {

    static int n;
    static FileWriter out;

    public static int getFib(int n) {
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(1); arr.add(1);
        while (arr.size() != n + 2) {
            arr.add(arr.get(arr.size() - 1) + arr.get(arr.size() - 2));
        }
        return arr.get(n + 1);
    }

    public static void vvd(String str) throws IOException {
        if (str.length() == 0) {
            vvd(str + "0");
            vvd(str + "1");
        } else if (str.length() == n) {
            //System.out.println(str);
            out.write('\n' + str);
        } else {
            if (str.charAt(str.length() - 1) == '0') {
                vvd(str + "0");
                vvd(str + "1");
            } else {
                vvd(str + "0");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("vectors.in"));
        out = new FileWriter(new File("vectors.out"));
        n = in.nextInt();
        //System.out.println(getFib(n));
        out.write(Integer.toString(getFib(n)));
        vvd("");
        out.close();
    }
}
