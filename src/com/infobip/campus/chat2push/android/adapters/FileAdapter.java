package com.infobip.campus.chat2push.android.adapters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.infobip.campus.chat2push.android.models.MessageModel;

public class FileAdapter {

	public static void writeToFile (Context context, String filename, ArrayList<MessageModel> messageList) {
		JSONArray jsonArray = new JSONArray();
        try {
        	for (MessageModel message : messageList) 
        		if (message.areYouOK()) 
        			jsonArray.put(message.getJSONObject());
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename + ".txt", Context.MODE_PRIVATE));
	        outputStreamWriter.write(jsonArray.toString());
	        outputStreamWriter.close();
	        
	        Log.d("Writing to file"+filename + ".txt"+": ", jsonArray.toString());
	    }
	    catch (IOException e) {
	        Log.e("Exception", "IOException: " + e.toString());
	    } catch (JSONException e) {
	    	Log.e("Exception", "JSONException: " + e.toString());
			e.printStackTrace();
		}
	}
	
	public static ArrayList<MessageModel> readFromFIle (Context context, String filename) {
		
		ArrayList<MessageModel> messageList = new ArrayList<MessageModel>();
		String messageListString = "";
		InputStream inputStream;
		try {
			inputStream = context.openFileInput(filename + ".txt");
			Log.d("Èitam file pod nazivom", filename + ".txt");
			if ( inputStream != null ) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String bufferString = "";
				StringBuilder stringBuilder = new StringBuilder();
				while ( (bufferString = bufferedReader.readLine()) != null ) {
					stringBuilder.append(bufferString);
				}
				inputStream.close();
				messageListString = stringBuilder.toString();
				Log.d("Zatvorio stream, sad kod sebe imam:", messageListString);
				JSONArray jsonArray = new JSONArray(messageListString);
				for (int i = 0; i < jsonArray.length(); ++i) {
					messageList.add(new MessageModel (jsonArray.getJSONObject(i)));
				}
			}
		} catch (FileNotFoundException e) {
			messageList = new ArrayList<MessageModel>();
			Log.e("Exception","FileNotFoundException: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			messageList = new ArrayList<MessageModel>();
			Log.e("Exception","IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			messageList = new ArrayList<MessageModel>();
			Log.e("Exception","JSONException: " + e.getMessage());
			e.printStackTrace();
		}
		
        return messageList;
	}
	
}
