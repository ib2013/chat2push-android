package com.infobip.campus.chat2push.android;


import com.infobip.campus.chat2push.android.adapters.MyApplication;
import com.infobip.campus.chat2push.android.models.MessageModel;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.infobip.push.AbstractPushReceiver;
import com.infobip.push.PushNotification;
import com.infobip.push.PushNotificationBuilder;




public class MyPushReceiver extends AbstractPushReceiver {
	
	//private Context context;
	
	public interface CallbackInterface {
		void addNewMessage (MessageModel newMessage);
	}
	
	@Override
    public void onRegistered(Context context) {
        Toast.makeText(context, "Successfully registered.", Toast.LENGTH_SHORT).show();
       
    }
    @Override
    protected void onRegistrationRefreshed(Context context) {
        Toast.makeText(context, "Registration is refreshed.", Toast.LENGTH_SHORT).show();
    }
    
    NotificationCompat.Builder mBuilder;
    
    @Override
    public void onNotificationReceived(PushNotification notification, Context context) {

    		//Nova poruka
    		MessageModel newMessage = new MessageModel("", "", new Date(System.nanoTime()));
    		//Kanal s kojeg je poruka došla!
    		String channel = "";
    		
    		//Parsiranje notification messagea:
    		String payload = notification.getMessage();
    		if (notification.getTitle().toString().equals("MESSAGE"))
    			try {
    				JSONObject json = new JSONObject(payload);
    				newMessage.setAuthor(json.getString("sent-by"));
    				newMessage.setText(json.getString("message"));
    				//TODO: smisliti što da se napravi s datumom!
    				//newMessage.setDate(new Date(Long.getLong(json.getString("time"))));
    				Toast.makeText(context, json.getString("time"), Toast.LENGTH_LONG).show();
    				channel = json.getString("channel");
    			} catch (JSONException e) {
    				Toast.makeText(context, "Error reading push message. Details: " + e.getMessage(), Toast.LENGTH_LONG).show();
    				e.printStackTrace();
    			}
    		else
    			Toast.makeText(context, "Received push notification was not a message! :/ \n I don't know what to do with it." , Toast.LENGTH_LONG).show();
    		
    		if (MyApplication.getCurrentActivity() != null) {
    			//if (MyApplication.getCurrentActivity().getTitle().equals(channel)) {
    				CallbackInterface callbackInterface = (CallbackInterface) MyApplication.getCurrentActivity();
    				callbackInterface.addNewMessage(newMessage);
    			//} 
    		} else {
    			
    			mBuilder = new NotificationCompat.Builder(context)
                	.setSmallIcon(R.drawable.ic_launcher)
                	.setContentTitle("New message from " + newMessage.getAuthor())
                	.setContentText(newMessage.getText());
        
    			Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
    			notificationIntent.setData(Uri.parse(notification.getUrl().toString()));
    			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);   
    			mBuilder.setContentIntent(pendingIntent);
    			mBuilder.setAutoCancel(true);
        
    			Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    			mBuilder.setSound(soundUri);
    			long[] vibraPattern = {0, 500, 250, 500 };
    			mBuilder.setVibrate(vibraPattern);
    			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    			Date d = new Date();
    			mNotificationManager.notify(notification.getId().toString()+d.getTime(), notification.getNotificationId(), mBuilder.build());
    		}
    		
    }
    
    private NotificationManager getSystemService(String notificationService) {
		// TODO Auto-generated method stub
		return null;
	}
    
	@Override
    protected void onNotificationOpened(PushNotification notification, Context context) {
        Toast.makeText(context, "Notification opened.", Toast.LENGTH_LONG).show();        
    }
	
    @Override
    public void onUnregistered(Context context) {
        Toast.makeText(context, "Successfully unregistered.", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onError(int reason, Context context) {
        Toast.makeText(context, "Error occurred." + reason, Toast.LENGTH_SHORT).show();
	}

}
