package PCMS;

import java.util.*;

class MyList {
    private int[] data;
    private int dl;

    public MyList() {
        data = new int[1];
        dl = 0;
    }

    public int size() {
        return dl;
    }

    public void add(int x) {
        if (data.length == dl) {
            data = Arrays.copyOf(data, dl * 2);
        }
        data[dl++] = x;
    }

    public int get(int index) {
        return data[index];
    }
}

public class E {
    public static int[] c, h, b;
    public static MyList[] a;

    public static void def(int u) {
        int l = 0, r = 1;
        List<Integer> q = new ArrayList<>();
        int n = a.length;
        for (int i = 0; i < n; i++) {
            h[i] = -1;
        }
        h[u] = 0;
        q.add(u);

        while (r - l > 0) {
            int cur = q.get(l);
            l++;

            for (int i = 0; i < a[cur].size(); i++) {
                int v = a[cur].get(i);
                if (v != cur) {
                    if (h[v] == -1) {
                        b[v] = cur;
                        h[v] = h[cur] + 1;
                        q.add(v);
                        r++;
                    }
                }
            }

        }

    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int mx = Integer.MIN_VALUE;
        c = new int[n];
        h = new int[n];
        b = new int[n];
        int[] d = new int[n];
        a = new MyList[n];

        for (int i = 0; i < n; i++) {
            a[i] = new MyList();
        }

        for (int i = 0; i < n - 1; i++) {
            int x = in.nextInt() - 1;
            int y = in.nextInt() - 1;
            a[x].add(y);
            a[y].add(x);
        }

        for (int i = 0; i < m; i++) {
            c[i] = in.nextInt() - 1;
        }

        h[c[0]] = 0;
        def(c[0]);
        int f = -1;
        for (int i = 0; i < m; i++) {
            mx = Integer.max(mx, h[c[i]]);
            if (mx == h[c[i]]) {
                f = c[i];
            }
        }

        int x = f;
        int id = 0;
        while (true) {
            if (x == c[0]) {
                d[id++] = x;
                break;
            }
            d[id++] = x;
            x = b[x];
        }

        if (id % 2 == 0) {
            System.out.println("NO");
            return;
        }

        int w = d[id / 2];
        h[w] = 0;
        def(w);
        for (int i = 0; i < m; i++) {
            if (h[c[i]] != h[c[0]]) {
                System.out.println("NO");
                return;
            }
        }

        System.out.println("YES");
        System.out.print(w + 1);

    }
}
