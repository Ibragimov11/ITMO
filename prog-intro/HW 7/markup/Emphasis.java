package markup;

import java.util.List;

public class Emphasis extends Marks implements Elements {

    public Emphasis(List<Elements> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder stringBuilder) {
        toMarkdown(stringBuilder, "*");
    }

    @Override
    public void toBBCode(StringBuilder stringBuilder) {
        toBBCode(stringBuilder, "[i]", "[/i]");
    }

}
