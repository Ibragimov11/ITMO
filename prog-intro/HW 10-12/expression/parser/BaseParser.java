package expression.parser;

public abstract class BaseParser {
    protected static StringSource source;
    protected char ch;

    public BaseParser(final StringSource source) {
            BaseParser.source = source;
        }

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : '\0';
    }

    protected void skipWhitespace() {
        while(Character.isWhitespace(ch)) {
            nextChar();
        }
    }

    protected boolean test(char expected) {
        skipWhitespace();
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected boolean between(char left, char right) {
        return left <= ch && ch <= right;
    }

    protected boolean isNum() {
        return between('0', '9');
    }

    protected boolean isVariable() {
        return ch == 'x' || ch == 'y' || ch == 'z';
    }
}
