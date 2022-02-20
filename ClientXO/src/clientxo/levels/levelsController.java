
package clientxo.levels;

import Network.Message;
import clientxo.FXMLDocumentController;
import clientxo.ClientXO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
/**
 *
 * @author Ali
 */
public class levelsController implements Initializable {
        
    @FXML
    private Label label;
    
    @FXML 
    private Button easy;
    @FXML
    private Button med;
    @FXML
    private Button hard;
    @FXML
    private Button closeBtn, minBtn;
    
    
    private double x = 0; 
    private double y = 0;

    
@FXML
private void closeButtonAction(){
        
        // Close Window Button
        Stage closeStage = (Stage) closeBtn.getScene().getWindow();
        closeStage.close();    
}

@FXML
private void minButtonAction(){
        
        // min Window Button
        Stage minStage = (Stage) minBtn.getScene().getWindow();
           minStage.setIconified(true);
}

@FXML
private void easyAction(ActionEvent event) throws IOException
{
        ClientXO.client.sendMessage(new Message("EasySingle",new String[]{Integer.toString(ClientXO.getId())})); 
}

@FXML
private void medAction(ActionEvent event) throws IOException
{
     ClientXO.client.sendMessage(new Message("MediumSingle",new String[]{Integer.toString(ClientXO.getId())})); 
}

@FXML
private void hardAction(ActionEvent event) throws IOException
{
    ClientXO.client.sendMessage(new Message("HardSingle",new String[]{Integer.toString(ClientXO.getId())})); 
}


@FXML
private void backAction(ActionEvent event) throws IOException
{
    new FXMLDocumentController().playTypeWindow();
}


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
