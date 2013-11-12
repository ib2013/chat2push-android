package com.infobip.campus.chat2push.android;

import java.util.ArrayList;
import java.util.Date;

import com.infobip.campus.chat2push.android.adapters.MessageArrayAdapter;
import com.infobip.campus.chat2push.android.models.MessageModel;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class ChannelActivity extends Activity {

	MessageArrayAdapter messageViewAdapter = null;
	final ArrayList<MessageModel> messageList = new ArrayList<MessageModel>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel);
		
		messageList.add(new MessageModel("Ljudina", "Maaajke mi; ja sam ljud'na! A buduæi da bi chat valjalo testirati na malo veèim stringovima još æu par stvari napisati!", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("Druga Ljudina", "Maaajke mi; i ja sam!!! I još bih samo nadodao da ova Ljudina iznad mene nije ništa veèa ljudima OD mene, pa æu i ja još nešto napisati. I to nešto æe biti veèe od ovoga iznad. Nek se zna.", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("3. Ljudina", "Da.", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("Ljudina", "Maaajke mi; ja sam ljud'na! A buduæi da bi chat valjalo testirati na malo veèim stringovima još æu par stvari napisati!", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("Druga Ljudina", "Maaajke mi; i ja sam!!! I još bih samo nadodao da ova Ljudina iznad mene nije ništa veèa ljudima OD mene, pa æu i ja još nešto napisati. I to nešto æe biti veèe od ovoga iznad. Nek se zna.", new Date(System.currentTimeMillis())));
		messageList.add(new MessageModel("3. Ljudina", "Da.", new Date(System.currentTimeMillis())));
		displayListView(messageList);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.channel, menu);
		return true;
	}
	
	private void displayListView(ArrayList<MessageModel> messageList) {
		// kreiraj ArrayAdaptar iz String Array		
		messageViewAdapter = new MessageArrayAdapter(this, R.layout.activity_channel_list, messageList);
		ListView listView = (ListView) findViewById(R.id.listView);
		// dodeli adapter u ListView
		listView.setAdapter(messageViewAdapter);
	}

}
