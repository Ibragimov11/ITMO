package md2html;

public class FragmentParser {
    private final String text;

    FragmentParser(String text) {
        this.text = text;
    }

    public void makeHTML(StringBuilder out) {
        if (isHeader(text)) {
            new HeaderParser(text).makeHTML(out);
        } else {
            new ParagraphParser(text).makeHTML(out);
        }
    }

    private boolean isHeader(String fragment) {
        int pos = 0;
        while (text.charAt(pos) == '#') {
            pos++;
            if (pos >= text.length()) {
                break;
            }
        }
        return pos < Math.min(fragment.length(), 7) && pos > 0 && fragment.charAt(pos) == ' ';
        // уровень заголовка <= 6 ?
    }

}
