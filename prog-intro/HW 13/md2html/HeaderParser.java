package md2html;

public class HeaderParser{
    private final String text;

    HeaderParser(String text) {
        this.text = text;
    }

    public void makeHTML(StringBuilder out) {
        int level = 0;
        int pos = 0;
        while (text.charAt(pos++) == '#') {
            level++;
        }
        out.append("<h").append(level).append(">");
        new TextParser(text.substring(level + 1)).makeHTML(out);
        out.append("</h").append(level).append(">");
    }
}
