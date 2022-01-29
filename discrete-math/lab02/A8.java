import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class A8 {

    static int n, k;
    static FileWriter out;

    private static void permutation(ArrayList<Integer> arr, int a) throws IOException {
        arr.add(a);
        if (arr.size() == k) {
            for (int i = 0; i < k; i++) {
                out.write(arr.get(i) + " ");
            }
            out.write('\n');
        } else {
            for (int i = a + 1; i <= n; i++) {
                if (!arr.contains(i)) {
                    ArrayList<Integer> arrnew = new ArrayList<>(arr);
                    permutation(arrnew, i);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("choose.in"));
        out = new FileWriter(new File("choose.out"));
        n = in.nextInt();
        k = in.nextInt();
        for (int i = 1; i <= n; i++) {
            permutation(new ArrayList<>(), i);
        }
        out.close();
    }
}
