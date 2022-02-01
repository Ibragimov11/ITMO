package ru.itmo.wp.web.page;

import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {
    private static State state;

    private void action(Map<String, Object> view) {
        if (state == null) {
            state = new State();
        }

        view.put("state", state);
    }

    private void onMove(HttpServletRequest request, Map<String, Object> view) {
        for (String key : request.getParameterMap().keySet()) {
            if (key.startsWith("cell_")) {
                int row = key.charAt(key.length() - 2) - '0';
                int column = key.charAt(key.length() - 1) - '0';

                if (0 <= row && row < 3 && 0 <= column && column < 3
                        && state.cells[row][column] == null && state.phase == State.Phase.RUNNING) {
                    state.makeMove(row, column);
                }
            }
        }

        throw new RedirectException("/ticTacToe");
    }

    private void newGame() {
        state = null;

        throw new RedirectException("/ticTacToe");
    }

    public static class State {
        private enum Cell {
            O, X
        }

        private enum Phase {
            RUNNING, WON_X, WON_O, DRAW
        }

        private final int size;
        private final Cell[][] cells;
        private Phase phase;
        private boolean crossesMove;

        public State() {
            size = 3;
            cells = new Cell[size][size];
            phase = Phase.RUNNING;
            crossesMove = true;
        }

        private void makeMove(int row, int col) {
            cells[row][col] = crossesMove ? Cell.X : Cell.O;
            phase = checkResult();
            crossesMove = !crossesMove;
        }

        private Phase checkResult() {
            int empty = 0;

            Cell turn = crossesMove ? Cell.X : Cell.O;

            for (int u = 0; u < 3; u++) {
                int row = 0;
                for (int v = 0; v < 3; v++) {
                    if (cells[u][v] == turn) {
                        row++;
                    }

                    if (cells[u][v] == null) {
                        empty++;
                    }
                }

                if (row == 3) {
                    return crossesMove ? Phase.WON_X : Phase.WON_O;
                }
            }

            for (int v = 0; v < 3; v++) {
                int column = 0;
                for (int u = 0; u < 3; u++) {
                    if (cells[u][v] == turn) {
                        column++;
                    }
                }

                if (column == 3) {
                    return crossesMove ? Phase.WON_X : Phase.WON_O;
                }
            }

            int diag1 = 0;
            int diag2 = 0;
            
            for (int w = 0; w < 3; w++) {
                if (cells[w][w] == turn) {
                    diag1++;
                }
                if (cells[2 - w][w] == turn) {
                    diag2++;
                }
            }
            
            if (diag1 == 3 || diag2 == 3) {
                return crossesMove ? Phase.WON_X : Phase.WON_O;
            }

            if (empty == 0) {
                return Phase.DRAW;
            }

            return Phase.RUNNING;
        }

        public int getSize() {
            return size;
        }

        public Cell[][] getCells() {
            return cells;
        }

        public Phase getPhase() {
            return phase;
        }

        public boolean isCrossesMove() {
            return crossesMove;
        }
    }
}
