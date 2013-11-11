package com.infobip.campus.chat2push.android;

import java.util.ArrayList;
import java.util.List;

import com.infobip.campus.chat2push.android.models.ChannelModel;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<ChannelModel>{

	private ArrayList<ChannelModel> channelList;
	private Context context;

	public MyArrayAdapter(Activity context, int davaViewResourceId, List<ChannelModel> data) {
		super(context, davaViewResourceId, data);
		
		this.context = context;
		this.channelList = new ArrayList<ChannelModel>();
		this.channelList.addAll(data);		
	}
	
	private class ViewHolder {
		TextView textViewChannelName, textViewChannelDescription;
		CheckBox checkboxChannelStatus;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		
		//Log.v("ConvertView", String.valueOf(position));

		if (convertView == null) {
			LayoutInflater viewInflater = ((Activity) context).getLayoutInflater();;
			convertView = viewInflater.inflate(R.layout.list_channel_item, null);

			viewHolder = new ViewHolder();
			viewHolder.textViewChannelName = (TextView) convertView.findViewById(R.id.textViewChannelName);
			viewHolder.textViewChannelDescription = (TextView) convertView.findViewById(R.id.textViewChannelDescription);
			viewHolder.checkboxChannelStatus = (CheckBox) convertView.findViewById(R.id.checkBoxChannelStatus);
			convertView.setTag(viewHolder);

			// napravi listener za checkpoint
			viewHolder.checkboxChannelStatus.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					CheckBox checkBox = (CheckBox) v;
					ChannelModel channelItem = (ChannelModel) checkBox.getTag();
					channelItem.setStatus(checkBox.isChecked());
				}
			});
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// doda konkretne podatke u novi contentView
		ChannelModel channelItem = channelList.get(position);
		viewHolder.textViewChannelName.setText(channelItem.getName());
		viewHolder.textViewChannelDescription.setText(channelItem.getDescription());
		viewHolder.checkboxChannelStatus.setChecked(channelItem.getStatus());
		viewHolder.checkboxChannelStatus.setTag(channelItem);
		
		return convertView;
	}

}
