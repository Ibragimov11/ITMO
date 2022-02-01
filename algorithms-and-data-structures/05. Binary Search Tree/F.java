import java.io.*;

public class F {
    static PrintWriter out = new PrintWriter(System.out);
    static MyScanner in;

    private static class Node {
        private Node left, right;
        private final int x;
        private final double y;
        private int count;

        public Node(int x) {
            this.y = Math.random();
            this.x = x;
            this.count = 1;
        }
    }

    public static void main(String[] args) throws IOException {
//        in = new MyScanner(new FileInputStream(args[0]));
        in = new MyScanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        String s;
        Node tree = null;
        for (int i = 0; i < n; i ++) {
            tree = add(tree, i, new Node(in.nextInt()));
        }
        int i, x;
        while (m-- > 0) {
            s = in.next();
            i = in.nextInt();
            if (s.equals("add")) {
                x = in.nextInt();
                n++;
                tree = add(tree, i, new Node(x));
            } else {
                n--;
                tree = delete(tree, i);
            }
        }
        out.write(n + "\n");
        print(tree);
        out.flush();
    }

    private static Node[] split(Node node, int i) {
        if (node == null) {
            return new Node[] { null, null };
        }
        int count = node.left == null ? 0 : node.left.count;
        Node[] p;
        if (i > count ) {
            p = split(node.right, i - count - 1);
            node.right = p[0];
            node.count = 1;
            node.count += node.left == null ? 0 : node.left.count;
            node.count += node.right == null ? 0 : node.right.count;
            return new Node[]{node, p[1]};
        } else {
            p = split(node.left, i);
            node.left = p[1];
            node.count = 1;
            node.count += node.left == null ? 0 : node.left.count;
            node.count += node.right == null ? 0 : node.right.count;
            return new Node[] {p[0], node};
        }
    }

    private static Node merge(Node a, Node b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
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

    private static Node delete(Node a, int i) {
        Node[] p = split(a, i - 1);
        p[1] = split(p[1], 1)[1];
        p[1] = merge(p[0], p[1]);
        return p[1];
    }

    private static void print(Node a) {
        if (a != null) {
            print(a.left);
            out.write(a.x + " ");
            print(a.right);
        }
    }

}

//class MyScanner {
//    final private Reader reader;
//
//    public MyScanner(InputStream input) {
//        reader = new BufferedReader(new InputStreamReader(input));
//    }
//
//    public int nextInt() throws IOException {
//        return Integer.parseInt(next());
//    }
//
//    public String next() throws IOException {
//        int read = reader.read();
//        StringBuilder sb = new StringBuilder();
//        while (Character.isWhitespace((char) read)) {
//            read = reader.read();
//        }
//        while (!Character.isWhitespace((char) read) && read != -1) {
//            sb.append((char) read);
//            read = reader.read();
//        }
//        return sb.toString();
//    }
//
//    public void close() throws IOException {
//        reader.close();
//    }
//}