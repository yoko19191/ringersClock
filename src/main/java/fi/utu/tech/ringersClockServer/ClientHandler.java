package fi.utu.tech.ringersClockServer;

import fi.utu.tech.ringersClock.entities.ServerCmd;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Agent of all WakeUpService
 */
public class ClientHandler extends Thread{
    public static ClientHandler instance;
    private Socket cs;

    private ArrayList<WakeUpService> services;

    public ClientHandler(Socket cs) {
        this.cs = cs;
        this.services = new ArrayList<WakeUpService>(1000);
    }

    @Override
    public void run(){

    }


    //Sender
    public static void sendServerCmdToClient(ObjectOutputStream client, ServerCmd cmd) {
        try {
            client.writeObject(cmd);
        }catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }


    private class MessageHandler{

    }

}
