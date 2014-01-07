package com.infobip.campus.chat2push.android;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SpinnerAdapter;

import com.infobip.campus.chat2push.android.adapters.ChannelArrayAdapter;
import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;
import com.infobip.campus.chat2push.android.managers.SessionManager;
import com.infobip.campus.chat2push.android.models.ChannelModel;

public class ChannelListActivity extends ActionBarActivity implements OnNavigationListener {

	ChannelArrayAdapter listViewAdapter = null;
	ArrayList<ChannelModel> channelList;
	boolean isPublic;
	boolean displaySubscribed;
	String filterZaListuKanala = "";

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        moveTaskToBack(true);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel_list);
		isPublic = true;
		displaySubscribed = false;
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		SpinnerAdapter aAdpt = ArrayAdapter.createFromResource(this, R.array.auth_array, com.infobip.campus.chat2push.android.R.drawable.custom_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(aAdpt, this);
	}

	
	@Override
	protected void onResume() {
        super.onResume();
        new LoadAllChannels().execute();	
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
	
	@SuppressLint("DefaultLocale")
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
			case R.id.settings :
				Intent settingsIntent = new Intent(this, SettingsActivity.class);
				this.startActivity(settingsIntent);
				break;
			case R.id.add_new_room :				
				Intent intent1 = new Intent(this, NewChannelActivity.class);
				startActivity(intent1);		
				break;				
		}		
		return true;
	}

	private void displayListView(ArrayList<ChannelModel> channelList) {
		// kreiraj ArrayAdaptar iz String Array
		ArrayList<ChannelModel> tempList = new ArrayList<ChannelModel>();
		if (channelList != null) {
			for(ChannelModel model : channelList) {
				if(displaySubscribed) {
					if(model.getStatus()) {
						tempList.add(model);
					}
				} else {
					if(isPublic) {
						if(model.getIsPublic()) {
							tempList.add(model);
						}
					} else {
						if(!model.getIsPublic()) {
							tempList.add(model);
						}
					}
				}
			}
		} else {
			Log.e("U channelListActivityu sam dobio sa je channelList null", "");
		}
		listViewAdapter = new ChannelArrayAdapter(this, R.layout.activity_channel_list, tempList);
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
				for(ChannelModel model : channelList) {
					Log.i("MODEL TAG", model.toString());
				}
			} catch (Exception e) {
				Log.d("ERROR LOADING CHANNELS: ", "FETCHING PROBLEM");
				errorCode = 1;
				e.printStackTrace();
			}
			Log.i("iTAG", "PROVERA STANJA---------------------------------------------");
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
			displaySubscribed = false;
			// prikazi samo PUBLIC kanale
			displayListView(channelList);
		}
		if(arg0==1) {
			isPublic = false;
			displaySubscribed = false;
			// prikazi samo PRIVATE kanale
			displayListView(channelList);
		}
		if(arg0==2) {
			displaySubscribed = true;
			// prikazi samo PUBLIC kanale
			displayListView(channelList);
		}
		return false;
	}
}


