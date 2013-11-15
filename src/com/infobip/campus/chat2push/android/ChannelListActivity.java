package com.infobip.campus.chat2push.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infobip.campus.chat2push.android.adapters.ChannelArrayAdapter;
import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;
import com.infobip.campus.chat2push.android.configuration.Configuration;
import com.infobip.campus.chat2push.android.managers.SessionManager;
import com.infobip.campus.chat2push.android.models.ChannelModel;
import com.infobip.campus.chat2push.android.models.MessageModel;

import android.R.drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChannelListActivity extends ActionBarActivity implements OnNavigationListener {

	ChannelArrayAdapter listViewAdapter = null;
	ArrayList<ChannelModel> channelList;
	boolean isPublic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel_list);
		isPublic = true;
		
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
//		R.layout.support_simple_spinner_dropdown_item
		
		SpinnerAdapter aAdpt = 
				ArrayAdapter.createFromResource(this, R.array.auth_array, com.infobip.campus.chat2push.android.R.drawable.custom_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(aAdpt, this);

		new LoadAllChannels().execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.channel_list, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.refresh:
				new LoadAllChannels().execute();
				break;
			case R.id.settings:
				break;
			case R.id.log_out :
				SessionManager.logout();
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				break;
				
		}
		
		return true;
	}

	private void displayListView(ArrayList<ChannelModel> channelList) {
		// kreiraj ArrayAdaptar iz String Array
		ArrayList<ChannelModel> tempList = new ArrayList<ChannelModel>();
		for(ChannelModel model : channelList) {
			if(isPublic) {
				if(model.getIsPublic()) {
					tempList.add(model);
				}
			}
			else {
				if(!model.getIsPublic()) {
					tempList.add(model);
				}
			}
		}
		listViewAdapter = new ChannelArrayAdapter(this,
				R.layout.activity_channel_list, tempList);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(listViewAdapter);

	}

	class LoadAllChannels extends AsyncTask<String, String, String> {
		int errorCode = 0;
		String textView = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		
		}

		protected String doInBackground(String... args) {

			channelList = new ArrayList<ChannelModel>();
			
			try {
					
					channelList = DefaultInfobipClient.fetchAllChannels(SessionManager.getCurrentUserName());
//					SessionManager.subscribeToChannels(channelList);
					for(ChannelModel model : channelList) {
						Log.i("MODEL TAG", model.toString());
					}

			} catch (Exception e) {
				Log.d("ERROR LOADING CHANNELS: ", "FETCHING PROBLEM");
				errorCode = 1;
				e.printStackTrace();

			}

			Log.i("iTAG",
					"PROVERA STANJA---------------------------------------------");
			return "LoadAllChannels return value";
		}

		protected void onPostExecute(String file_url) {
			runOnUiThread(new Runnable() {
				public void run() {
					SessionManager.subscribeToChannels(channelList);
					displayListView(channelList);

				}
			});
		}

	}

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		if(arg0==0) {
			isPublic = true;
			// prikazi samo PUBLIC kanale
			displayListView(channelList);
		}
		if(arg0==1) {
			isPublic = false;
			// prikazi samo PRIVATE kanale
			displayListView(channelList);
		}
		return false;
	}

}
