package com.ricoh.yunsang.customView;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class SetNotifyTime extends Service
{

	private int mMinute = 0;
	private int mHour = 0;

	@Override
	public IBinder onBind(Intent arg0)
	{

		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Intent intent2 = new Intent(SetNotifyTime.this, NotifyReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(SetNotifyTime.this, 0, intent2, 0);
		SharedPreferences sp = getSharedPreferences("dialogsettings", MODE_APPEND);
		String setTime = sp.getString("nownotifytime", "00:00");
		if (setTime.equals("----:----"))
		{

		} else
		{
			String[] str = setTime.split(":");
			mHour = Integer.parseInt(str[0]);
			mMinute = Integer.parseInt(str[1]);

			long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)

			Calendar calendar = Calendar.getInstance();

			calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然会有8个小时的时间差
			calendar.set(Calendar.MINUTE, mMinute);
			calendar.set(Calendar.HOUR_OF_DAY, mHour);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			// 选择的每天定时时间
			long selectTime = calendar.getTimeInMillis();
			long systemTime = System.currentTimeMillis();
			// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
			if (systemTime > selectTime)
			{
				Toast.makeText(SetNotifyTime.this, "设置的时间小于当前时间，明天会提醒你背单词的哟", Toast.LENGTH_SHORT).show();
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				selectTime = calendar.getTimeInMillis();
			}

			// 计算现在时间到设定时间的时间差
			long time = selectTime - systemTime;
			firstTime += time;

			// 进行闹铃注册
			AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

			manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
			Log.i("tan", "time ==== " + time + ", selectTime ===== " + selectTime + ", systemTime ==== " + systemTime + ", firstTime === " + firstTime);

			Toast.makeText(SetNotifyTime.this, "设置背单词提醒成功! ", Toast.LENGTH_LONG).show();
		}

		return super.onStartCommand(intent, flags, startId);

	}

	public void setNotificationtime()
	{

	}

}
