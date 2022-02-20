/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SinglePlayer;

/**
 *
 * @author LapTop MarKet
 */
import java.util.ArrayList;

public abstract class AI {
    public abstract int[] getMove(Board board, int turns);

    /**
     * Gets a list of all empty cells remaining on the given board. All cell locations use the
     * Board's definition of cell numbers, which start at 0 and go top to bottom, left to right.
     *
     * @param board Board being used
     * @return List of empty cells' numbers
     */
    protected static ArrayList<Integer> getEmptyCells(Board board) {
        ArrayList<Integer> emptyCells = new ArrayList<>(9);

        for (int i = 0; i < 9; i++) {
            if (board.getCell(i / 3, i % 3) == CellState.BLANK) {
                emptyCells.add(i);
            }
        }

        return emptyCells;
    }

    /**
     * Predicts if either the computer or player can win in the next move. If X or O can't win,
     * its value will be null.
     *
     * @param board Board being used
     * @return { winning X cell number, winning O cell number }
     */
    protected static int[] predictWin(Board board) {
        int[] winner;
        int[] cells = new int[3];
        int xWin = -1, oWin = -1;

        //Test columns
        for (int c = 0; (xWin == -1 || oWin == -1) && c < 3; c++ ) {
            for (int r = 0; r < 3; r++) {
                cells[r] = c * 3 + r;
            }

            winner = winsNextTurn(board, cells[0], cells[1], cells[2]);
            if (winner != null) {
                if (winner[0] == CellState.X.ordinal()) {
                    xWin = winner[1];
                } else if (winner[0] == CellState.O.ordinal()) {
                    oWin = winner[1];
                }
            }
        }

        //Test rows
        for (int r = 0; (xWin == -1 || oWin == -1) && r < 3; r++) {
            for (int c = 0; c < 3; c++ ) {
                cells[c] = c * 3 + r;
            }

            winner = winsNextTurn(board, cells[0], cells[1], cells[2]);
            if (winner != null) {
                if (winner[0] == CellState.X.ordinal()) {
                    xWin = winner[1];
                } else if (winner[0] == CellState.O.ordinal()) {
                    oWin = winner[1];
                }
            }
        }

        //Test diagonals
        if (xWin == -1 || oWin == -1) {
            winner = winsNextTurn(board, 0, 4, 8);

            if (winner != null) {
                if (winner[0] == CellState.X.ordinal()) {
                    xWin = winner[1];
                }
                else if (winner[0] == CellState.O.ordinal()) {
                    oWin = winner[1];
                }
            }
        }
        if (xWin == -1 || oWin == -1) {
            winner = winsNextTurn(board, 2, 4, 6);

            if (winner != null) {
                if (winner[0] == CellState.X.ordinal()) {
                    xWin = winner[1];
                }
                else if (winner[0] == CellState.O.ordinal()) {
                    oWin = winner[1];
                }
            }
        }

        int[] winningMoves = { xWin, oWin };
        return winningMoves;
    }

    /**
     * Returns the cell number of the winning cell and which state would win it as an ordinal.
     * If there is no winner, null is returned.
     *
     * @param board Tic-Tac-Toe Board being played on
     * @param cell1 Int location of first cell
     * @param cell2 Int location of second cell
     * @param cell3 Int location of third cell
     * @return { CellState winner's ordinal, cell number }
     */
    private static int[] winsNextTurn(Board board, int cell1, int cell2, int cell3) {
        final int PLAYER = 0, CELL = 1;
        int[] winner = new int[2];

        CellState cs1 = board.getCell(Board.intToCell(cell1));
        CellState cs2 = board.getCell(Board.intToCell(cell2));
        CellState cs3 = board.getCell(Board.intToCell(cell3));

        if (cs1.matches(cs2) && cs3 == CellState.BLANK) {
            winner[PLAYER] = cs1.ordinal();
            winner[CELL]   = cell3;
            return winner;
        }

        if (cs2.matches(cs3) && cs1 == CellState.BLANK) {
            winner[PLAYER] = cs2.ordinal();
            winner[CELL]   = cell1;
            return winner;
        }

        if (cs1.matches(cs3) && cs2 == CellState.BLANK) {
            winner[PLAYER] = cs1.ordinal();
            winner[CELL]   = cell2;
            return winner;
        }

        return null;
    }
}
