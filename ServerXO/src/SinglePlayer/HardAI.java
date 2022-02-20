/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SinglePlayer;

public class HardAI extends AI {
    /**
     * Chooses the next move as intelligently as possible. If the center square is open, it will
     * choose it immediately. Thus, the center square will be filled on either turn 0 or 1. Next,
     * try to win, or else block the player from winning. If there are no immediate wins, try to
     * take a corner, or a side if a corner isn't available. This mode should always win or tie.
     * At this time, the AI always plays as O.
     *
     * @param board Board being used
     * @param turns Turn number isn't used in this implementation
     * @return { column, row } or null if no move available
     */
    @Override
    public int[] getMove(Board board, int turns) {
        if (board.getCell(1, 1) == CellState.BLANK) {
            return Board.intToCell(4);
        }

        if (turns >= 3) { //No one can win before 4th move
            final int X_WIN = 0, O_WIN = 1;
            int[] winner = predictWin(board);

            if (winner[O_WIN] != -1) {
                return Board.intToCell(winner[O_WIN]);
            } else if (winner[X_WIN] != -1) {
                return Board.intToCell(winner[X_WIN]);
            }
        }

        final int[] corners = { 0, 6, 2, 8 };
        int[] currentCell;
        for (int i = 0; i < 4; i++) {
            currentCell = Board.intToCell(corners[i]);
            if (board.getCell(currentCell) == CellState.BLANK) {
                return currentCell;
            }
        }

        for (int i = 1; i < 8; i += 2) {
            currentCell = Board.intToCell(i);
            if (board.getCell(currentCell) == CellState.BLANK) {
                return currentCell;
            }
        }

        return null;
    }
}