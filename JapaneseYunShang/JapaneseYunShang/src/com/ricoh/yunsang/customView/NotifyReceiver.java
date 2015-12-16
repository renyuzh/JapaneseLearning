package com.ricoh.yunsang.customView;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore.Audio;
import android.util.Log;
import android.widget.Toast;

import com.ricoh.R;
import com.ricoh.yunsang.login.LoginActivity;

public class NotifyReceiver extends BroadcastReceiver
{
	static final int NOTIFICATION_ID = 0x123;
	private static final int HELLO = 1;

	@Override
	public void onReceive(Context context, Intent arg1)
	{

		Notification.Builder builder = new Notification.Builder(context);

		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setTicker("背单词时间到了~~~");
		builder.setContentTitle("设置时间到了");
		builder.setContentText("赶快进入云裳开启学习吧~");
		builder.setWhen(System.currentTimeMillis());
		builder.setAutoCancel(true);
//		builder.setDefaults(Notification.DEFAULT_ALL);
		builder.setVibrate(new long[] {0,300,500,700});
//		builder.setSound(Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "5"));
		builder.setDefaults(Notification.DEFAULT_SOUND);

		Intent notificationIntent = new Intent(context, LoginActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(contentIntent);

		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		nm.notify(HELLO, builder.getNotification());

		Toast.makeText(context, "闹铃响了, 可以做点事情了~~", Toast.LENGTH_LONG).show();
		Log.i("tan", "the notifition is ok");
	}
}
