package fi.utu.tech.ringersClock.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/*
 * Entity class presenting a WakeUpGroup. The class is not complete.
 * You need to add some variables.
 */

public class WakeUpGroup implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	//private Integer ID;
	private Alarm alarm;


	public WakeUpGroup(String name,int hour, int minute,boolean isAllowRain, boolean isAllowTempUnderZero) {
		super();
		this.name = name;
		this.alarm = new Alarm(hour,minute,isAllowRain,isAllowTempUnderZero);
	}

	public String getName() {
		return this.name;
	}

	public Alarm getAlarm(){return this.alarm;}

	@Override
	public String toString() {
		return this.getName()+"\n"+this.alarm.toString();
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof WakeUpGroup){
			var comp = (WakeUpGroup) o;
			if(this.toString()==comp.toString()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

}
