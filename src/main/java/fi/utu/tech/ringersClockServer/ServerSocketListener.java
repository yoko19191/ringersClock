package fi.utu.tech.ringersClockServer;

import fi.utu.tech.ringersClock.entities.ServerCmd;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ServerSocketListener extends Thread {

	public static ServerSocketListener instance;
	private ServerSocket serverSocket;
	private Container container;
	private String host;
	private int port;


	public ServerSocketListener(String host, int port) {
		this.host = host;
		this.port = port;
		this.container = new Container();
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
				//new ClientHandler(clientSocket).start();
				var newClientHandler = new ClientHandler(clientSocket);
				Container.clientQueue.add(newClientHandler);
				newClientHandler.start();
			}catch(IOException e){
				e.printStackTrace();
			}//try-catch
		}//while
	}


}
