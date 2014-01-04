package com.infobip.campus.chat2push.android.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.infobip.campus.chat2push.android.ChannelActivity;
import com.infobip.campus.chat2push.android.R;
import com.infobip.campus.chat2push.android.client.DefaultInfobipClient;
import com.infobip.campus.chat2push.android.managers.SessionManager;
import com.infobip.campus.chat2push.android.models.ChannelModel;

public class ChannelArrayAdapter extends ArrayAdapter<ChannelModel>{

	private ArrayList<ChannelModel> channelList;
	private Context context;

	public ChannelArrayAdapter(Activity context, int davaViewResourceId, List<ChannelModel> data) {
		super(context, davaViewResourceId, data);		
		this.context = context;
		this.channelList = new ArrayList<ChannelModel>();
		this.channelList.addAll(data);		
	}
	
	private class ViewHolder {
		TextView textViewChannelName, textViewChannelDescription;
		CheckBox checkboxChannelStatus;
		LinearLayout linearLayoutClickable;
		ImageView imageView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			LayoutInflater viewInflater = ((Activity) context).getLayoutInflater();
			convertView = viewInflater.inflate(R.layout.list_channel_item, null);

			viewHolder = new ViewHolder();
			viewHolder.textViewChannelName = (TextView) convertView.findViewById(R.id.textViewChannelName);
			viewHolder.textViewChannelDescription = (TextView) convertView.findViewById(R.id.textViewChannelDescription);
			viewHolder.checkboxChannelStatus = (CheckBox) convertView.findViewById(R.id.checkBoxChannelStatus);
			viewHolder.linearLayoutClickable = (LinearLayout) convertView.findViewById(R.id.linearLayoutClickable);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.privateImageView);
			convertView.setTag(viewHolder);

			// napravi listener za checkpoint
			viewHolder.checkboxChannelStatus.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					CheckBox checkBox = (CheckBox) v;
					ChannelModel channelItem = (ChannelModel) checkBox.getTag();
					channelItem.setStatus(checkBox.isChecked());
					if (checkBox.isChecked()) {
						new SubscribeToChannel().execute(channelItem.getName());
						
						SessionManager.subscribeToChannelByName(channelItem.getName());
						Log.d("Background od SubscribeToChannel", "SessionManager.subscribeToChannelByName je prosao");
					}
					else {
						new UnsubscribeFromChannel().execute(channelItem.getName());
						
						SessionManager.unsubscribeFromChannelByName(channelItem.getName());
						Log.d("Background od UnsubscribeFromChannel", "SessionManager.unsubscribeFromChannelByName je prosao");
					}
				}
			});
			
			viewHolder.linearLayoutClickable.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Log.i("iTAG", "KLIKNUTO NA LIST_ELEMENT");
					LinearLayout linear = (LinearLayout) v;
					TextView tv = (TextView) linear.getChildAt(0);
					String ime = tv.getText().toString();
					new SubscribeToChannel().execute(ime);
					SessionManager.subscribeToChannelByName(ime);
					Log.d("Background od SubscribeToChannel", "SessionManager.subscribeToChannelByName je prosao");
					Intent intent = new Intent(getContext(), ChannelActivity.class);
					intent.putExtra("channelName", tv.getText().toString());
					Log.i("channelName = ", tv.getText().toString());
					getContext().startActivity(intent);					
				}
			});
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// doda konkretne podatke u novi contentView
		ChannelModel channelItem = channelList.get(position);
		if(!channelItem.getIsPublic()) {
			viewHolder.imageView.setImageResource(R.drawable.privatekey_icon);
		}
		viewHolder.textViewChannelName.setText(channelItem.getName());
		viewHolder.textViewChannelDescription.setText(channelItem.getDescription());
		viewHolder.checkboxChannelStatus.setChecked(channelItem.getStatus());
		viewHolder.checkboxChannelStatus.setTag(channelItem);
		return convertView;
	}
	
	
	
	
	
	//ACYNCTASK ZA PRETPLACIVANJE NA KANALE NA SERVERU!!! NADAM SE DA CE OVO OVAKO RADIT!!!
	
	class SubscribeToChannel extends AsyncTask<String, String, String> {
		int errorCode = 0;
		String textView = "";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		protected String doInBackground(String... args) {
			Log.d("Background od SubscribeToChannel", "username je: " + SessionManager.getCurrentUserName() + " ime kanala za dodati: " + args[0]);
			DefaultInfobipClient.registerUserToChannel(SessionManager.getCurrentUserName(), args[0]);
			Log.d("Background od SubscribeToChannel", "Prošao mi je DefaultInfobipClient.registerUserToChannel");
			return "Subscribe to channel return value";
		}

		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
		}
	}
	
	class UnsubscribeFromChannel extends AsyncTask<String, String, String> {
		int errorCode = 0;
		String textView = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		protected String doInBackground(String... args) {
			Log.d("Background od UnsubscribeFromChannel", "username je: " + SessionManager.getCurrentUserName() + " ime kanala za obrisati: " + args[0]);
			DefaultInfobipClient.unregisterUserFromChannel(SessionManager.getCurrentUserName(), args[0]);
			Log.d("Background od UnsubscribeFromChannel", "Prošao mi je DefaultInfobipClient.unregisterUserFromChannel");
			return "Subscribe to channel return value";
		}

		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
		}
	}
}
