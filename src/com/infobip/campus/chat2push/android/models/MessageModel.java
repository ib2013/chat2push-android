package com.infobip.campus.chat2push.android.models;

import java.util.Date;

public class MessageModel {
	
	private String author, text;
	private Date timestamp;
	
	public String getAuthor () {
		return author;
	}
	
	public String getText () {
		return text;
	}
	
	public String getDateAsString () {
		return timestamp.toString();
	}
}
