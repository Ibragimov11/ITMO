import java.util.*;

public class ReverseMin {

    public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
      int[] v = new int[1000000];
      ArrayList<Integer> dl = new ArrayList<>();
      ArrayList<Integer> m1 = new ArrayList<>();
      ArrayList<Integer> m2 = new ArrayList<>();
      int k = 0;
      dl.add(0);
      while (sc.hasNextLine()) {
        Scanner sn = new Scanner(sc.nextLine());
        while (sn.hasNextInt()) {
          v[k] = sn.nextInt();
          k += 1;
        }
        dl.add(k);
      }

      for (int i = 0; i < dl.size() - 1; i++) {
        if (dl.get(i + 1) - dl.get(i) == 0) {
          m1.add(Integer.MAX_VALUE);
          if (i == 0) {
            m2.add(Integer.MAX_VALUE);
          }
        }
        for (int j = dl.get(i); j < dl.get(i + 1); j++) {
          int h = j - dl.get(i);
          if (i == 0 & h == 0) {
            m1.add(v[j]);
            m2.add(v[j]);
          } else {
            if (h == 0) {
              m1.add(v[j]);
              if (v[j] < m2.get(h)) {
                m2.set(h, v[j]);
              }
            } else {
              if (h == m2.size()) {
                m2.add(v[j]);
              } else {
                if (v[j] < m2.get(h)) {
                  m2.set(h, v[j]);
                }
              }
              if (v[j] < m1.get(i)) {
                m1.set(i, v[j]);
              }
            }
          }
        }
      }

      for (int i = 0; i < dl.size() - 1; i++) {
        if (dl.get(i + 1) - dl.get(i) != 0) {
          for (int j = dl.get(i); j < dl.get(i + 1); j++) {
            int h = j - dl.get(i);
            int m = m1.get(i);
            if (m2.get(h) < m) {
              m = m2.get(h);
            }
            System.out.print(m + " ");
          }
        }
        System.out.println();
      }
    }

}
