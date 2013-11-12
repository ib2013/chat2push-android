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
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

public class ChannelActivity extends ActionBarActivity implements CallbackInterface {
	
	protected MyApplication myApplication;
	protected String channelName = "";
	MessageArrayAdapter messageViewAdapter = null;
	ArrayList<MessageModel> messageList = new ArrayList<MessageModel>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel);
		
		myApplication = (MyApplication)this.getApplicationContext();
		
		Intent intent = getIntent();
		channelName = intent.getStringExtra("channelName");
		this.setTitle(channelName);
		
	
		//Hardcodeano za testiranje:
		/*
		messageList.add(new MessageModel("Ljudina", "Maaajke mi; ja sam ljud'na! A buduæi da bi chat valjalo testirati na malo veèim stringovima još æu par stvari napisati!", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("Druga Ljudina", "Maaajke mi; i ja sam!!! I još bih samo nadodao da ova Ljudina iznad mene nije ništa veèa ljudima OD mene, pa æu i ja još nešto napisati. I to nešto æe biti veèe od ovoga iznad. Nek se zna.", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("3. Ljudina", "Da.", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("Ljudina", "Maaajke mi; ja sam ljud'na! A buduæi da bi chat valjalo testirati na malo veèim stringovima još æu par stvari napisati!", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("Druga Ljudina", "Maaajke mi; i ja sam!!! I još bih samo nadodao da ova Ljudina iznad mene nije ništa veèa ljudima OD mene, pa æu i ja još nešto napisati. I to nešto æe biti veèe od ovoga iznad. Nek se zna.", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("3. Ljudina", "Da.", new Date(System.currentTimeMillis())));
		*/
		
		new LoadMessages().execute();
		
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
        
        //Spremanje loadanih poruka za kasnije..
        //TODO ovo treba MASNO testirati.
        //jer ja nemam pojma sto ja to ovdje radim...
        //isto tako ne znam je li to usklaðeno s onim dolje
        //jer dolje isto nisam imao pojma sto radim!
        
        
        JSONArray jsonArray = new JSONArray();
        try {
        	for (MessageModel message : messageList) 
            	jsonArray.put(message.getJSONObject());
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(channelName + ".txt", Context.MODE_PRIVATE));
	        outputStreamWriter.write(jsonArray.toString());
	        outputStreamWriter.close();
	        
	        Log.e("Write to file: ", jsonArray.toString());
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } catch (JSONException e) {
			e.printStackTrace();
		}
        
        super.onPause();
    }
	
	@Override
    protected void onDestroy() {        
        clearReferences();
        super.onDestroy();
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
			try {
				
				//OPASNO!!! Ovo nemam pojma radi li!
				//TODO opako provjeriti...
				//ponavljam, ovaj dio koda se sastoji od nasumicnih poziva metoda!
				//eto, toliko... upozoren si.
				
				String messageListString = "";
				InputStream inputStream = openFileInput(channelName + ".txt");
		        if ( inputStream != null ) {
		            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		            String bufferString = "";
		            StringBuilder stringBuilder = new StringBuilder();
		            while ( (bufferString = bufferedReader.readLine()) != null ) {
		                stringBuilder.append(bufferString);
		            }
		            inputStream.close();
		            messageListString = stringBuilder.toString();
		            JSONArray jsonArray = new JSONArray(messageListString);
		            for (int i = 0; i < jsonArray.length(); ++i) {
		            	messageList.add(new MessageModel (jsonArray.getJSONObject(i)));
		            }
		        }
		        //messageList.addAll(DefaultInfobipClient.fetchAllMessages(channelName, messageList.get(messageList.size()).getDate() , new Date(System.currentTimeMillis())));
		    }
		    catch (FileNotFoundException e) {
		        Log.e("login activity", "File not found: " + e.toString());
		    } catch (IOException e) {
		        Log.e("login activity", "Can not read file: " + e.toString());
		    } catch (Exception e) {
				Log.d("ERROR LOADING Masseges: ", "FETCHING PROBLEM");
				e.printStackTrace();
			}
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
	
	
}
