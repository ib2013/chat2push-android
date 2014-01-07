package com.infobip.campus.chat2push.android;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.infobip.campus.chat2push.android.MyPushReceiver.CallbackInterface;
import com.infobip.campus.chat2push.android.adapters.FileAdapter;
import com.infobip.campus.chat2push.android.adapters.MessageArrayAdapter;
import com.infobip.campus.chat2push.android.adapters.MyApplication;
import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;
import com.infobip.campus.chat2push.android.managers.SessionManager;
import com.infobip.campus.chat2push.android.models.MessageModel;

public class ChannelActivity extends ActionBarActivity implements CallbackInterface {
	
	protected MyApplication myApplication;
	protected String channelName = "";
	MessageArrayAdapter messageViewAdapter = null;
	ArrayList<MessageModel> messageList = new ArrayList<MessageModel>();
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel);
		
		myApplication = (MyApplication)this.getApplicationContext();
		context = this;
		Intent intent = getIntent();
		channelName = intent.getStringExtra("channelName");
		this.setTitle(channelName);
				
		ImageButton sendMessageButton = (ImageButton) findViewById(R.id.image_button_send_message);
		final EditText editTextMessage = (EditText) findViewById(R.id.edit_text_message);
		sendMessageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String messageText = new String (editTextMessage.getText().toString());
				editTextMessage.setText("");
				if (!messageText.replaceAll(" ", "").equals("")) {
					new SendMessage().execute(SessionManager.getCurrentUserName(), channelName, messageText);
					messageList.add(new MessageModel(" " + SessionManager.getCurrentUserName(), messageText, new Date(0)));
					displayListView(messageList);
				}				
			}
		});
	}

	
	@Override
	protected void onResume() {
        super.onResume();
        myApplication.setCurrentActivity(this);
        new LoadMessages().execute();	
    }
	
	@Override
    protected void onPause() {
        clearReferences();
        if (getPreferences(MODE_PRIVATE).getBoolean(channelName + "-cach", true)) {
        	Log.d("Pisem u file...", "filename="+channelName+".txt");
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
		getMenuInflater().inflate(R.menu.channel, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings :
				Intent settingsIntent = new Intent(this, SettingsActivity.class);
				this.startActivity(settingsIntent);
				break;
			case R.id.log_out :
				SessionManager.logout();
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				break;
			}
			return false;
	}
	
	private void clearReferences(){
        Activity currActivity = MyApplication.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) {
        	myApplication.setCurrentActivity(null);
        }
    }
	
	public void addNewMessage (MessageModel newMessage) {
		int indexToReplace = -1;
		Log.d("newMessage u channelactivityu kad doðe nova poruka...", "poruka glasi|" + (new MessageModel(" " + newMessage.getAuthor(), newMessage.getText(), new Date(0))).toString());
		indexToReplace = messageList.indexOf(new MessageModel(" " + newMessage.getAuthor(), newMessage.getText(), new Date(0)));
		if (indexToReplace > -1) {
			messageList.set(indexToReplace, newMessage);
		} else {
			messageList.add(newMessage);
		}
		displayListView(messageList);
	}	
	
	private void displayListView(ArrayList<MessageModel> messageList) {
		messageViewAdapter = new MessageArrayAdapter(this, R.layout.activity_channel_list, messageList);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(messageViewAdapter);
	}

	class LoadMessages extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			Log.d("Ulazi u doInBackground, user je: ", SessionManager.getCurrentUserName());
			Log.d("Ulazi u doInBackground, channel je: ", channelName);
			/*if (getPreferences(MODE_PRIVATE).getBoolean(channelName + "-cach", true)) {
				messageList.addAll(FileAdapter.readFromFIle(context, channelName));
				Log.d("Procitao je file, u messageList ima ovoliko elemenata: ", "" + messageList.size());
			} else {
				Log.d("Preskoèeno èitanje iz filea, idem odmah na net!", "Ludilo brale!");
			}*/
			Date  startTime = new Date(0);
			if (messageList.size() != 0) {
				startTime = messageList.get(messageList.size()-1).getDate();
			}
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
			Log.d("Ulazi u doInBackground, user je: ", SessionManager.getCurrentUserName());
			DefaultInfobipClient.sendMessage(args[0], args[1], args[2]);
			Log.d("Obavio : DefaultInfobipClient.sendMessage s argumentima: ", args[0] + " " + args[1] + " " + args[2]);
			return "doInBackgroundReturnValue";
		}

		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
		}
	}	
}
