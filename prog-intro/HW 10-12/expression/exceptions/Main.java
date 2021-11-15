package expression.exceptions;

import expression.operations.Add;
import expression.operations.Const;
import expression.operations.Negate;

public class Main {
    public static void main(String[] args) throws ParseException {
        System.out.println(new CheckedAbs(new CheckedSqrt(new Const(99))).evaluate(0));
        System.out.println(new CheckedAbs(new CheckedSqrt(new Const(99))));
        System.out.println(new Negate(new Const(5)).equals(new Negate(new Const(5))));
        System.out.println(new Add(new Const(8), new Const(5)).equals(new Add(new Const(8), new Const(5))));
        System.out.println(new CheckedAdd(new Const(1000000000), new Const(1000000000)).evaluate(0));
        System.out.println(new ExpressionParser().parse("x*y"));
        System.out.println(new ExpressionParser().parse("x*y").evaluate(0, 0, 0));
        System.out.println(new ExpressionParser().parse("x*y+(z-1   )/10").evaluate(1, 1, 11));
        System.out.println(new ExpressionParser().parse("x*y+(z-1   )/10"));

        try {
            System.out.println(new ExpressionParser().parse("x *  * z"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println(new ExpressionParser().parse("* y * z"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println(new ExpressionParser().parse("x*y+%(%(z-1   )/10"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println(new ExpressionParser().parse("x*y(+(z-1)/10"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println(new ExpressionParser().parse("y("));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println(new ExpressionParser().parse("y(x+y)"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(new ExpressionParser().parse("(2+2)"));

        try {
            System.out.println(new ExpressionParser().parse("2+2)"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println(new ExpressionParser().parse("(2+2"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println(new ExpressionParser().parse("((x) / (z)) (/ (1616686859)"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println(new ExpressionParser().parse("10 20"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(new ExpressionParser().parse("x / y"));
        System.out.println(new ExpressionParser().parse("x +  10"));
        System.out.println(new ExpressionParser().parse("sqrt(x * y * z)"));
        System.out.println(new ExpressionParser().parse("sqrt x "));
        System.out.println(new ExpressionParser().parse("sqrt x * y"));
        System.out.println(new ExpressionParser().parse("sqrt (x * y)"));
        System.out.println(new ExpressionParser().parse("sqrt x * y * z"));
        System.out.println(new ExpressionParser().parse("sqrt4"));
        System.out.println(new ExpressionParser().parse("abs-49"));
    }
}
