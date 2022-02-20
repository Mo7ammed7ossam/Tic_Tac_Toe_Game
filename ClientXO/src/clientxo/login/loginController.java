
package clientxo.login;

import Network.*;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import validpkg.Validation;

/**
 *
 * @author Abdeladl
 */
public class loginController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private Button closeBtn, minBtn;

    @FXML
    public TextField name;
    @FXML
    public PasswordField password;
    public static Client client;
     FXMLDocumentController controller;

    @FXML
    private void closeButtonAction() {

        // Close Window Button
        Stage closeStage = (Stage) closeBtn.getScene().getWindow();
        closeStage.close();
    }

    @FXML
    private void minButtonAction() {

        // mini Window Button
        Stage minStage = (Stage) minBtn.getScene().getWindow();
        minStage.setIconified(true);
    }

    @FXML
    private void SignUpButtonAction(ActionEvent event) throws IOException {
        // Sign up Button
        new FXMLDocumentController().signUpWindow();
    }

    @FXML
    private void LoginButton(ActionEvent event) throws IOException {
        // Login Button
        if(Validation.isName(name.getText()))
        {
            if(Validation.isPassword(password.getText()))
            {
                Message msg = new Message("Login",new String []{name.getText(),password.getText()});
                ClientXO.client.sendMessage(msg);
            }
        }
        

    }

     @FXML
    private void backAction(ActionEvent event) throws IOException{
        new FXMLDocumentController().mainWindow();
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
