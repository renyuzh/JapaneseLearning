package android.ricohkana.fq.hiragana;

import com.ricoh.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

//用于点击菜单键后，五十音图简介界面
public class InfoActivity extends Activity {
	
	private ImageButton info_back_btn;
	private TextView info_tvcontent;
	
	@Override
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.info);
		info_back_btn = (ImageButton) findViewById(R.id.info_back_btn);
		
		//设置返回按钮的监听事件
		info_back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		info_tvcontent = (TextView) findViewById(R.id.info_tvcontent);
		info_tvcontent.setText(R.string.info_contentstr);
	}

	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {

		boolean bool = true;

		if (paramInt == KeyEvent.KEYCODE_BACK) {
			// 按下键盘上返回按钮
			String fromActivity = (String) getIntent().getCharSequenceExtra("fromActivity");
			Intent localIntent = null;
			if (fromActivity.equals("KanaDetailActivity")) {
				System.out.println("KanaDetailActivity");
				localIntent = new Intent(this, KanaDetailActivity.class);
			}else if (fromActivity.equals("ShowKanasActivity")) {
				System.out.println("ShowKanasActivity");
				localIntent = new Intent(this, ShowKanasActivity.class);
			}else {
				System.out.println("otherActivity");
			}
			
			 finish();
			 //startActivity(localIntent);
			bool = true;
		} else {
			bool = super.onKeyDown(paramInt, paramKeyEvent);
		}

		return bool;
	}
}
