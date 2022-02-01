package expression.operations;

public class Subtract extends BinaryOperation {

    public Subtract(MathExpression lhs, MathExpression rhs) {
        super(lhs, rhs, "-");
    }

    @Override
    public int f(int l, int r) {
        return l - r;
    }

}
