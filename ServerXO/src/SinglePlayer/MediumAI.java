/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SinglePlayer;

import java.util.ArrayList;
import java.util.Random;

public class MediumAI extends AI {
    private static Random generator = new Random();

    /**
     * Chooses the next move by trying to win first, then check to block the player's win. If there
     * are no upcoming winning moves, it will choose a remaining empty cell at random.
     *
     * @param board Board being used
     * @param turns Turn number isn't used in this implementation
     * @return { column, row } or null if no move available
     */
    @Override
    public int[] getMove(Board board, int turns) {
        if (turns >= 3) {
            final int X_WIN = 0, O_WIN = 1;
            int[] winner = predictWin(board);

            if (winner[O_WIN] != -1) {
                return Board.intToCell(winner[O_WIN]);
            } else if (winner[X_WIN] != -1) {
                return Board.intToCell(winner[X_WIN]);
            }
        }

        ArrayList<Integer> emptyCells = getEmptyCells(board);
        int move = emptyCells.get(generator.nextInt(emptyCells.size()));
        return Board.intToCell(move);
    }
}