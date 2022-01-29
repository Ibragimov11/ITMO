import java.io.*;

public class H {
    static PrintWriter out = new PrintWriter(System.out);
    static MyScanner in;

    private static class Node {
        private Node left, right;
        private final int x;
        private final double y;
        private int count;
        private boolean rev;

        public Node(int x) {
            this.y = Math.random();
            this.x = x;
            this.count = 1;
            this.rev = false;
        }
    }

    public static void main(String[] args) throws IOException {
//        in = new MyScanner(new FileInputStream(args[0]));
        in = new MyScanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        Node tree = null;
        for (int i = 0; i < n; i ++) {
            tree = add(tree, i, new Node(i + 1));
        }
        int l, r;
        while (m-- > 0) {
            l = in.nextInt();
            r = in.nextInt();
            tree = reverse(tree, l, r);
        }
        print(tree);
        out.flush();
    }

    private static Node[] split(Node a, int i) {
        if (a == null) {
            return new Node[] { null, null };
        }
        push(a);
        int count = a.left == null ? 0 : a.left.count;
        Node[] p;
        if (i > count ) {
            p = split(a.right, i - count - 1);
            a.right = p[0];
            a.count = 1;
            a.count += a.left == null ? 0 : a.left.count;
            a.count += a.right == null ? 0 : a.right.count;
            return new Node[]{a, p[1]};
        } else {
            p = split(a.left, i);
            a.left = p[1];
            a.count = 1;
            a.count += a.left == null ? 0 : a.left.count;
            a.count += a.right == null ? 0 : a.right.count;
            return new Node[] {p[0], a};
        }
    }

    private static Node merge(Node a, Node b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        push(a);
        push(b);
        if (a.y > b.y) {
            a.count += b.count;
            a.right = merge(a.right, b);
            return a;
        } else {
            b.count += a.count;
            b.left = merge(a, b.left);
            return b;
        }
    }

    private static Node add(Node a, int i, Node node) {
        Node[] p = split(a, i);
        p[0] = merge(p[0], node);
        p[1] = merge(p[0], p[1]);
        return p[1];
    }

    private static Node reverse(Node a, int l, int r) {
        Node[] p1 = split(a, r);
        Node[] p2 = split(p1[0], l - 1);
        if (p2[1] != null) {
            p2[1].rev = !p2[1].rev;
        }
        return merge(p2[0], merge(p2[1], p1[1]));
    }

    private static void push(Node a) {
        if (a.rev) {
            Node t = a.left;
            a.left = a.right;
            a.right = t;
            if (a.left != null) {
                a.left.rev = !a.left.rev;
            }
            if (a.right != null) {
                a.right.rev = !a.right.rev;
            }
        }
        a.rev = false;
    }

    private static void print(Node a) {
        if (a != null) {
            push(a);
            print(a.left);
            out.write(a.x + " ");
            print(a.right);
        }
    }

}

class MyScanner {
    final private Reader reader;

    public MyScanner(InputStream input) {
        reader = new BufferedReader(new InputStreamReader(input));
    }

    public int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    public String next() throws IOException {
        int read = reader.read();
        StringBuilder sb = new StringBuilder();
        while (Character.isWhitespace((char) read)) {
            read = reader.read();
        }
        while (!Character.isWhitespace((char) read) && read != -1) {
            sb.append((char) read);
            read = reader.read();
        }
        return sb.toString();
    }

    public void close() throws IOException {
        reader.close();
    }
}