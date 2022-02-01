import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class B {
    private static class Node {
        private Node left, right;
        private final int x;
        private final double y;

        public Node(int x) {
            this.x = x;
            this.y = Math.random();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
//        Scanner in = new Scanner(new FileInputStream(args[0]));
        Scanner in = new Scanner(System.in);
        Node tree = null;
        while (in.hasNext()) {
            String s = in.next();
            int x = in.nextInt();
            switch (s) {
                case "insert": {
                    if (!exists(tree, x)) {
                        tree = insert(tree, new Node(x));
                    }
                    break;
                }
                case "exists": {
                    System.out.println(exists(tree, x));
                    break;
                }
                case "next": {
                    next(tree, x);
                    break;
                }
                case "prev": {
                    prev(tree, x);
                    break;
                }
                default: {
                    if (exists(tree, x)) {
                        tree = delete(tree, x);
                    }
                    break;
                }
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
            a.right = merge(a.right, b);
            return a;
        } else {
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
            return node;
        }
        if (node.x < a.x) {
            a.left = insert(a.left, node);
        } else {
            a.right = insert(a.right, node);
        }
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
        return a;
    }

    private static void next(Node a, int x) {
        if (a == null) {
            System.out.println("none");
        } else {
            int ans = -1000000001;
            while (a != null) {
                if (a.x > x) {
                    ans = a.x;
                    a = a.left;
                } else {
                    a = a.right;
                }
            }
            if (ans > x) {
                System.out.println(ans);
            } else {
                System.out.println("none");
            }
        }
    }

    private static void prev(Node a, int x) {
        if (a == null) {
            System.out.println("none");
        } else {
            int ans = 1000000001;
            while (a != null) {
                if (a.x < x) {
                    ans = a.x;
                    a = a.right;
                } else {
                    a = a.left;
                }
            }
            if (ans < x) {
                System.out.println(ans);
            } else {
                System.out.println("none");
            }
        }
    }

}
