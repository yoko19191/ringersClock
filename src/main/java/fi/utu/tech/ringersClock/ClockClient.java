package fi.utu.tech.ringersClock;

/*
 * A class for handling network related stuff
 */

import fi.utu.tech.ringersClock.entities.ClientCmd;
import fi.utu.tech.ringersClock.entities.ServerCmd;
import fi.utu.tech.ringersClock.entities.WakeUpGroup;

import java.io.*;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;

public class ClockClient extends Thread {

	private static ClockClient instance = null;
	private ServerHandler sh;
	private String host;
	private int port;
	private Gui_IO gio;

	private Thread thread;

	private Socket server;
	private InputStream iS;
	private OutputStream oS;
	private ObjectOutputStream oOut;
	private ObjectInputStream oIn;

	public ClockClient(String host, int port, Gui_IO gio) throws IOException {
		this.host = host;
		this.port = port;
		this.gio = gio;
		this.sh = new ServerHandler();
		this.server = new Socket(host,port);
		this.thread = new Thread(this);
		this.thread.start();

		if(ClockClient.instance != null){close();}
		ClockClient.instance = this;
	}

	@Override
	public void run() {
		System.out.println("Host name: " + host + " Port: " + port + " Gui_IO:" + gio.toString());
		try{
			iS = server.getInputStream();
			oS = server.getOutputStream();
			oOut = new ObjectOutputStream(oS);
			oIn = new ObjectInputStream(iS);

			while(server.isConnected()){
				sh.handle(oIn.readObject());
			}//while

		}catch(IOException | ClassNotFoundException e){
			System.err.println("Disconnected from server.");
		}//try-catch
	}//run

	public static void close(){
		if(instance != null){
			try{
				instance.oIn.close();
				instance.oOut.close();
				instance.server.close();
				System.out.println("Server connection closed");
			}catch(IOException e){
				e.printStackTrace();
			}//try-catch
		}//if
	}

	//sender
	public static void send(ClientCmd<?> cmd){
		//ClockClient.sendSerializable(cmd);
		if(instance != null){
			try{
				instance.oOut.writeObject(cmd);
				instance.oOut.flush();
			}catch(IOException e){
				e.printStackTrace();
			}//try-catch
		}else{
			System.err.println("Client instance not found, send data to server failed.");
		}
	}//sender



	//process command from server
	private class ServerHandler{

		/*---------------------------*/
		public void handle(Object obj){
			try{
				if(obj instanceof ServerCmd){
					var cmd = (ServerCmd) obj;
					var command = cmd.getCommand();
					var payload = cmd.getPayload();
					System.out.println("Command from server:"+command.toString()+" : "+(payload!=null?payload.toString():"NULL"));
					switch(command)
					{
						case UPDATE_EXISTING_GROUP_CMD:
							if(payload instanceof ArrayList){
								handleUpdateExistingGroup((ArrayList<WakeUpGroup>) payload);
							}
							break;
						case APPEND_TO_STATUS_CMD:
							if(payload instanceof String){
								handleAppendToStatus((String) payload);
							}
							break;
						case CONFIRM_ALARM_CMD:
							if(payload instanceof WakeUpGroup){
								handleConfirmAlarm((WakeUpGroup) payload);
							}
							break;
						case ALARM_USER_CMD:
							handleAlarmUser();
							break;
						case ALARM_CANCELED_CMD:
							handleAlarmCancelled();
							break;
						case ALARM_SETUP_CMD:
							if(payload instanceof Instant){
								handleAlarmSetUp((Instant) payload);
							}
							break;
						default:
							System.err.println("Unknown Server Command Type.");
					}//switch-case
				}else{
					System.err.println("Received non-ServerCmd Object.(This should happen)");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}//handle

		/*-------handle-methods--------*/
		private void handleUpdateExistingGroup(ArrayList<WakeUpGroup> groupList){
			gio.fillGroups(groupList);
		}

		private void handleAppendToStatus(String text){
			gio.appendToStatus(text);
		}

		private void handleConfirmAlarm(WakeUpGroup group){
			gio.clearAlarmTime();
			gio.confirmAlarm(group);
		}

		private void handleAlarmUser(){
			gio.alarm();
		}

		private void handleAlarmCancelled(){
			gio.clearAlarmTime();
		}

		private void handleAlarmSetUp(Instant time){
			gio.setAlarmTime(time);
		}


	}//private class ServerHandler

}
