package com.infobip.campus.chat2push.android;

//import com.example.helloworld.R;
//import com.infobip.campus.chat2push.android.R;

import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;
import com.infobip.campus.chat2push.android.configuration.Configuration;

import com.infobip.campus.chat2push.android.*;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {

	String filterZaListuKanala = "";
	
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
				if(DefaultInfobipClient.loginUser(userNameEditText.getText().toString(),
						passwordEditText.getText().toString())
						) {
							Configuration.CURRENT_USER_NAME = userNameEditText.getText().toString();
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
					
					userNameEditText.setText("");
					passwordEditText.setText("");
					userNameEditText.requestFocus();
					
				}	
			}
		});
		
		// register button
		Button registerUserButton = (Button) findViewById(R.id.buttonRegisterUser);
		registerUserButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ako postoji taj user
				if(DefaultInfobipClient.loginUser(userNameEditText.getText().toString(),
				passwordEditText.getText().toString())
				) {
					// prikazi alert da taj user postoji u bazi
					new AlertDialog.Builder(MainActivity.this)
					.setTitle("Registration error")
					.setMessage("Username " + userNameEditText.getText().toString() + " already exists in database!")
					.setNegativeButton("Close", null)
					.show();
					
					userNameEditText.setText("");
					passwordEditText.setText("");
					userNameEditText.requestFocus();
			}
				
				// else REGISTRUJ USERA
				else {
					// ako username nije duzi od 2 karaktera
					if(!(userNameEditText.getText().toString().length() < 3)) {
						// ako password nije duzi od 5 karaktera
						if(!(passwordEditText.getText().toString().length()<6)) {
							// ako je sve u redu, registerUser prolazi
							if(DefaultInfobipClient.registerUser(userNameEditText.getText().toString(),
									passwordEditText.getText().toString())) {
									new AlertDialog.Builder(MainActivity.this)
									.setTitle("New account created")
									.setMessage("Welcome,  " + userNameEditText.getText().toString() + "!")
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
				
			}
		});
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
