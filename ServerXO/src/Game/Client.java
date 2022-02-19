/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Network.Message;
import serverxo.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.*;

/**
 *
 *  @author mohamed Alwakiel
 */
class Client extends Thread {

    Socket socket;
    ObjectInputStream input;
    ObjectOutputStream output;

    public Client(Socket socket, ObjectInputStream input, ObjectOutputStream output) {
        this.socket = socket;
        this.input = input;
        this.output = output;
    }

    public void run() {
        int isLogin = -1;
        boolean isSignup = false;
        while (isLogin == -1) {
            try {

                Message msg = (Message) input.readObject();
                System.out.println(msg.getType());
                if ("Login".equals(msg.getType())) {
                    isLogin = isLoggedin(msg, isLogin);
                } else if ("Signup".equals(msg.getType())) {
                    isSignup = isSignedup(msg, isSignup);
                }
            } catch (IOException ex) {
                System.out.println("client is offline");
                return;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean isSignedup(Message msg, boolean isSignup) {
        try {
            if (msg == null) 
            {
                System.out.println("client is offline");
                return true;
            }
            
            System.out.println(msg.getType());
            System.out.println(msg.getData()[0] + " " + msg.getData()[1] + " " + msg.getData()[2]);
            
            if ("Signup".equals(msg.getType())) 
            {
                isSignup = GameController.dbManger.signUp(msg.getData()[0], msg.getData()[1], msg.getData()[2]);
            }
            
            
            if (isSignup != false) 
            {
                ArrayList<Player> players = GameController.players;
                
                players.add(new Player(GameController.players.size()+1, msg.getData()[0],0)); // to add last player sign up
                
                FXMLDocumentController.updatePlayerList();
                
                Player.broadCastPlayerList();
                
                msg = new Message("Signup", new String[]{"Accept", Integer.toString(1)});
                
                output.writeObject(msg);
            } 
            else 
            {
                msg = new Message("Signup", new String[]{"Wrong", Integer.toString(1)});
                
                output.writeObject(msg);
            }
            
            System.out.println(msg.getType());
        } 
        catch (IOException ex) 
        {
            System.out.println("client is offline");

        }
        return isSignup;
    }

    // check if loging --> then go to DB and make validation
    public int isLoggedin(Message msg, int isLogin) {

        try {
            if (msg == null) {      // 
                System.out.println("client is offline");
                this.input.close();
                this.output.close();
                this.socket.close();
                return 0;
            }
            
            System.out.println(msg.getType());  // print login
            
            System.out.println(msg.getData()[0] + " " + msg.getData()[1]); // print user and pass
            
            if ("Login".equals(msg.getType())) // if login action
            {
                isLogin = GameController.dbManger.login(msg.getData()[0], msg.getData()[1]); // return id or -1
            }
            
            System.out.println("Login Result:" + isLogin); // print id or -1
            
            if (isLogin != -1) {    // player exist 
                makePlayerOnline(this, isLogin);    // change status to online
            } 
            else 
            {
                output.writeObject(new Message("Login", new String[]{"Wrong"})); // pass message to client
            }
            
            System.out.println(msg.getType());  // print log in
            
        } catch (IOException ex) {
            System.out.println("client is offline");
            return 0;
        }
        return isLogin;
    }

   // handle to player to make it online and set connections to player object
   public void makePlayerOnline(Client client, int id) {
        
    int playersLength = GameController.players.size(); // get number of all players
    
    try {
        System.out.println("Sending Accept Message"); // print accept message

        // send accept message to the client
        Message msg = new Message("Login", new String[]{"Accept", Integer.toString(id)});
        output.writeObject(msg);
        
    } catch (IOException ex) {
        System.out.println("can not send message accept to the client ! ");
        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    for (int i = 0; i < playersLength; i++) 
    {
        Player player = GameController.players.get(i);
        
        if (player.idnum == id) // establish connections to that player
        {
            player.socket = client.socket;
            player.output = client.output;
            player.input = client.input;
            
            //making player online
            player.isOnline = 1;

            // end     
            player.startThread();
            
            try {
                // send to client hello message 
                player.output.writeObject(new Message("Hello", new String[]{}));
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    FXMLDocumentController.updatePlayerList();
    Player.broadCastPlayerList();
}

}