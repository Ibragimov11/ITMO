package expression.operations;

import java.util.Objects;

public class Variable implements MathExpression {
    private final String var;

    public Variable(String var) {
        this.var = var;
    }

    @Override
    public int evaluate(int valueOfVariable) {
        return valueOfVariable;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (var) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
        }
        throw new AssertionError("no name");
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(var, variable.var);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(var) * 31;
    }

}

