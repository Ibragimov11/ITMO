import java.util.ArrayList;
import java.util.List;

public class Tournament {

    List<Player> players = new ArrayList<>();
    int c, num;
    int m, n, k, type;
    int[] table;

    public Tournament(MyReader in) {
        System.out.println("Enter the number of players");
        this.num = in.readInt();
        while (num < 2) {
            System.out.println("number of players should be >= 2");
            num = in.readInt();
        }
        System.out.println("Enter the number of circles");
        this.c = in.readInt();
        while (c < 1) {
            System.out.println("c should be >= 1");
            c = in.readInt();
        }
        this.table = new int[num];
        System.out.println("Enter " + num +  " numbers: 1 for HumanPlayer, 2 for RandomPlayer, 3 for SequentialPlayer");
        for (int i = 0; i < num; i++) {
            System.out.println("Type of " + (i + 1) + " player:");
            type = in.readPlayer();
            if (type == 1) {
                players.add(new HumanPlayer());
            } else if (type == 2) {
                players.add(new RandomPlayer());
            } else{
                players.add(new SequentialPlayer());
            }
        }
    }

    public Tournament() {
        this(new MyReader());
    }

    public void start() {
        for (int circle = 0; circle < c; circle++) {
            for (int i = 0; i < num - 1; i++) {
                for (int j = i + 1; j < num; j++) {
                    Game game = new Game(true, players.get(i), players.get(j));
                    int result;
                    System.out.println("\nMatch №" +  (circle + 1) + " of player" + (i + 1) + " and player" + (j+1) + ":");
                    System.out.println("Enter m, n, k");
                    MyReader myReader = new MyReader();
                    int[] a = myReader.readMNK();
                    m = a[0];
                    n = a[1];
                    k = a[2];
                    while (m < 1 || n < 1 || k < 1 || k > Math.max(m, n)) {
                        System.out.println("m should be >= 1; n >= 1; 1 <= k <= max(m, n)");
                        a = myReader.readMNK();
                        m = a[0];
                        n = a[1];
                        k = a[2];
                    }
                    if (circle % 2 == 0) {
                        System.out.println("\nplayer" + (i + 1) + " -> X; player" + (j + 1) + " -> O" );
                        result = game.play(new MNKBoard(m, n, k), i + 1, j + 1);
                    } else {
                        System.out.println("\nplayer" + (j + 1) + " -> X; player" + (i + 1) + " -> O" );
                        result = game.play(new MNKBoard(m, n, k), j + 1, i + 1);
                    }
                    if (result == 0) {
                        table[i]++;
                        table[j]++;
                        System.out.println("Game result: Draw");
                    } else {
                        table[result - 1] += 3;
                        System.out.println("Game result: " + result);
                    }
                }
            }
            System.out.println("\nCircle №" + (circle + 1) + " ended");
            System.out.println("\n================================================");
        }
    }

    public void getResult() {
        System.out.println("\nFinal result:");
        for (int i = 0; i < num; i++) {
            System.out.println("Player" + (i + 1) + " - " + table[i] + " points");
        }
    }
}
