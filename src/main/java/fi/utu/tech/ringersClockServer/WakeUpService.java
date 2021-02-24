package fi.utu.tech.ringersClockServer;

import fi.utu.tech.ringersClock.entities.SCMD;
import fi.utu.tech.ringersClock.entities.ServerCmd;
import fi.utu.tech.ringersClock.entities.WakeUpGroup;
import fi.utu.tech.weatherInfo.FMIWeatherService;

import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Every WakeUpGroup in server has this service
 * its contain field store a WakeUpGroup and all Clients' ObjectOutPutStream
 * its methods
 */
public class WakeUpService extends Thread {

	private final WakeUpGroup group;

	private BlockingQueue<ObjectOutputStream> memberQueue;

	private boolean isConfirmAlarm = false;

	public WakeUpService(WakeUpGroup group,ObjectOutputStream leaderOOut) {
		super();
		this.group = group;
		//Group leader's handler --> index 0
		this.memberQueue = new ArrayBlockingQueue<ObjectOutputStream>(100);
		this.memberQueue.add(leaderOOut);
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
					var confirmCmd = new ServerCmd<>(SCMD.CONFIRM_ALARM_CMD,this.group);
					var confirmMessage = new ServerCmd<>(SCMD.APPEND_TO_STATUS_CMD,"Confirm alarm all?");
					//Send confirm command to leader client
					ObjectOutputStream leader = memberQueue.take();
					ClientHandler.send(confirmCmd,leader);
					ClientHandler.send(confirmMessage, leader);
					//clientHandlers.get(0).send(confirmCmd);
					if(isConfirmAlarm){
						var alarmCmd = new ServerCmd<>(SCMD.ALARM_USER_CMD);
						var statusCmd = new ServerCmd<>(SCMD.APPEND_TO_STATUS_CMD,"Alarm goes off.");
						//Send alarmCmd to every clients
						for(ObjectOutputStream oOut:memberQueue){
							ClientHandler.send(alarmCmd,oOut);
							ClientHandler.send(statusCmd,oOut);
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

	//get WakeUpGroup
	public WakeUpGroup getGroup() {
		return group;
	}

	public void addObjectOutputStream(ObjectOutputStream user){
		this.memberQueue.add(user);
	}

	/**
	 * todo: NullPointerException
	 */
	public void removeObjectOutputStream(ObjectOutputStream removeOut){
		this.memberQueue.remove(removeOut);

	}


}
