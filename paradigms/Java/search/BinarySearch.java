package search;

public class BinarySearch {
    // Inv3: a.length = 1 || forall i = 1..(a.length - 1): a[i] <= a[i - 1]
    private static int RecBS(int x, int[] a, int l, int r) {
        // Imm: l < r
        // Pred: a.length >= 0
        int len = a.length;
        // Post: len = a.length && len >= 0
        // Pred: len == 0 || a[len - 1] > x)
        if (len == 0 || a[len - 1] > x) {
            return len;
        }
        // Post: result = a.length
        // Pred: Inv3.1 && r - l == 1
        if (r - l == 1) {
            return r;
        }
        // Post: result = r
        // Inv3.1: -1 <= l < result <= r <= a.length && r - l > 0
        // Pred: r - l > 1 && Inv3.1
        int m = (r + l) / 2;
        // Post: m = (r - l) / 2 && Inv3.1 -> 0 <= m < a.length
        // Pred: 0 <= m < a.length && Inv3.1
        if (a[m] > x) {
            // Pred: 0 <= m < a.length && Inv3.1 && a[m] > x
            l = m;
            // Post: l == m && Inv3.1
        } else {
            // Pred: Inv3.1 && a[m] <= x
            r = m;
            // Post: r == m && Inv3.1
        }
        // Post: (l == m || r == m) && Inv3.1
        return RecBS(x, a, l, r);
    }

    // Inv2: a.length = 0 || forall i = 1..(a.length - 1): a[i] <= a[i - 1]
    private static int IterBS(int x, int[] a) {
        // Pred: a.length >= 0
        int len = a.length;
        // Post: len = a.length && len >= 0
        // Pred: len == 0 || a[len - 1] > x)
        if (len == 0 || a[len - 1] > x) {
            return len;
        }
        // Post: result = a.length
        // Pred: len > 0 && a[len - 1] <= x
        int l = -1;
        // Post: l = -1 && a[len - 1] <= x
        // Pred: len > 0 && a[len - 1] <= x
        int r = len;
        // Post: (0 < r = a.length && a[len - 1] <= x) -> result <= r
        // Inv2.1: -1 <= l < result <= r <= a.length
        while (r - l > 1) {
            // Pred: r - l > 1 && Inv2.1
            int m = (r + l) / 2;
            // Post: m = (r + l) / 2 && -1 <= l < m < r <= a.length
            // Pred: -1 <= l < m < r <= a.length
            if (a[m] > x) {
                // Pred: -1 <= l < m < r <= a.length && a[m] > x && l < result <= r
                l = m;
                // Post: l = m && -1 <= l < r <= a.length && (a[l] > x) -> l < result <= r
            } else {
                // Pred: -1 <= l < m < r <= a.length && a[m] <= x && l < result <= r
                r = m;
                // Post: r = m && -1 <= l < r <= a.length && (a[r] <= x) -> l < result <= r
            }
            // Post: Inv2.1
        }
        //Post: (Inv 2.1 && r - l == 1) -> result = r
        return r;
    }

    // Inv1: forall i = 0..(args.length - 1): args[i] is number && args.length > 0
    public static void main(String[] args) {
        // Pred: args.length > 0
        int n = args.length - 1;
        // Post: n >= 0
        // Pred: Inv1
        int x = Integer.parseInt(args[0]);
        // Post: x = a[0] && x is int && Inv1
        // Pred: True
        int i = 0;
        // Post: i = 0;
        // Pred: n >= 0
        int[] a = new int[n];
        // Post: new int[] a && a.length == n && n >= 0
        // Pred: n == a.length && i == 0 && n >= 0 && Inv1
        while (i < n) {
            // Pred: 0 <= i < a.length && args[i + 1] is number
            a[i] = Integer.parseInt(args[i + 1]);
            // a[i] == args[i + 1] && i' = i
            // Pred: i' < a.length
            i++;
            // Post: i = i' + 1 && i <= a.length
        }
        // Post: forall i = 0..(a.length - 1): a[i] = args[i + 1] && i == a.length
        // Pred: x is int && a is array of ints
        int r1 = IterBS(x, a);
        // Post: r1 = result1
        // Pred: x is int && a is array of ints
        int r2 = RecBS(x, a, -1, a.length);
        // Post: r2 = result2
        // Pred: r1 == r2
        if (r1 == r2) {
            System.out.println(r1);
        } else {
            System.out.println("The code is incorrect, because r1 =  " + r1 + " and r2 = " + r2);
        }
        // Post: result == r1 == r2
    }

}
