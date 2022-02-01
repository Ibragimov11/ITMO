import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordStatInput {
    public static void main(String[] args) {
        Map<String, Integer> map = new LinkedHashMap<>();
        StringBuilder builder = new StringBuilder();
        BufferedWriter writer = null;
        MyScanner scanner = null;

        try {
            scanner = new MyScanner(args[0], "utf-8");
            while (scanner.hasNext()) {
                char ch = (char) scanner.next();
                if (Character.isLetter(ch) || Character.getType(ch) == Character.DASH_PUNCTUATION || ch == '\'') {
                    builder.append(ch);
                } else {
                    if(!builder.toString().isBlank()) {
                        String word = builder.toString();
                        map.merge(word.toLowerCase(), 1, (a, b) -> a + b);
                        builder.setLength(0);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл для чтения не найден");
        } catch (IOException e) {
            System.out.println("Ошибка чтения");
        } finally {
            if (scanner != null) {
                try {
                    scanner.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        builder.setLength(0);

        try {
            Set<String> words = map.keySet();
            for (String word : words) {
                int n = map.get(word);
                builder.append(word).append(" ").append(n);
                writer.write(builder.toString());
                writer.newLine();
                builder.setLength(0);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл для записи не найден");
        } catch (IOException e) {
            System.out.println("Ошибка записи");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }
}
