
package clientxo;

import Network.Message;
import clientxo.playerForm.playTypeController;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import validpkg.Validation;

/**
 *
 * @author Ali
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private Button closeBtn, loginBtn, minBtn;

    private double x = 0;
    private double y = 0;
    public  playTypeController ptController;
    //public static String currentFXML;
    @FXML
    // action of closing window
    private void closeButtonAction() 
    {
        Message msg = new Message("CloseConn",new String []{});
        //send message to server to remove this client from list
        ClientXO.client.sendMessage(msg);
        //call function of close conncetion
        ClientXO.client.closeConn();
        Stage closeStage = (Stage) closeBtn.getScene().getWindow();
        closeStage.close();
    }

    @FXML
    private void minButtonAction() 
    {
        // to hide stage
        Stage minStage = (Stage) minBtn.getScene().getWindow();
        minStage.setIconified(true);
    }
    
    //call Sign Up function to redirect to register Form
    @FXML
    private void SignUpButtonAction(ActionEvent event) throws IOException 
    {
        signUpWindow();
    }
    // Login redirect action
    @FXML
    private void loginButtonAction(ActionEvent event) throws IOException 
    {
        logInWindow();
    }
    public void LoadXML(String xml) throws IOException
    {
        Parent pageView = FXMLLoader.load(getClass().getResource(xml));
        Scene pageScene = new Scene(pageView);
        Stage stageWindow = ClientXO.getGlobalStage();
        pageView.setOnMousePressed((MouseEvent e) -> 
        {
            x = e.getSceneX();
            y = e.getSceneY();
        });
                
        pageView.setOnMouseDragged((MouseEvent e) -> 
        {
            stageWindow.setX(e.getScreenX() - x);
            stageWindow.setY(e.getScreenY() - y);
        });
        stageWindow.setScene(pageScene);
        stageWindow.show();
    }
    public void signUpWindow() {
        Platform.runLater(() -> {
            try 
            {
                LoadXML("./signup/signup.fxml");
            } 
            catch (IOException ex) 
            {
                System.out.println("Sorry Couldn't find SignUp Page");
            }
       });
    }

    public void logInWindow() {
        Platform.runLater(() -> {
            try {
                LoadXML("./login/login.fxml");
            } 
            catch (IOException ex) 
            {
                System.out.println("Couldn't find Login Form XML");
            }
        });
    }

    // Levels Scene 
    public void singlePlayWindow() {
        Platform.runLater(() -> {
            try {
                LoadXML("./levels/levels.fxml");
            } 
            catch (IOException ex) 
            {
                System.out.println("Error with Loading Levels XML Form");
            }
        });
    }

    public void listWindow() {
        Platform.runLater(() -> 
        {
            try {
                LoadXML("./list/list.fxml");
            } 
            catch (IOException ex) 
            {
                System.out.println("Can't Laod List of Users FXML");
            }
        });
    }

    // Levels Scene 
    public void gameWindow() {
        Platform.runLater(() -> {
            try {
                LoadXML("./game/GameCore.fxml");
            } 
            catch (IOException ex) {
                System.out.println("Can't Load Game XO Form");
            }
        });
    }

    // Type Scene 
    public void playTypeWindow() {
        Platform.runLater(() -> {
            try {
                LoadXML("./playerForm/playType.fxml");
            } 
            catch (IOException ex) 
            {
                System.out.println("Can't load Welcome to play Single or Multi FXmL");
            }
        });
    }
       
        public void multiGameWindow() {
        Platform.runLater(() -> {
            try {
                LoadXML("./multigame/MultiGame.fxml");
            } 
            catch (IOException ex) 
            {
                System.out.println("I can't Load Window Multi Game Mode");
                
            }
        });
    }


    public void mainWindow() {
        try {
            LoadXML("FXMLDocument.fxml");
        }
        catch (IOException ex)
        {
            System.out.println("Can't Load Main Welcome Window");        }
        }
    public void winnerLosserAlert(String title,String text,String img)
    {
        Dialog alert = new Dialog();
        alert.initStyle(StageStyle.UNDECORATED);
        ButtonType AgainButton= new ButtonType("Again",ButtonData.OK_DONE);                        
        alert.getDialogPane().getButtonTypes().add(AgainButton);
        alert.setTitle(title);
        alert.setGraphic(new ImageView(FXMLDocumentController.this.getClass().getResource(img).toString()));
        alert.setContentText(text);
        alert.getDialogPane().setStyle("-fx-background-color:#85A7E4;");
        alert.setHeaderText(null);
        alert.showAndWait();            
    }
    public void winAlert(String state){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {                     
                        if (null != state) 
                        {
                            switch (state) {
                                case "WIN":
                                    winnerLosserAlert("Congratulation 🥳🥳🥳","Nice Game You are The Winner 🥰🥰♫","win.png");
                                    break;
                                case "LOSE":
                                    winnerLosserAlert("Game Over","All Luck with You Next Time","lose.png");
                                    break;
                                case "DRAW":
                                    winnerLosserAlert("Game Over","Draw No Winner","draw.png");
                                    break;
                                default:
                                    break;
                            }
                            new FXMLDocumentController().playTypeWindow();
                        }
                    }
                }); }
    
    
    public void requestSent(String user){
       Platform.runLater(() -> {
                Validation.alertMessageError("Play Request", "you request Sent to "+user+"\n Waiting for response");

       });
    }
    
    public void noConnect(){
       Platform.runLater(() -> {
                    Validation.alertMessageError("Play Request", "Connection Cut Off");
       });
    }
    
    
    public void playAccept(String idPl,String idP2){
       Platform.runLater(() -> {
            Dialog alert = new Dialog();
                    alert.initStyle(StageStyle.UNDECORATED);
                    
                    ButtonType AcceptButton= new ButtonType("Accept",ButtonData.OK_DONE);
                    ButtonType CancelButton= new ButtonType("Cancel",ButtonData.OK_DONE);
                    alert.getDialogPane().getButtonTypes().addAll(AcceptButton,CancelButton);
                    alert.setTitle("Play request !!");
                    alert.setContentText(idPl+ "  Sent You Request to play" );
                    alert.getDialogPane().setStyle("-fx-background-color:#85A7E4;");
                    alert.setHeaderText(null);
                    
             Optional<ButtonType> result = alert.showAndWait();
              if(result.get() == AcceptButton){
                    System.out.println("player id : " + idPl + "sent request to : " + ClientXO.getId());
                    Message message = (new Message("playRequest", new String[]{"accept",idPl,Integer.toString(ClientXO.getId())}));
                    ClientXO.client.sendMessage(message);                    
               } else if(result.get() == CancelButton){
                   Message message = (new Message("playRequest", new String[]{"reject",idPl,Integer.toString(ClientXO.getId())}));
                    ClientXO.client.sendMessage(message);
                    alert.close();
               }
       });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
