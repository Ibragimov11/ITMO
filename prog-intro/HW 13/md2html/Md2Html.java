package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Md2Html {
    public static void main(String[] args) {
        StringBuilder HTML = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8))){
            String line = "";
            StringBuilder fragment = new StringBuilder();
            while (line != null && (line = reader.readLine()) != null) {
                while (line != null && !line.equals("")) {
                    fragment.append(line).append('\n');
                    line = reader.readLine();
                }
                if (fragment.length() != 0) {
                    fragment.setLength(fragment.length() - 1);
                    new FragmentParser(fragment.toString()).makeHTML(HTML);
                    HTML.append('\n');
                }
                fragment = new StringBuilder();
            }
        } catch (FileNotFoundException e) {
            System.err.println("The input file is not found");
        } catch (IOException e) {
            System.err.println("Read error");
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))) {
            writer.write(HTML.toString());
        } catch (FileNotFoundException e) {
            System.err.println("The output file is not found");
        } catch (IOException e) {
            System.err.println("Write error");
        }
    }
}
