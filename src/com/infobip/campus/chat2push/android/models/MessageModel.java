package com.infobip.campus.chat2push.android.models;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class MessageModel {
	
	private String author, text;
	private Date timestamp;
	
	public MessageModel (String author, String text, Date timestamp) {
		super ();
		this.author = author;
		this.text = text;
		this.timestamp = timestamp;
	}
	
	public MessageModel(JSONObject jsonObject) throws JSONException {
		super();
		this.author = jsonObject.getString("sent-by");
		this.text = jsonObject.getString("message");
		this.timestamp = new Date(jsonObject.getLong("time"));
	}
	
	public JSONObject getJSONObject () throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sent-by", author);
		jsonObject.put("message", text);
		jsonObject.put("time", timestamp.getTime());
		return jsonObject;	
	}
	
	public JSONObject getJSONObjectWithChannal (String chanalName) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sent-by", author);
		jsonObject.put("message", text);
		jsonObject.put("time", timestamp.getTime());
		jsonObject.put("chhanel", chanalName);
		return jsonObject;	
	}

	public String toString () {
		String string ="";
		string = author + ": " + text + "; " + timestamp;
		return string;
	}
	
	public boolean areYouOK () {
		boolean response = true;
		if (author == null || text == null || timestamp == null) {
			response = false;
			Log.e("Pogrešno napravljen model", "");
		}		
		return response;
	}
	
	public boolean equals (MessageModel secondMessage) {
	
		boolean response = false;
		
		if ( secondMessage.getAuthor().equals(author) &&
			 secondMessage.getText().equals(text) )
			response = true;
		
		return response;
		
	}
	
	public boolean timelyEquals (MessageModel secondMessage) {
		
		boolean response = false;
		
		if ( secondMessage.getAuthor().equals(author) &&
			 secondMessage.getText().equals(text) &&
			 secondMessage.getDate() == timestamp )
			response = true;
		
		return response;
		
	}
	
	public String getAuthor () {
		return author;
	}
	
	public String getText () {
		return text;
	}
	
	public String getDateAsString () {
		return timestamp.toString();
	}
	
	public Date getDate () {
		return timestamp;
	}
	
	public void setAuthor (String str) {
		author = str;
	}
	
	public void setText (String str) {
		text = str;
	}
	
	public void setDate (Date dat) {
		timestamp = dat;
	}
}
