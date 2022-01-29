import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class D {
    private static class Node {
        private Node left, right;
        private final int x;
        private final double y;
        private long sum;

        public Node(int x) {
            this.x = x;
            this.sum = x;
            this.y = Math.random();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
//        Scanner in = new Scanner(new FileInputStream(args[0]));
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Node tree = null;
        String s;
        int l, r = 0;
        long d = 0;
        boolean b = false;
        while (n-- > 0) {
            s = in.next();
            l = in.nextInt();
            if (s.equals("?")) {
                r = in.nextInt();
            }
            if (s.equals("+")) {
                if (!b) {
                    if (!exists(tree, l)) {
                        tree = insert(tree, new Node(l));
                    }
                } else {
                    if (!exists(tree, (int) ((l + d) % 1e9))) {
                        tree = insert(tree, new Node((int) ((l + d) % 1e9)));
                    }
                }
                b = false;
            } else {
                d = sum(tree, l, r, false, false);
                b = true;
                System.out.println(d);
            }
        }
//        print(tree);
        in.close();
    }

    private static Node[] split(Node node, int x) {
        Node[] array = new Node[2];
        if (node == null) {
            array[0] = null;
            array[1] = null;
            return array;
        }
        Node[] p;
        if (node.x < x) {
            p = split(node.right, x);
            node.right = p[0];
            array[0] = node;
            array[1] = p[1];
        } else {
            p = split(node.left, x);
            node.left = p[1];
            array[0]  = p[0];
            array[1] = node;
        }
        node.sum = node.x;
        if (node.left != null) {
            node.sum += node.left.sum;
        }
        if (node.right != null) {
            node.sum += node.right.sum;
        }
        return array;
    }

    private static boolean exists(Node a, int x) {
        if (a == null) {
            return false;
        }
        if (a.x == x) {
            return true;
        } else if (a.x < x) {
            return exists(a.right, x);
        } else {
            return exists(a.left, x);
        }
    }

    private static Node insert(Node a, Node node) {
        if (a == null) {
            return node;
        }
        if (node.y > a.y) {
            Node[] p = split(a, node.x);
            node.left = p[0];
            node.right = p[1];
            node.sum = node.x;
            if (node.left != null) {
                node.sum += node.left.sum;
            }
            if (node.right != null) {
                node.sum += node.right.sum;
            }
            return node;
        }
        if (node.x < a.x) {
            a.left = insert(a.left, node);
        } else {
            a.right = insert(a.right, node);
        }
        a.sum += node.x;
        return a;
    }

    private static long sum(Node a, long l, long r, boolean ll, boolean rr) {
        if (a == null) {
            return 0;
        }
        if (a.x < l) {
            return (sum(a.right, l, r, ll, rr));
        } else if (a.x > r) {
            return sum(a.left, l, r, ll, rr);
        } else {
            long sum = a.x;
            if (ll) {
                if (a.left != null) {
                    sum += a.left.sum;
                }
                sum += sum(a.right, l, r, ll, rr);
            } else if (rr) {
                if (a.right != null) {
                    sum += a.right.sum;
                }
                sum += sum(a.left, l, r, ll, rr);
            } else {
                sum += sum(a.left, l, r, ll, true) + sum(a.right, l, r, true, rr);
            }
            return sum;
        }
    }

    private static void print(Node a) {
        if (a != null) {
            System.out.println("node.x = " + a.x + " node.sum = " + a.sum);
            print(a.left);
            print(a.right);
        }
    }

}
