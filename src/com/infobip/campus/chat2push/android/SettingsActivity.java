package com.infobip.campus.chat2push.android;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import java.util.List;
import com.infobip.campus.chat2push.android.MainActivity.LoginUser;
import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {
	/** 
	 * Determines whether to always show the simplified settings UI, where
	 * settings are presented in a single list. When false, settings are shown
	 * as a master/detail two-pane view on tablets. When true, a single pane is
	 * shown on tablets.
	 */
	private static final boolean ALWAYS_SIMPLE_PREFS = true;

	@SuppressLint("NewApi")
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
//		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		addPreferencesFromResource(R.xml.settings);
		
		Intent intent = getIntent();
		
		EditTextPreference etp = (EditTextPreference) findPreference("prefResendPassword");
		etp.setText(intent.getStringExtra("userName"));
//		SwitchPreference switchSound = (SwitchPreference) findPreference("prefSound");
//		boolean switchValue = sharedPrefs.getBoolean("prefSound", true);
//
//		if(switchValue) {
//			Log.i("Iz sharedPrefs-a:", "Prosao switchSound.isChecked()");
//			switchSound.setChecked(true);
//		}
//		else {
//			Log.i("Iz sharedPrefs-a:", "Nije prosao switchSound.isChecked()");
//			switchSound.setChecked(false);
//		}
			
//		switchSound.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
//				SwitchPreference switchSound = (SwitchPreference) findPreference("prefSound");
//				Editor editor = sharedPrefs.edit();
//				if(switchSound.isChecked()) {
//					Log.i("Unutar listenera:", "Prosao switchSound.isChecked()");
//					editor.putBoolean("prefSound", true);
//				}
//				else {
//					Log.i("Unutar listenera:", "Nije prosao switchSound.isChecked()");
//					editor.putBoolean("prefSound", false);
//				}
//				
//				editor.commit();
//				
//				Log.i("Posle listenera, prefSound je:", String.valueOf(sharedPrefs.getBoolean("prefSound", true)));
//					
//				
//				return false;
//			}
//		});
		
		etp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference arg0, Object arg1) {
//				DefaultInfobipClient.sendPassword(arg1.toString());
				Log.i("PORUKA IZ SETTINGSA", arg1.toString());
				return false;
			}
		});
		
		

		

		
	}

	/**
	 * Shows the simplified settings UI if the device configuration if the
	 * device configuration dictates that a simplified, single-pane UI should be
	 * shown.
	 */
}
