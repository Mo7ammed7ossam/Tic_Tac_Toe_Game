/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SinglePlayer;

import java.util.Arrays;

public class Board {

    private CellState[][] squares;  //[column][row]
    private boolean xTurn;

    public Board(boolean xFirst) {
        xTurn = xFirst;
        squares = new CellState[3][3];
        clearBoard();
    }

    public Board(char arr[][]) {
        squares = new CellState[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(arr[i][j]=='X')
                    squares[i][j] = CellState.X;
                else if(arr[i][j]=='O')
                    squares[i][j] = CellState.O;
                else  squares[i][j] = CellState.BLANK;
            }
        }
    }

    public Board() {
        this(true);
    }

    public boolean makeTurn(int column, int row) {
        if (column < 0 || column > 2 || row < 0 || row > 2) {
            return false;
        }

        if (xTurn) {
            squares[column][row] = CellState.X;
        } else {
            squares[column][row] = CellState.O;
        }

        xTurn = !xTurn;

        return true;
    }

    final public void clearBoard() {
        for (CellState[] row : squares) {
            Arrays.fill(row, CellState.BLANK);
        }
    }

    public CellState getCell(int column, int row) {
        return squares[column][row];
    }

    public CellState getCell(int[] cell) {
        if (cell.length != 2) {
            throw new IllegalArgumentException("Cells must have columns and rows.");
        }

        return getCell(cell[0], cell[1]);
    }

    public CellState verifyWinner() {
        CellState winner = null;

        //Test columns
        for (int r = 0; winner == null && r < 3; r++) {
            if (lineMatches(squares[0][r], squares[1][r], squares[2][r])) {
                winner = squares[0][r];
            }
        }

        //Test rows
        for (int c = 0; winner == null && c < 3; c++) {
            if (lineMatches(squares[c][0], squares[c][1], squares[c][2])) {
                winner = squares[c][0];
            }
        }

        //Test diagonals
        if (winner == null && lineMatches(squares[0][0], squares[1][1], squares[2][2])) {
            winner = squares[0][0];
        }
        if (winner == null && lineMatches(squares[0][2], squares[1][1], squares[2][0])) {
            winner = squares[0][2];
        }

        return winner;
    }

    /**
     * Converts an int to a cell. All cell numbers start at 0 and go top to
     * bottom, left to right.
     *
     * @param cellNumber Cell number to be converted
     * @return { column, row }
     */
    public static int[] intToCell(int cellNumber) {
        int cell[] = {cellNumber / 3, cellNumber % 3};
        return cell;
    }

    private static boolean lineMatches(CellState cell1, CellState cell2, CellState cell3) {
        return (cell1.matches(cell2) && cell2.matches(cell3));
    }
}
