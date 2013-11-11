package com.infobip.campus.chat2push.android.client;

import java.util.ArrayList;
import java.util.Date;

import com.infobip.campus.chat2push.android.models.ChannelMessage;
import com.infobip.campus.chat2push.android.models.ChannelModel;

public interface InfobipClient {
	
	public abstract boolean registerUser(String userName, String password);
	
	public abstract boolean loginUser(String userName, String password);
	
	public abstract ArrayList<ChannelModel> fetchAllChannels(String userName);
	
	public abstract ArrayList<ChannelMessage> fetchAllMessages(ChannelModel channel, Date startTime, Date endTime);

}
