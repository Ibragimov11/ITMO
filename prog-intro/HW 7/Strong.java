package markup;

import java.util.List;

public class Strong extends Marks implements Elements {

    public Strong(List<Elements> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder stringBuilder) {
        toMarkdown(stringBuilder, "__");
    }

    @Override
    public void toBBCode(StringBuilder stringBuilder) {
        toBBCode(stringBuilder, "[b]", "[/b]");
    }

}
