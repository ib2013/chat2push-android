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
import com.infobip.campus.chat2push.android.ChannelActivity.LoadMessages;
import com.infobip.campus.chat2push.android.MainActivity.LoginUser;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChannelListActivity extends ActionBarActivity implements OnNavigationListener {

	ChannelArrayAdapter listViewAdapter = null;
	ArrayList<ChannelModel> channelList;
	boolean isPublic;
	String filterZaListuKanala = "";

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

//		new LoadAllChannels().execute();
		
	}

	@Override
	protected void onResume() {
        super.onResume();
        new LoadAllChannels().execute();	
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.channel_list, menu);
		
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			public boolean onQueryTextSubmit(String arg0) {
				return false;
			}
			
			public boolean onQueryTextChange(String arg0) {
				filterZaListuKanala = arg0;
				addItemsOnListView();
//				displayListView(channelList);
				return false;
			}
		});
		
		MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
	
			@Override
			public boolean onMenuItemActionExpand(MenuItem arg0) {
				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem arg0) {
				filterZaListuKanala = "";
				addItemsOnListView();
				displayListView(channelList);
				return true;
			}
		});

		return true;
	}
	
	private void addItemsOnListView() {
		final ArrayList<ChannelModel> tempChannelList = new ArrayList<ChannelModel>();
		for (ChannelModel model : channelList)	
			if (model.getName().toLowerCase().contains(filterZaListuKanala.toLowerCase()))
				tempChannelList.add(model);
		displayListView(tempChannelList);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.refresh:
				new LoadAllChannels().execute();
				break;
			case R.id.log_out :
				SessionManager.logout();
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				break;
			case R.id.add_new_room :
				
				final EditText editTextNewRoomName = new EditText(this);
				editTextNewRoomName.setInputType(InputType.TYPE_CLASS_TEXT);
				editTextNewRoomName.setHint("New room name =?");
				new AlertDialog.Builder(this)
				.setTitle("Adding a new room")
				.setView(editTextNewRoomName)
				.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new CreateNewRoom().execute(editTextNewRoomName.getText().toString(), "Undescribed.");
					}
				})
				.setNegativeButton("ABORT", null)
				.show();
				
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

	class CreateNewRoom extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		
		}

		protected String doInBackground(String... args) {

			try {
				
				DefaultInfobipClient.createNewRoom (args[0], args[1]);

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


