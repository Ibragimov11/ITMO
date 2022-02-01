import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class B {
    static Boolean[] allow;
    static Map<Integer, Map<String, Set<Integer>>> transitions;

    private static Boolean check(String word) {
        Set<Integer> tops = new HashSet();
        tops.add(1);
        Set<Integer> new_tops;
        for (int i = 0; i < word.length(); i++) {
            String letter = Character.toString(word.charAt(i));
            new_tops = new HashSet<>();
            for (int x : tops) {
                if (transitions.get(x).containsKey(letter)) {
                    new_tops.addAll(transitions.get(x).get(letter));
                }
            }
            if (new_tops.isEmpty()) {
                return false;
            } else {
                tops = new_tops;
            }
        }
        for (int x : tops) {
            if (allow[x - 1]) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new File("problem2.in"));
        FileWriter out = new FileWriter("problem2.out");
        String word = in.next();
        int n = in.nextInt();
        int m = in.nextInt();
        int k = in.nextInt();
        allow = new Boolean[n];
        Arrays.fill(allow, false);
        for (int i = 0; i < k; i++) {
            allow[in.nextInt() - 1] = true;
        }
        transitions = new HashMap<>();
        for (int i = 0; i < n; i++) {
            transitions.put(i + 1, new HashMap<>());
        }
        for (int i = 0; i < m; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            String letter = in.next();
            if (!transitions.get(a).containsKey(letter)) {
                transitions.get(a).put(letter, new HashSet<>());
            }
            transitions.get(a).get(letter).add(b);
        }
        if (check(word)) {
            out.write("Accepts");
        } else {
            out.write("Rejects");
        }
        in.close();
        out.close();
    }
}
