package expression.exceptions;

import expression.operations.Add;
import expression.operations.MathExpression;

public class CheckedAdd extends Add {
    public CheckedAdd(MathExpression lhs, MathExpression rhs) {
        super(lhs, rhs);
    }

    public int f(final int l, final int r) {
        if (l > 0 && r > 0 && Integer.MAX_VALUE - l < r) {
            throw new OverflowException("overflow1");
        } else if (l < 0 && r < 0 && Integer.MIN_VALUE - l > r) {
            throw new OverflowException("overflow2");
        }
        return l + r;
    }

}
