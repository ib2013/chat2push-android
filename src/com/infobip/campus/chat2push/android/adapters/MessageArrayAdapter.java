package com.infobip.campus.chat2push.android.adapters;

import java.util.ArrayList;
import java.util.List;

import com.infobip.campus.chat2push.android.R;
import com.infobip.campus.chat2push.android.R.id;
import com.infobip.campus.chat2push.android.R.layout;
import com.infobip.campus.chat2push.android.models.MessageModel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageArrayAdapter extends ArrayAdapter<MessageModel>{

	private ArrayList<MessageModel> messageList;
	private Context context;

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
		
		//Log.v("ConvertView", String.valueOf(position));

		if (convertView == null) {
			LayoutInflater viewInflater = ((Activity) context).getLayoutInflater();;
			convertView = viewInflater.inflate(R.layout.list_message_item, null);

			viewHolder = new ViewHolder();
			viewHolder.textViewAuthor = (TextView) convertView.findViewById(R.id.textViewMessageAuthor);
			viewHolder.textViewText = (TextView) convertView.findViewById(R.id.textViewMessageText);
			viewHolder.textViewTime = (TextView) convertView.findViewById(R.id.textViewMessageTime);
			viewHolder.imageVIewAuthorIcon = (ImageView) convertView.findViewById(R.id.imageViewMessageAutorIcon);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// doda konkretne podatke u novi contentView
		MessageModel messageItem = messageList.get(position);
		viewHolder.textViewAuthor.setText(messageItem.getAuthor());
		viewHolder.textViewText.setText(messageItem.getText());
		viewHolder.textViewTime.setText(messageItem.getDateAsString());
		//TODO: rijesiti ikone usera!
		viewHolder.imageVIewAuthorIcon.setBackgroundColor(Color.rgb((messageItem.getAuthor().charAt(0)*5)%255, (messageItem.getAuthor().charAt(1)*6)%255, (messageItem.getAuthor().charAt(2)*7)%255));
		return convertView;
	}

}
