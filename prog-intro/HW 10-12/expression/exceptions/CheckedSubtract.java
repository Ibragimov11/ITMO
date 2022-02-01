package expression.exceptions;

import expression.operations.MathExpression;
import expression.operations.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(MathExpression lhs, MathExpression rhs) {
        super(lhs, rhs);
    }

    public int f(final int l, final int r) {
        if (l == 0 && r == Integer.MIN_VALUE || l < 0 && r > 0 && l < Integer.MIN_VALUE + r
                || l > 0 && r < 0 && l > Integer.MAX_VALUE + r) {
            throw new OverflowException("overflow");
        }
        return l - r;
    }
}
