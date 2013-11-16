package com.infobip.campus.chat2push.android.adapters;

import java.util.ArrayList;
import java.util.List;

import com.infobip.campus.chat2push.android.R;
import com.infobip.campus.chat2push.android.models.UserModel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class UsersToSubscribeArrayAdapter extends ArrayAdapter<UserModel> {
	
	private ArrayList<UserModel> users;
	private Context context;

	public UsersToSubscribeArrayAdapter (Activity context, int davaViewResourceId, List<UserModel> data) {
		super(context, davaViewResourceId, data);
		
		this.context = context;
		this.users = new ArrayList<UserModel>();
		this.users.addAll(data);
		
	}
	
	private class ViewHolder {
		TextView textViewUsername; 
		CheckBox checkBoxAddUser;
		ImageView imageViewUserIcon;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			LayoutInflater viewInflater = ((Activity) context).getLayoutInflater();
			convertView = viewInflater.inflate(R.layout.list_known_user_item, null);

			viewHolder = new ViewHolder();
			viewHolder.textViewUsername = (TextView) convertView.findViewById(R.id.textViewUserName);
			viewHolder.checkBoxAddUser = (CheckBox) convertView.findViewById(R.id.checkBoxAddUser);
			viewHolder.imageViewUserIcon = (ImageView) convertView.findViewById(R.id.imageViewUserIcon);
			convertView.setTag(viewHolder);


			viewHolder.checkBoxAddUser.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					users.get(position).changeStatus();
					
				}
			});
		
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// doda konkretne podatke u novi contentView
		String userName = users.get(position).getUsername();
		
//		Log.d("Users arrayadapter trenutno ima userName: ", userName);
		
		viewHolder.textViewUsername.setText(userName);
		viewHolder.imageViewUserIcon.setBackgroundColor(Color.rgb(((userName +"   ").charAt(0)*5)%200+50, ((userName +"   ").charAt(1)*6)%200+50, ((userName +"   ").charAt(2)*7)%200+50));
		viewHolder.checkBoxAddUser.setChecked(users.get(position).getStatus());
		
//		Log.d("Proslo upisivanje podataka u view za : ", userName);
		
		return convertView;
	}

}
