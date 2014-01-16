package com.simulation.neteasy.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.simulation.neteasy.SplashActivity;
import com.zifeng.wangyi.R;
/**
 * 
 * @author 紫枫
 *
 */
public class NotifiUtil {

	public static void sendNotifi(Context context,String tickerText){
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon, tickerText, System.currentTimeMillis());
		Intent intent = new Intent(context, SplashActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 100, intent , PendingIntent.FLAG_ONE_SHOT & PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, "网易新闻", tickerText, contentIntent );
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		nm.notify(100, notification );
	}
}
