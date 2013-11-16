package com.infobip.campus.chat2push.android.adapters;

import java.util.ArrayList;
import java.util.List;

import com.infobip.campus.chat2push.android.R;
import com.infobip.campus.chat2push.android.configuration.Configuration;
import com.infobip.campus.chat2push.android.managers.SessionManager;
import com.infobip.campus.chat2push.android.models.MessageModel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageArrayAdapter extends ArrayAdapter<MessageModel>{

	private ArrayList<MessageModel> messageList;
	private Context context;
	private String previousMessageAuthor = "";

	public MessageArrayAdapter(Activity context, int davaViewResourceId, List<MessageModel> data) {
		super(context, davaViewResourceId, data);
		
		this.context = context;
		this.messageList = new ArrayList<MessageModel>();
		this.messageList.addAll(data);		
	}
	
	private class ViewHolder {
		TextView textViewAuthor, textViewText, textViewTime;
		ImageView imageVIewAuthorIcon;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		
		if(messageList.get(position) != null) {
			
			MessageModel currentMessageItem = messageList.get(position);
				
			Log.d("MessageArrayAdapter je u jednom koraku dobio message:", currentMessageItem.toString());
			
			LayoutInflater viewInflater = ((Activity) context).getLayoutInflater();
				
				//Ako je "privremena poruka":
				if (currentMessageItem.getAuthor().charAt(0) == ' ') {
					convertView = viewInflater.inflate(R.layout.list_message_right_item, null);
					viewHolder = new ViewHolder();
					viewHolder.textViewAuthor = (TextView) convertView.findViewById(R.id.textViewMessageAuthor);
					viewHolder.textViewText = (TextView) convertView.findViewById(R.id.textViewMessageText);
					viewHolder.textViewTime = (TextView) convertView.findViewById(R.id.textViewMessageTime);
					viewHolder.imageVIewAuthorIcon = (ImageView) convertView.findViewById(R.id.imageViewMessageAutorIcon);
					convertView.setTag(viewHolder);
			
					// doda konkretne podatke u novi contentView
					viewHolder.textViewAuthor.setText(currentMessageItem.getAuthor());
					viewHolder.textViewText.setText(currentMessageItem.getText());
					viewHolder.textViewTime.setText("Sending message...");
					//TODO: rijesiti ikone usera!
//					Log.d("Autor ove poruke je: ", currentMessageItem.getAuthor());
					viewHolder.imageVIewAuthorIcon.setBackgroundColor(Color.rgb(((currentMessageItem.getAuthor().substring(1)+"   ").charAt(0)*5)%200+50, ((currentMessageItem.getAuthor().substring(1)+"   ").charAt(1)*6)%200+50, ((currentMessageItem.getAuthor().substring(1)+"   ").charAt(2)*7)%200+50));
				} else {
					Log.d("Message array adapter, pa ka�e: ", "previousMessageAuthor: " + previousMessageAuthor + ", currentMessageItem.getAuthor(): " + currentMessageItem.getAuthor());
					//Ako ne appenda poruku (defoultno ponasanje):
					if (!currentMessageItem.getAuthor().equals(previousMessageAuthor)) { 
						Log.d("Zaklju�io da mi treba defoultni view", "U njega stavljam: " + currentMessageItem.toString());
						if (currentMessageItem.getAuthor().equals(SessionManager.getCurrentUserName()))  {
							convertView = viewInflater.inflate(R.layout.list_message_right_item, null);
						} else {
							convertView = viewInflater.inflate(R.layout.list_message_left_item, null);
						}

						viewHolder = new ViewHolder();
						viewHolder.textViewAuthor = (TextView) convertView.findViewById(R.id.textViewMessageAuthor);
						viewHolder.textViewText = (TextView) convertView.findViewById(R.id.textViewMessageText);
						viewHolder.textViewTime = (TextView) convertView.findViewById(R.id.textViewMessageTime);
						viewHolder.imageVIewAuthorIcon = (ImageView) convertView.findViewById(R.id.imageViewMessageAutorIcon);
						convertView.setTag(viewHolder);
		
						// doda konkretne podatke u novi contentView
						viewHolder.textViewAuthor.setText(currentMessageItem.getAuthor());
						viewHolder.textViewText.setText(currentMessageItem.getText());
						viewHolder.textViewTime.setText(currentMessageItem.getDateAsString());
						//TODO: rijesiti ikone usera!
//						Log.d("Autor ove poruke je: ", currentMessageItem.getAuthor());
						viewHolder.imageVIewAuthorIcon.setBackgroundColor(Color.rgb(((currentMessageItem.getAuthor()+"   ").charAt(0)*5)%200+50, ((currentMessageItem.getAuthor()+"   ").charAt(1)*6)%200+50, ((currentMessageItem.getAuthor()+"   ").charAt(2)*7)%200+50));
					}
					else {
				
						//ako appenda poruku:
						if (currentMessageItem.getAuthor().equals(SessionManager.getCurrentUserName()))  {
							convertView = viewInflater.inflate(R.layout.list_appendedmessage_right_item, null);
						} else {
							convertView = viewInflater.inflate(R.layout.list_appendedmessage_left_item, null);
						}

						viewHolder = new ViewHolder();
						//viewHolder.textViewAuthor = (TextView) convertView.findViewById(R.id.textViewMessageAuthor);
						viewHolder.textViewText = (TextView) convertView.findViewById(R.id.textViewMessageText);
						viewHolder.textViewTime = (TextView) convertView.findViewById(R.id.textViewMessageTime);
						//viewHolder.imageVIewAuthorIcon = (ImageView) convertView.findViewById(R.id.imageViewMessageAutorIcon);
						convertView.setTag(viewHolder);
		
						// doda konkretne podatke u novi contentView
						//viewHolder.textViewAuthor.setText(currentMessageItem.getAuthor());
						viewHolder.textViewText.setText(currentMessageItem.getText());
						viewHolder.textViewTime.setText(currentMessageItem.getDateAsString());
					}
			}
//			previousMessageAuthor = currentMessageItem.getAuthor();
		}
		
		return convertView;
	}

}
