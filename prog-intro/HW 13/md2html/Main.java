package md2html;

public class Main {
    public static void main(String[] args) {
        StringBuilder out;

        out =  new StringBuilder();
        new TextParser("*Hello*Hello").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new TextParser("'Hello'Hello").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new TextParser("*Hello*").makeHTML(out);
        System.out.println(out);

        out = new StringBuilder();
        new FragmentParser("__Hello__world").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new TextParser("*__Hello__world*").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new TextParser("<>&").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new FragmentParser("# Заголовок первого уровня\n\n").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new FragmentParser("'#'").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new FragmentParser("`#`.\n\n").makeHTML(out);
        System.out.println(out);

        StringBuilder sl = new StringBuilder("слэша: \\*.");
        for (int i = 0; i < sl.length(); i++) {
            System.out.print(sl.charAt(i));
        }

        out =  new StringBuilder();
        new FragmentParser(" могут быть заэкранированы\nпри помощи обратного слэша: \\*.").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new FragmentParser("Знаете ли вы, что в Markdown, одиночные * и _\nне означают выделение?\nОни так же могут быть заэкранированы\nпри помощи обратного слэша: \\*.").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new FragmentParser("######### Hello world ###").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new FragmentParser("######### Hello world ### #").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new FragmentParser("\\").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new FragmentParser("**Привет `например` тебе**").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new FragmentParser("*-Привет*-").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new FragmentParser("*привет-*-*").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new FragmentParser("*привет_*").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new FragmentParser("**Привет *например* тебе**").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new FragmentParser("####").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new FragmentParser("*привет-").makeHTML(out);
        System.out.println(out);

        out =  new StringBuilder();
        new TextParser("*__Hello__\nworld*").makeHTML(out);
        System.out.println(out);

    }
}
