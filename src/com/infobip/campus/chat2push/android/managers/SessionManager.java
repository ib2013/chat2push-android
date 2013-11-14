package com.infobip.campus.chat2push.android.managers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.infobip.campus.chat2push.android.adapters.MyApplication;
import com.infobip.campus.chat2push.android.configuration.Configuration;
import com.infobip.campus.chat2push.android.models.ChannelModel;
import com.infobip.push.ChannelRegistrationListener;
import com.infobip.push.PushNotificationManager;

public class SessionManager {
	
	static PushNotificationManager manager;
	
	static SharedPreferences sharedPreferences; 
		
	private static void initialize () {
		
		if (manager == null) {
			//popuni initializira manager:
			manager = new PushNotificationManager(MyApplication.getAppContext());
			manager.initialize(Configuration.SENDER_ID, Configuration.APP_ID, Configuration.APP_SECRET);
			//registrira po potrebi:
			//(ista registracija za svakog usera, kasnije se samo pretplati na druge kanale)
			if (!manager.isRegistered()) 
				manager.register();
			//kasnije sam upravljam notificationima:
			manager.overrideDefaultMessageHandling(true);	
//			//izbrise pretplatu na kanale:
//			ChannelRegistrationListener channelRegistrationListener = null;
//			manager.registerToChannels(new ArrayList<String>(), true, channelRegistrationListener);
		}
		if(sharedPreferences == null) {
			//dobavi sharedPreferences:
			sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
		}
			
	}
	
	public static void loginUser (String username, String password) throws JSONException {
		
		initialize();
		String oldUsername = sharedPreferences.getString("currentUserName", " ");
		if ( !oldUsername.equals(username)) {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("currentUserName", username);
			editor.commit();
			//TODO za kasnije sve dovrsiti
//			JSONArray jsonAllUsenames = new JSONArray(sharedPreferences.getString("allUsersData", " "));
//			jsonAllUsenames.
		}
		
	}
	
	public static void logout () {
		
		initialize();
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("currentUserName", " ");
		editor.commit();
		//izbrise pretplatu na kanale:
		ChannelRegistrationListener channelRegistrationListener = null;
		manager.registerToChannels(new ArrayList<String>(), true, channelRegistrationListener);
		
	}
	
	public static boolean isAnyUserLogedIn () {
		
		initialize();
		String oldUsername = sharedPreferences.getString("currentUserName", " ");
		if (oldUsername.equals(" "))
			return false;
		return true;
		
	}
	
	public static void subscribeToChannels (ArrayList<ChannelModel> channelList) {
		
		initialize();		
		ArrayList<String> channelNames = new ArrayList<String>();
		for (ChannelModel channelItem : channelList)
			if (channelItem.getStatus())
				channelNames.add(channelItem.getName());
		ChannelRegistrationListener channelRegistrationListener = null;
		manager.registerToChannels(channelNames, true, channelRegistrationListener);
		
	}

	public static String getCurrentUserName () {
		
		initialize();
		return sharedPreferences.getString("currentUserName", " ");
		
	}
	
//	public static nešto nešto
	
}
