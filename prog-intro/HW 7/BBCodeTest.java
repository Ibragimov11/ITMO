package markup;

import java.util.Map;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BBCodeTest extends MarkdownTest {
    private static final Map<String, String> BB_CODE = Map.of(
            "*<", "[i]",
            "*>", "[/i]",
            "__<", "[b]",
            "__>", "[/b]",
            "~<", "[s]",
            "~>", "[/s]"
    );

    public static void main(String[] args) {
        new BBCodeTest().run();
    }

    @Override
    protected void test(final Paragraph paragraph, final String expected) {
        super.test(paragraph, expected);
        test(paragraph::toBBCode, expected, BB_CODE);
    }
}
