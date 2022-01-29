import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class A25 {
    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("nextchoose.in"));
        FileWriter out = new FileWriter(new File("nextchoose.out"));
        int n = in.nextInt();
        int k = in.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < k; i++) {
            arr[i] = in.nextInt();
        }
        int p = n;
        int j = k - 1;
        while (arr[j] == p) {
            p--;
            j--;
            if (j == -1) {
                out.write("-1");
                break;
            }
        }
        if (j != -1) {
            for (int i = 0; i < j; i++) {
                out.write(arr[i] + " ");
            }
            for (int i = j; i < k; i++) {
                if (i == j) {
                    arr[i]++;
                    out.write(arr[i] + " ");
                } else {
                    arr[i] = arr[i - 1] + 1;
                    out.write(arr[i] + " ");
                }
            }
        }
        out.close();
    }
}
