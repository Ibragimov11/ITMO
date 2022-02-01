import java.io.*;

public class MyScanner {
	final private Reader reader;
	private int read = 0;
	private String line;

	public MyScanner(String str) {
		reader = new BufferedReader(new StringReader(str));
	}

	public MyScanner(InputStream input) throws IOException {
		reader = new BufferedReader(new InputStreamReader(input));
	}

	public MyScanner(String file, String code) throws IOException {
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), code));
	}

	public boolean hasNextWord() throws IOException {
		while (read != -1) {
			read = reader.read();
			if (Character.isLetter((char)read) || (char)read == '-') {
				return true;
			}
		}
		return false;
	}

	public String nextWord() throws IOException {
		StringBuilder sb = new StringBuilder();
		while (!Character.isWhitespace((char)read) && read != -1) {
			sb.append((char)read);
			read = reader.read();
		}
		return sb.toString();
	}

	public boolean hasNextInt() throws IOException {
		while (read != -1) {
			read = reader.read();
			if (Character.isDigit((char)read) || (char)read == '-') {
				return true;
			}
		}
		return false;
	}

	public int nextInt() throws IOException {
		StringBuilder sb = new StringBuilder();
		while (!Character.isWhitespace((char)read) && read != -1) {
			sb.append((char) read);
			read = reader.read();
		}
		return Integer.parseInt(sb.toString());
	}

	public boolean hasNext() throws IOException {
		read = reader.read();
		return read != -1;
	}

	public int next() throws IOException {
		return read;
	}

	public boolean hasNextLine() throws IOException {
		StringBuilder sb = new StringBuilder();
		while (read != -1) {
			read = reader.read();
			if ((char)read == '\n' || read == -1) {
				line = sb.toString();
				return true;
			}
			sb.append((char)read);
		}
		return false;
	}

	public String nextLine() throws IOException {
		return line;
	}

	public void close() throws IOException {
		reader.close();
	}
}
