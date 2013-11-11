package com.infobip.campus.chat2push.android.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.infobip.campus.chat2push.android.configuration.Configuration;
import com.infobip.campus.chat2push.android.models.MessageModel;
import com.infobip.campus.chat2push.android.models.ChannelModel;

public class DefaultInfobipClient implements InfobipClient {

	private String getResponseText(HttpResponse response) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String responseText = new String();
		String line;
		while ((line = rd.readLine()) != null) {
			responseText += line;
		}

		return responseText;
	}

	@Override
	public boolean registerUser(String userName, String password) {
		Gson gson = new Gson();

		try {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("username", userName);
			jsonObject.addProperty("password", password);

			StringEntity parms = new StringEntity(gson.toJson(jsonObject));
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(Configuration.SERVER_LOCATION
					+ "user/register");
			request.addHeader("content-type", "application/json");
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
			String responseText = getResponseText(response);

			int responseCode = response.getStatusLine().getStatusCode();

			return responseCode == 200;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean loginUser(String userName, String password) {
		Gson gson = new Gson();

		try {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("username", userName);
			jsonObject.addProperty("password", password);

			StringEntity parms = new StringEntity(gson.toJson(jsonObject));
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(Configuration.SERVER_LOCATION
					+ "user/login");
			request.addHeader("content-type", "application/json");
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
			String responseText = getResponseText(response);

			int responseCode = response.getStatusLine().getStatusCode();

			return responseCode == 200;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public ArrayList<ChannelModel> fetchAllChannels(String userName) {
		Gson gson = new Gson();

		try {
			
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("username", userName);
			
			StringEntity parms = new StringEntity(gson.toJson(jsonObject));
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(Configuration.SERVER_LOCATION + "user/login");
			request.addHeader("content-type", "application/json");
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
			String responseText = getResponseText(response);
			
			int responseCode = response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public ArrayList<MessageModel> fetchAllMessages(ChannelModel channel, Date startTime, Date endTime) {
		return null;
	}

}
