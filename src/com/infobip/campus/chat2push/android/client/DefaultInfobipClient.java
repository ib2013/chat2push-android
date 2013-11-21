package com.infobip.campus.chat2push.android.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infobip.campus.chat2push.android.configuration.Configuration;
import com.infobip.campus.chat2push.android.managers.SessionManager;
import com.infobip.campus.chat2push.android.models.ChannelModel;
import com.infobip.campus.chat2push.android.models.MessageModel;
import com.infobip.campus.chat2push.android.models.UserModel;

public class DefaultInfobipClient {

	public static String registerUser(String userName, String password,
			String phoneNumber) {
		Gson gson = new Gson();

		try {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("username", userName);
			jsonObject.addProperty("password", password);
			jsonObject.addProperty("phoneNumber", phoneNumber);

			StringEntity parms = new StringEntity(gson.toJson(jsonObject));
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(Configuration.SERVER_LOCATION
					+ "user/register");
			request.addHeader("content-type", "application/json");
			request.setEntity(parms);
			HttpResponse response = client.execute(request);

			String responseText = getResponseText(response);

			int responseCode = response.getStatusLine().getStatusCode();

			
			if (responseCode == 200 || responseCode == 201) {
				return null;
			} else if (responseCode == 476) {
				return "MISSING_REGISTRATION";
			}

			else {
				return responseText;
			}
		} catch (Exception e) {
			return "Connection error!";
		}
	}

	public static String loginUser(String userName, String password) {
		Gson gson = new Gson();

		try {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("username", userName);
			jsonObject.addProperty("password", password);
			
			Log.d("Saljem request...", userName+"|"+password);
			StringEntity parms = new StringEntity(gson.toJson(jsonObject));
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(Configuration.SERVER_LOCATION
					+ "user/login");
			request.addHeader("content-type", "application/json");
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
			String responseText = getResponseText(response);
			Log.d("Dobio odgovor:", responseText);
			int responseCode = response.getStatusLine().getStatusCode();

			Log.d("loginUser", responseText + " " + responseCode);
			
			if (responseCode == 200 || responseCode== 201) {
				return null;
			} else if (responseCode == 476) {
				return "MISSING_VERIFICATION";
			}

			else {
				return responseText;
			}
		} catch (Exception e) {
			Log.e("Exeption u getanju responsa za login:", e.getMessage());
			return "Connection error!";
		}
	}

	public static boolean verifyUser(String userName, int registrationCode) {
		Gson gson = new Gson();

		try {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("username", userName);
			jsonObject.addProperty("registrationCode", registrationCode);

			StringEntity parms = new StringEntity(gson.toJson(jsonObject));
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(Configuration.SERVER_LOCATION
					+ "user/verify");
			request.addHeader("content-type", "application/json");
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
			String responseText = getResponseText(response);
			Log.i("RESPONSE_TEXT", responseText);
			int responseCode = response.getStatusLine().getStatusCode();

			if (responseCode == 200 || responseCode == 201) {
				// Configuration.CURRENT_USER_NAME = userName;
				return true;
			} else {
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
					+ "channel/fetch/" + userName);
			HttpResponse response = client.execute(request);
			String responseText = getResponseText(response);

			Log.d("fetchAllChannels je dohvatio sljedeci popis", responseText);
			
			int responseCode = response.getStatusLine().getStatusCode();

			channelList = parseJsonChannelModel(responseText);

			return channelList;

		} catch (Exception e) {
			channelList = new ArrayList<ChannelModel>();

			return channelList;
		}
	}

	public static ArrayList<MessageModel> fetchAllMessages(String channelName,
			Date startTime, Date endTime) {
		Log.d("Trenutni korisnik je: ", SessionManager.getCurrentUserName());
		Log.d("Start time ", "" + startTime.getTime());
		Log.d("End time ", "" + endTime.getTime());
		Gson gson = new Gson();
		ArrayList<MessageModel> messageList;

		try {
			HttpClient client = new DefaultHttpClient();
			
			Log.d("U fetchAllMessages ide na:", Configuration.SERVER_LOCATION
					+ "message/fetch/" + SessionManager.getCurrentUserName()
					+ "/" + channelName.replaceAll(" ", "%20") + "/" + startTime.getTime() + "/"
					+ endTime.getTime() );
			HttpGet request = new HttpGet(Configuration.SERVER_LOCATION
					+ "message/fetch/" + SessionManager.getCurrentUserName()
					+ "/" + channelName.replaceAll(" ", "%20") + "/" + startTime.getTime() + "/"
					+ endTime.getTime());

			Log.d("Request ", request.getURI().toString());

			HttpResponse response = client.execute(request);
			String responseText = getResponseText(response);

			Log.d("responseText: ", responseText);

			int responseCode = response.getStatusLine().getStatusCode();

			messageList = parseJsonMessageModel(responseText);

			return messageList;

		} catch (Exception e) {
			return new ArrayList<MessageModel>();
		}
	}
	
	public static ArrayList<UserModel> fetchKnownUsers ( String userName) {
		
		ArrayList<String> stringArray = null;
		ArrayList<ChannelModel> channels = new ArrayList<ChannelModel>();
		Set<UserModel> userNamesSet = new HashSet<UserModel>();
		
		channels.addAll(fetchAllChannels(userName));
		
		Log.d("fetchKnownUsers ce pokupiti usere iz sljedecih soba:", channels.toString());
		
		for (ChannelModel channel : channels) {
			
			Gson gson = new Gson();

			try {

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("name", channel.getName());
				jsonObject.addProperty("description", "");

				StringEntity parms = new StringEntity(gson.toJson(jsonObject));
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(Configuration.SERVER_LOCATION
						+ "channel/fetchUsersByRoom");
				Log.d("-------",channel.getName());
				Log.d("-----",request.getURI().toString());
				request.addHeader("content-type", "application/json");
				request.setEntity(parms);
				
				HttpResponse response = client.execute(request);
				String responseText = getResponseText(response);

				userNamesSet.addAll(parseJsonUserNames(responseText));
				
				int responseCode = response.getStatusLine().getStatusCode();

			} catch (Exception e) {
				Log.e("Exception fetchKnownUsers", e.getMessage());
//				return "Connection error!";
			}
			
		}
		
		Log.d("client pred kraj rada ima set ovaj: ", userNamesSet.toString());
		ArrayList<UserModel> response = new ArrayList<UserModel>(userNamesSet);
		
		Log.d("fetchKnownUsers mi je kao rezultat pokusao uvaliti", response.toString());
		
		return response;
		
	}

	public static String sendMessage(String userName, String channelName,
			String messageText) {

		Gson gson = new Gson();

		try {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("username", userName);
			jsonObject.addProperty("channel", channelName);
			jsonObject.addProperty("messageText", messageText);

			StringEntity parms = new StringEntity(gson.toJson(jsonObject));
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(Configuration.SERVER_LOCATION
					+ "message/send");
			request.addHeader("content-type", "application/json");
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
			String responseText = getResponseText(response);

			int responseCode = response.getStatusLine().getStatusCode();

			if (responseCode == 200 || responseCode == 201) {
				Log.d("U sendMessage javio status: ", String.valueOf(responseCode));
				return null;
			} else {
				Log.e("U sendMessage javio status: ", String.valueOf(responseCode));
				Log.e("U sendMessage javio status: ", responseText);
				return responseText;
			}
		} catch (Exception e) {
			Log.e("U sendMessage izbacio exception: ", e.getMessage());
			return "Connection error!";
		}
	}
	
	public static boolean registerUserToChannel(String userName, String channelName){
		//username, chanel (za JSON); channel/addUserToRoom
		
		Gson gson = new Gson();

		try {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("username", userName);
			jsonObject.addProperty("channel", channelName);

			StringEntity parms = new StringEntity(gson.toJson(jsonObject));
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(Configuration.SERVER_LOCATION
					+ "channel/addUserToRoom");
			
			Log.d("DefaultInfobipClient.registerUserToChannel:", request.getURI().toString());
			Log.d("DefaultInfobipClient.registerUserToChannel:", jsonObject.get("username").getAsString() + "aha! " + jsonObject.toString());
			
			
			request.addHeader("content-type", "application/json");
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
			String responseText = getResponseText(response);

			int responseCode = response.getStatusLine().getStatusCode();
			
			Log.d("DefaultInfobipClient.registerUserToChannel je obavio http pricu sa odgovorom:", responseText);
			
			if (responseCode == 200 || responseCode == 201) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			Log.e("DefaultInfobipClient.registerUserToChannel je imao exception", e.getMessage());
			return false;
		}

	}
	
	public static boolean unregisterUserFromChannel(String userName, String channelName){
		//username, chanel (za JSON); channel/addUserToRoom
		
		Gson gson = new Gson();

		try {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("username", userName);
			jsonObject.addProperty("channel", channelName);

			StringEntity parms = new StringEntity(gson.toJson(jsonObject));
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(Configuration.SERVER_LOCATION
					+ "channel/removeUserFromRoom");
			request.addHeader("content-type", "application/json");
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
			String responseText = getResponseText(response);

			int responseCode = response.getStatusLine().getStatusCode();

			if (responseCode == 200 || responseCode == 201) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}
	
	public static boolean createNewRoom(String name, String description, Boolean isPrivate){
		Gson gson = new Gson();

		try {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("name", name);
			jsonObject.addProperty("description", description);
			jsonObject.addProperty("isPublic", (!isPrivate));

			StringEntity parms = new StringEntity(gson.toJson(jsonObject));
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(Configuration.SERVER_LOCATION
					+ "channel/add");
			request.addHeader("content-type", "application/json");
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
			String responseText = getResponseText(response);

			int responseCode = response.getStatusLine().getStatusCode();

			if (responseCode == 200 || responseCode == 201) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	public void resendConfirmationNumber(String username){
		Gson gson = new Gson();

		try {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("username", username);

			StringEntity parms = new StringEntity(gson.toJson(jsonObject));
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(Configuration.SERVER_LOCATION
					+ "user/resendCode");
			request.addHeader("content-type", "application/json");
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
			String responseText = getResponseText(response);

			int responseCode = response.getStatusLine().getStatusCode();
		} catch (Exception e) {
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
			boolean isPublic;

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
						"subscribed").getAsBoolean();
			} catch (Exception e) {
				isUserSubscribedToChannel = false;
			}
			
			try {
				isPublic = jsonElement.getAsJsonPrimitive(
						"public").getAsBoolean();
			} catch (Exception e) {
				isPublic = false;
			}
			
			Log.d("Parsirani channel model: ", (new ChannelModel(channelName,
					channelDescription, isUserSubscribedToChannel)).toString());
			channelList.add(new ChannelModel(channelName, channelDescription,
					isUserSubscribedToChannel, isPublic));
		}

		return channelList;
	}
	
	private static Set<UserModel> parseJsonUserNames(String jsonResponse) {
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonTree = jsonParser.parse(jsonResponse);
		JsonArray jsonArray = jsonTree.getAsJsonArray();

		Set<UserModel> result = new HashSet<UserModel>();

		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonElement = jsonArray.get(i).getAsJsonObject();
			String userName;

			try {
				userName = jsonElement.getAsJsonPrimitive("username").getAsString();
			} catch (Exception e) {
				Log.e("srfgl ihgs jkgd", "adèfk jgdjfkghsujkd");
				userName = "";
			}
			Log.d("Jason parser nam je izbacio", userName);
			result.add(new UserModel(userName, false));
		}
		Log.d("Sveckupa jason je izbacio:", result.toString());
		return result;

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
				messageText = jsonElement.getAsJsonPrimitive("message")
						.getAsString();
			} catch (Exception e) {
				messageText = "";
			}
			try {
				sentBy = jsonElement.getAsJsonPrimitive("username").getAsString();
			} catch (Exception e) {
				sentBy = "";
			}

			try {
				time = new Date(jsonElement.getAsJsonPrimitive(
						"messageDate").getAsLong());
			} catch (Exception e) {
				Log.e("Jason parser u clientu je failao procitati vrijeme", "");
				time = new Date(0);
			}
			
			MessageModel newMessageToAdd = new MessageModel(sentBy, messageText, time);
			if (newMessageToAdd.areYouOK()) 
				messageList.add(new MessageModel(sentBy, messageText, time));
		}

		for (MessageModel messageItem : messageList)
			Log.d("Parsirani responseText: ", messageItem.toString());

		return messageList;
	}

	private static String getResponseText(HttpResponse response)
			throws IOException {
		try {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String responseText = new String();
		String line;
		while ((line = rd.readLine()) != null) {
			responseText += line;
		}

		return responseText;
		} catch (Exception e) {
			Log.e("On NE ÈITA!", e.getMessage());
			return "[]";
		}
	}
}
