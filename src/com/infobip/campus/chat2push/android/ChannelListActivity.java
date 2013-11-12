package com.infobip.campus.chat2push.android;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.infobip.campus.chat2push.android.adapters.ChannelArrayAdapter;
import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;
import com.infobip.campus.chat2push.android.configuration.Configuration;
import com.infobip.campus.chat2push.android.models.ChannelModel;

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
	ChannelModel mdl1 = new ChannelModel();
	ChannelModel mdl2 = new ChannelModel();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel_list);
		
		new LoadAllChannels().execute();
		
//		mdl1.setName("Ime kanala 1");
//		mdl1.setStatus(false);
//		mdl1.setDescription("Description kanala 1");
//		
//		mdl2.setName("Ime kanala 2");
//		mdl2.setStatus(true);
//		mdl2.setDescription("Description kanala 2");
//		
//		channelList.add(mdl1);
//		channelList.add(mdl2);
		
		displayListView(channelList);
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.channel_list, menu);
		return true;
	}
	
	private void displayListView(ArrayList<ChannelModel> channelList) {
		// kreiraj ArrayAdaptar iz String Array		
		listViewAdapter = new ChannelArrayAdapter(this, R.layout.activity_channel_list, channelList);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(listViewAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2==1) {
					Intent intent = new Intent(ChannelListActivity.this, ChannelActivity.class);
					startActivity(intent);
				}
				
				
			}
		});
		// dodeli adapter u ListView
		
	}
	
	class LoadAllChannels extends AsyncTask<String, String, String> {
		int errorCode = 0;
		String textView = "";

		// metoda koja postavi ProgressDialog
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			pDialog = new ProgressDialog(MainActivity.this);
//			pDialog.setMessage("Loading channels. Please wait....");
//			pDialog.setIndeterminate(false);
//			pDialog.setCancelable(false);
//			pDialog.show();
		}

		// run metoda zahteva
		protected String doInBackground(String... args) {
			// UCITAVANJE KANALA
			// httpGet request sa odgovarajuceg urla
			try {
				
				channelList = DefaultInfobipClient.fetchAllChannels(Configuration.CURRENT_USER_NAME);
//				HttpClient client = new DefaultHttpClient();
//				HttpGet request = new HttpGet(Conf.url_all_channels);
//				request.addHeader(Conf.headerName, Conf.headerValue);
//				HttpResponse response = client.execute(request);
//				// ucitavanje tog dobijenog odgovora u string format u JSON
//				// obliku
//				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

//				String line = "";
//				while ((line = rd.readLine()) != null) {
//					textView += line;
//				}
//				
//				// parsiranje JSON formata u format ArrayList<String> channels
//				try {
//					JSONArray oneArray = new JSONArray(textView);
//					for (int i = 0; i < oneArray.length(); i++) {
//						JSONObject c = oneArray.getJSONObject(i);
//						String name = c.getString("name");
//						channels.add(name);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//					errorCode = 1;
				String rez="";
				for (ChannelModel x : channelList){
					rez+=x.getName() + "-" + x.getDescription() + "-" + x.getStatus();
				}
				Log.d("CHANNELS: ", "SUCCES " + rez);
//				}
			} catch (Exception e) {
				Log.d("ERROR LOADING CHANNELS: ", "FETCHING PROBLEM");
				errorCode = 1;
				e.printStackTrace();
			}
			
			return "prazan string";
		}

		// metoda koja ukloni ProgressDialog i osvezi UI. Osvezavanje UIa samo u
		// PostExecute metodi. Nikako u doInBackground()
		protected void onPostExecute(String file_url) {
//			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					Log.d("DISPLAYING: ", "...");
					displayListView(channelList);
//					if (errorCode == 0) {
//						addItemsOnListView();
//						writeChannelListToFile(channels);
//					} else {
//						Toast.makeText(MainActivity.this, "ERROR - CONNECTION PROBLEM", Toast.LENGTH_LONG).show();
//					}
				}
			});
		}

	}

}
