package expression.parser;

public class Main {
    public static void main(String[] args) {
        System.out.println(new ExpressionParser().parse("6 & 1 + 2").evaluate(1));
        System.out.println(new ExpressionParser().parse("6 ^ 1 + 2").evaluate(1));
        System.out.println(new ExpressionParser().parse("6 | 1 + 2").evaluate(1));
        System.out.println(new ExpressionParser().parse("x").evaluate(10));
        System.out.println(new ExpressionParser().parse("5 | 2"));
        System.out.println(new ExpressionParser().parse("5 ^ 2"));
        System.out.println(new ExpressionParser().parse("5 & 2"));
        System.out.println(new ExpressionParser().parse("5 + 2"));
        System.out.println(new ExpressionParser().parse("5 - 2"));
        System.out.println(new ExpressionParser().parse("5 * 2"));
        System.out.println(new ExpressionParser().parse("5 / 2"));
        System.out.println(new ExpressionParser().parse("-(-(-\t\t-5 + 16   *x*y) + 1 * z)"));
        System.out.println(new ExpressionParser().parse("-2147483648"));
    }
}
