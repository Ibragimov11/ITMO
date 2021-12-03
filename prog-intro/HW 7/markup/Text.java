package markup;

public class Text implements Elements {

    final private String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void toMarkdown(StringBuilder stringBuilder) {
        stringBuilder.append(text);
    }

    @Override
    public void toBBCode(StringBuilder stringBuilder) {
        stringBuilder.append(text);
    }

}
