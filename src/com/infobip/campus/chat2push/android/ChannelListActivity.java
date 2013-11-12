package com.infobip.campus.chat2push.android;

import java.util.ArrayList;

import com.infobip.campus.chat2push.android.adapters.ChannelArrayAdapter;
import com.infobip.campus.chat2push.android.models.ChannelModel;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.ListView;

public class ChannelListActivity extends ActionBarActivity {
	
	ChannelArrayAdapter listViewAdapter = null;
	final ArrayList<ChannelModel> channelList = new ArrayList<ChannelModel>();
	ChannelModel mdl1 = new ChannelModel();
	ChannelModel mdl2 = new ChannelModel();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel_list);
		mdl1.setName("Ime kanala 1");
		mdl1.setStatus(true);
		mdl1.setDescription("Description kanala 1");
		
		mdl2.setName("Ime kanala 2");
		mdl2.setStatus(true);
		mdl2.setDescription("Description kanala 2");
		
		channelList.add(mdl1);
		channelList.add(mdl2);
		
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
		// dodeli adapter u ListView
		listView.setAdapter(listViewAdapter);
	}

}
