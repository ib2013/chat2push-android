package com.infobip.campus.chat2push.android;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.infobip.campus.chat2push.android.MyPushReceiver.CallbackInterface;
import com.infobip.campus.chat2push.android.adapters.FileAdapter;
import com.infobip.campus.chat2push.android.adapters.MessageArrayAdapter;
import com.infobip.campus.chat2push.android.adapters.MyApplication;
import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;
import com.infobip.campus.chat2push.android.configuration.Configuration;
import com.infobip.campus.chat2push.android.models.MessageModel;
import com.infobip.push.ChannelRegistrationListener;
import com.infobip.push.PushNotificationManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ChannelActivity extends ActionBarActivity implements CallbackInterface {
	
	protected MyApplication myApplication;
	protected String channelName = "";
	MessageArrayAdapter messageViewAdapter = null;
	ArrayList<MessageModel> messageList = new ArrayList<MessageModel>();
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
//		deleteFile(channelName + ".txt");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel);
		
		myApplication = (MyApplication)this.getApplicationContext();
		context = this;
		
		Intent intent = getIntent();
		channelName = intent.getStringExtra("channelName");
		this.setTitle(channelName);
			
		new LoadMessages().execute();
				
		//listener za send button:
		ImageButton sendMessageButton = (ImageButton) findViewById(R.id.image_button_send_message);
		final EditText editTextMessage = (EditText) findViewById(R.id.edit_text_message);
		sendMessageButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String messageText = new String (editTextMessage.getText().toString());
				editTextMessage.setText("");
				new SendMessage().execute(Configuration.CURRENT_USER_NAME, channelName, messageText);
				
			}
		});
		
		//displayListView(messageList);
		
		//PRIVREMENA!!! prijava na infobipov server za testiranje notifikacija!
		PushNotificationManager manager;
		manager = new PushNotificationManager(getApplicationContext());
		manager.initialize(Configuration.SENDER_ID, Configuration.APP_ID, Configuration.APP_SECRET);
		if (!manager.isRegistered()) 
			manager.register();
		manager.overrideDefaultMessageHandling(true);	
		ChannelRegistrationListener channelRegistrationListener = null;
		List<String> channels = new ArrayList<String>();
		channels.add(channelName);
		manager.registerToChannels(channels, true, channelRegistrationListener);
		
	}

	
	@Override
	protected void onResume() {
        super.onResume();
        myApplication.setCurrentActivity(this);
    }
	
	@Override
    protected void onPause() {
        clearReferences();
        if (getPreferences(MODE_PRIVATE).getBoolean(channelName + "-cach", true)) {
        	FileAdapter.writeToFile(this, channelName, messageList);  
        }
        super.onPause();
    }
	
	@Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.channel, menu);
		MenuItem item = menu.findItem(R.id.dont_cache);
		if(getPreferences(MODE_PRIVATE).getBoolean(channelName + "-cach", true))
				item.setTitle("Don't cach");
		else
				item.setTitle("Cach");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO napraviti za pravi meni, trenutno je samo za gumb za testiranje ChannelActivitya.
		switch (item.getItemId()) {
		case R.id.dont_cache :
			if (getPreferences(0).getBoolean(channelName + "-cach", true)) {
				deleteFile(channelName + ".txt");
				SharedPreferences.Editor preferenceEditor = getPreferences(MODE_PRIVATE).edit();
				preferenceEditor.putBoolean(channelName + "-cach", false);
				preferenceEditor.commit();
				item.setTitle("Cach");
			} else {
				SharedPreferences.Editor preferenceEditor = getPreferences(MODE_PRIVATE).edit();
				preferenceEditor.putBoolean(channelName + "-cach", true);
				preferenceEditor.commit();
				item.setTitle("Don't cach");
			}
		}
		return false;
	}
	
	private void clearReferences(){
        Activity currActivity = myApplication.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this))
        	myApplication.setCurrentActivity(null);
    }
	
	public void addNewMessage (MessageModel newMessage) {
		messageList.add(newMessage);
		displayListView(messageList);
	}
	
	
	private void displayListView(ArrayList<MessageModel> messageList) {
		// kreiraj ArrayAdaptar iz String Array		
		messageViewAdapter = new MessageArrayAdapter(this, R.layout.activity_channel_list, messageList);
		ListView listView = (ListView) findViewById(R.id.listView);
		// dodeli adapter u ListView
		listView.setAdapter(messageViewAdapter);
	}

	class LoadMessages extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			
			deleteFile(channelName + ".txt");
			
			Log.d("Ulazi u doInBackground, user je: ", Configuration.CURRENT_USER_NAME);
			Log.d("Ulazi u doInBackground, channel je: ", channelName);
			messageList.addAll(FileAdapter.readFromFIle(context, channelName));
			Log.d("Procitao je file, u messageList ima ovoliko elemenata: ", "" + messageList.size());
			Date  startTime = new Date(0);
			if (messageList.size() != 0)
				startTime = messageList.get(messageList.size()).getDate();
			Log.d("startTime je inicijaliziran na: ", "" + startTime);
			messageList.addAll(DefaultInfobipClient.fetchAllMessages(channelName, startTime, new Date(System.currentTimeMillis())));
			Log.d("Procitao je poruke sa servera, messageList sad ima ovoliko elemenata: ", "" + messageList.size());
		    return "doInBackgroundReturnValue";
		}

		protected void onPostExecute(String file_url) {
			runOnUiThread(new Runnable() {
				public void run() {
					displayListView(messageList);
				}
			});
		}

	}
	
	class SendMessage extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {	
			
			DefaultInfobipClient.sendMessage(args[0], args[1], args[2]);
			
			return "doInBackgroundReturnValue";
		}

		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
		}

	}
	
	
}
