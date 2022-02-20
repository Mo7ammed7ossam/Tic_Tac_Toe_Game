package SinglePlayer;
        import java.util.Random;
import java.util.Scanner;

public class ConsoleGame {
    private final static Board board = new Board();
    private final static Scanner scanner = new Scanner(System.in);
    private final static Random generator = new Random();
    private final static int MAX_TURNS = 9;

    public static void main(String[] args) {
//        String playAgain;
//        do {
//            AI computer = null;
//
//            System.out.print("Play against the computer [Y/n]? ");
//            String input = scanner.nextLine();
//            boolean playComputer = false;
//            if (!input.equalsIgnoreCase("n") && !input.equalsIgnoreCase("no")) {
//                playComputer = true;
//
//                do {
//                    System.out.print("Pick game mode [easy | medium | hard]: ");
//                    input = scanner.nextLine();
//
//                    if (input.equalsIgnoreCase("easy")) {
//                        computer = new EasyAI();
//                    } else if (input.equalsIgnoreCase("medium")) {
//                        computer =  new MediumAI();
//                    } else if (input.equalsIgnoreCase("hard")) {
//                        computer = new HardAI();
//                    } else {
//                        System.out.print("Invalid mode. ");
//                    }
//                } while (!input.equalsIgnoreCase("easy") && !input.equalsIgnoreCase("medium")
//                            && !input.equalsIgnoreCase("hard"));
//            }
//
//            int computerTurn = playComputer ? generator.nextInt(2) : -1;
//            int[] move;
//            CellState winner = null;
//            for (int turns = 0; turns < MAX_TURNS && winner == null; turns++) {
//                if (turns % 2 == computerTurn) { //Computer goes second
//                    move = computer.getMove(board, turns);
//                } else {
//                    drawBoard();
//                    move = promptMove(turns);
//                }
//                if (move == null) {
//                    System.out.println("Thanks for playing!");
//                } else {
//                    board.makeTurn(move[0], move[1]);
//                    System.out.println();
//                    if (turns >= 4) {
//                        winner = board.verifyWinner();
//                    }
//                }
//            }
//
//            if (winner != null) {
//                drawBoard();
//                System.out.println(winner + " won!!");
//            } else {
//                System.out.println("It was a tie.");
//            }
//
//            System.out.print("Play again [y/N]? ");
//            playAgain = scanner.nextLine();
//            if (playAgain.equalsIgnoreCase("y") || playAgain.equalsIgnoreCase("yes")) {
//                board.clearBoard();
//            }
//        } while (playAgain.equalsIgnoreCase("y") || playAgain.equalsIgnoreCase("yes"));
//        
         AI computer =  new EasyAI();
         Board b = new Board();
         char arr[][] = new char [3][3];
         arr[1][0]='X';
         arr[1][1]='O';
         arr[2][0]='X';
         int move [] ;
         move = computer.getMove(new Board(arr),3);
         arr[move[1]][move[0]]='O';
         for(int i=0;i<3;i++){
             for(int j=0;j<3;j++){
                 if(arr[i][j]== 'X' || arr[i][j]== 'O')
                    System.out.print(arr[i][j]);
                 else System.out.print(".");
             }
             System.out.println("");
            }
         System.out.println(move[0]+" "+move[1]);

    }

    private static void drawBoard() {
        String b = "\n";
        b += "1  " + cellStr(0, 0) + " | " + cellStr(1, 0) + " | " + cellStr(2, 0) + "\n"
          +  "  ---+---+---\n"
          +  "2  " + cellStr(0, 1) + " | " + cellStr(1, 1) + " | " + cellStr(2, 1) + "\n"
          +  "  ---+---+---\n"
          +  "3  " + cellStr(0, 2) + " | " + cellStr(1, 2) + " | " + cellStr(2, 2) + "\n"
          +  "   1   2   3\n";

        System.out.println(b);
    }

    private static String cellStr(int column, int row) {
        return board.getCell(column, row).toString();
    }

    /**
     * Prompts the user for their move.
     *
     * @param turn Turn number
     * @return Selected cell, or null if user wants to quit
     */
    private static int[] promptMove(int turn) {
        if (turn % 2 == 0) {
            System.out.println("It's X's turn!");
        } else {
            System.out.println("It's O's turn!");
        }

        int[] cell = { 0, 0 };
        boolean validTurn = false;
        do {
            System.out.print("Enter your turn (<column> <row>): ");
            String[] input = scanner.nextLine().split(" ");

            if (input.length < 2) {
                if (input[0].equalsIgnoreCase("quit")) {
                    System.exit(0);
                } else {
                    System.out.println("Please enter both a column and a row!");
                }
            }
            else {
                try {
                    cell[0] = Integer.parseInt(input[0]) - 1;
                    cell[1] = Integer.parseInt(input[1]) - 1;

                    if (cell[0] < 0 || cell[0] > 2 || cell[1] < 0 || cell[1] > 2) {
                        System.out.println("That cell doesn't exist!");
                    } else if (board.getCell(cell[0], cell[1]) != CellState.BLANK) {
                        System.out.println("That cell has already been played!");
                    } else {
                        validTurn = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid column and row.");
                }
            }
        } while (!validTurn);

        return cell;
    }
}