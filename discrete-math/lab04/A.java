import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class A {
    static Boolean[] allow;
    static Map<Integer, Map<String, Integer>> transitions;

    private static Boolean check(String word) {
        int top = 1;
        for (int i = 0; i < word.length(); i++) {
            String letter = Character.toString(word.charAt(i));
            if (transitions.get(top).containsKey(letter)) {
                top = transitions.get(top).get(letter);
            } else {
                return false;
            }
        }
        return allow[top - 1];
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new File("problem1.in"));
        FileWriter out = new FileWriter("problem1.out");
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
            transitions.get(a).put(letter, b);
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
