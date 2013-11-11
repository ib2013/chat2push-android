package com.infobip.campus.chat2push.android;

import com.infobip.campus.chat2push.android.adapters.ChannelArrayAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

public class ChannelListActivity extends ActionBarActivity {
	
	ChannelArrayAdapter listViewAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.channel_list, menu);
		return true;
	}

}
