package com.infobip.campus.chat2push.android;

//import com.example.helloworld.R;
//import com.infobip.campus.chat2push.android.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;
import com.infobip.campus.chat2push.android.managers.SessionManager;

public class MainActivity extends ActionBarActivity {
	
	public String m_Text;
	
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
		setContentView(R.layout.activity_main);

		if(SessionManager.isAnyUserLogedIn()) {
			new LoginUser().execute(SessionManager.getCurrentUserName(), SessionManager.getCurrentUserPassword());
			Intent intent = new Intent(MainActivity.this, ChannelListActivity.class);
			startActivity(intent);
		} else {
			final String[] users = new String[] {};
			final AutoCompleteTextView userNameEditText = (AutoCompleteTextView) findViewById(R.id.editTextUserName);
			final EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);
		
			Intent intent = getIntent();
			userNameEditText.setText(intent.getStringExtra("userName"));
			passwordEditText.setText(intent.getStringExtra("password"));
			if(intent.getBooleanExtra("fromRegistration", false)) {
				final EditText txtUrl = new EditText(MainActivity.this);
				final Button resendButton = (Button) findViewById(R.id.buttonResendConfirmation);
				resendButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						//DefaultInfobipClient.resendVerificationNumber(intent.getStringExtra("userName"));
					}
				});
				resendButton.setVisibility(View.VISIBLE);
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
		
			ArrayAdapter<String> autoCompletionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, users);
			userNameEditText.setAdapter(autoCompletionAdapter);
			Button loginButton = (Button) findViewById(R.id.buttonLogin);
			loginButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					new LoginUser().execute(userNameEditText.getText().toString(), passwordEditText.getText().toString());
				}
			});		
			Button registerUserButton = (Button) findViewById(R.id.buttonRegisterUser);
			registerUserButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent registerIntent = new Intent(MainActivity.this, RegistrationActivity.class);
					if (userNameEditText.getText().toString() != null && passwordEditText.getText().toString() != null) {
						registerIntent.putExtra("userName", userNameEditText.getText().toString());
						registerIntent.putExtra("password", passwordEditText.getText().toString());
					}
					startActivity(registerIntent);
				}
			});
			Button resendButton = (Button) findViewById(R.id.buttonResendConfirmation);
			resendButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//DefaultInfobipClient.resendVerificationNumber(intent.getStringExtra("userName"));				
				}
			});
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO napraviti za pravi meni, trenutno je samo za gumb za testiranje ChannelActivitya.
		switch (item.getItemId()) {
			case R.id.settings :
				Intent intent = new Intent(this, SettingsActivity.class);
				final AutoCompleteTextView userNameEditText = (AutoCompleteTextView) findViewById(R.id.editTextUserName);
				intent.putExtra("userName", userNameEditText.getText().toString());
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
				if(DefaultInfobipClient.loginUser(args[0], args[1]) == null) {
					isValidLogin = true;
					isVerified = true;
					SessionManager.loginUser(args[0], args[1]);
				} else if(DefaultInfobipClient.loginUser(args[0], args[1]).equals("MISSING_VERIFICATION")) {
					isValidLogin = true;
					if(args.length == 3) {
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
						} else {
							final Button resendButton = (Button) findViewById(R.id.buttonResendConfirmation);
							resendButton.setVisibility(View.VISIBLE);
							final EditText txtUrl = new EditText(MainActivity.this);
							int maxLength = 4;    
							txtUrl.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
							txtUrl.setInputType(InputType.TYPE_CLASS_NUMBER);
							Log.d("Sad i trebao izletiti dialog!", "");
							new AlertDialog.Builder(MainActivity.this)
								.setTitle("Insert confirmation number")
								.setView(txtUrl)
								.setPositiveButton("ok", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										EditText userNameEditText = (EditText) findViewById(R.id.editTextUserName);
										EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);
										Log.d("Ovo je onclick na ok u dialogu!", txtUrl.getText().toString());
										new LoginUser().execute(userNameEditText.getText().toString(),
											passwordEditText.getText().toString(), txtUrl.getText().toString());
									}
								})
								.setNegativeButton("Cancel", null)
								.show();
						}
					} else {
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
