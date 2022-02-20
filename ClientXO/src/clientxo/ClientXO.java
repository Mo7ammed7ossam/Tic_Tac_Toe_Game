
package clientxo;

import Network.Client;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent; 
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Ali_Tarek
 */
public class ClientXO extends Application {

    // to drag and drop stage in any place in screen
    private double xOffset = 0;
    private double yOffset = 0;
    private static Stage globalStage;
    public static Client client;
    public static int id;

    public static int getId() 
    {
        return id;
    }

    public static void setId(int id) 
    {
        ClientXO.id = id;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);

        stage.initStyle(StageStyle.UNDECORATED);

        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        globalStage = stage;
        stage.setScene(scene);
        stage.show();
        try 
        {
            client = new Client(new Socket("localhost", 5050));
            client.start();
        } 
        catch (IOException ex) 
        {
            System.out.println("Client => No Connect Check Server First");
        }
        finally
        {
            System.out.println("Client Connect to Server Successfully");
        }

    }

    public static Stage getGlobalStage() 
    {
        return globalStage;
    }

    public static void setGlobalStage(Stage globalStage) 
    {
        ClientXO.globalStage = globalStage;
    }

    public static void main(String[] args) 
    {
        launch(args);
    }

}
