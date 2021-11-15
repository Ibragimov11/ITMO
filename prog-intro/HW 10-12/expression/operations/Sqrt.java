package expression.operations;

public class Sqrt extends UnaryOperation {

    public Sqrt(MathExpression expression) {
        super(expression, "sqrt");
    }

    @Override
    protected int f(int value) {
        return (int) Math.sqrt(value);
    }

}
