import java.util.Scanner;

public class MyReader {

    public Scanner scanner;

    public MyReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public MyReader() {
        this(new Scanner(System.in));
    }

    public int readInt() { // Ввод до первого целого числа
        String s = scanner.next();
        while (!isInt(s)) {
            System.out.println("Your input is incorrect, try again");
            s = scanner.next();
        }
        return Integer.parseInt(s);
    }

    public int[] readMove() { // Ход человека
        int[] a = new int[2];
        String row = scanner.next();
        String col = scanner.next();
        while (!(isInt(row) && isInt(col))) {
            System.out.println("Your input is incorrect, try again");
            row = scanner.next();
            col = scanner.next();
        }
        a[0] = Integer.parseInt(row);
        a[1] = Integer.parseInt(col);
        return a;
    }

    public int[] readMNK() { // Размеры доски и К
        int[] a = new int[3];
        String m = scanner.next();
        String n = scanner.next();
        String k = scanner.next();
        while (!(isInt(m) && isInt(n) && isInt(k))) {
            System.out.println("m, n, k should be int, try again");
            m = scanner.next();
            n = scanner.next();
            k = scanner.next();
        }
        a[0] = Integer.parseInt(m);
        a[1] = Integer.parseInt(n);
        a[2] = Integer.parseInt(k);
        return a;
    }

    public int readPlayer() { // Тип игрока
        String s = scanner.next();
        while (!isInt(s) || Integer.parseInt(s) > 3 || Integer.parseInt(s) < 1) {
            System.out.println("Your input must be int, >= 1 and <= 3");
            s = scanner.next();
        }
        return Integer.parseInt(s);
    }

    private boolean isInt(String s) { // Проверка на целое число
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
