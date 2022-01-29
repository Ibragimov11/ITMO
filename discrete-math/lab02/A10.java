import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class A10 {

    static int n;
    static FileWriter out;

    private static void partition(ArrayList<Integer> arr) throws IOException {
        int sum = 0;
        for (int elem : arr) {
            sum += elem;
        }
        if (sum == n) {
            int l = arr.size();
            out.write(Integer.toString(arr.get(l - 1)));
            l--;
            if (l > 0) {
                for (int j = l - 1; l > 0; l--) {
                    out.write("+" + arr.get(l - 1));
                }
            }
            out.write('\n');
        } else if (sum < n) {
            for (int j = Math.min(n - sum, arr.get(arr.size() - 1)); j > 0; j--) {
                ArrayList<Integer> arr1 = new ArrayList<>(arr);
                arr1.add(j);
                partition(arr1);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        //Scanner in = new Scanner(new File("partition.in"));
        out = new FileWriter(new File("partition.out"));
        n = in.nextInt();
        for (int i = n; i >= 1; i--) {
            ArrayList<Integer> a = new ArrayList<>();
            a.add(i);
            partition(a);
        }
        out.close();
    }
}
