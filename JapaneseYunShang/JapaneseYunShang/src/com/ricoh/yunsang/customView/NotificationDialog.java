package com.ricoh.yunsang.customView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.ricoh.R;

public class NotificationDialog extends Dialog
{
	Context context;
	Button ok;
	Button clear;
	Button cancel;
	TimePicker timePiker;
	TextView tiemtext;
	SharedPreferences sp;
	Editor editor;
	Integer hour = 0;// 滚轴的小时
	Integer minute = 0;// 滚轴的分钟

	public NotificationDialog(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;

	}

	public NotificationDialog(Context context, int theme)
	{
		super(context, theme);
		this.context = context;
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_notify_dialog);
		initViews();

	}

	private void initViews()
	{
		sp = getContext().getSharedPreferences("dialogsettings", Context.MODE_PRIVATE);
		editor = sp.edit();

		ok = (Button) findViewById(R.id.button1);
		clear = (Button) findViewById(R.id.button2);
		cancel = (Button) findViewById(R.id.button3);
		timePiker = (TimePicker) findViewById(R.id.timePicker1);
		tiemtext = (TextView) findViewById(R.id.textView2);
		timePiker.setIs24HourView(true);
		String nownotifytime = sp.getString("nownotifytime", "00:00");
		String[] strarray = nownotifytime.split(":");
		if (strarray[0].equals("----"))
		{
			hour = 0;
			minute = 0;
		} else
		{
			hour = Integer.parseInt(strarray[0]);
			minute = Integer.parseInt(strarray[1]);
		}

		timePiker.setCurrentHour(hour);
		timePiker.setCurrentMinute(minute);

		tiemtext.setText(nownotifytime);

		hour = timePiker.getCurrentHour();
		minute = timePiker.getCurrentMinute();

		timePiker.setOnTimeChangedListener(new OnTimeChangedListener()
		{

			@Override
			public void onTimeChanged(TimePicker arg0, int arg1, int arg2)
			{
				hour = arg1;
				minute = arg2;
				String nownotifytime;
				if (minute < 10)
				{
					nownotifytime = hour.toString() + ":" + "0" + minute.toString();
				} else
				{
					nownotifytime = hour.toString() + ":" + minute.toString();
				}

				tiemtext.setText(nownotifytime);
			}
		});

		// ok按钮实现
		ok.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				String nownotifytime;
				if (minute < 10)
				{
					nownotifytime = hour.toString() + ":" + "0" + minute.toString();
				} else
				{
					nownotifytime = hour.toString() + ":" + minute.toString();
				}

				tiemtext.setText(nownotifytime);
				editor.putString("nownotifytime", tiemtext.getText().toString());
				editor.commit();
				NotificationDialog.this.cancel();

				// 用设定的时间建立Notification
				Intent intent = new Intent(context, SetNotifyTime.class);
				context.startService(intent);

			}

		});
		// clear按钮的事件(nofifition还没做)
		clear.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				tiemtext.setText("----:----");
				editor.putString("nownotifytime", tiemtext.getText().toString());
				editor.commit();

			}
		});
		// cancel按钮的事件（complete）
		cancel.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				NotificationDialog.this.cancel();
			}
		});

	}

}
