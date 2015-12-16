package com.ricoh.japanesestudy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.ricoh.R;
import com.ricoh.slidemenutest.MainActivity;
import com.ricoh.yunsang.customView.DifficultDialog;
import com.ricoh.yunsang.customView.NotificationDialog;
import com.ricoh.yunsang.customView.TimerDialog;

public class Setting extends Activity implements View.OnClickListener
{
	Button sns_share, study_remind, difficult_setting, limited_setting, feedback_btn, check_new_version, about_us_btn;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		findViews();

	}

	private void findViews()
	{

		this.sns_share = (Button) findViewById(R.id.sns_share);
		this.study_remind = (Button) findViewById(R.id.study_remind);
		this.difficult_setting = (Button) findViewById(R.id.difficult_setting);
		this.limited_setting = (Button) findViewById(R.id.limited_setting);
		this.feedback_btn = (Button) findViewById(R.id.feedback_btn);
		this.check_new_version = (Button) findViewById(R.id.check_new_version);
		this.about_us_btn = (Button) findViewById(R.id.about_us_btn);

		this.sns_share.setOnClickListener((OnClickListener) this);
		this.study_remind.setOnClickListener((OnClickListener) this);
		this.difficult_setting.setOnClickListener((OnClickListener) this);
		this.limited_setting.setOnClickListener((OnClickListener) this);
		this.feedback_btn.setOnClickListener((OnClickListener) this);
		this.check_new_version.setOnClickListener((OnClickListener) this);
		this.about_us_btn.setOnClickListener((OnClickListener) this);

	}

	public void onClick(View paramView)
	{
		switch (paramView.getId())
		{
		case R.id.sns_share:

			showShare();

			break;
		case R.id.study_remind:

			Dialog dialog = new NotificationDialog(Setting.this);
			// dialog.setTitle("设置提醒时间");
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
			dialog.show();
			WindowManager windowManager = getWindowManager();
			WindowManager.LayoutParams windowParams = dialog.getWindow().getAttributes();
			Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高用
			windowParams.width = (int) (d.getWidth() * 0.9); // 设置宽度
			windowParams.height = (int) (d.getHeight() * 0.75); // 设置高度
			dialog.getWindow().setAttributes(windowParams);
			dialog.findViewById(R.id.button1);

			break;
		case R.id.difficult_setting:

			Dialog dialog2 = new DifficultDialog(Setting.this);
			// dialog2.setTitle("设置难度");
			dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog2.getWindow().setBackgroundDrawable(new BitmapDrawable());
			dialog2.show();
			WindowManager windowManager2 = getWindowManager();
			WindowManager.LayoutParams windowParams2 = dialog2.getWindow().getAttributes();
			Display d2 = windowManager2.getDefaultDisplay(); // 获取屏幕宽、高用
			windowParams2.width = (int) (d2.getWidth() * 0.9); // 设置宽度
			windowParams2.height = (int) (d2.getHeight() * 0.5); // 设置高度
			dialog2.getWindow().setAttributes(windowParams2);

			break;
		case R.id.limited_setting:

			Dialog dialog3 = new TimerDialog(Setting.this);
			// dialog2.setTitle("设置难度");
			dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog3.getWindow().setBackgroundDrawable(new BitmapDrawable());
			dialog3.show();
			WindowManager windowManager3 = getWindowManager();
			WindowManager.LayoutParams windowParams3 = dialog3.getWindow().getAttributes();
			Display d3 = windowManager3.getDefaultDisplay(); // 获取屏幕宽、高用
			windowParams3.width = (int) (d3.getWidth() * 0.9); // 设置宽度
			windowParams3.height = (int) (d3.getHeight() * 0.5); // 设置高度
			dialog3.getWindow().setAttributes(windowParams3);

			break;
		case R.id.feedback_btn:

			Intent intent1 = new Intent();
			intent1.setClass(Setting.this, Feedback_setting.class);
			startActivity(intent1);

			break;
		case R.id.check_new_version:
			
			Toast.makeText(getApplicationContext(), "已经是最新版本", Toast.LENGTH_SHORT).show();
			
			break;
		case R.id.about_us_btn:
			
			Intent intent2 = new Intent();
			intent2.setClass(Setting.this, About_us.class);
			startActivity(intent2);
			
			break;
		}
	}

	private void showShare()
	{
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("云裳日语手机版，掌上单词本，随时随地开启日语学习！分享自@云裳日语");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("/sdcard/test.jpg");
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}
}
