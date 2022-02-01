import java.util.ArrayList;
import java.io.IOException;
import java.util.List;

public class ReverseAbc {
    public static void main(String[] args) throws IOException {
		MyScanner scanner = new MyScanner(System.in);
		List<String> list = new ArrayList<>();
		String str;
		while(scanner.hasNextLine()) {
			list.add(".");
			str = scanner.nextLine();
			MyScanner sc = new MyScanner(str);
			while(sc.hasNextWord()) {
				String s = sc.nextWord();
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < s.length(); i++) {
					sb.append(s.charAt(i));
				}
				list.add(sb.toString());
			}
			sc.close();
		}
		scanner.close();
		for(int i = list.size() - 2; i >= 0; i--) {
			if (!list.get(i).equals(".")) {
				System.out.print(list.get(i) + " ");
			} else {
				System.out.println();
			}
		}
	} 
}
