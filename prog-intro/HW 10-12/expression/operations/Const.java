package expression.operations;

public class Const implements MathExpression {
    private final int value;

    public Const(int value) {
        this.value = value;
    }

    @Override
    public int evaluate(int valueOfVariable) {
        return value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        Const const1 = (Const) o;
        return value == const1.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value) * 29;
    }
}
