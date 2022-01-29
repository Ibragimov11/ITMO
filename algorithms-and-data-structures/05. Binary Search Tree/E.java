import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class E {
    private static class Node {
        private Node left, right;
        private final int x;
        private final double y;
        private int count;

        public Node(int x) {
            this.x = x;
            this.count = 1;
            this.y = Math.random();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
//        Scanner in = new Scanner(new FileInputStream(args[0]));
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Node tree = null;
        int s, k;
        while (n-- > 0) {
            s = in.nextInt();
            k = in.nextInt();
            if (s == 1) {
                if (!exists(tree, k)) {
                    tree = insert(tree, new Node(k));
                }
            } else if (s == -1) {
                tree = delete(tree, k);
            } else {
                System.out.println(search(tree, (tree.count + 1 - k)));
            }
        }
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
        node.count = 1;
        if (node.left != null) {
            node.count += node.left.count;
        }
        if (node.right != null) {
            node.count += node.right.count;
        }
        return array;
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
            node.count = 1;
            if (node.left != null) {
                node.count += node.left.count;
            }
            if (node.right != null) {
                node.count += node.right.count;
            }
            return node;
        }
        if (node.x < a.x) {
            a.left = insert(a.left, node);
        } else {
            a.right = insert(a.right, node);
        }
        a.count += 1;
        return a;
    }

    private static Node delete(Node a, int x) {
        if (a.x == x) {
            return merge(a.left, a.right);
        }
        if (x < a.x) {
            a.left = delete(a.left, x);
        } else {
            a.right = delete(a.right, x);
        }
        a.count--;
        return a;
    }

    private static int search(Node a, int k) { // какой-то из вариантов обработан неверно
        if (a.left == null) {
            if (a.right == null) {
                return a.x;
            } else {
                if (k == 1) {
                    return a.x;
                } else {
                    return search(a.right, k - 1);
                }
            }
        } else if (a.right == null) {
            if (a.left.count == k - 1) {
                return a.x;
            } else {
                return search(a.left, k);
            }
        } else {
            if (a.left.count >= k) {
                return search(a.left, k);
            } else if (a.left.count == k - 1) {
                return a.x;
            } else {
                return search(a.right, k - 1 - a.left.count);
            }
        }
    }

    private static void print(Node a) {
        if (a != null) {
            System.out.println("node.x = " + a.x + " node.y = " + a.y + " node.count = " + a.count);
            print(a.left);
            print(a.right);
        }
    }

}
