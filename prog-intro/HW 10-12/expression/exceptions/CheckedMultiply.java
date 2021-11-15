package expression.exceptions;

import expression.operations.MathExpression;
import expression.operations.Multiply;

public class CheckedMultiply extends Multiply {

    public CheckedMultiply(MathExpression lhs, MathExpression rhs) {
        super(lhs, rhs);
    }

    public int f(final int l, final int r) {
        if (l > 0 && r > 0 && l > Integer.MAX_VALUE / r || l < 0 && r < 0 && l < Integer.MAX_VALUE / r || (l > 0 && r < 0 || l < 0 && r > 0) && Math.min(l, r) < Integer.MIN_VALUE / Math.max(l, r)) {
            throw new OverflowException("overflow");
        }
        return l * r;
    }
}
