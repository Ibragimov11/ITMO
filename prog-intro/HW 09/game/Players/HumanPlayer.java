import java.io.PrintStream;

public class HumanPlayer implements Player {
    private final PrintStream out;
    private final MyReader reader;

    public HumanPlayer(final PrintStream out, final MyReader reader) {
        this.out = out;
        this.reader = reader;
    }

    public HumanPlayer() {
        this(System.out, new MyReader());
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            out.println(cell + "'s move");
            out.println("Enter row and column");
            int[] mn = reader.readMove();
            final Move move = new Move(mn[0], mn[1], cell);
            if (position.isValid(move)) {
                return move;
            }
            out.println("Move " + move + " is invalid");
        }
    }
}
