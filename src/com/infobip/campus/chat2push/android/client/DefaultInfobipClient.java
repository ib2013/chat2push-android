package com.infobip.campus.chat2push.android.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infobip.campus.chat2push.android.configuration.Configuration;
import com.infobip.campus.chat2push.android.models.ChannelModel;
import com.infobip.campus.chat2push.android.models.MessageModel;

public class DefaultInfobipClient {

	public static boolean registerUser(String userName, String password) {
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

			return responseCode == 201;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean loginUser(String userName, String password) {
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

			if (responseCode == 201){
				Configuration.CURRENT_USER_NAME = userName;
				return true;
			}
			else{
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static ArrayList<ChannelModel> fetchAllChannels(String userName) {
		Gson gson = new Gson();
		ArrayList<ChannelModel> channelList;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(Configuration.SERVER_LOCATION
					+ "channel2/fetch"); // + userName
			request.addHeader("content-type", "application/json");
			HttpResponse response = client.execute(request);
			String responseText = getResponseText(response);

			int responseCode = response.getStatusLine().getStatusCode();

			channelList = parseJsonChannelModel(responseText);

			return channelList;

		} catch (Exception e) {
			return new ArrayList<ChannelModel>();
		}
	}

	public static ArrayList<MessageModel> fetchAllMessages(
			ChannelModel channel, Date startTime, Date endTime) {

		Gson gson = new Gson();
		ArrayList<MessageModel> messageList;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(Configuration.SERVER_LOCATION
					+ "message/fetch/username=" + Configuration.CURRENT_USER_NAME + "&channel="
					+ channel.getName() + " &start-time="
					+ startTime.toString() + "&end-time=" + endTime.toString());
			HttpResponse response = client.execute(request);
			String responseText = getResponseText(response);

			int responseCode = response.getStatusLine().getStatusCode();

			messageList = parseJsonMessageModel(responseText);

			return messageList;

		} catch (Exception e) {
			return new ArrayList<MessageModel>();
		}
	}

	private static ArrayList<ChannelModel> parseJsonChannelModel(
			String jsonResponse) {
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonTree = jsonParser.parse(jsonResponse);
		JsonArray jsonArray = jsonTree.getAsJsonArray();

		ArrayList<ChannelModel> channelList = new ArrayList<ChannelModel>();

		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonElement = jsonArray.get(i).getAsJsonObject();
			String channelName;
			String channelDescription;
			boolean isUserSubscribedToChannel;

			try {
				channelName = jsonElement.getAsJsonPrimitive("name")
						.getAsString();
			} catch (Exception e) {
				channelName = "";
			}
			try {
				channelDescription = jsonElement.getAsJsonPrimitive(
						"description").getAsString();
			} catch (Exception e) {
				channelDescription = "";
			}

			try {
				isUserSubscribedToChannel = jsonElement.getAsJsonPrimitive(
						"isSubscribed").getAsBoolean();
			} catch (Exception e) {
				isUserSubscribedToChannel = false;
			}

			channelList.add(new ChannelModel(channelName, channelDescription,
					isUserSubscribedToChannel));
		}

		return channelList;
	}

	private static ArrayList<MessageModel> parseJsonMessageModel(
			String jsonResponse) {
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonTree = jsonParser.parse(jsonResponse);
		JsonArray jsonArray = jsonTree.getAsJsonArray();

		ArrayList<MessageModel> messageList = new ArrayList<MessageModel>();

		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonElement = jsonArray.get(i).getAsJsonObject();
			String messageText;
			String sentBy;
			Date time;
			
			boolean isUserSubscribedToChannel;

			try {
				messageText = jsonElement.getAsJsonPrimitive("message-text")
						.getAsString();
			} catch (Exception e) {
				messageText = "";
			}
			try {
				sentBy = jsonElement.getAsJsonPrimitive(
						"sent-by;").getAsString();
			} catch (Exception e) {
				sentBy = "";
			}

			try {
				time = new Date(jsonElement.getAsJsonPrimitive(
						"time").getAsString());
			} catch (Exception e) {
				time = new Date(0);
			}

			messageList.add(new MessageModel(sentBy,messageText,time));
		}

		return messageList;
	}

	private static String getResponseText(HttpResponse response)
			throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String responseText = new String();
		String line;
		while ((line = rd.readLine()) != null) {
			responseText += line;
		}

		return responseText;
	}
}
