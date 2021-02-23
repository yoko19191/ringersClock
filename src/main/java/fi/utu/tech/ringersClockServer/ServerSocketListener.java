package fi.utu.tech.ringersClockServer;

import fi.utu.tech.ringersClock.entities.ServerCmd;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerSocketListener extends Thread {

	public static ServerSocketListener instance;
	private ServerSocket serverSocket;

	private String host;
	private int port;

	private ArrayList<ClientHandler> clientHandlers;

	public ServerSocketListener(String host, int port) {
		this.host = host;
		this.port = port;
		this.clientHandlers = new ArrayList<ClientHandler>();
		ServerSocketListener.instance = this;
	}

	@Override
	public void run() {
		try{
			this.serverSocket = new ServerSocket(port);
			System.out.println("Server Running at port:"+port);
		}catch(IOException e){
			e.printStackTrace();
		}//try-catch
		while(!serverSocket.isClosed()){
			try{
				Socket clientSocket = serverSocket.accept();
				clientHandlers.add(new ClientHandler(clientSocket));
			}catch(IOException e){
				e.printStackTrace();
			}//try-catch
		}//while
	}

	//sender


}
