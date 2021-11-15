package PCMS;

import java.util.*;

public class J {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[][] array = new int[n][n];
        for (int i = 0; i < n; i++) {
            String str = in.next();
            for (int j = 0; j < n; j++) {
                array[i][j] = (Character.getNumericValue(str.charAt(j)));
            }
        }

        for (int i = 0; i < n - 2; i ++) {
            for (int j = i + 1; j < n - 1; j++) {
                if (array[i][j] > 0) {
                    for (int k = j + 1; k < n; k++) {
                        array[i][k] = (array[i][k] + 10 - array[j][k]) % 10;
                    }
                }
            }
        }

        for (int i = 0; i < n; i ++) {
            for (int j = 0; j < n; j++) {
                System.out.print(array[i][j]);
            }
            System.out.println();
        }

    }
}
