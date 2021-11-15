package expression.operations;

public class Divide extends BinaryOperation {

    public Divide(MathExpression lhs, MathExpression rhs) {
        super(lhs, rhs, "/");
    }

    @Override
    public int f(int l, int r) {
        return l / r;
    }

}
