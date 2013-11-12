package com.infobip.campus.chat2push.android;

//import com.example.helloworld.R;
//import com.infobip.campus.chat2push.android.R;

import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		final EditText userNameEditText = (EditText) findViewById(R.id.editTextUserName);
		final EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);
		
		// LOGIN BUTTON
		Button loginButton = (Button) findViewById(R.id.buttonLogin);
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(DefaultInfobipClient.loginUser(userNameEditText.getText().toString(),
						passwordEditText.getText().toString())) {
							Intent intent = new Intent(MainActivity.this, ChannelListActivity.class);
							startActivity(intent);
					}
				else {
				
				new AlertDialog.Builder(MainActivity.this)
				.setTitle("Incorrect username/password combination!")
				.setMessage("Create account as " + userNameEditText.getText().toString() + "?")
				.setNegativeButton("Close", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(MainActivity.this, ChannelListActivity.class);
						startActivity(intent);
					}
				})
				.setPositiveButton("OK", null) // umesto null ide listener i registerUser(userNameEditText.getText().toString(), passwordNameEditText.getText().toString());
				.show();

			}	
			} });
		
		// REGISTER BUTTON
		Button registerUserButton = (Button) findViewById(R.id.buttonRegisterUser);
		registerUserButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				if(Client.login(userNameEditText.getText().toString(),
//				passwordEditText.getText().toString())) {
					new AlertDialog.Builder(MainActivity.this)
					.setTitle("Error!")
					.setMessage("Username " + userNameEditText.getText().toString() + " already exists in database!")
					.setNegativeButton("Close", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
							startActivity(intent);
							
						}
					})
					.show();
//				}
					
				//else registerUser(userNameEditText.getText().toString(), passwordNameEditText.getText().toString());
					
				
			}
		});
		
		
//		client.fetchAllChannels(String userName);
//		client.createUserId(String userName, String password);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
