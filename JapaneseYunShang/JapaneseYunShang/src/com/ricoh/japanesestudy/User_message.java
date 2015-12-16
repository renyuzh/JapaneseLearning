package com.ricoh.japanesestudy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ricoh.R;
import com.ricoh.slidemenutest.MainActivity;
import com.ricoh.yunsang.login.LoginActivity;

public class User_message extends Activity
{
	SharedPreferences sp;
	String dictionaryName;
	String userName;
	
	TextView user_name;
	TextView user_lv;
	TextView user_time;
	Button user_exit;
	
	int startYear;
	int startMonth;
	int startDay;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.persona_lcenter);
		
		if(getIntent() != null && getIntent().getExtras() != null){
			userName = getIntent().getExtras().getString("username");
			sp = getSharedPreferences("thefirsttime", MODE_PRIVATE);
			dictionaryName = sp.getString("COURSE", "");
		}
		
		user_name = (TextView)findViewById(R.id.user_activity_button1);
		user_lv = (TextView)findViewById(R.id.user_show_level);
		user_time = (TextView)findViewById(R.id.user_message5);
		user_exit = (Button)findViewById(R.id.user_button);
		
		startYear = getIntent().getIntExtra("startYear",2014);
		startMonth = getIntent().getIntExtra("startMonth",01);
		startDay = getIntent().getIntExtra("startDay",01);
		
		user_name.setText(userName);
		user_lv.setText("Lv1");
		user_time.setText(startYear + "Äê" + startMonth + "ÔÂ" + startDay + "ÈÕ");
		
		user_exit.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(User_message.this, LoginActivity.class);
				intent.putExtra("exit_login", 0);
				cancleLogin();
				startActivity(intent);
			}
		});
	}
	
	public void cancleLogin()
	{
		sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		if (sp.getBoolean("ISCHECK", true))
		{

			sp.edit().putBoolean("AUTO_ISCHECK", false).commit();

		}

	}

}
