package fi.utu.tech.ringersClockServer;

import fi.utu.tech.ringersClock.entities.SCMD;
import fi.utu.tech.ringersClock.entities.ServerCmd;
import fi.utu.tech.ringersClock.entities.WakeUpGroup;
import fi.utu.tech.weatherInfo.FMIWeatherService;

import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * Every WakeUpGroup in server has this service
 * its contain field store a WakeUpGroup and all Clients' ObjectOutPutStream
 * its methods
 */
public class WakeUpService extends Thread {

	private WakeUpGroup group;
	private ArrayList<ObjectOutputStream> clients;

	private boolean isConfirmAlarm = false;

	public WakeUpService(WakeUpGroup group,ObjectOutputStream leaderOOut) {
		super();
		this.group = group;
		this.clients = new ArrayList<ObjectOutputStream>(){};
		clients.add(leaderOOut);
		this.start();
	}

	@Override
	public void run() {
		//BLOCK THREAD(10s)
		while(true){
			Instant currentTime = Instant.now().atZone(ZoneId.systemDefault()).toInstant();
			Instant groupAlarm = group.getAlarm().toInstant();
			try{
				//If alarm has goes off or its happening
				if(currentTime.compareTo(groupAlarm)>=0 && group.getAlarm().isWeatherAllowed(FMIWeatherService.getWeather())){
					var confirmCmd = new ServerCmd<>(SCMD.CONFIRM_ALARM_CMD);
					//Send confirm command to client
					ClientHandler.sendServerCmdToClient(clients.get(0),confirmCmd);
					if(isConfirmAlarm){
						var alarmCmd = new ServerCmd<>(SCMD.ALARM_USER_CMD);
						var statusCmd = new ServerCmd<>(SCMD.APPEND_TO_STATUS_CMD,"Alarm goes off.");
						//Send alarmCmd to every clients
						for(ObjectOutputStream client : clients){
							ClientHandler.sendServerCmdToClient(client,alarmCmd);
							ClientHandler.sendServerCmdToClient(client,statusCmd);
						}
					}//isConfirmAlarm
				}//isAlarmGoesOff
				sleep(10000);
			}catch(Exception e){
				e.printStackTrace();
			}
		}//while
	}

	//Set status of WakeupService
	public synchronized void setConfirmAlarm(boolean isConfirmAlarm){
		this.isConfirmAlarm = isConfirmAlarm;
	}

	//Add client
	public synchronized boolean addClient(ObjectOutputStream memberOOut){
		if(clients.size() >=1){
			clients.add(memberOOut);
			return true;
		}else{
			return false;
		}
	}

	//Remove Client
	public synchronized boolean removeClient(ObjectOutputStream memberOOut){
		if(clients.contains(memberOOut)){
			clients.remove(memberOOut);
			return true;
		}else{
			System.err.println("Removed client not found!");
			return false;
		}
	}

	//get WakeUpGroup
	public WakeUpGroup getGroup() {
		return group;
	}


}
