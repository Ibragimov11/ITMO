import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class A4 {
    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("chaincode.in"));
        FileWriter out = new FileWriter(new File("chaincode.out"));
        int n = in.nextInt();
        ArrayList<String> str = new ArrayList<>();
        String s = "";
        for (int i = 0; i < n; i ++) {
            s = s.concat("0");
        }
        str.add(s);
        out.write(s + '\n');
        for (int i = 0; i < Math.pow(2, n); i++) {
            s = str.get(str.size() - 1).substring(1);
            if (!str.contains(s + "1")) {
                str.add(s + "1");
                out.write(s + "1" + '\n');
            } else if (!str.contains(s + "0")) {
                str.add(s + "0");
                out.write(s + "0" + '\n');
            }
        }
        out.close();
    }
}
