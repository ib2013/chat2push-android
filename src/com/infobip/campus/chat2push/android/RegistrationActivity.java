package com.infobip.campus.chat2push.android;

import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;
import com.infobip.campus.chat2push.android.configuration.Configuration;
import com.infobip.campus.chat2push.android.managers.SessionManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		setTitle("Create new account");
		
		final EditText userNameEditText = (EditText) findViewById(R.id.editTextRegisterUserName);
		final EditText passwordEditText = (EditText) findViewById(R.id.editTextRegisterPassword);
		final EditText confirmPasswordEditText = (EditText) findViewById(R.id.editTextRegisterConfirmPassword);
		final EditText phoneNumberEditText = (EditText) findViewById(R.id.editTextRegisterPhoneNumber);
		final Button buttonSendConfirmationNumber = (Button) findViewById(R.id.buttonSendConfirmationNumber);
		
		TelephonyManager telephonyManager;
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		Log.i("BROJ_TELEFONA", telephonyManager.getLine1Number());
		
		Intent intent = getIntent();
		userNameEditText.setText(intent.getStringExtra("userName"));
		passwordEditText.setText(intent.getStringExtra("password"));
		phoneNumberEditText.setText(telephonyManager.getLine1Number().toString());
		
		
		
		buttonSendConfirmationNumber.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!(userNameEditText.getText().toString().length() < 3)) {
					if(!(passwordEditText.getText().toString().length()<6)) {
						if(!userNameEditText.getText().toString().contains(" ") &&
								!passwordEditText.getText().toString().contains(" ")) {
							if(passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
								if(phoneNumberEditText.getText().toString() !=null
										&& phoneNumberEditText.getText().toString()!= ""
										&& !phoneNumberEditText.getText().toString().contains(" ")) {
									new RegisterUser().execute(userNameEditText.getText().toString(),
											passwordEditText.getText().toString(),
											phoneNumberEditText.getText().toString());
								}
								else {
									new AlertDialog.Builder(RegistrationActivity.this)
									.setTitle("Registration error")
									.setMessage("Invalid phone number")
									.setNeutralButton("ok", null)
									.show();
								}
								
							}
							else {
								new AlertDialog.Builder(RegistrationActivity.this)
								.setTitle("Registration error")
								.setMessage("Password does not match the confirm password")
								.setNeutralButton("ok", null)
								.show();
							}
								
						}
						else {
							new AlertDialog.Builder(RegistrationActivity.this)
							.setTitle("Registration error")
							.setMessage("Username and password must not contain any spaces")
							.setNeutralButton("ok", null)
							.show();
						}
						
					}
					else {
						
						new AlertDialog.Builder(RegistrationActivity.this)
						.setTitle("Registration error")
						.setMessage("Password must contain at least 6 characters!")
						.setNeutralButton("ok", null)
						.show();
						
						passwordEditText.setText("");
						passwordEditText.requestFocus();
					}
				}
				
				else {
					new AlertDialog.Builder(RegistrationActivity.this)
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
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
				if(DefaultInfobipClient.registerUser(args[0], args[1], args[2]) == null) {
					isValidRegister = true;
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
						
						Toast verificationToast = Toast.makeText(getApplicationContext(), "Please wait for your verification number", Toast.LENGTH_LONG);
						verificationToast.show();
						
						EditText userNameEditText = (EditText) findViewById(R.id.editTextRegisterUserName);
						EditText passwordEditText = (EditText) findViewById(R.id.editTextRegisterPassword);
						
						Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
						intent.putExtra("userName", userNameEditText.getText().toString());
						intent.putExtra("password", passwordEditText.getText().toString());
						startActivity(intent);
					}
					else {
						new AlertDialog.Builder(RegistrationActivity.this)
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
