package markup;

import java.util.List;

abstract class Marks {
    protected List<Elements> elements;

    protected Marks(List<Elements> elements) {
        this.elements = elements;
    }

    protected void toMarkdown(StringBuilder stringBuilder, String mark) {
        stringBuilder.append(mark);
        for (Elements element : elements) {
            element.toMarkdown(stringBuilder);
        }
        stringBuilder.append(mark);
    }

    protected void toBBCode(StringBuilder stringBuilder, String l, String r) {
        stringBuilder.append(l);
        for (Elements element : elements) {
            element.toBBCode(stringBuilder);
        }
        stringBuilder.append(r);
    }

}
