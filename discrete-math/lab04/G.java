import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class G {
    static Boolean[] allow1;
    static Boolean[] allow2;
    static Boolean[][] used;
    static int[][] trans1;
    static int[][] trans2;

    private static Boolean check() {
        Deque<int[]> queue = new ArrayDeque<>();
        queue.addLast(new int[]{1, 1});
        while (!queue.isEmpty()) {
            int[] first = queue.pollFirst();
            int u = first[0];
            int v = first[1];
            if (allow1[u] != allow2[v]) {
                return false;
            }
            used[u][v] = true;
            for (int i = 0; i < 26; i++) {
                if ((trans1[u][i] > 0 || trans2[v][i] > 0) && !used[trans1[u][i]][trans2[v][i]]) {
                    queue.addLast(new int[]{trans1[u][i], trans2[v][i]});
                }
            }

        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new File("equivalence.in"));
        int n1 = in.nextInt();
        int m1 = in.nextInt();
        int k1 = in.nextInt();
        allow1 = new Boolean[n1 + 1];
        Arrays.fill(allow1, false);
        for (int i = 0; i < k1; i++) {
            allow1[in.nextInt()] = true;
        }
        trans1 = new int[n1 + 1][26];
        for (int[] x : trans1) {
            Arrays.fill(x, 0);
        }
        for (int i = 0; i < m1; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            int letter = in.next().charAt(0) - 'a';
            trans1[a][letter] = b;
        }
        int n2 = in.nextInt();
        int m2 = in.nextInt();
        int k2 = in.nextInt();
        allow2 = new Boolean[n2 + 1];
        Arrays.fill(allow2, false);
        for (int i = 0; i < k2; i++) {
            allow2[in.nextInt()] = true;
        }
        trans2 = new int[n2 + 1][26];
        for (int[] x : trans2) {
            Arrays.fill(x, 0);
        }
        for (int i = 0; i < m2; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            int letter = in.next().charAt(0) - 'a';
            trans2[a][letter] = b;
        }
        in.close();
        FileWriter out = new FileWriter("equivalence.out");
        used = new Boolean[n1 + 1][n2 + 1];
        for (Boolean[] x : used) {
            Arrays.fill(x, false);
        }
        if (check()) {
            out.write("YES");
        } else {
            out.write("NO");
        }
        out.close();
    }
}
