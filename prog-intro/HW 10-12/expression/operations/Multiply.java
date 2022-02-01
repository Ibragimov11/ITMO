package expression.operations;

public class Multiply extends BinaryOperation {

    public Multiply(MathExpression lhs, MathExpression rhs) {
        super(lhs, rhs, "*");
    }

    @Override
    public int f(int l, int r) {
        return l * r;
    }

}
