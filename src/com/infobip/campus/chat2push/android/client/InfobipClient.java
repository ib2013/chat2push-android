package com.infobip.campus.chat2push.android.client;

import java.util.ArrayList;

import com.infobip.campus.chat2push.android.models.ChannelModel;

public interface InfobipClient {
	
	public abstract boolean registerUser(String userName, String password);
	
	public abstract boolean loginUser(String userName, String password);
	
	ArrayList<ChannelModel> fetchAllChannels(String userName);
	
	

}
