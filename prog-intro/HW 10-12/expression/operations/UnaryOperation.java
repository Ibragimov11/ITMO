package expression.operations;

public abstract class UnaryOperation implements MathExpression {
    protected final MathExpression expression;
    protected final String operation;

    protected UnaryOperation(MathExpression expression, String operation) {
        this.expression = expression;
        this.operation = operation;
    }

    public int evaluate(int valueOfVariable) {
        return f(expression.evaluate(valueOfVariable));
    }

    public int evaluate(int x, int y, int z) {
        return f(expression.evaluate(x, y, z));
    }

    protected abstract int f(int value);

    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass() == getClass()) {
            return ((UnaryOperation) o).expression.equals(this.expression);
        }
        return false;
    }

    @Override
    public String toString() {
        return operation + "(" + expression.toString() + ")";
    }

    @Override
    public int hashCode() {
        return expression.hashCode() * 31;
    }

}
