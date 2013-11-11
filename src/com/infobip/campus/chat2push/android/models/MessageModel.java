package com.infobip.campus.chat2push.android.models;

import java.util.Date;

public class MessageModel {
	
	private String author, text;
	private Date timestamp;
	
	public MessageModel (String author, String text, Date timestamp) {
		super ();
		this.author = author;
		this.text = text;
		this.timestamp = timestamp;
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
