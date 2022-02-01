public class Sum{

	public static void main(String[] args){
		int result = 0;
		for (String arg : args) {
			String str = arg.trim();
			int i1 = -1;
			int l = str.length();
			for (int j = 0; j < str.length(); j++) {
				char c = str.charAt(j);
				if (!(Character.isWhitespace(c)) && i1 < 0) {
					i1 = j;
				} else if (Character.isWhitespace(c) && i1 >= 0) {
					result += Integer.parseInt(str.substring(i1, j));
					i1 = -1;
				}
				if (j == l - 1 && i1 >= 0) {
					result += Integer.parseInt(str.substring(i1));
					i1 = -1;
				}
			}
		}
		System.out.println(result);
	}

}
