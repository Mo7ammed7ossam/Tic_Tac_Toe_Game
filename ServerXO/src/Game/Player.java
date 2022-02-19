package Game;

import Database.DBManger;
import Network.Message;
import SinglePlayer.*;
import serverxo.*;

import java.util.Arrays;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.control.TableView;

import javafx.scene.text.Text;

/**
 *
 * @author mohamed alwakiel
 */
public class Player {

    char mark;
    Player opponent;
    Socket socket;
    ObjectInputStream input;
    ObjectOutputStream output;
    Game game;
    int idnum;
    String names;
    int points;
    int isOnline;
    Text online;

    /**
     * Constructs a handler thread for a given socket and mark initializes the
     * stream fields, displays the first two welcoming messages.
     */
    public Player() {
        this.idnum = -1;
        this.socket = null;
        this.isOnline = 0;
    }

    public Player(int ID, String name, int points) {
        this.idnum = ID;
        this.names = name;
        this.points = points;
        this.socket = null;
        this.isOnline = 0;
    }

    public void startThread() {
        new Thread(() -> {
            try {
                while (true) {
                    System.out.println("Listening Player");

                    Message msg = (Message) input.readObject(); // read from client

                    Player p1 = null;
                    Player p2 = null;
                    Message outputMsg = null;
                    System.out.println(msg.getType()); // print login

                    ////
                    if (msg == null || msg.getType().equals("CloseConn")) // try to close game or log out
                    {
                        if (Player.this.game != null) // object from game --> game open
                        {
                            if (Player.this.game instanceof MultiGame) // object from multigame --> 2players
                            {
                                handleCloseGame(Player.this);
                            }
                        }

                        Player.this.isOnline = 0;
                        Player.this.input.close();
                        Player.this.output.close();
                        Player.this.socket.close();

                        System.out.println("player is offline");

                        FXMLDocumentController.updatePlayerList();

                        Player.broadCastPlayerList();
                        return;
                    }

                    ////////
                    if (msg.getType().equals("multiPlay")) {
                        System.out.println(msg.getData()[0] + " " + msg.getData()[1]);
                        p1 = getPlayer(Integer.parseInt(msg.getData()[0]));
                        p2 = getPlayer(Integer.parseInt(msg.getData()[1]));
                        if (p2.isOnline == 1) {
                            Message playRequest = (new Message("playRequest",
                                    new String[] { Integer.toString(p1.idnum), Integer.toString(p2.idnum) }));
                            p2.output.writeObject(playRequest);

                        }
                    } else if (msg.getType().equals("playRequest")) {
                        if (msg.getData()[0].equals("accept")) {
                            p1 = getPlayer(Integer.parseInt(msg.getData()[1]));
                            p2 = getPlayer(Integer.parseInt(msg.getData()[2]));

                            if (p1.isOnline == 1 && p2.isOnline == 1) {
                                String[] oldGame = GameController.dbManger.getGameBoard(msg); // xxxooo, 1

                                outputMsg = (new Message("play", new String[] { Integer.toString(p1.idnum),
                                        Integer.toString(p2.idnum), oldGame[0] }));

                                p1.output.writeObject(outputMsg);
                                p2.output.writeObject(outputMsg);

                                intializeMultiGame(p1, p2, oldGame[0], oldGame[1]); // p1,p2, senario, p1_id,
                            }
                        } else {
                            Message rejected = (new Message("reject",
                                    new String[] { Integer.toString(p1.idnum), Integer.toString(p2.idnum) }));
                            p1.output.writeObject(rejected);
                        }
                    } else if (msg.getType().equals("chatting")) {
                        Player player = null;
                        player = getPlayer(Integer.parseInt(msg.getData()[0]));
                        MultiGame multiGame = (MultiGame) player.game;
                        Player opponent1 = (player == multiGame.p1) ? multiGame.p2 : multiGame.p1;
                        Message chat = new Message("chatting", new String[] { msg.getData()[0], msg.getData()[1],
                                player.getNames(), opponent1.getNames() });
                        player.output.writeObject(chat);
                        opponent1.output.writeObject(chat);
                    }

                    //////////////////////
                    if (msg.getType().equals("EasySingle")) {
                        Player player = getPlayer(Integer.parseInt(msg.getData()[0]));
                        player.output.writeObject(new Message("StartEasyGame", new String[] {}));

                        intializeSingleGame(player, new EasyAI());

                        System.out.println("StartEasyGame :" + msg.getData()[0]);
                    }

                    //////////////
                    if (msg.getType().equals("MediumSingle")) {
                        Player player = getPlayer(Integer.parseInt(msg.getData()[0]));
                        player.output.writeObject(new Message("StartMediumGame", new String[] {}));
                        intializeSingleGame(player, new MediumAI());
                        System.out.println("StartMediumGame : " + msg.getData()[0]);
                    }

                    ///////////
                    if (msg.getType().equals("HardSingle")) {
                        Player player = getPlayer(Integer.parseInt(msg.getData()[0]));
                        player.output.writeObject(new Message("StartHardGame", new String[] {}));
                        intializeSingleGame(player, new HardAI());
                        System.out.println("StartHardGame : " + msg.getData()[0]);
                    }

                    ////////////
                    if (msg.getType().startsWith("Move")) {
                        handleMove(msg); // client
                    }

                    ////////////
                    if (msg.getType().equals("listRequest")) {
                        Player player = getPlayer(Integer.parseInt(msg.getData()[0]));
                        outputMsg = new Message("listResponse", new String[] {});
                        outputMsg.setData(playerListToArray(GameController.players));
                        player.output.writeObject(outputMsg);
                    }
                }
            } catch (IOException ex) {
                try {
                    System.out.println("Player is offline");
                    if (Player.this.game != null) {
                        handleCloseGame(Player.this);
                    }
                    broadCastPlayerList();
                    FXMLDocumentController.updatePlayerList();
                    Player.broadCastPlayerList();
                    Player.this.isOnline = 0;
                    if (!Player.this.socket.isClosed()) {
                        Player.this.socket.close();
                    }
                    return;
                } catch (IOException ex1) {
                    Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();

    }

    public void handleMultiplayer(Message msg) {
        Message outputMsg = null;
        Player p1 = null;
        Player p2 = null;
        System.out.println(msg.getData()[0] + " " + msg.getData()[1]);
        p1 = getPlayer(Integer.parseInt(msg.getData()[0]));
        p2 = getPlayer(Integer.parseInt(msg.getData()[1]));

        if (p1.isOnline == 1 && p2.isOnline == 1) {
            try {
                outputMsg = (new Message("multiPlay",
                        new String[] { Integer.toString(p1.idnum), Integer.toString(p2.idnum) }));
                p1.output.writeObject(outputMsg);
                p2.output.writeObject(outputMsg);

            } catch (IOException ex) {
                Logger.getLogger(Player.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void intializeSingleGame(Player player, AI mode) {
        SingleGame game = new SingleGame();
        game.p1 = player;
        game.Computer = mode;
        game.turn = 'X';
        player.game = game;
        player.mark = 'X';
    }

    public void intializeMultiGame(Player p1, Player p2, String Scenario, String Xplayer) {
        MultiGame game = new MultiGame(); // p2

        game.p1 = p1;
        game.p2 = p2;

        game.turn = 'X';

        p1.game = game;
        p1.mark = 'X';

        p2.game = game;
        p2.mark = 'O';

        p2.opponent = p1;
        p1.opponent = p2;

        if (!Scenario.equals("")) // scenario exist
        {
            int ind = 0;
            int xCounter = 0;
            int oCounter = 0;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {

                    char T = Scenario.charAt(ind++);

                    if (T == 'X') {
                        xCounter++;
                        game.board[j][i] = 'X';
                    } else if (T == 'O') {
                        oCounter++;
                        game.board[j][i] = 'O';
                    }
                    System.out.print(game.board[i][j]);
                }
                System.out.println();
            }
            if (oCounter == xCounter) {
                game.turn = 'X';
            } else {
                game.turn = 'O';
            }
            if (p1.idnum == Integer.parseInt(Xplayer)) {
                p1.mark = 'X';
                p2.mark = 'O';
            } else {
                p1.mark = 'O';
                p2.mark = 'X';
            }
        }
    }

    public void handleMove(Message msg) {
        System.out.println("request to " + msg.getType());

        int col = Integer.parseInt(msg.getType().charAt(5) + ""); // index 5 --> col
        int row = Integer.parseInt(msg.getType().charAt(7) + ""); // index 7 --> row

        Player player = getPlayer(Integer.parseInt(msg.getData()[0])); // id

        if (player.game instanceof SingleGame) {
            handleSingleGame(player, col, row);
        } else {
            handleMultiGame(player, col, row);
        }
    }

    public static void broadCastPlayerList() {

        ArrayList<Player> players = GameController.players;
        int listSize = players.size();

        for (int i = 0; i < listSize; i++) {
            Player player = players.get(i);

            if (player.isOnline == 1 && player.socket != null) //
            {
                Message msg = new Message("listResponse", new String[] {}); // sent msg to client
                msg.setData(playerListToArray(GameController.players)); // get array of palyers

                try {
                    player.output.writeObject(msg);
                } catch (IOException ex) {
                    System.out.println("Can't Send The List");
                }
            } else if (player.isOnline == 2 && player.socket != null) {
                Message msg = new Message("listResponse", new String[] {});
                msg.setData(playerListToArray(GameController.players));

                try {
                    player.output.writeObject(msg);
                } catch (IOException ex) {
                    System.out.println("Can't Send The List");
                }
            }
        }

    }

    public void handleSingleGame(Player player, int col, int row) {
        try {
            player.isOnline = 2;
            Player.broadCastPlayerList();
            SingleGame singleGame = (SingleGame) player.game;
            if (singleGame.legalMove(col, row, player.mark)) {
                player.output.writeObject(new Message("Move X " + col + " " + row, new String[] {}));
                game.noOfTurns++;
                if (singleGame.hasWinner()) {
                    player.isOnline = 1;
                    Player.broadCastPlayerList();
                    // Add Winning Points
                    Thread.sleep(500);
                    player.output.writeObject(new Message("WIN", new String[] {}));
                    if (singleGame.Computer instanceof EasyAI) {
                        player.setPoints(points += 5);
                    }
                    if (singleGame.Computer instanceof MediumAI) {
                        player.setPoints(points += 10);
                    }
                    if (singleGame.Computer instanceof HardAI) {
                        player.setPoints(points += 15);
                    }
                    GameController.dbManger.update(player);
                    FXMLDocumentController.sortPlayerList();
                    FXMLDocumentController.updatePlayerList();
                    Player.broadCastPlayerList();

                } else if (singleGame.boardFilledUp()) {
                    player.isOnline = 1;
                    Player.broadCastPlayerList();
                    Thread.sleep(500);
                    player.output.writeObject(new Message("DRAW", new String[] {}));
                } else {

                    int move[] = singleGame.computerTurn();
                    game.noOfTurns++;
                    singleGame.legalMove(move[0], move[1], 'O');
                    player.output.writeObject(new Message("Move O " + move[0] + " " + move[1], new String[] {}));
                    System.out.println("Computer Mover " + move[0] + " " + move[1]);
                    if (singleGame.hasWinner()) {
                        player.isOnline = 1;
                        Player.broadCastPlayerList();
                        Thread.sleep(500);
                        player.output.writeObject(new Message("LOSE", new String[] {}));
                    } else if (singleGame.boardFilledUp()) {
                        player.isOnline = 1;
                        Player.broadCastPlayerList();
                        Thread.sleep(500);
                        player.output.writeObject(new Message("DRAW", new String[] {}));

                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Player.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (InterruptedException ex) {
            Logger.getLogger(Player.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(Player.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleMultiGame(Player player, int col, int row) {
        try {
            MultiGame multiGame = (MultiGame) player.game;

            Player opponent = (player == multiGame.p1) ? multiGame.p2 : multiGame.p1;

            player.isOnline = 2;
            opponent.isOnline = 2;

            Player.broadCastPlayerList();

            if (multiGame.legalMove(col, row, player.mark)) {
                player.output.writeObject(new Message("Move " + player.mark + " " + col + " " + row, new String[] {}));
                opponent.output
                        .writeObject(new Message("Move " + player.mark + " " + col + " " + row, new String[] {}));

                game.noOfTurns++;

                System.out.println(
                        "player : " + player.idnum + player.mark + " opponent : " + opponent.idnum + opponent.mark);

                if (multiGame.hasWinner()) {

                    player.isOnline = 1;
                    opponent.isOnline = 1;

                    Player.broadCastPlayerList();

                    if (multiGame instanceof MultiGame) {
                        Thread.sleep(500);

                        player.output.writeObject(new Message("WIN", new String[] {}));
                        opponent.output.writeObject(new Message("LOSE", new String[] {}));

                        player.setPoints(points += 10);

                        FXMLDocumentController.sortPlayerList();

                        GameController.dbManger.update(player);

                        GameController.dbManger.deleteOldGame(player.idnum, opponent.idnum);

                        FXMLDocumentController.updatePlayerList();
                        Player.broadCastPlayerList();
                    }

                } else if (multiGame.boardFilledUp()) { // draw
                    player.isOnline = 1;
                    opponent.isOnline = 1;
                    Player.broadCastPlayerList();
                    Thread.sleep(500);
                    GameController.dbManger.deleteOldGame(player.idnum, opponent.idnum);

                    player.output.writeObject(new Message("DRAW", new String[] {}));
                    opponent.output.writeObject(new Message("DRAW", new String[] {}));

                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Player.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (InterruptedException ex) {
            Logger.getLogger(Player.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(Player.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleCloseGame(Player player) {
        int x, o;
        if (player.mark == 'X') { // check which player try to close the game
            x = player.idnum;
            o = opponent.idnum;
        } else {
            o = player.idnum;
            x = opponent.idnum;
        }

        System.out.println("x :" + x + " o: " + o);

        System.out.println(Arrays.toString(player.game.board));

        String strArr = "";

        for (int i = 0; i < player.game.board.length; i++) {
            for (int j = 0; j < player.game.board.length; j++) {
                strArr += player.game.board[j][i];
            }
        }

        // boolean senario = GameController.dbManger.setGame(x, o, strArr); // store the game into db

        try {
            if (opponent.isOnline == 0) {
                player.isOnline = 1;
            }

            opponent.output.writeObject(new Message("OpponentLeft", new String[] {}));
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }

        // System.out.println(senario);

        player.isOnline = 0;

        opponent.isOnline = 1;
    }

    public Player getPlayer(int id) {
        ArrayList<Player> players = GameController.players;
        int playersSize = players.size();
        Player player = null;
        for (int i = 0; i < playersSize; i++) {
            if (players.get(i).idnum == id) {
                player = players.get(i);
            }
        }
        return player;
    }

    /**
     * function return players as an array of string.
     */
    public static String[] playerListToArray(ArrayList<Player> playerList) {
        int listSize = playerList.size();
        String[] result = new String[listSize]; // array of numbers of players

        for (int i = 0; i < listSize; i++) {
            Player player = playerList.get(i);

            // store player data on array
            result[i] = player.getIdnum() + "/" + player.getNames() + "/" + player.getPoints() + "/"
                    + player.isIsOnline();
            System.out.println("Player 1 :" + result[i]);
        }
        return result;
    }

    /**
     * Handles the otherPlayerMoved message.
     */
    public char getMark() {
        return mark;
    }

    public void setMark(char mark) {
        this.mark = mark;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public void setInput(ObjectInputStream input) {
        this.input = input;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getIdnum() {
        return idnum;
    }

    public void setIdnum(int idnum) {
        this.idnum = idnum;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int isIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    /**
     * Accepts notification of who the opponent is.
     */
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Text getOnline() {
        if (isOnline == 1) {
            online = new Text("Online");
            online.setStyle("-fx-fill: green; -fx-font-weight: bold;");
        } else if (isOnline == 0) {
            online = new Text("Offline");
            online.setStyle("-fx-fill: red; -fx-font-weight: bold;");
        } else if (isOnline == 2) {
            online = new Text("Busy");
            online.setStyle("-fx-fill: orange; -fx-font-weight: bold;");
        }
        return online;
    }

    public void setOnline(Text online) {
        this.online = online;
    }

}
