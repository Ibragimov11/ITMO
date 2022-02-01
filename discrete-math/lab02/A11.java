import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class A11 {

    static int n;
    static FileWriter out;

    public static void subset(ArrayList<Integer> arr) throws IOException {
        for (int x : arr) {
            out.write(x + " ");
        }
        out.write('\n');
        int k = 1;
        if (arr.size() != 0) {
            k = arr.get(arr.size() - 1) + 1;
        }
        if (arr.size() != n) {
            for (int i = k; i <= n; i++) {
                if (!arr.contains(i)) {
                    ArrayList<Integer> a = new ArrayList<>(arr);
                    a.add(i);
                    subset(a);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("subsets.in"));
        out = new FileWriter(new File("subsets.out"));
        n = in.nextInt();
        ArrayList<Integer> arr = new ArrayList<>();
        subset(arr);
        out.close();
    }

}
