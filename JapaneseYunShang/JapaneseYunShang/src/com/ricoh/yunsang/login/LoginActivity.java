package com.ricoh.yunsang.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ricoh.R;
import com.ricoh.japanesestudy.ChooseNewCourceActivity;
import com.ricoh.slidemenutest.MainActivity;
import com.ricoh.yunsang.DBService.WordService;

public class LoginActivity extends Activity
{

	private EditText userName, password;
	private CheckBox rem_pw, auto_login;
	private Button btn_login;
	RelativeLayout btn_regist;
	private String userNameValue, passwordValue;
	private SharedPreferences sp;
	
	int exit_login;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		WordService.initialize();

		setContentView(R.layout.activity_login);

		// 获得实例对象
		sp = this.getSharedPreferences("thefirsttime", Context.MODE_PRIVATE);
		userName = (EditText) findViewById(R.id.et_name);
		password = (EditText) findViewById(R.id.et_key);
		rem_pw = (CheckBox) findViewById(R.id.cb_mima);
		auto_login = (CheckBox) findViewById(R.id.cb_auto);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_regist = (RelativeLayout) findViewById(R.id.btn_register);
		sp.edit().putBoolean("AUTO_ISCHECK", sp.getBoolean("AUTO_ISCHECK", false)).commit();
		sp.edit().putBoolean("ISCHECK", sp.getBoolean("ISCHECK", false)).commit();

		exit_login = getIntent().getIntExtra("exit_login",1);
		
		// 判断记住密码多选框的状态
		if (sp.getBoolean("ISCHECK", false))
		{
			// 设置默认是记录密码状态
			rem_pw.setChecked(true);
			userName.setText(sp.getString("USER_NAME", ""));
			password.setText(sp.getString("PASSWORD", ""));
			
			if(exit_login == 1){
				// 判断自动登陆多选框状态
				if (sp.getBoolean("AUTO_ISCHECK", false))
				{
					// 设置默认是自动登录状态
					auto_login.setChecked(true);
					// 跳转界面
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.putExtra("login_days", 1);
					intent.putExtra("username", userName.getText().toString());
					LoginActivity.this.startActivity(intent);

				}
			}
			
		}

		// 登录监听事件
		btn_login.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				userNameValue = userName.getText().toString();
				passwordValue = password.getText().toString();

				String username = sp.getString("USER_NAME", "@@");
				String password = sp.getString("PASSWORD", "@@");

				if (userNameValue.equals(username) && passwordValue.equals(password))
				{
					Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

					String course = sp.getString("COURSE", "@@");
					if (course.equals("@@"))
					{
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this, ChooseNewCourceActivity.class);
						intent.putExtra("login_days", 1);
						intent.putExtra("username", userName.getText().toString());
						startActivity(intent);
					} else
					{

						// 跳转界面
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						intent.putExtra("login_days", 1);
						intent.putExtra("username", userName.getText().toString());
						LoginActivity.this.startActivity(intent);

					}
				} else
				{

					Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show();
				}

			}
		});
		btn_regist.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);

			}
		});

		// 监听记住密码多选框按钮事件
		rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (rem_pw.isChecked())
				{

					System.out.println("记住密码已选中");
					sp.edit().putBoolean("ISCHECK", true).commit();

				} else
				{

					System.out.println("记住密码没有选中");
					sp.edit().putBoolean("ISCHECK", false).commit();

				}

			}
		});

		// 监听自动登录多选框事件
		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (auto_login.isChecked())
				{
					System.out.println("自动登录已选中");
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

				} else
				{
					System.out.println("自动登录没有选中");
					sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		// TODO Auto-generated method stub
		switch (keyCode)
		{

		case KeyEvent.KEYCODE_BACK:

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
			intent.addCategory(Intent.CATEGORY_HOME);
			this.startActivity(intent);
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
