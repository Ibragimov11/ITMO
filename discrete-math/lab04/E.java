import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
//RTE на 32 тесте
public class E {
    static Set<Integer> allow;
    static Set<Integer> allow_dka;
    static Long[][] ways;
    static int[][] dka;
    static Set<Integer> visited;
    static Map<Integer, Map<Character, Set<Integer>>> transitions;
    static Map<Set<Integer>, Integer> set_to_index = new HashMap<>();

    private static int generate_index(Set<Integer> set) {
        if (!set_to_index.containsKey(set)) {
            set_to_index.put(set, set_to_index.size() + 1);
        }
        return set_to_index.get(set);
    }

    private static void nka_to_dka() {
        Deque<Set<Integer>> queue = new ArrayDeque<>();
        Set<Integer> arr = new HashSet<>();
        arr.add(1);
        queue.addLast(arr);
        while (!queue.isEmpty()) {
            Set<Integer> Gosha = queue.pollFirst();
            int q = generate_index(Gosha);
            for (int x : Gosha) {
                if (allow.contains(x)) {
                    allow_dka.add(q);
                    break;
                }
            }
            visited.add(q);
            for (char i = 'a'; i <= 'z'; i++) {
                Set<Integer> set = new HashSet<>();
                for (int x : Gosha) {
                    if (transitions.get(x).containsKey(i)) {
                        set.addAll(transitions.get(x).get(i));
                    }
                }
                if (!set.isEmpty()) {
                    int p = generate_index(set);
                    dka[q][i - 'a'] = p;
                    if (!visited.contains(p)) {
                        queue.addLast(set);
                    }
                }
            }
        }
    }

    private static long check(int l) {
        Set<Integer> tops = new HashSet<>();
        tops.add(1);
        for (int i = 0; i < l; i++) {
            Set<Integer> temp_tops = new HashSet<>();
            for (int x : tops) {
                for (int j = 0; j < 26; j++) {
                    if (dka[x][j] != 0) {
                        temp_tops.add(dka[x][j]);
                        ways[i + 1][dka[x][j]] += ways[i][x] % 1000000007;
                    }
                }
            }
            tops = temp_tops;
        }
        long count = 0;
        for (int i = 0; i < ways[l].length; i++) {
            if (allow_dka.contains(i)) {
                count += ways[l][i] % 1000000007;
            }
        }
        return count % 1000000007;
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new File("problem5.in"));
        FileWriter out = new FileWriter("problem5.out");
        int n = in.nextInt();
        int m = in.nextInt();
        int k = in.nextInt();
        int l = in.nextInt();
        ways = new Long[l + 1][(int) Math.pow(2, n) + 1];
        for (Long[] way : ways) {
            Arrays.fill(way, (long) 0);
        }
        ways[0][1] = (long) 1;
        allow = new HashSet<>();
        for (int i = 0; i < k; i++) {
            allow.add(in.nextInt());
        }
        transitions = new HashMap<>();
        dka = new int[(int) Math.pow(2, n) + 1][26];
        allow_dka = new HashSet<>();
        if (allow.contains(1)) {
            allow_dka.add(1);
        }
        visited = new HashSet<>();
        for (int i = 0; i < n; i++) {
            transitions.put(i + 1, new HashMap<>());
        }
        for (int i = 0; i < m; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            char letter = in.next().charAt(0);
            if (!transitions.get(a).containsKey(letter)) {
                transitions.get(a).put(letter, new HashSet<>());
            }
            transitions.get(a).get(letter).add(b);
        }
        nka_to_dka();
        out.write(Long.toString(check(l)));
        in.close();
        out.close();
    }
}
