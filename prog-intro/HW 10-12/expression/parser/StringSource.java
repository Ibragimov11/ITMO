package expression.parser;

public class StringSource implements CharSource {
    private final String str;
    public int pos;

    public StringSource(final String str) {
        this.str = str;
        pos = 0;
    }

    @Override
    public boolean hasNext() {
        return pos < str.length();
    }

    @Override
    public char next() {
        return str.charAt(pos++);
    }
}
