package expression.operations;

public class LogicXor extends BinaryOperation {

    public LogicXor(MathExpression lhs, MathExpression rhs) {
        super(lhs, rhs, "^");
    }

    @Override
    public int f(int l, int r) {
        return l ^ r;
    }

}
