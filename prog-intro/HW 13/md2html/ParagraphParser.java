package md2html;

public class ParagraphParser {
    private final String text;

    ParagraphParser(String text) {
        this.text = text;
    }

    public void makeHTML(StringBuilder out) {
        out.append("<p>");
        new TextParser(text).makeHTML(out);
        out.append("</p>");
    }
}
