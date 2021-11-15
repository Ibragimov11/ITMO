package PCMS;

import java.util.*;

public class I {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int xr = Integer.MIN_VALUE;
        int yr = Integer.MIN_VALUE;
        int xl = Integer.MAX_VALUE;
        int yl = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            int h = in.nextInt();
            if (x - h < xl) {
                xl = x - h;
            }
            if (y - h < yl) {
                yl = y - h;
            }
            if (x + h > xr) {
                xr = x + h;
            }
            if (y + h > yr) {
                yr = y + h;
            }
        }
        int h1 = Math.max(xr - xl, yr - yl);
        System.out.print((xl + xr) / 2 + " ");
        System.out.print((yl + yr) / 2 + " ");
        System.out.println((h1 / 2) + (h1 % 2));
    }
}
