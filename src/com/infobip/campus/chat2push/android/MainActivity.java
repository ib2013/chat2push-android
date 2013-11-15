package com.infobip.campus.chat2push.android;

//import com.example.helloworld.R;
//import com.infobip.campus.chat2push.android.R;

import java.sql.Date;
import java.util.ArrayList;

import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;
import com.infobip.campus.chat2push.android.configuration.Configuration;


import com.infobip.campus.chat2push.android.managers.SessionManager;
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
import android.service.textservice.SpellCheckerService.Session;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DialerFilter;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	public String m_Text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if(SessionManager.isAnyUserLogedIn()) {
			Intent intent = new Intent(MainActivity.this, ChannelListActivity.class);
			startActivity(intent);
			finish();
		}
		
		// ovde ce da se dobija lista korisnika iz SessionManager
		final String[] users = new String[] { "Korisnik1", "Mica", "Pera", "Zika" };
		final AutoCompleteTextView userNameEditText = (AutoCompleteTextView) findViewById(R.id.editTextUserName);
		final EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);
		
		Intent intent = getIntent();
		userNameEditText.setText(intent.getStringExtra("userName"));
		passwordEditText.setText(intent.getStringExtra("password"));
		if(intent.getBooleanExtra("fromRegistration", false)) {
			final EditText txtUrl = new EditText(MainActivity.this);
			int maxLength = 4;    
			txtUrl.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
			txtUrl.setInputType(InputType.TYPE_CLASS_NUMBER);
			new AlertDialog.Builder(MainActivity.this)
			.setTitle("Insert confirmation number")
			.setView(txtUrl)
			.setPositiveButton("ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					EditText userNameEditText = (EditText) findViewById(R.id.editTextUserName);
					EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);
					new LoginUser().execute(userNameEditText.getText().toString(),
							passwordEditText.getText().toString(), txtUrl.getText().toString());
				}
			})
			.setNegativeButton("Cancel", null)
			.show();
		}
		
		// autocompletion
		ArrayAdapter<String> autoCompletionAdapter =
				new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, users);
		userNameEditText.setAdapter(autoCompletionAdapter);
		
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
				
				Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
				intent.putExtra("userName", userNameEditText.getText().toString());
				intent.putExtra("password", passwordEditText.getText().toString());
				startActivity(intent);
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
	
	public void clearEditTexts() {
		EditText userNameEditText = (EditText) findViewById(R.id.editTextUserName);
		userNameEditText.setText("");
		EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);
		passwordEditText.setText("");
	}
	
	class LoginUser extends AsyncTask<String, String, String> {
		
		boolean isValidLogin = false;
		boolean isVerified = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			try {
				Log.i("LoginUser_ARGUMENT_LIST", args[0] + " -  " + args[1]);
				if(DefaultInfobipClient.loginUser(args[0], args[1]) == null) {
					isValidLogin = true;
					isVerified = true;
					SessionManager.loginUser(args[0], args[1]);
				}
				else if(DefaultInfobipClient.loginUser(args[0], args[1]).equals("MISSING_VERIFICATION")) {
					isValidLogin = true;
					if(args.length == 3) {
						Log.i("argumenti f-je verifyUser:", args[0] + " " + Integer.parseInt(args[2]));
						if(DefaultInfobipClient.verifyUser(args[0], Integer.parseInt(args[2]))) {
							isVerified = true;
							SessionManager.loginUser(args[0], args[1]);
						}
					}
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
						if(isVerified) {
						
						// kasnije cemo da stavimo rotating spinner
						new AlertDialog.Builder(MainActivity.this)
						.setTitle("Login successful")
						.setMessage("Welcome,  " + SessionManager.getCurrentUserName() + "!")
						.setPositiveButton("ok", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
								Intent intent = new Intent(MainActivity.this, ChannelListActivity.class);
								startActivity(intent);
							}
						})
						.show();
						}
						else {
							final EditText txtUrl = new EditText(MainActivity.this);
							int maxLength = 4;    
							txtUrl.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
							txtUrl.setInputType(InputType.TYPE_CLASS_NUMBER);
							new AlertDialog.Builder(MainActivity.this)
							.setTitle("Insert confirmation number")
							.setView(txtUrl)
							.setPositiveButton("ok", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									EditText userNameEditText = (EditText) findViewById(R.id.editTextUserName);
									EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);
									new LoginUser().execute(userNameEditText.getText().toString(),
											passwordEditText.getText().toString(), txtUrl.getText().toString());
									Log.i("PROBA INPUTA", txtUrl.getText().toString());
								}
							})
							.setNegativeButton("Cancel", null)
							.show();
						}
					}
					else {
						new AlertDialog.Builder(MainActivity.this)
						.setTitle("Login error")
						.setMessage("Incorrect username/password combination!")
						.setPositiveButton("OK", null)
						.show();
						
						clearEditTexts();
					}
				}
			});
		}

	}

}
