package expression.exceptions;

import expression.operations.Divide;
import expression.operations.MathExpression;

public class CheckedDivide extends Divide {

    public CheckedDivide(MathExpression lhs, MathExpression rhs) {
        super(lhs, rhs);
    }

    public int f(final int l, final int r) {
        if (r == 0) {
            throw new DBZException("division by zero");
        } else if (l == Integer.MIN_VALUE && r == -1) {
            throw new OverflowException("overflow");
        }
        return l / r;
    }
}
