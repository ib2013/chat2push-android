package com.infobip.campus.chat2push.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.infobip.campus.chat2push.android.MyPushReceiver.CallbackInterface;
import com.infobip.campus.chat2push.android.adapters.MessageArrayAdapter;
import com.infobip.campus.chat2push.android.adapters.MyApplication;
import com.infobip.campus.chat2push.android.configuration.Configuration;
import com.infobip.campus.chat2push.android.models.MessageModel;
import com.infobip.push.ChannelRegistrationListener;
import com.infobip.push.PushNotificationBuilder;
import com.infobip.push.PushNotificationManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class ChannelActivity extends Activity implements CallbackInterface {
	
	protected MyApplication myApplication;
	protected String channelName = "";
	MessageArrayAdapter messageViewAdapter = null;
	final ArrayList<MessageModel> messageList = new ArrayList<MessageModel>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel);
		
		myApplication = (MyApplication)this.getApplicationContext();
		
		Intent intent = getIntent();
		channelName = intent.getStringExtra("channelName") + "TEST";
		this.setTitle(channelName);
		
		//Hardcodeano za testiranje:
		messageList.add(new MessageModel("Ljudina", "Maaajke mi; ja sam ljud'na! A buduæi da bi chat valjalo testirati na malo veèim stringovima još æu par stvari napisati!", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("Druga Ljudina", "Maaajke mi; i ja sam!!! I još bih samo nadodao da ova Ljudina iznad mene nije ništa veèa ljudima OD mene, pa æu i ja još nešto napisati. I to nešto æe biti veèe od ovoga iznad. Nek se zna.", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("3. Ljudina", "Da.", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("Ljudina", "Maaajke mi; ja sam ljud'na! A buduæi da bi chat valjalo testirati na malo veèim stringovima još æu par stvari napisati!", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("Druga Ljudina", "Maaajke mi; i ja sam!!! I još bih samo nadodao da ova Ljudina iznad mene nije ništa veèa ljudima OD mene, pa æu i ja još nešto napisati. I to nešto æe biti veèe od ovoga iznad. Nek se zna.", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("3. Ljudina", "Da.", new Date(System.currentTimeMillis())));
		displayListView(messageList);
		
		//PRIVREMENA!!! prijava na infobipov server za testiranje notifikacija!
		PushNotificationManager manager;
		PushNotificationBuilder builder;
		
		manager = new PushNotificationManager(getApplicationContext());
		manager.initialize(Configuration.SENDER_ID, Configuration.APP_ID, Configuration.APP_SECRET);
		if (!manager.isRegistered()) 
			manager.register();
		manager.overrideDefaultMessageHandling(true);	
		ChannelRegistrationListener channelRegistrationListener = null;
		List<String> channels = new ArrayList<String>();
		channels.add("TEST");
		manager.registerToChannels(channels, true, channelRegistrationListener);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.channel, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
        super.onResume();
        myApplication.setCurrentActivity(this);
    }
	
	@Override
    protected void onPause() {
        clearReferences();
        super.onPause();
    }
	
	@Override
    protected void onDestroy() {        
        clearReferences();
        super.onDestroy();
    }
	
	public void addNewMessage (MessageModel newMessage) {
		messageList.add(newMessage);
		displayListView(messageList);
	}
	
	private void clearReferences(){
        Activity currActivity = myApplication.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this))
        	myApplication.setCurrentActivity(null);
    }
	
	private void displayListView(ArrayList<MessageModel> messageList) {
		// kreiraj ArrayAdaptar iz String Array		
		messageViewAdapter = new MessageArrayAdapter(this, R.layout.activity_channel_list, messageList);
		ListView listView = (ListView) findViewById(R.id.listView);
		// dodeli adapter u ListView
		listView.setAdapter(messageViewAdapter);
	}

}
