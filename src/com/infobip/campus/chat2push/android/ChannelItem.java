package com.infobip.campus.chat2push.android;

public class ChannelItem {
	
	private String name, description;
	private Boolean status;

	public String getName () {
		return name;
	}

	public String getDescription () {
		return description;
	}

	public Boolean getStatus () {
		return status;
	}	
	
	public void setName (String str) {
		name = str;
	}	
	
	public void setDescription (String str) {
		description = str;
	}
	
	public void setStatus (Boolean bool) {
		status = bool;
	}
	
	public void changeStatus () {
		status = !status;
	}
	
}
