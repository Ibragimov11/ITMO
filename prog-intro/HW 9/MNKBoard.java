import java.util.Arrays;
import java.util.Map;

public class MNKBoard implements Board, Position {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.'
    );

    private final Cell[][] cells;
    private Cell turn;
    private final int m, n, k;

    public MNKBoard(int m, int n, int k) {
        this.m = m;
        this.n = n;
        this.k = k;
        this.cells = new Cell[m][n];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    @Override
    public Result makeMove(final Move move) {
        cells[move.getRow()][move.getColumn()] = move.getValue();

        int empty = 0;

        for (int u = 0; u < m; u++) {
            int maxRow = 0;
            int inRow = 0;
            for (int v = 0; v < n; v++) {
                if (cells[u][v] == turn) {
                    inRow++;
                } else {
                    maxRow = Math.max(maxRow, inRow);
                    inRow = 0;
                }
                if (cells[u][v] == Cell.E) {
                    empty++;
                }
            }
            maxRow = Math.max(maxRow, inRow);
            if (maxRow >= k) {
                return Result.WIN;
            }
        }

        for (int v = 0; v < n; v++) {
            int maxColumn = 0;
            int inColumn = 0;
            for (int u = 0; u < m; u++) {
                if (cells[u][v] == turn) {
                    inColumn++;
                } else {
                    maxColumn = Math.max(maxColumn, inColumn);
                    inColumn = 0;
                }
            }
            maxColumn = Math.max(maxColumn, inColumn);
            if (maxColumn >= k) {
                return Result.WIN;
            }
        }

        int maxDiag1 = 0;
        int maxDiag2 = 0;

        if (k <= m && k <= n) {
            for (int u = 0; u < m - k + 1; u++) {
                for (int v = 0; v < n - k + 1; v++) {
                    int inDiag1 = 0;
                    int inDiag2 = 0;
                    for (int w = 0; w < k; w++) {
                        if (cells[u + w][v + w] == turn) {
                            inDiag1++;
                        } else {
                            maxDiag1 = Math.max(maxDiag1, inDiag1);
                            inDiag1 = 0;
                        }
                        if (cells[m - 1 - (u + w)][v + w] == turn) {
                            inDiag2++;
                        } else {
                            maxDiag2 = Math.max(maxDiag2, inDiag2);
                            inDiag2 = 0;
                        }
                    }
                    maxDiag1 = Math.max(maxDiag1, inDiag1);
                    maxDiag2 = Math.max(maxDiag2, inDiag2);
                }
            }
        }

        if (maxDiag1 >= k || maxDiag2 >= k) {
            return Result.WIN;
        }
        if (empty == 0) {
            return Result.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        return Result.UNKNOWN;
    }

    @Override
    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < m
                && 0 <= move.getColumn() && move.getColumn() < n
                && cells[move.getRow()][move.getColumn()] == Cell.E;
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public int getN() {
        return n;
    }

    @Override
    public int getM() {
        return m;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(" ");
        for (int r = 0; r < n; r ++) {
            sb.append(r);
        }
        for (int r = 0; r < m; r++) {
            sb.append("\n");
            sb.append(r);
            for (int c = 0; c < n; c++) {
                sb.append(SYMBOLS.get(cells[r][c]));
            }
        }
        return sb.toString();
    }
}
