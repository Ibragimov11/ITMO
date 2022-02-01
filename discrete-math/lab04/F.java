import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class F {
    static Boolean[] allow1;
    static Boolean[] allow2;
    static Boolean[] visited;
    static int[][] trans1;
    static int[][] trans2;

    private static Boolean check(int v1, int v2) {
        visited[v1 - 1] = true;
        if (allow1[v1 - 1] != allow2[v2 - 1]) {
            return false;
        }
        for (int i = 0; i < 26; i++) {
            if (trans1[v1 - 1][i] == 0 && trans2[v2 - 1][i] != 0 || trans2[v2 - 1][i] == 0 && trans1[v1 - 1][i] != 0) {
                return false;
            }
        }
        boolean bool = true;
        for (int i = 0; i < 26; i++) {
            if (trans1[v1 - 1][i] == 0) {
                continue;
            }
            if (!visited[trans1[v1 - 1][i] - 1]) {
                bool = check(trans1[v1 - 1][i], trans2[v2 - 1][i]);
            }
            if (!bool) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new File("isomorphism.in"));
        int n1 = in.nextInt();
        int m1 = in.nextInt();
        int k1 = in.nextInt();
        allow1 = new Boolean[n1];
        Arrays.fill(allow1, false);
        for (int i = 0; i < k1; i++) {
            allow1[in.nextInt() - 1] = true;
        }
        trans1 = new int[n1][26];
        for (int[] x : trans1) {
            Arrays.fill(x, 0);
        }
        for (int i = 0; i < m1; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            int letter = in.next().charAt(0) - 'a';
            trans1[a - 1][letter] = b;
        }
        int n2 = in.nextInt();
        int m2 = in.nextInt();
        int k2 = in.nextInt();
        allow2 = new Boolean[n2];
        Arrays.fill(allow2, false);
        for (int i = 0; i < k2; i++) {
            allow2[in.nextInt() - 1] = true;
        }
        trans2 = new int[n2][26];
        for (int[] x : trans2) {
            Arrays.fill(x, 0);
        }
        for (int i = 0; i < m2; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            int letter = in.next().charAt(0) - 'a';
            trans2[a - 1][letter] = b;
        }
        in.close();
        FileWriter out = new FileWriter("isomorphism.out");
        if (n1 != n2 || m1 != m2 || k1 != k2) {
            out.write("NO");
        } else {
            visited = new Boolean[n1];
            Arrays.fill(visited, false);
            if (check(1, 1)) {
                out.write("YES");
            } else {
                out.write("NO");
            }
        }
        out.close();
    }
}
