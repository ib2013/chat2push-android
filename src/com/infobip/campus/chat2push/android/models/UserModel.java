package com.infobip.campus.chat2push.android.models;

//model se koristi za dodavanje korisnika u channel
public class UserModel {
	
	private String username;
	//Status kaze je li dani user trenutno oznacen
	private boolean status;
	
	public UserModel () {
		super();
		username = "";
		status = false;
	}
	
	public UserModel (String username, boolean status) {
		super();
		this.username = username;
		this.status = status;
	}
	
	public String getUsername () {
		return username;
	}
	
	public boolean getStatus () {
		return status;
	}

	public void setUsername (String username) {
		this.username = username;
	}
	
	public void setStatus (boolean status) {
		this.status = status;
	}
	
	public void changeStatus () {
		status = !status;
	}
}