/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Game;

/**
 *
 * @author Gehad
 */
public class Game {

    char board[][];
    Player p1;
    char turn;
    int noOfTurns;

    public Game() {
        board = new char[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = ' ';
        noOfTurns = 0;
    }

    public boolean legalMove(int col, int row, char player) {
        if (player == turn && board[col][row] == ' ') {
            board[col][row] = turn;
            turn = (turn == 'X') ? 'O' : 'X';
            return true;
        }
        return false;
    }

    public boolean hasWinner() {
        return (board[0][0] != ' ' && board[0][0] == board[1][0] && board[0][0] == board[2][0])
                || (board[0][1] != ' ' && board[0][1] == board[1][1] && board[0][1] == board[2][1])
                || (board[0][2] != ' ' && board[0][2] == board[1][2] && board[0][2] == board[2][2])
                || (board[0][0] != ' ' && board[0][0] == board[0][1] && board[0][0] == board[0][2])
                || (board[1][0] != ' ' && board[1][0] == board[1][1] && board[1][0] == board[1][2])
                || (board[2][0] != ' ' && board[2][0] == board[2][1] && board[2][0] == board[2][2])
                || (board[0][0] != ' ' && board[0][0] == board[1][1] && board[0][0] == board[2][2])
                || (board[2][0] != ' ' && board[2][0] == board[1][1] && board[2][0] == board[0][2]);
    }

    public boolean boardFilledUp() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}
