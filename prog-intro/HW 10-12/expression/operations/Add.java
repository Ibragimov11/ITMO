package expression.operations;

public class Add extends BinaryOperation {

    public Add(MathExpression lhs, MathExpression rhs) {
        super(lhs, rhs, "+");
    }

    @Override
    public int f(int l, int r) {
        return l + r;
    }

}
