/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import SinglePlayer.AI;
import SinglePlayer.Board;

/**
 *
 * @author Gehad
 */
public class SingleGame extends Game {
  AI Computer;

  public int[] computerTurn() {
    int move[];
    move = Computer.getMove(new Board(board), noOfTurns);
    return move;
  }

}
