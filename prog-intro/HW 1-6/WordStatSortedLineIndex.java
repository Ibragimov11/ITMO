import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordStatSortedLineIndex {
    public static void main(String[] args) {
        Map<String, List<Integer>> map = new TreeMap<>();
        StringBuilder builder = new StringBuilder();
        BufferedWriter writer = null;
        MyScanner scanner = null;
        int j = 1;

        try {
            scanner = new MyScanner(args[0], "utf-8");
            int i = 0;
            while (scanner.hasNext()) {
                int next = scanner.next();
                char ch = (char) next;
                if (Character.isLetter(ch) || Character.getType(ch) == Character.DASH_PUNCTUATION || ch == '\'') {
                    builder.append(ch);
                } else if (ch == '\n') {
                    if (!builder.toString().isBlank()) {
                        i++;
                        String word = builder.toString();
                        if (map.get(word.toLowerCase()) != null) {
                            List<Integer> ar = map.get(word.toLowerCase());
                            ar.add(j);
                            ar.add(i);
                            map.put(word.toLowerCase(), ar);
                        } else {
                            List<Integer> ar = new ArrayList<>();
                            ar.add(j);
                            ar.add(i);
                            map.put(word.toLowerCase(), ar);
                        }
                    }
                    j++;
                    i = 0;
                    builder.setLength(0);
                } else {
                    if (!builder.toString().isBlank()) {
                        i++;
                        String word = builder.toString();
                        if (map.get(word.toLowerCase()) != null) {
                            List<Integer> ar = map.get(word.toLowerCase());
                            ar.add(j);
                            ar.add(i);
                            map.put(word.toLowerCase(), ar);
                        } else {
                            List<Integer> ar = new ArrayList<>();
                            ar.add(j);
                            ar.add(i);
                            map.put(word.toLowerCase(), ar);
                        }
                    }
                    builder.setLength(0);
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
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8));
            Set<String> words = map.keySet();
            for (String word : words) {
                List<Integer> ar = map.get(word.toLowerCase());
                builder.append(word).append(" ").append(ar.size() / 2);
                for (int i = 0; i < ar.size() - 1; i += 2){
                    builder.append(" ").append(ar.get(i)).append(":").append(ar.get(i + 1));
                }
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
