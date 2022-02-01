package markup;

import java.util.List;

public class Strikeout extends Marks implements Elements {

    public Strikeout(List<Elements> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder stringBuilder) {
        toMarkdown(stringBuilder, "~");
    }

    @Override
    public void toBBCode(StringBuilder stringBuilder) {
        toBBCode(stringBuilder, "[s]", "[/s]");
    }

}
