package com.infobip.campus.chat2push.android.models;

public class ChannelModel {

	private String name, description;
	private Boolean status;
	private Boolean isPublic;

	public ChannelModel() {

	}

	public ChannelModel(String channelName, String channelDescription,
			boolean isUserSubscribedToChannel) {
		name = channelName;
		description = channelDescription;
		status = isUserSubscribedToChannel;
	}
	
	public ChannelModel(String channelName, String channelDescription,
			boolean isUserSubscribedToChannel, boolean isPublic) {
		name = channelName;
		description = channelDescription;
		status = isUserSubscribedToChannel;
		this.isPublic = isPublic;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Boolean getStatus() {
		return status;
	}
	
	public void setName(String str) {
		name = str;
	}

	public void setDescription(String str) {
		description = str;
	}

	public void setStatus(Boolean bool) {
		status = bool;
	}

	public void changeStatus() {
		status = !status;
	}
	
	public Boolean getIsPublic() {
		return isPublic;
	}
	
	public void setIsPublic(Boolean bool) {
		isPublic = bool;
	}

	@Override
	public String toString() {
		return "ChannelModel [name=" + name + ", description=" + description
				+ ", status=" + status + ", isPublic=" + isPublic + "]";
	}
}
