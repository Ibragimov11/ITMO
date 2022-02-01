import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class D {
    static Boolean[] allow;
    static Long[][] ways;
    static int[][] transitions;

    private static long check(int l) {
        Set<Integer> tops = new HashSet<>();
        tops.add(1);
        for (int i = 0; i < l; i++) {
            Set<Integer> temp_tops = new HashSet<>();
            for (int x : tops) {
                for (int j = 0; j < 26; j++) {
                    if (transitions[x][j] != 0) {
                        temp_tops.add(transitions[x][j]);
                        ways[i + 1][transitions[x][j]] += ways[i][x] % 1000000007;
                    }
                }
            }
            tops = temp_tops;
        }
        long count = 0;
        for (int i = 0; i < ways[l].length; i++) {
            if (allow[i]) {
                count += ways[l][i] % 1000000007;
            }
        }
        return count % 1000000007;
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new File("problem4.in"));
        FileWriter out = new FileWriter("problem4.out");
        int n = in.nextInt();
        int m = in.nextInt();
        int k = in.nextInt();
        int l = in.nextInt();
        allow = new Boolean[n + 1];
        ways = new Long[l + 1][n + 1];
        Arrays.fill(allow, false);
        for (Long[] way : ways) {
            Arrays.fill(way, (long) 0);
        }
        ways[0][1] = (long) 1;
        for (int i = 0; i < k; i++) {
            allow[in.nextInt()] = true;
        }
        transitions = new int[n + 1][26];
        for (int i = 0; i < m; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            int letter = in.next().charAt(0) - 'a';
            transitions[a][letter] = b;
        }
        out.write(Long.toString(check(l)));
        in.close();
        out.close();
    }
}
