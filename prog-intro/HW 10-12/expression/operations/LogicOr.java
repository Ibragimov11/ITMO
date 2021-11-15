package expression.operations;

public class LogicOr extends BinaryOperation {

    public LogicOr(MathExpression lhs, MathExpression rhs) {
        super(lhs, rhs, "|");
    }

    @Override
    public int f(int l, int r) {
        return l | r;
    }

}
