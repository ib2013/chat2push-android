package com.infobip.campus.chat2push.android.models;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class MessageModel extends Object {
	
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
		if (author.charAt(0) == ' ') {
			response = false;
			Log.e("Autor ima razmak na pocetku, radi se o sending poruci ", this.toString());
		}
		return response;
	}
	
//	public boolean equals (MessageModel secondMessage) {
//	
//		boolean response = false;
//		
//		if ( secondMessage.getAuthor().equals(this.author) &&
//			 secondMessage.getText().equals(this.text) )
//			response = true;
//		
//		return response;
//		
//	}
	
	
	
	public boolean timelyEquals (MessageModel secondMessage) {
		
		boolean response = false;
		
		if ( secondMessage.getAuthor().equals(author) &&
			 secondMessage.getText().equals(text) &&
			 secondMessage.getDate() == timestamp )
			response = true;
		
		return response;
		
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageModel other = (MessageModel) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
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
