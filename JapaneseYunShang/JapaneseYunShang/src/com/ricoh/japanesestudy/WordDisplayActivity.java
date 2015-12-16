package com.ricoh.japanesestudy;

import java.util.Locale;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ricoh.R;
import com.ricoh.yunsang.Data.Word;

public class WordDisplayActivity extends Activity{
	
	ImageButton playVoice;
	TextView hiraganatv;
	TextView katakanatv;
	ListView lv;
	TextToSpeech tts;
	String katakana;
	String hiragana;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.ricoh.R.layout.worddisplay);
		//装载tts
		tts = new TextToSpeech(this, new OnInitListener()
		{
			@Override
			public void onInit(int status)
			{
				// 如果装载TTS引擎成功
				if (status == TextToSpeech.SUCCESS)
				{
					// 设置使用美式英语朗读
					int result = tts.setLanguage(Locale.JAPANESE);
					// 如果不支持所设置的语言
					if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
						&& result != TextToSpeech.LANG_AVAILABLE)
					{
						Toast.makeText(WordDisplayActivity.this
							, "TTS暂时不支持这种语言的朗读。", Toast.LENGTH_LONG)
							.show();
					}
				}
			}

		});
		
		//intent中包含一个word对象
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		Word wordOb = (Word) bundle.getSerializable("word");
		//格式默认为 平假名%片假名
		String word = wordOb.getWord();
		String[] words = word.split("%");
		//平假名
		 hiragana = words[0];
		//片假名
		katakana = words[1];

		String explanation1 = wordOb.getExplanation_1()==null?"":wordOb.getExplanation_1();
		String explanation2 = wordOb.getExplanation_2().equals("null")?"":wordOb.getExplanation_2();
		String explanation3 = wordOb.getExplanation_3().equals("null")?"":wordOb.getExplanation_3();
		String[] explanations = new String[]{explanation1,explanation2,explanation3};
		hiraganatv = (TextView) this.findViewById(R.id.display_hiragana);
		katakanatv = (TextView) this.findViewById(R.id.display_katakana);
		lv = (ListView) this.findViewById(R.id.display_explanationview);
		hiraganatv.setText(hiragana);
		katakanatv.setText(katakana);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,explanations);
		lv.setAdapter(adapter);
		
		playVoice=(ImageButton) findViewById(R.id.imageButton1);
		playVoice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				tts.speak(katakana,
						TextToSpeech.QUEUE_ADD, null);
				
			}
		});
	}
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		if (tts != null)
		{
			tts.shutdown();
		}
		super.onDestroy();
	}
}
