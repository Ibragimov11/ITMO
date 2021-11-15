package expression.operations;

public class Negate extends UnaryOperation {

    public Negate(MathExpression expression) {
        super(expression, "-");
    }

    @Override
    protected int f(int value) {
        return -value;
    }

}
