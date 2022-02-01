package expression.operations;

import java.util.Objects;

public abstract class BinaryOperation implements MathExpression{
    protected final MathExpression lhs, rhs;
    protected final String operation;

    protected BinaryOperation(MathExpression lhs, MathExpression rhs, String operation) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operation = operation;
    }

    public int evaluate(int valueOfVariable) {
        return f(lhs.evaluate(valueOfVariable), rhs.evaluate(valueOfVariable));
    }

    public int evaluate(int x, int y, int z) {
        return f(lhs.evaluate(x, y, z), rhs.evaluate(x, y, z));
    }

    protected abstract int f(int l, int r);

    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass() == getClass()) {
            BinaryOperation that = (BinaryOperation) o;
            return this.lhs.equals(that.lhs) && this.rhs.equals(that.rhs);
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + lhs.toString() + " " + operation + " " + rhs.toString() + ")";
    }

    @Override
    public int hashCode() {
        int res = Objects.hashCode(lhs) * 31;
        res = (res + Objects.hashCode(rhs)) * 31;
        res = res + Objects.hashCode(getClass()) * 29;
        return res;
    }

}
