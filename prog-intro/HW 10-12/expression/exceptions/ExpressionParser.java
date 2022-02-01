package expression.exceptions;

import expression.operations.Const;
import expression.operations.MathExpression;
import expression.operations.Variable;
import expression.parser.BaseParser;
import expression.parser.StringSource;

import java.util.List;

public class ExpressionParser extends BaseParser implements Parser {
    private char operation;
    private int constant;
    private String var;
    private final List<Character> symbols = List.of(')', '(', '+', '*', '/');

    public ExpressionParser() {
        super(new StringSource(""));
    }

    @Override
    public MathExpression parse(String line) throws ParseException {
        source = new StringSource(line);
        if (source.hasNext()) {
            nextChar();
            MathExpression result = parseAS();
            if (operation == ')' || ch != '\0') {
                throw new InvalidBracketsException("No opening parenthesis:  " + source.pos);
            }
            return result;
        }
        throw new ParseException("Your input is empty");
    }

   /* private MathExpression parseAS() throws ParseException{
        MathExpression l = parseMD();
        while (operation == '+' || operation == '-') {
            if (operation == '+') {
                MathExpression r = parseMD();
                l = new CheckedAdd(l, r);
            } else {
                MathExpression r = parseMD();
                l = new CheckedSubtract(l, r);
            }
        }
        return l;
    }

    private MathExpression parseMD() throws ParseException {
        MathExpression l = parseE();
        while (operation == '*' || operation == '/') {
            if (operation == '*') {
                MathExpression r = parseE();
                l = new CheckedMultiply(l, r);
            } else {
                MathExpression r = parseE();
                l = new CheckedDivide(l, r);
            }
        }
        return l;
    }*/

    private MathExpression parseAS() throws ParseException{
        return parseBinary('+', '-');
    }

    private MathExpression parseMD() throws ParseException{
        return parseBinary('*', '/');
    }

    private MathExpression parseBinary(char op1, char op2) throws ParseException{
        MathExpression l = op1 == '+' ? parseMD() : parseE();
        while (operation == op1 || operation == op2) {
            if (operation == op1) {
                MathExpression r = op1 == '+' ? parseMD() : parseE();
                l = op1 == '+' ? new CheckedAdd(l, r) : new CheckedMultiply(l, r);
            } else {
                MathExpression r = op1 == '+' ? parseMD() : parseE();
                l = op1 == '+' ? new CheckedSubtract(l, r) : new CheckedDivide(l, r);
            }
        }
        return l;
    }

    private MathExpression parseE() throws ParseException {
        parseSymbol(true);
        if (operation == '(') {
            MathExpression expression = parseAS();
            if (operation != ')') {
                throw new InvalidBracketsException("No closing parenthesis:  " + source.pos);
            }
            parseSymbol(false);
            return expression;
        } else if (operation == ')' && ch == '(') {
            throw new ParseException("Invalid expression: error found in pos =  " + source.pos);
        } else if (operation == 'c') {
            MathExpression expression = new Const(constant);
            parseSymbol(false);
            if (operation == '(' || operation == 'c' || operation == 'v') {
                throw new ParseException("Invalid expression: error found in pos =  " + source.pos);
            }
            return expression;
        } else if (operation == 'v') {
            MathExpression expression = new Variable(var);
            parseSymbol(false);
            if (operation == '(' || operation == 'c' || operation == 'v') {
                throw new ParseException("Invalid expression: error found in pos =  " + source.pos);
            }
            return expression;
        } else if (operation == '-') {
            return new CheckedNegate(parseE());
        } else if (symbols.contains(operation)) {
            throw new ParseException("No argument:  " + source.pos);
        } else if (operation == 's') {
            return new CheckedSqrt(parseE());
        } else if (operation == 'a') {
            return new CheckedAbs(parseE());
        }
        throw new ParseException("No argument:  " + source.pos);
    }

    private String parseVar() throws ParseException {
        final StringBuilder var = new StringBuilder();
        while (isVariable()) {
            var.append(ch);
            nextChar();
        }
        if (var.length() != 1) {
            throw new InvalidVarException("Invalid variable:  " + source.pos);
        }
        return var.toString();
    }

    private int parseC(boolean minus) {
        final StringBuilder number = new StringBuilder();
        if (minus) {
            number.append("-");
        }
        while (isNum()) {
            number.append(ch);
            nextChar();
        }
        try {
            return Integer.parseInt(number.toString());
        } catch (NumberFormatException e) {
            if (minus) {
                throw new InvalidConstException("Constant overflow 1:  " + source.pos);
            }
            throw new InvalidConstException("Constant overflow 2:  " + source.pos);
        }
    }

    private void parseSymbol(boolean minus1) throws ParseException {
        skipWhitespace();
        if (test('-')) {
            if (minus1 && isNum()) {
                constant = parseC(true);
                operation = 'c';
            } else {
                operation = '-';
            }
        } else if (symbols.contains(ch)) {
            operation = ch;
            nextChar();
        } else if (Character.isLetter(ch)) {
            if (ch == 's') {
                nextChar();
                if (test('q') && test('r') && test('t')) {
                    if (Character.isLetter(ch) || ch == '\0') {
                        throw new ParseException("invalid sqrt entry:  " + source.pos);
                    } else {
                        operation = 's';
                    }
                }
//
            } else if (ch == 'a') {
                nextChar();
                if (test('b') && test('s')) {
                    if (Character.isLetter(ch) || ch == '\0') {
                        throw new ParseException("invalid abs entry:  " + source.pos);
                    } else {
                        operation = 'a';
                    }
                }
            } else {
                var = parseVar();
                operation = 'v';
            }
        } else if (isNum()) {
            constant = parseC(false);
            operation = 'c';
        } else if(ch == '\0') {
            operation = '\0';
        } else {
            throw new InvalidSymbolException("Invalid symbol:  " + source.pos);
        }
    }

}
