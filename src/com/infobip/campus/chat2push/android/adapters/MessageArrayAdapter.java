package com.infobip.campus.chat2push.android.adapters;

import java.util.ArrayList;
import java.util.List;

import com.infobip.campus.chat2push.android.MainActivity;
import com.infobip.campus.chat2push.android.R;
import com.infobip.campus.chat2push.android.R.id;
import com.infobip.campus.chat2push.android.R.layout;
import com.infobip.campus.chat2push.android.configuration.Configuration;
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
import android.widget.Toast;

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
		
		MessageModel currentMessageItem = messageList.get(position);

		if (convertView == null) {
			LayoutInflater viewInflater = ((Activity) context).getLayoutInflater();
			
			if (currentMessageItem.getAuthor().equals(Configuration.CURRENT_USER_NAME)) {
				convertView = viewInflater.inflate(R.layout.list_message_right_item, null);
				Toast.makeText(getContext(), currentMessageItem.getAuthor(), Toast.LENGTH_SHORT).show();
			}
			else
				convertView = viewInflater.inflate(R.layout.list_message_left_item, null);

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
		viewHolder.textViewAuthor.setText(currentMessageItem.getAuthor());
		viewHolder.textViewText.setText(currentMessageItem.getText());
		viewHolder.textViewTime.setText(currentMessageItem.getDateAsString());
		//TODO: rijesiti ikone usera!
		viewHolder.imageVIewAuthorIcon.setBackgroundColor(Color.rgb((currentMessageItem.getAuthor().charAt(0)*5)%200+50, (currentMessageItem.getAuthor().charAt(1)*6)%200+50, (currentMessageItem.getAuthor().charAt(2)*7)%200+50));
		return convertView;
	}

}
