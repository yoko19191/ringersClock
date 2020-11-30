package fi.utu.tech.ringersClock.entities;

import java.io.Serializable;

/*
 * Entity class presenting a WakeUpGroup. The class is not complete.
 * You need to add some variables.
 */

public class WakeUpGroup implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private Integer ID;

	public WakeUpGroup(Integer id, String name) {
		super();
		this.ID = id;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public Integer getID() {
		return this.ID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setID(Integer ID) {
		this.ID = ID;
	}

	@Override
	public String toString() {
		return this.getName();
	}

}
