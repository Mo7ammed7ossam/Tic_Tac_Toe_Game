/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverxo;

import Database.DBManger;
import Game.*;
import Game.Player;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author mohamed Alwakiel
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    Button btn;
    int Count = 2;
    Image onOff;
    String state;

    @FXML
    private Button closeBtn, minBtn;

    @FXML
    private TableColumn<Player, Integer> tblId;
    @FXML
    private TableColumn<Player, String> tblNames;
    @FXML
    private TableColumn<Player, Integer> tblScore;
    @FXML
    private TableView<Player> tableScores;
    @FXML
    private TableColumn<Player, Text> stateplay;

    Image imgPlace;
    DBManger db = new DBManger();
    GameController gc;

    static ObservableList<Player> usersList = FXCollections.observableArrayList();
    @FXML
    private Label label;

    public FXMLDocumentController() {
        this.tableScores = new TableView<>();
        this.tblId = new TableColumn();
        this.tblNames = new TableColumn();
        this.tblScore = new TableColumn();
        this.stateplay = new TableColumn();
    }

    // function return an image to change the button
    @FXML
    public Image Count() throws IOException {

        if ((Count % 2) == 0) {
            onOff = new Image(getClass().getResourceAsStream("on.png"));
            state = "on";

            // start server
            gc = new GameController(); // connect with db - fetch players - create serverSocket

            gc.start(); // start listening , if any player start a connection

            // fetch all players and store it in userList
            loadUserList();

            // Set all players in table
            tableScores.setItems(usersList);
        } else // action off and close server
        {
            onOff = new Image(getClass().getResourceAsStream("off.png"));
            state = "off";

            // stop server --> close server socket and end socket
            gc.close();

            // remove players form table
            tableScores.setItems(null);
        }
        return onOff;
    }

    @FXML
    private void closeButtonAction() throws IOException {

        // Close Window Button
        Stage closeStage = (Stage) closeBtn.getScene().getWindow();
        // stop server
        gc.close();
        closeStage.close();
    }

    @FXML
    private void minButtonAction() {

        // minimize Window Button
        Stage minStage = (Stage) minBtn.getScene().getWindow();
        minStage.setIconified(true);
    }

    @FXML
    public void ServerAction(ActionEvent e) throws IOException {
        btn = (Button) e.getSource(); // select the button to make operation on it

        // function count() to make soome check before change the image and open server
        btn.setGraphic(new ImageView(Count()));

        Count++; // odd for (off) -- even for (on)
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("off.png"))));

        // Initializing the columns
        tblId.setCellValueFactory(new PropertyValueFactory<>("idnum"));
        tblNames.setCellValueFactory(new PropertyValueFactory<>("names"));
        tblScore.setCellValueFactory(new PropertyValueFactory<>("points"));
        stateplay.setCellValueFactory(new PropertyValueFactory<>("online"));

        // add Event Listener To table List
        tableScores.setOnMouseClicked((MouseEvent click) -> {
            if (click.getClickCount() == 2) { // return mouse clicked
                tableScores.getSelectionModel().getSelectedItem();
                System.out.println("You Clicked Player : "
                        + ((Player) tableScores.getSelectionModel().getSelectedItem()).getNames());
            }
        });

    }

    // get users from DB
    public static void loadUserList() {
        ArrayList<Player> playerList = GameController.players;
        ArrayList<Player> playerList = GameController.players;
        int listSize = playerList.size();

        usersList.clear();

        for (int i = 0; i < listSize; i++) {
            Player player = playerList.get(i);
            usersList.add(player);
            System.out.println(
                    "Id : " + player.getIdnum() + "  Name : " + player.getNames() + "  Points : " + player.getPoints());
        }

        System.out.println("UserList Size: " + usersList.size());
    }

    // function to update the table that appeare on the server view
    public static void updatePlayerList() {
        Platform.runLater(() -> {
            // get table element
            TableView<Player> playerTable = (TableView<Player>) ServerXO.getGlobalStage().getScene()
                    .lookup("#tableScores");
            if (playerTable != null) {
                System.out.println("FILL THE TABLE");

                loadUserList(); // fetch players from DB

                playerTable.setItems(usersList);
            }
        });
    }

    // function to ordering players
    public static void sortPlayerList() {
        Collections.sort(GameController.players, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p2.getPoints() - p1.getPoints();
            }
        });
    }
}
