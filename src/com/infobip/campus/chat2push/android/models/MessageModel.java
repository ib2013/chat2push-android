package com.infobip.campus.chat2push.android.models;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

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
		this.timestamp = new Date(System.currentTimeMillis());
	}
	
	public JSONObject getJSONObject () throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sent-by", author);
		jsonObject.put("message", text);
		jsonObject.put("time", timestamp);
		return jsonObject;	
	}
	
	public JSONObject getJSONObjectWithChannal (String chanalName) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sent-by", author);
		jsonObject.put("message", text);
		jsonObject.put("time", timestamp);
		jsonObject.put("chhanel", chanalName);
		return jsonObject;	
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
