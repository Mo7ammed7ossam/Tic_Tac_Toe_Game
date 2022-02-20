
package Network;

import clientxo.ClientXO;
import clientxo.FXMLDocumentController;
import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import player.Player;
import validpkg.Validation;

/**
 *
 * @author Ali
 */
public class Client extends Thread 
{

    private Socket socket;
    //to read message from server
    private ObjectInputStream input;
    // to sen request to server
    private ObjectOutputStream output;
    //FXMLDocumentController gameController = null;
    
    // Constructor to get listen socken and Input out put stream
    public Client(Socket socket, ObjectInputStream input, ObjectOutputStream output) 
    {
        this.socket = socket;
        this.input = input;
        this.output = output;
    }
    // intialize socket and input out with relation to socket
    public Client(Socket socket) throws IOException 
    {
        this.socket = socket;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
    }
    
    // function to close Connection
    public void closeConn() 
    {
        try 
        {
            this.input.close();
            this.socket.close();
            this.output.close();
            Platform.exit();
        } 
        catch (IOException ex) 
        {
            System.out.println("can't close the session");
        }
        finally
        {
            System.out.println("Connection Close Successfully");
        }
        
    }

    public void sendMessage(Message msg) 
    {
        try 
        {
            output.writeObject(msg);
        } 
        catch (IOException ex) 
        {
            System.out.println("error with sending message to server");
        }
        finally
        {
            System.out.print("message sent to server successfully");
        }
    }

    @Override
    public void run() {
        Message msg = null;
        while (true) {
            try {
                
                msg = (Message) input.readObject();
                //if message come with null then server Must be Offline
                if (msg == null) 
                {
                    System.out.println("server is offline Can't send request Now");
                    return;
                }
                
                if (msg.getType().equals("Login")) 
                {
                    handleLogin(msg);
                } 
                else if (msg.getType().equals("Signup")) 
                {
                    handleSignup(msg);
                }
                else if (msg.getType().equals("playRequest")) 
                {
                    new FXMLDocumentController().playAccept(msg.getData()[0], msg.getData()[1]);
                }
                else if (msg.getType().equals("play")) 
                {
                    new FXMLDocumentController().multiGameWindow();
                    if (!msg.getData()[2].equals("")) 
                    {
                        retrieveOldGame(msg.getData()[2]);
                    }
                } 
                else if (msg.getType().equals("chatting")) 
                {
                    handleChatting(msg.getData());
                } 
                else if (msg.getType().equals("StartEasyGame")) 
                {
                    new FXMLDocumentController().gameWindow();
                }
                else if (msg.getType().equals("StartMediumGame")) 
                {
                    new FXMLDocumentController().gameWindow();
                }
                else if (msg.getType().equals("StartHardGame")) 
                {
                    new FXMLDocumentController().gameWindow();
                }
                else if (msg.getType().startsWith("Move")) 
                {
                    handleMove(msg.getType());
                }
                //alerts
                else if (msg.getType().startsWith("WIN")) 
                {
                    new FXMLDocumentController().winAlert("WIN");
                }
                else if (msg.getType().startsWith("LOSE")) 
                {
                    new FXMLDocumentController().winAlert("LOSE");
                }
                else if (msg.getType().equals("DRAW")) 
                {
                    new FXMLDocumentController().winAlert("DRAW");
                }
                else if (msg.getType().equals("listResponse")) 
                {
                    fillPlayerList(msg.getData());
                }
                else if(msg.getType().equals("OpponentLeft"))
                {
                    new FXMLDocumentController().noConnect();
                }
            } 
            catch (ClassNotFoundException ex) 
            {
                System.out.println("Error in parsing request to class msessage!!");
            } 
            catch (IOException ex) 
            {
                System.out.println("server is offline");
                return;
            }
        }

    }
// we stop here
    public boolean handleLogin(Message msg) {
        if (msg.getData()[0].equals("Accept")) 
        {
            ClientXO.setId(Integer.parseInt(msg.getData()[1]));
            new FXMLDocumentController().playTypeWindow();
            return true;
        }
        Validation.alertMessageError("Authentication Failed", "please make Sure you Enter Correct Data !");
        return false;
    }

    public void handleMove(String move) 
    {
        
        Platform.runLater(() -> {
            char T = move.charAt(5);
            ImageView Turn = new ImageView(new Image(getClass().getResourceAsStream(T + ".png")));
            int col = Integer.parseInt(move.charAt(7) + "");
            int row = Integer.parseInt(move.charAt(9) + "");
            String btnId = "";
            if (col == 0 && row == 0) {
                btnId = "1";
            } else if (col == 1 && row == 0) {
                btnId = "2";
            } else if (col == 2 && row == 0) {
                btnId = "3";
            } else if (col == 0 && row == 1) {
                btnId = "4";
            } else if (col == 1 && row == 1) {
                btnId = "5";
            } else if (col == 2 && row == 1) {
                btnId = "6";
            } else if (col == 0 && row == 2) {
                btnId = "7";
            } else if (col == 1 && row == 2) {
                btnId = "8";
            } else if (col == 2 && row == 2) {
                btnId = "9";
            }
            Button btn = (Button) ClientXO.getGlobalStage().getScene().lookup("#btn" + btnId);
            btn.setGraphic(Turn);
        });

    }

    public void multiPlay() 
    {
        //show List xml to choose one of online player to play with
        new FXMLDocumentController().listWindow();
    }

    public void fillPlayerList(String[] players) 
    {
        Platform.runLater(() -> {
            ObservableList<Player> playerList = ArrayToPlayerList(players);
            TableView<Player> playerTable = (TableView<Player>) ClientXO.getGlobalStage().getScene().lookup("#tableScores");
            if (playerTable != null) 
            {
                playerTable.setItems(playerList);
            }
        });
    }

    public ObservableList<Player> ArrayToPlayerList(String[] players) 
    {
        ObservableList<Player> playerList = FXCollections.observableArrayList();
        System.out.println("ARRTOPLAYERLIST");
        for (int i = 0; i < players.length; i++) {
            StringTokenizer st = new StringTokenizer(players[i], "/");
            Player p = new Player();
            p.setIdnum(Integer.parseInt(st.nextToken()));
            p.setNames(st.nextToken());
            p.setPoints(Integer.parseInt(st.nextToken()));
            p.setIsOnline(Integer.valueOf(st.nextToken()));
            playerList.add(p);
            System.out.println(p.getIdnum() + " " + p.getNames() + " " + p.getPoints() + " " + p.isIsOnline());
        }
        return playerList;
    }

    public void retrieveOldGame(String scenario) {
        Platform.runLater(() -> {
            int ind=0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    char T = scenario.charAt(ind++);
                    if(T=='X'||T=='O'){
                        ImageView Turn = new ImageView(new Image(getClass().getResourceAsStream(T + ".png")));
                        int col = j;
                        int row = i;
                        String btnId = "";
                        if (col == 0 && row == 0) {
                            btnId = "1";
                        } else if (col == 1 && row == 0) {
                            btnId = "2";
                        } else if (col == 2 && row == 0) {
                            btnId = "3";
                        } else if (col == 0 && row == 1) {
                            btnId = "4";
                        } else if (col == 1 && row == 1) {
                            btnId = "5";
                        } else if (col == 2 && row == 1) {
                            btnId = "6";
                        } else if (col == 0 && row == 2) {
                            btnId = "7";
                        } else if (col == 1 && row == 2) {
                            btnId = "8";
                        } else if (col == 2 && row == 2) {
                            btnId = "9";
                        }
                        Button btn = (Button) ClientXO.getGlobalStage().getScene().lookup("#btn" + btnId);
                        btn.setGraphic(Turn);
                    }
                }
            }
        });
    }

    ;
    public void handleChatting(String msg[]) {
        Platform.runLater(() -> {

            TextArea chatArea = (TextArea) ClientXO.getGlobalStage().getScene().lookup("#chatArea");
            if (chatArea != null) {
                chatArea.setText(chatArea.getText() + msg[2] + ": " + msg[1]);
                System.out.println(msg[2] + ": " + msg[1]);
            }
        });
    }

    
    public void redirectToLogin() {
        new FXMLDocumentController().logInWindow();
    }

    public boolean handleSignup(Message msg) 
    {
        if (msg.getData()[0].equals("Accept")) {
            redirectToLogin();
            return true;
        } 
        else 
        {
            Validation.alertMessageError("Name", "Please Enter another Name Already Taken");
            return false;
        }
    }
}
