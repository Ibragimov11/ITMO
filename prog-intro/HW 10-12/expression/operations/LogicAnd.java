package expression.operations;

public class LogicAnd extends BinaryOperation {

    public LogicAnd(MathExpression lhs, MathExpression rhs) {
        super(lhs, rhs, "&");
    }

    @Override
    public int f(int l, int r) {
        return l & r;
    }

}