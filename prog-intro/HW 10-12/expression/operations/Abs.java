package expression.operations;

public class Abs extends UnaryOperation {

    public Abs(MathExpression expression) {
        super(expression, "abs");
    }

    @Override
    protected int f(int value) {
        return Math.abs(value);
    }

}
