package com.bbk.plugins.push;

import org.json.JSONException;
import org.json.JSONObject;

import io.yunba.android.manager.YunBaManager;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;


public class YunPushReceiver extends BroadcastReceiver {

	private final static String REPORT_MSG_SHOW_NOTIFICARION = "1000";
	private final static String REPORT_MSG_SHOW_NOTIFICARION_FAILED = "1001";
	public static Activity mActivity;
	
	private static int notifId = 0;
	@Override
	public void onReceive(Context context, Intent intent) {
		 if (YunBaManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
			
			String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
			String msg = intent.getStringExtra(YunBaManager.MQTT_MSG);
			
			try {
				
				JSONObject msgJsonObject = new JSONObject(msg);
				if(mActivity != null && msgJsonObject != null){
					String ns = Context.NOTIFICATION_SERVICE;
					NotificationManager mNotificationManager = (NotificationManager) mActivity.getSystemService(ns);
					CharSequence tickerText = msgJsonObject.getString("content");
					long when = System.currentTimeMillis();
					Context context2 = mActivity.getApplicationContext();
					//閼惧嘲褰囨惔鏃傛暏閸ョ偓鐖�
					int icon = context2.getResources().getIdentifier("icon", "drawable", context2.getPackageName());
					Notification notification = new Notification(icon, tickerText, when); 
					notification.flags = Notification.FLAG_AUTO_CANCEL; 
					notification.defaults = Notification.DEFAULT_ALL;
					CharSequence contentTitle = msgJsonObject.getString("title");
					CharSequence contentText = msgJsonObject.getString("content");
					Intent notificationIntent = new Intent(mActivity, mActivity.getClass());
					PendingIntent contentIntent = PendingIntent.getActivity(mActivity, ++notifId,
							notificationIntent, 0);
					notification.setLatestEventInfo(context2, contentTitle, contentText,
							contentIntent);
					mNotificationManager.notify(++notifId, notification);
				}
			} catch (Exception e) {
				Log.e("ERROR", e.getMessage());
			}
			Log.d("YUN", msg);



		     
		} else if(YunBaManager.PRESENCE_RECEIVED_ACTION.equals(intent.getAction())) {
			 //msg from presence.
			 String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);		
				String payload = intent.getStringExtra(YunBaManager.MQTT_MSG);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append("Received message presence: ").append(YunBaManager.MQTT_TOPIC)
						.append(" = ").append(topic).append(" ")
						.append(YunBaManager.MQTT_MSG).append(" = ").append(payload);
				Log.d("DemoReceiver", showMsg.toString());
				
		}

	}
	//send msg to MainActivity
//	private void processCustomMessage(Context context, Intent intent) {
//	
//			intent.setAction(MainActivity.MESSAGE_RECEIVED_ACTION);
//			intent.addCategory(context.getPackageName());
//			context.sendBroadcast(intent);
//		
//	}
}
