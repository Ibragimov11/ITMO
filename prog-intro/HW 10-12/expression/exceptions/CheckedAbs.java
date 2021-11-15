package expression.exceptions;

import expression.operations.Abs;
import expression.operations.MathExpression;

public class CheckedAbs extends Abs {

    public CheckedAbs(final MathExpression expression) {
        super(expression);
    }

    public int f(final int value) {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException("integer overflow");
        }
        return Math.abs(value);
    }

}
