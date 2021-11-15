package PCMS;

import java.util.*;

public class A {
    public static void main(String[] args) {
        Scanner scanner = new Scanner (System.in);
        double a = scanner.nextDouble();
        double b = scanner.nextDouble();
        double n = scanner.nextDouble();
        double k = 2 * Math.ceil((n - b) / (b - a)) + 1;
        System.out.println((int) k);
    }
}
