package search;

public class BinarySearchMin {

    // Imm: a.length > 1 && 0 < (k - index of min element in a): (for all i = 1..(n- 1): a[i] > a[i - 1] || i == k) && a[0] > a[n - 1]

    // Inv0: Imm || a.length == 1 || a[0] < a[r]
    private static int RecBS(int[] a, int l, int r) {
        // Pred: a[l] < a[r] || (a[l] == a[r] -> a.length == 1)
        if (a[l] <= a[r]) {
            return l;
        }
        // Post: l = result
        // Inv3: (Inv0 && a.length != 1 && !(a[l] <= a[r])) -> a[l] > a[r] && Imm
        // Inv3 -> Inv3.1
        // Inv3.1: 0 <= l < result <= r < a.length
        // Pred: r - l == 1 && Inv3.1
        if (r - l == 1) {
            return r; // r - index
        }
        // Post: r = result
        // Pred: r - l > 1 && Inv3.1
        int m = (l + r) / 2;
        // Post: -1 <= l < m < r < a.length
        // Pred: -1 <= l < m < r < a.length
        if (a[m] < a[r]) {
            // Pred: Inv3 && a[m] < a[r] -> l < result <= m
            r = m;
            // Post: (l < result <= m && l < m == r < r') -> Inv3.1
        } else {
            // Pred: Inv3 && a[m] > a[r] -> m < result <= r
            l = m;
            // Post: (m < result <= r && l' < l == m < r) -> Inv3.1
        }
        // Post: Inv3.1 && r - l < r' - l'
        // Pred: Inv3.1 && r - l < r' - l'
        return RecBS(a, l, r);
    }

    // Imm: a.length > 1 && 0 < (k - index of min element in a): (for all i = 1..(n- 1): a[i] > a[i - 1] || i == k) && a[0] > a[n - 1]

    // Inv0: Imm || a.length == 1 || a[0] < a[a.length - 1]
    private static int IterBS(int[] a) {
        // Pred: True
        int l = 0;
        // Post: l == 0
        // Pred: a.length > 1
        int r = a.length - 1;
        // Post: 0 == l < r == a.length - 1
        // Pred: a[l] < a[r] || (a[l] == a[r] -> a.length == 1)
        if (a[l] <= a[r]) {
            return l; // l - index
        }
        // Post: l = result
        // Inv2: (Inv0 && a.length != 1 && !(a[0] < a[a.length - 1])) -> a[l] > a[r] && Imm
        // Inv2 -> Inv2.1
        // Inv2.1: 0 <= l < result <= r < a.length
        while (r - l > 1) {
            // Pred: r - l > 1 && Inv2.1
            int m = (l + r) / 2;
            // Post: -1 <= l < m < r < a.length
            // Pred: -1 <= l < m < r < a.length
            if (a[m] < a[r]) {
                // Pred: Inv2 && a[m] < a[r] -> l < result <= m
                r = m;
                // Post: (l < result <= m && l < m == r < r') -> Inv2.1
            } else {
                // Pred: Inv2 && a[m] > a[r] -> m < result <= r
                l = m;
                // Post: (m < result <= r && l' < l == m < r) -> Inv2.1
            }
            // Post: Inv2.1 && r - l < r' - l'
        }
        // Post: (Inv2.1 && r - l <= 1) -> result = r
        // Pred: r = result
        return r; // r - index
    }

    // Inv1: forall i = 0..(args.length - 1): args[i] is number && args.length > 0
    public static void main(String[] args) {
        // Pred: args.length > 0
        int n = args.length;
        // Post: 0 < n == args.length
        // Pred: True
        int i = 0;
        // Post: i == 0
        // Pred: n > 0
        int[] a = new int[n];
        // Post: new int[] a && 0 < a.length == n
        // Pred: Inv1
        while (i < n) {
            // Pred: Inv1 -> a[i] is number
            a[i] = Integer.parseInt(args[i]);
            // Post: a[i] = args[i]
            // Pred: True
            i++;
            // Post: i = i' + 1
        }
        // Post: forall i = 0..(a.length - 1): a[i] == args[i]
        // Pred: True
        int r1 = IterBS(a);
        // Post: r1 = result1
        // Pred: True
        int r2 = RecBS(a, 0, a.length - 1);
        // Post: r2 = result2
        // Pred: r1 == r2
        if (r1 == r2) {
            System.out.println(a[r1]);
        } else {
            System.out.println("The code is incorrect, because r1 =  " + r1 + " and r2 = " + r2);
        }
        // Post: result == r1 == r2
    }

}
