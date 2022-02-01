import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class A24 {
    public static void main(String[] args) throws IOException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("nextperm.in"));
        FileWriter out = new FileWriter(new File("nextperm.out"));
        int n = in.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = in.nextInt();
        }
        boolean srt = true;
        boolean srtn = true;
        if (n == 1) {
            out.write("0");
            out.write("\n0");
        } else {
            for (int i = 0; i < n - 1; i++) {
                if (arr[i] < arr[i + 1]) {
                    srtn = false;
                } else if (arr[i] > arr[i + 1]) {
                    srt = false;
                }
            }
            if (srt) {
                for (int j = 0; j < n; j++) {
                    out.write("0 ");
                }
                out.write('\n');
                for (int j = 0; j < n - 2; j++) {
                    out.write(arr[j] + " ");
                }
                out.write(arr[n - 1] + " ");
                out.write(arr[n - 2] + " ");
            } else if (srtn) {
                for (int j = 0; j < n - 2; j++) {
                    out.write(arr[j] + " ");
                }
                out.write(arr[n - 1] + " ");
                out.write(arr[n - 2] + " ");
                out.write('\n');
                for (int j = 0; j < n; j++) {
                    out.write("0 ");
                }
            } else {
                int mn = Integer.MAX_VALUE;
                int j = n - 1;
                ArrayList<Integer> a1 = new ArrayList<>();
                while (arr[j] < mn) {
                    mn = arr[j];
                    a1.add(arr[j]);
                    j--;
                }
                for (int u = 0; u < j; u++) {
                    out.write(arr[u] + " ");
                }
                Collections.sort(a1);
                int v = 0;
                while (a1.get(v) < arr[j]) {
                    v++;
                    if (v == a1.size()) {
                        break;
                    }
                }
                v--;
                out.write(a1.get(v) + " ");
                a1.set(v, arr[j]);
                Collections.sort(a1);
                Collections.reverse(a1);
                for (int x : a1) {
                    out.write(x + " ");
                }
                out.write('\n');

                int mx = Integer.MIN_VALUE;
                j = n - 1;
                ArrayList<Integer> a2 = new ArrayList<>();
                while (arr[j] > mx) {
                    mx = arr[j];
                    a2.add(arr[j]);
                    j--;
                }
                for (int u = 0; u < j; u++) {
                    out.write(arr[u] + " ");
                }
                Collections.sort(a2);
                v = a2.size() - 1;
                while (a2.get(v) > arr[j]) {
                    v--;
                    if (v == -1) {
                        break;
                    }
                }
                v++;
                out.write(a2.get(v) + " ");
                a2.set(v, arr[j]);
                Collections.sort(a2);
                for (int x : a2) {
                    out.write(x + " ");
                }
            }
        }
        out.close();
    }
}
