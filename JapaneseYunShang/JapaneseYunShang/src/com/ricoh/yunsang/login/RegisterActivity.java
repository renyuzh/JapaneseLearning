package com.ricoh.yunsang.login;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ricoh.R;
import com.ricoh.japanesestudy.ChooseNewCourceActivity;

/**
 * @author tan
 * @see 密码的加密还没实现
 * @see 当前数据为单用户 （需要增加多用用户的sharedpreference ）
 */
public class RegisterActivity extends Activity
{

	private SharedPreferences sp;
	private EditText loginMail;
	private EditText password;
	private EditText confirmPassword;
	public String username;
	private Calendar c;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.register_activity);

		// 登录邮箱
		loginMail = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		confirmPassword = (EditText) findViewById(R.id.editText3);
		Button regist = (Button) findViewById(R.id.button1);
		regist.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				if (checkRegistMessage() == true)
				{
					String username = loginMail.getText().toString();
					String userpassword = password.getText().toString();

					// 获得SharedPreferences实例对象

					sp = RegisterActivity.this.getSharedPreferences("thefirsttime", Context.MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putString("USER_NAME", username);
					editor.putString("PASSWORD", userpassword);
					editor.putString("COURSE", "@@");

					editor.putBoolean("SEVEN_SELECTED", true);

					editor.commit();
					// 提示注册成功
					username = sp.getString("USER_NAME", "");

					Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();

					c = Calendar.getInstance();
					intent.putExtra("username", username);
					intent.putExtra("startYear", c.get(Calendar.YEAR));
					intent.putExtra("startMonth", c.get(Calendar.MONTH));
					intent.putExtra("startDay", c.get(Calendar.DAY_OF_MONTH));
					intent.putExtra("first_register", 1);
					intent.putExtra("login_days", 1);
					

					intent.setClass(RegisterActivity.this, ChooseNewCourceActivity.class);
					startActivity(intent);

				}

			}

			private Boolean checkRegistMessage()
			{
				String mail = loginMail.getText().toString();
				// 检查是否是邮箱格式
				Boolean mailflag;
				String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
				Pattern regex = Pattern.compile(check);
				Matcher matcher = regex.matcher(mail);
				mailflag = matcher.matches();
				if (mailflag == false)
				{
					Toast.makeText(RegisterActivity.this, "请用正确的邮箱格式注册", Toast.LENGTH_SHORT).show();
					return false;
				}
				// 检查邮箱是否存在
				Boolean exist;
				// 需要向服务器发送请求检查用户名是否存在（还没实现）

				// 检查密码长度
				Boolean passwordflag;
				String reg = "[0-9]*";
				Pattern regex2 = Pattern.compile(reg);
				Matcher matcher2 = regex2.matcher(password.getText().toString());
				passwordflag = matcher2.matches();
				if (passwordflag == false)
				{
					Toast.makeText(RegisterActivity.this, "该密码不符合要求", Toast.LENGTH_SHORT).show();
					return false;

				}

				// 检查两次输入是否一致
				if (password.getText().toString().equals(confirmPassword.getText().toString()))
				{
					return true;
				} else
				{
					Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
					return false;
				}

			}
		});

	}

}
