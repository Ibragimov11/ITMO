package markup;

import java.util.List;

public class Paragraph extends Marks implements Elements {

    public Paragraph(List<Elements> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder stringBuilder) {
        toMarkdown(stringBuilder, "");
    }

    @Override
    public void toBBCode(StringBuilder stringBuilder) {
        toBBCode(stringBuilder, "", "");
    }

}
