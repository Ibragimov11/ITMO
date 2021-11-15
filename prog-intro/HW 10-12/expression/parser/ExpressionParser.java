package expression.parser;

import expression.operations.*;

import java.util.List;

public class ExpressionParser extends BaseParser implements Parser {
    private char operation;
    private int constant;
    private String var;
    private final List<Character> symbols = List.of(')', '(', '|', '^', '&', '+', '*', '/');

    public ExpressionParser() {
        super(new StringSource(""));
    }

    @Override
    public MathExpression parse(String line) {
        source = new StringSource(line);
        if (source.hasNext()) {
            nextChar();
            return parseLogicOr();
        }
        throw new AssertionError("Your input is empty");
    }

    private MathExpression parseLogicOr() {
        MathExpression l = parseLogicXor();
        while (operation == '|') {
            MathExpression r = parseLogicXor();
            l = new LogicOr(l, r);
        }
        return l;
    }

    private MathExpression parseLogicXor() {
        MathExpression l = parseLogicAnd();
        while (operation == '^') {
            MathExpression r = parseLogicAnd();
            l = new LogicXor(l, r);
        }
        return l;
    }

    private MathExpression parseLogicAnd() {
        MathExpression l = parseAS();
        while (operation == '&') {
            MathExpression r = parseAS();
            l = new LogicAnd(l, r);
        }
        return l;
    }

    private MathExpression parseAS() {
        MathExpression l = parseMD();
        while (operation == '+' || operation == '-') {
            if (operation == '+') {
                MathExpression r = parseMD();
                l = new Add(l, r);
            } else {
                MathExpression r = parseMD();
                l = new Subtract(l, r);
            }
        }
        return l;
    }

    private MathExpression parseMD() {
        MathExpression l = parseE();
        while (operation == '*' || operation == '/') {
            if (operation == '*') {
                MathExpression r = parseE();
                l = new Multiply(l, r);
            } else {
                MathExpression r = parseE();
                l = new Divide(l, r);
            }
        }
        return l;
    }

    private MathExpression parseE() {
        parseSymbol(true);
        if (operation == '(') {
            MathExpression expression = parseLogicOr();
            parseSymbol(false);
            return expression;
        } else if (operation == 'c') {
            MathExpression expression = new Const(constant);
            parseSymbol(false);
            return expression;
        } else if (operation == 'v') {
            MathExpression expression = new Variable(var);
            parseSymbol(false);
            return expression;
        } else if (operation == '-') {
            return new Negate(parseE());
        }
        throw new AssertionError("expression is incorrect");
    }

    private String parseVar() {
        final StringBuilder var = new StringBuilder();
        while (between('a', 'z') || between('A', 'Z')) {
            var.append(ch);
            nextChar();
        }
        return var.toString();
    }

    private int parseC(boolean minus) {
        final StringBuilder number = new StringBuilder();
        while (isNum()) {
            number.append(ch);
            nextChar();
        }
        return minus ? Integer.parseInt(number.insert(0, "-").toString()) : Integer.parseInt(number.toString());
    }

    private void parseSymbol(boolean minusC) {
        skipWhitespace();
        if (test('-')) {
            if (minusC && isNum()) {
                constant = parseC(true);
                operation = 'c';
            } else {
                operation = '-';
            }
        } else if (symbols.contains(ch)) {
            operation = ch;
            nextChar();
        } else if (Character.isLetter(ch)) {
            var = parseVar();
            operation = 'v';
        } else if (isNum()) {
            constant = parseC(false);
            operation = 'c';
        }
    }

}
