package expression.exceptions;

import expression.operations.MathExpression;
import expression.operations.Sqrt;

public class CheckedSqrt extends Sqrt {

    public CheckedSqrt(final MathExpression expression) {
        super(expression);
    }

    public int f(final int value) {
        if (value < 0) {
            throw new InvalidSqrtException("sqrt by negative integer");
        }
        return (int) Math.sqrt(value);
    }

}
