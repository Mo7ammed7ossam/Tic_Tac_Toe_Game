package SinglePlayer;

import java.util.ArrayList;
import java.util.Random;

public class EasyAI extends AI {
    Random generator;

    public EasyAI() {
        generator = new Random();
    }

    /**
     * Chooses the next move by selecting an empty remaining cell at random.
     *
     * @param board Board being used
     * @param turns Turn number isn't used in this implementation
     * @return { column, row } or null if no move available
     */
    @Override
    public int[] getMove(Board board, int turns) {
        ArrayList<Integer> emptyCells = getEmptyCells(board);

        int move = emptyCells.get(generator.nextInt(emptyCells.size()));
        return Board.intToCell(move);
    }
}