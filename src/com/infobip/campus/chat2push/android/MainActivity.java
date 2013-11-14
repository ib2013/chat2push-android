package com.infobip.campus.chat2push.android;

//import com.example.helloworld.R;
//import com.infobip.campus.chat2push.android.R;

import java.util.ArrayList;

import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;
import com.infobip.campus.chat2push.android.configuration.Configuration;


import com.infobip.campus.chat2push.android.models.ChannelModel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
				//LOGIN USERA
				
				new LoginUser().execute(userNameEditText.getText().toString(),
						passwordEditText.getText().toString());
				
			}
		});
		
		// register button
		Button registerUserButton = (Button) findViewById(R.id.buttonRegisterUser);
		registerUserButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// REGISTRUJ USERA

					// ako username nije duzi od 2 karaktera
					if(!(userNameEditText.getText().toString().length() < 3)) {
						// ako password nije duzi od 5 karaktera
						if(!(passwordEditText.getText().toString().length()<6)) {
							if(!userNameEditText.getText().toString().contains(" ")) {
								new RegisterUser().execute(userNameEditText.getText().toString(),
										passwordEditText.getText().toString());
							}
							else {
								new AlertDialog.Builder(MainActivity.this)
								.setTitle("Registration error")
								.setMessage("Password must not contain any spaces")
								.setNeutralButton("ok", null)
								.show();
							}
							
						}
						else {
							
							new AlertDialog.Builder(MainActivity.this)
							.setTitle("Registration error")
							.setMessage("Password must contain at least 6 characters!")
							.setNeutralButton("ok", null)
							.show();
							
							passwordEditText.setText("");
							passwordEditText.requestFocus();
						}
					}
					
					else {
						new AlertDialog.Builder(MainActivity.this)
						.setTitle("Registration error")
						.setMessage("Username must contain at least 3 characters!")
						.setNeutralButton("ok", null)
						.show();
							
						userNameEditText.setText("");
						passwordEditText.setText("");
						userNameEditText.requestFocus();
					}
				
			}
		});
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO napraviti za pravi meni, trenutno je samo za gumb za testiranje ChannelActivitya.
		switch (item.getItemId()) {
		case R.id.test_channel_activity :
			Intent intent = new Intent(this, ChannelActivity.class);
			intent.putExtra("channelName", "TEST");
			this.startActivity(intent);				
		}
		return false;
	}
	
	class LoginUser extends AsyncTask<String, String, String> {
		
		boolean isValidLogin = true;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			try {
				Log.i("LoginUser_ARGUMENT_LIST", args[0] + " -  " + args[1]);
				if(DefaultInfobipClient.loginUser(args[0], args[1]) == null) {
					isValidLogin = true;
					Configuration.CURRENT_USER_NAME = args[0];
				}
			} catch (Exception e) {
				Log.d("LoginUser doInBackground EXCEPTION:", "Login error!");
				e.printStackTrace();

			}
			return "LoginUser doInBackground return value";
		}

		protected void onPostExecute(String file_url) {
			runOnUiThread(new Runnable() {
				public void run() {
					if(isValidLogin) {
						new AlertDialog.Builder(MainActivity.this)
						.setTitle("Login successful")
						.setMessage("Welcome,  " + Configuration.CURRENT_USER_NAME + "!")
						.setPositiveButton("ok", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(MainActivity.this, ChannelListActivity.class);
								startActivity(intent);
							}
						})
						.show();
					}
					else {
						new AlertDialog.Builder(MainActivity.this)
						.setTitle("Login error")
						.setMessage("Incorrect username/password combination!")
						.setPositiveButton("OK", null)
						.show();
					}
				}
			});
		}

	}
	
	class RegisterUser extends AsyncTask<String, String, String> {
		
		boolean isValidRegister = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			try {
				Log.i("RegisterUser_ARGUMENT_LIST", args[0] + " -  " + args[1]);
				if(DefaultInfobipClient.registerUser(args[0], args[1]) == null) {
					isValidRegister = true;
					Configuration.CURRENT_USER_NAME = args[0];
				}
			} catch (Exception e) {
				Log.d("RegisterUser doInBackground EXCEPTION:", "Error registering user!");
				e.printStackTrace();

			}
			return "RegisterUser doInBackground return value";
		}

		protected void onPostExecute(String file_url) {
			runOnUiThread(new Runnable() {
				public void run() {
					if(isValidRegister) {
						new AlertDialog.Builder(MainActivity.this)
						.setTitle("New account created")
						.setMessage("Welcome,  " + Configuration.CURRENT_USER_NAME + "!")
						.setPositiveButton("ok", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(MainActivity.this, ChannelListActivity.class);
								startActivity(intent);
							}
						})
						.show();
					}
					else {
						new AlertDialog.Builder(MainActivity.this)
						.setTitle("Registration error")
						.setMessage("Unable to create new account")
						.setPositiveButton("OK", null)
						.show();
					}
				}
			});
		}

	}
	
}
