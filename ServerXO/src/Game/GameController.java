/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Database.DBManger;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Gehad
 */
public class GameController extends Thread {

    public static ArrayList<Player> players;
    static DBManger dbManger = null;
    ServerSocket listener;
    Socket s;

    public GameController() {
        try {
            dbManger = new DBManger();
            players = dbManger.loadPlayer();
            listener = new ServerSocket(5050);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Listening");
                s = new Socket();
                s = listener.accept();
                System.out.println("New Client Connected");
                Client client = new Client(s,new ObjectInputStream(s.getInputStream()),new ObjectOutputStream(s.getOutputStream()));
                client.start();
            } catch (IOException ex) {
                    
                    //                Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Server not listen");
                    break;
            }
        }
    }

    public void close() throws IOException{
        s.close();
        listener.close();
    }
    
    public ServerSocket getListener() {
        return listener;
    }

    public void setListener(ServerSocket listener) {
        this.listener = listener;
    }

    public Socket getS() {
        return s;
    }

    public void setS(Socket s) {
        this.s = s;
    }
    
    
}

