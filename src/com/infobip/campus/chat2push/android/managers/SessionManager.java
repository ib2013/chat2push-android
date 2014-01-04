package com.infobip.campus.chat2push.android.managers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.infobip.campus.chat2push.android.adapters.MyApplication;
import com.infobip.campus.chat2push.android.configuration.Configuration;
import com.infobip.campus.chat2push.android.models.ChannelModel;
import com.infobip.push.ChannelObtainListener;
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
			editor.putString("currentUserPassword", password);
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
		if (oldUsername.equals(" ")) {
			return false;
		}
		return true;		
	}
	
	public static void subscribeToChannels (ArrayList<ChannelModel> channelList) {		
		initialize();		
		ArrayList<String> channelNames = new ArrayList<String>();
		for (ChannelModel channelItem : channelList) {
			if (channelItem.getStatus()) {
				channelNames.add(channelItem.getName());
			}
		}
		ChannelRegistrationListener channelRegistrationListener = null;
		manager.registerToChannels(channelNames, false, channelRegistrationListener);
		Log.d("Pretplatio sam se na: ", channelNames.toString());		
	}
	
	public static void subscribeToChannelByName (String channelName) {		
		initialize();		
		List<String> channelNames = new ArrayList<String>();
		channelNames.add(channelName);
		Log.d("U SessionMenager.subscribeToChannelByName", "Poslat cu mu da se pretplatim na: " + channelNames.toString());
		ChannelRegistrationListener channelRegistrationListener = null;
		manager.registerToChannels(channelNames, false, channelRegistrationListener);
		Log.d("U SessionMenager.subscribeToChannelByName", "Sve je proslo bez greske!");		
	}
	
	public static void unsubscribeFromChannelByName (String channelName) {		
		initialize();
		class DefaultChannelObtainListener implements ChannelObtainListener {			
			ArrayList<String> obtainedChannelNames;
			String channelToUnsubscribeFrom = "";
			
			DefaultChannelObtainListener( String channelName) {
				super();
				obtainedChannelNames= new ArrayList<String>();
				channelToUnsubscribeFrom = channelName;
			}

			@Override
			public void onChannelsObtained(String[] channels) {				
				for (String channelName : channels) {
					obtainedChannelNames.add(channelName);
				}
				obtainedChannelNames.remove(channelToUnsubscribeFrom);
				ChannelRegistrationListener channelRegistrationListener = null;
				manager.registerToChannels(obtainedChannelNames, true, channelRegistrationListener);			
			}
			@Override
			public void onChannelObtainFailed(int reason) {
				// TODO Auto-generated method stub
				
			}			
		}		
		ChannelObtainListener channelObtainListener = new DefaultChannelObtainListener(channelName);
		manager.getRegisteredChannels(channelObtainListener);
	}

	public static String getCurrentUserName () {
		initialize();
		return sharedPreferences.getString("currentUserName", " ");
	}
	
	public static String getCurrentUserPassword () {		
		initialize();
		return sharedPreferences.getString("currentUserPassword", " ");		
	}
}
