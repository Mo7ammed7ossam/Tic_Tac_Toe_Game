package player;

import javafx.scene.text.Text;
/**
 *
 * @author Abdeladl
 */
public class Player {
    int idnum;
    String names;
    int points;
    int isOnline;
    Text online;


     public Player() {
        this.idnum = -1;
        this.isOnline = 0;
    }

    public Player(int ID, String name, int points) {
        this.idnum = ID;
        this.names = name;
        this.points = points;
        this.isOnline = 0;
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

    public Text getOnline() {
        if(isOnline == 1){
              online = new Text("Online");
              online.setStyle("-fx-fill: green; -fx-font-weight: bold;");
        } else if(isOnline == 0){
              online = new Text("Offline");
              online.setStyle("-fx-fill: red; -fx-font-weight: bold;"); 
        }else if(isOnline == 2){
              online = new Text("Busy");
              online.setStyle("-fx-fill: orange; -fx-font-weight: bold;"); 
        }
        return online;
    }

    public void setOnline(Text online) {
        this.online = online;
    }

}
