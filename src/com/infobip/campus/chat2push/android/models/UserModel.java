package com.infobip.campus.chat2push.android.models;

//model se koristi za dodavanje korisnika u channel
public class UserModel {
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserModel other = (UserModel) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

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