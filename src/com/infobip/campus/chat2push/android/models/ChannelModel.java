package com.infobip.campus.chat2push.android.models;

public class ChannelModel {
	
	private String name, description;
	private Boolean status;

	public ChannelModel(String channelName, String channelDescription,
			boolean isUserSubscribedToChannel) {
		name=channelName;
		description=channelDescription;
		status=isUserSubscribedToChannel;
		// TODO Auto-generated constructor stub
	}

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
