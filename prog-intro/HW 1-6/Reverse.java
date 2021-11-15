import java.util.*;
import java.io.IOException;

public class Reverse {
    public static void main(String[] args) throws IOException {
		MyScanner scanner = new MyScanner(System.in);
		List list = new ArrayList();
		String str;
		while(scanner.hasNextLine()) {
			list.add(".");
			str = scanner.nextLine();
			MyScanner sc = new MyScanner(str);
			while(sc.hasNextInt()) {
				list.add(sc.nextInt());
			}
		}
		for(int i = list.size() - 2; i >= 0; i--) {
			if (list.get(i) != ".") {
				System.out.print(list.get(i) + " ");
			} else {
				System.out.println();
			}
		}
	} 
}
