import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class A7 {

    static int n;
    static FileWriter out;

    private static void gen(ArrayList<Integer> arr) throws IOException{
        if (arr.size() == n) {
            for (int i = 0; i < n; i++) {
                //System.out.print(arr.get(i) + " ");
                out.write(arr.get(i) + " ");
            }
            //System.out.println();
            out.write('\n');
        } else {
            for (int i = 1; i <= n; i++) {
                if (!arr.contains(i)) {
                    ArrayList<Integer> a = new ArrayList<>();
                    if (arr.size() != 0) {
                        a.addAll(arr);
                    }
                    a.add(i);
                    gen(a);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        //Scanner in = new Scanner(new File("permutations.in"));
        out = new FileWriter(new File("permutations.out"));
        n = in.nextInt();
        ArrayList<Integer> arr = new ArrayList<>();
        gen(arr);
        out.close();
    }
}