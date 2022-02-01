public class Game {
    private final boolean log;
    private final Player player1, player2;
    int i, j;

    public Game(final boolean log, final Player player1, final Player player2) {
        this.log = log;
        this.player1 = player1;
        this.player2 = player2;
    }

    public int play(Board board, int i, int j) {
        this.i = i;
        this.j = j;
        while (true) {
            final int result1 = move(board, player1, i);
            if (result1 != -1) {
                return result1;
            }
            final int result2 = move(board, player2, j);
            if (result2 != -1) {
                return result2;
            }
        }
    }

    private int move(final Board board, final Player player, final int no) {
        final Move move = player.move(board.getPosition(), board.getCell());
        final Result result = board.makeMove(move);
        log("\nPlayer " + no + " move: " + move);
        log("\nPosition:\n" + board);
        if (result == Result.WIN) {
            log("\nPlayer " + no + " won");
            return no;
        } else if (result == Result.DRAW) {
            log("Draw");
            return 0;
        } else {
            return -1;
        }
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }
}
