package com.infobip.campus.chat2push.android;

//import com.example.helloworld.R;
//import com.infobip.campus.chat2push.android.R;

import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;
import com.infobip.campus.chat2push.android.client.InfobipClient;
import com.infobip.campus.chat2push.android.configuration.Configuration;

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
		
		// login button
		Button loginButton = (Button) findViewById(R.id.buttonLogin);
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// ako loginUser vrati true, znaci da user/password kombinacija postoji u bazi
				// postavlja USER_NAME na usera i prelazi na ChannelListActivity
				if(true
//						DefaultInfobipClient.loginUser(userNameEditText.getText().toString(),
//						passwordEditText.getText().toString())
						) {
							//Configuration.USER_NAME = userNameEditText.getText().toString();
							Intent intent = new Intent(MainActivity.this, ChannelListActivity.class);
							startActivity(intent);
				}
				
				// ako loginUser vrati false, izbaci alert da je netacna user/password kombinacija
				else {
					new AlertDialog.Builder(MainActivity.this)
					.setTitle("Login error")
					.setMessage("Incorrect username/password combination!")
					.setPositiveButton("OK", null)
					.show();
				}	
			}
		});
		
		// register button
		Button registerUserButton = (Button) findViewById(R.id.buttonRegisterUser);
		registerUserButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ako postoji taj user
				if(true
//						DefaultInfobipClient.loginUser(userNameEditText.getText().toString(),
//				passwordEditText.getText().toString())
				) {
					// prikazi alert da taj user postoji u bazi
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
			}
				
				// else REGISTRUJ USERA
				else {
					if(DefaultInfobipClient.registerUser(userNameEditText.getText().toString(),
						passwordEditText.getText().toString())) {
							new AlertDialog.Builder(MainActivity.this)
							.setTitle("New account created")
							.setMessage("Welcome,  " + userNameEditText.getText().toString() + "!")
							.setNeutralButton("ok", null)
							.show();
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
