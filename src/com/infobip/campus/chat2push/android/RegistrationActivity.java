package com.infobip.campus.chat2push.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;

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

		buttonSendConfirmationNumber.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!(userNameEditText.getText().toString().length() < 3)) {
					if(!(passwordEditText.getText().toString().length()<6)) {
						if(!userNameEditText.getText().toString().contains(" ") &&
								!passwordEditText.getText().toString().contains(" ")) {
							if(passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
								if(phoneNumberEditText.getText().toString() !=null
										&& !phoneNumberEditText.getText().toString().equals("")
										&& !phoneNumberEditText.getText().toString().contains(" ")) {
											new RegisterUser().execute(
													userNameEditText.getText().toString(),
													passwordEditText.getText().toString(),
													phoneNumberEditText.getText().toString());
										} else {
											new AlertDialog.Builder(RegistrationActivity.this)
												.setTitle("Registration error")
												.setMessage("Invalid phone number")
												.setNeutralButton("ok", null)
												.show();
										}
							} else {
								new AlertDialog.Builder(RegistrationActivity.this)
									.setTitle("Registration error")
									.setMessage("Password does not match the confirm password")
									.setNeutralButton("ok", null)
									.show();
							}
						} else {
							new AlertDialog.Builder(RegistrationActivity.this)
								.setTitle("Registration error")
								.setMessage("Username and password must not contain any spaces")
								.setNeutralButton("ok", null)
								.show();
						}						
					} else {
						new AlertDialog.Builder(RegistrationActivity.this)
							.setTitle("Registration error")
							.setMessage("Password must contain at least 6 characters!")
							.setNeutralButton("ok", null)
							.show();
						
							passwordEditText.setText("");
							passwordEditText.requestFocus();
					}
				} else {
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
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings :
				Intent settingsIntent = new Intent(this, SettingsActivity.class);
				this.startActivity(settingsIntent);
				break;
			}
			return false;
	}
	
	class RegisterUser extends AsyncTask<String, String, String> {
		
		boolean isValidRegister = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			try {
				if(DefaultInfobipClient.registerUser(args[0], args[1], args[2]) == null) {
					isValidRegister = true;
				}	
			} catch (Exception e) {
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
						intent.putExtra("fromRegistration", true);
						startActivity(intent);
					} else {
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
