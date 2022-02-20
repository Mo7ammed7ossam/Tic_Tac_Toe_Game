/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientxo.signup;

import Network.Message;
import clientxo.ClientXO;
import clientxo.FXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import validpkg.Validation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import player.Player;


/**
 *
 * @author Abdeladl
 */
public class SignUpController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private Button closeBtn, minBtn;
    @FXML
    public Label passerror, repasserror;
    @FXML
    public  TextField name, email;
    @FXML
    public PasswordField password, repassword;

    Player player;

    @FXML
    private void closeButtonAction() {

        // Close Window Button
        Stage closeStage = (Stage) closeBtn.getScene().getWindow();
        closeStage.close();
    }

    @FXML
    private void minButtonAction() {

        // Close Window Button
        Stage minStage = (Stage) minBtn.getScene().getWindow();
        minStage.setIconified(true);
    }
    @FXML
    private void loginAction(ActionEvent event) throws IOException {
        // Login Button
        new FXMLDocumentController().logInWindow();
        
    }
    
    


    @FXML
    // Sign up Button Action rename it
    private void signAction() throws IOException {
        
        new FXMLDocumentController().signUpWindow();

        
        // We Will Handle Sign Up Validation
        if(Validation.isName(name.getText()))
        {
            if(Validation.isEmail(email.getText()))
            {
                if(Validation.isPassword(password.getText()))
                {
                    if(Validation.isEqual(password.getText(), repassword.getText()))
                    {
                        Message msg = new Message("Signup",new String []{name.getText(), password.getText(), email.getText()});
                        ClientXO.client.sendMessage(msg);
                    }
                }
            }
        }
    }
    
    @FXML
private void backAction(ActionEvent event) throws IOException{
    new FXMLDocumentController().mainWindow();
        
}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
