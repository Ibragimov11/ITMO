package expression.operations;

public class Main {
    public static void main(String[] args) {
        System.out.println(new Subtract(
                new Multiply(
                        new Const(2),
                        new Variable("x")
                ),
                new Const(3)
        ).evaluate(5));
        System.out.println(
                new Multiply(new Const(2), new Variable("x"))
                        .equals(new Multiply(new Const(2), new Variable("x")))
        );
        System.out.println(new Subtract(
                new Multiply(
                        new Const(2),
                        new Variable("x")
                ),
                new Const(3)
        ));
        System.out.println(new LogicOr(
                new LogicXor(
                        new LogicAnd(new Const(5), new Const(7)),
                        new Const(9)),
                new Const(11))
        );
        System.out.println(new Divide(new Divide(
                new Const(0), new Const(1)),
                new Const(2)
                ).evaluate(1)
        );
        System.out.println(new Divide(
                new Const(100), new Divide(
                new Const(10), new Const(10))
        ).evaluate(1));
        System.out.println(new Negate(new Const(5)).equals(new Negate(new Const(5))));
    }
}
