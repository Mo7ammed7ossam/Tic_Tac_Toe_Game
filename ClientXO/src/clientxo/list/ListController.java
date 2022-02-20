
package clientxo.list;

import Network.Message;
import clientxo.ClientXO;
import clientxo.FXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import player.Player;

/**
 *
 * @author Ali
 */
public class ListController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private Button closeBtn;

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

    public int idOfOpponent;

    public ListController() {
        this.tableScores = new TableView<>();
        this.tblId = new TableColumn();
        this.tblNames = new TableColumn();
        this.tblScore = new TableColumn();
        this.stateplay = new TableColumn();

    }

    @FXML
    private void closeButtonAction() {

        // Close Window Button
        Stage closeStage = (Stage) closeBtn.getScene().getWindow();
        closeStage.close();
    }

    @FXML
    private void backAction(ActionEvent event) throws IOException {
        new FXMLDocumentController().playTypeWindow();
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        tblId.setCellValueFactory(new PropertyValueFactory<>("idnum"));
        tblNames.setCellValueFactory(new PropertyValueFactory<>("names"));
        tblScore.setCellValueFactory(new PropertyValueFactory<>("points"));
        stateplay.setCellValueFactory(new PropertyValueFactory<>("online"));
        tableScores.setOnMouseClicked((MouseEvent click) -> {
            if (click.getClickCount() == 2) {
                tableScores.getSelectionModel().getSelectedItem();
                idOfOpponent = ((Player) tableScores.getSelectionModel().getSelectedItem()).getIdnum();
                
                if (ClientXO.getId() != idOfOpponent) {
                    Message msg = new Message("multiPlay", new String[]{Integer.toString(ClientXO.getId()), Integer.toString(idOfOpponent)});
                    ClientXO.client.sendMessage(msg);

                    String user = ((Player) tableScores.getSelectionModel().getSelectedItem()).getNames();
                    new FXMLDocumentController().requestSent(user);
                }
            }
        });

        //Lets Fill The List
        Message msg = new Message("listRequest", new String[]{Integer.toString(ClientXO.getId())});
        ClientXO.client.sendMessage(msg);
    }

    public int getIdOfOpponent() {
        return idOfOpponent;
    }

    public void setIdOfOpponent(int idOfOpponent) {
        this.idOfOpponent = idOfOpponent;
    }

    public TableView<Player> getTableScores() {
        return tableScores;
    }

    public void setTableScores(TableView<Player> tableScores) {
        this.tableScores = tableScores;
    }
    
    

}
