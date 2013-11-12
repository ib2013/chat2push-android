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
import com.infobip.campus.chat2push.android.models.ChannelModel;
import com.infobip.campus.chat2push.android.models.MessageModel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChannelListActivity extends ActionBarActivity {

	ChannelArrayAdapter listViewAdapter = null;
	ArrayList<ChannelModel> channelList = new ArrayList<ChannelModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel_list);

		new LoadAllChannels().execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.channel_list, menu);
		return true;
	}

	private void displayListView(ArrayList<ChannelModel> channelList) {
		// kreiraj ArrayAdaptar iz String Array
		listViewAdapter = new ChannelArrayAdapter(this,
				R.layout.activity_channel_list, channelList);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(listViewAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 1) {
					Intent intent = new Intent(ChannelListActivity.this,
							ChannelActivity.class);
					startActivity(intent);
				}

			}
		});
	}

	class LoadAllChannels extends AsyncTask<String, String, String> {
		int errorCode = 0;
		String textView = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {

			channelList = DefaultInfobipClient
					.fetchAllChannels(Configuration.CURRENT_USER_NAME);
			for (ChannelModel model : channelList) {
				Log.i("MODEL TAG", model.toString());
			}

			Log.i("iTAG",
					"PROVERA STANJA---------------------------------------------");
			return "doInBackgroundReturnValue";
		}

		protected void onPostExecute(String file_url) {
			runOnUiThread(new Runnable() {
				public void run() {

					displayListView(channelList);

				}
			});
		}

	}

}
