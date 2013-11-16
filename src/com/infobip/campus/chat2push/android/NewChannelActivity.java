package com.infobip.campus.chat2push.android;

import java.util.ArrayList;

import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;
import com.infobip.campus.chat2push.android.managers.SessionManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class NewChannelActivity extends ActionBarActivity {
	
	ArrayList<String> usersToRegister = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_channel);
		
		final EditText editTextChannelName = (EditText) findViewById(R.id.editTextChannelName);
		final EditText eidtTextChannelDescription = (EditText) findViewById(R.id.editTextChannelDescription);
		final CheckBox checkBoxIsChannelPrivate = (CheckBox) findViewById(R.id.checkBoxIsChannelPrivate);
		
		Button buttonAddChannel = (Button) findViewById(R.id.buttonAddNewChannel);
		buttonAddChannel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("Prije ulasku u onclick stanje checkboxa je: ", String.valueOf(checkBoxIsChannelPrivate.isChecked()));
				new CreateNewRoom().execute(editTextChannelName.getText().toString(), eidtTextChannelDescription.getText().toString(), String.valueOf(checkBoxIsChannelPrivate.isChecked()));	
				SessionManager.subscribeToChannelByName(editTextChannelName.getText().toString());
				finish();
			}
		});
		
		//TODO: lista usera koji ce se automatski predplatiti na ovaj channel!
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_channel, menu);
		return true;
	}
	
	
	class CreateNewRoom extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		
		}

		protected String doInBackground(String... args) {

			try {
				Log.d("Ulazne specke su mi : ", args[0] + " " + args[1] + " " + args[2]);
				boolean isPrivate = false;
				if (args[2].equals("true"))
					isPrivate = true;
				Log.d("Radimo novu sobu sa specifikacijama: ", args[0] + " " + args[1] + " " + String.valueOf(isPrivate));
				DefaultInfobipClient.createNewRoom (args[0], args[1], isPrivate);
				Log.d("Background od SubscribeToChannel", "username je: " + SessionManager.getCurrentUserName() + " ime kanala za dodati: " + args[0]);
				DefaultInfobipClient.registerUserToChannel(SessionManager.getCurrentUserName(), args[0]);
				Log.d("Background od SubscribeToChannel", "Prošao mi je DefaultInfobipClient.registerUserToChannel");
				return "Subscribe to channel return value";

			} catch (Exception e) {
				Log.d("ERROR CREATING ROOM: ", e.getMessage());
				e.printStackTrace();

			}
			return "LoadAllChannels return value";
		}

		protected void onPostExecute(String file_url) {

			super.onPostExecute(file_url);

		}

	}
	
}
