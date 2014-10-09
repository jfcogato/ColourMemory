package com.jfcogato.colourmemory.models;

import java.io.Serializable;

public class UsersObject implements Serializable {

	//I made it serializable for future features, if we need to pass the userObject between activities
	private static final long serialVersionUID = -7060210544600464481L;

	private String name = "";
	private String points = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

}
