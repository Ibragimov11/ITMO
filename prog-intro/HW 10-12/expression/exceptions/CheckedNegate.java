package expression.exceptions;

import expression.operations.MathExpression;
import expression.operations.Negate;

public class CheckedNegate extends Negate {

    public CheckedNegate(MathExpression expression) {
        super(expression);
    }

    @Override
    protected int f(final int value) {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException("overflow");
        }
        return -value;
    }
}
