
package clientxo.playerForm;

import Network.Client;
import Network.Message;
import clientxo.ClientXO;
import static clientxo.ClientXO.client;
import clientxo.FXMLDocumentController;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import player.Player;
/**
 *
 * @author Abdeladl
 */
public class playTypeController implements Initializable {

    @FXML
    private Label label;
    @FXML 
    Player player;

    @FXML
    private Button closeBtn, loginBtn, minBtn;
    @FXML
    private void closeButtonAction() {

        // Close Window Button
        Stage closeStage = (Stage) closeBtn.getScene().getWindow();
        closeStage.close();
         Message msg = new Message("CloseConn",new String []{});
        ClientXO.client.sendMessage(msg);
        ClientXO.client.closeConn();
    }

    @FXML
    private void minButtonAction() {

        // minimize Window Button
        Stage minStage = (Stage) minBtn.getScene().getWindow();
        minStage.setIconified(true);
    }

    @FXML
    private void SingleButtonAction(ActionEvent event) throws IOException {
        new FXMLDocumentController().singlePlayWindow();
    }

    @FXML
    private void MultiButtonAction(ActionEvent event) throws IOException {

        new FXMLDocumentController().listWindow();


    }

    @FXML
    private void LogOutAction(ActionEvent event) throws IOException {
          Message msg = new Message("CloseConn",new String []{});
        ClientXO.client.sendMessage(msg);
        ClientXO.client.closeConn();
       try {
            // TODO
            client = new Client(new Socket("localhost", 5050));
            client.start();
        }
       catch (IOException ex) 
        {
            System.out.println("Client => No Connect ");

        }
       finally
       {
            System.out.println("Client Connect to Server");

       }
             
        ClientXO.client.redirectToLogin();
    }
        
       


    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
    }

}
