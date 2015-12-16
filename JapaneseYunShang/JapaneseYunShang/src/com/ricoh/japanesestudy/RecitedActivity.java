package com.ricoh.japanesestudy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ricoh.R;
import com.ricoh.yunsang.DBService.WordService;
import com.ricoh.yunsang.Data.Word;

public class RecitedActivity extends Activity {
	private TextView wordbody_tv;
	private TextView soundmark_tv;
	private TextView correct_tv;
	private ListView option_lv;

	private ImageButton addToWordnote;
	private WordService wordService;
	private List<Word> wordList;
	// 设置选项列表的adapter
	private SimpleAdapter adapter;
	List<Map<String, Object>> listItems;
	private boolean flag = false;
	private boolean actual;
	private Word keyword;
	private String keywordWord;
	boolean firstAnswer;
	private String body;
	private String pron;
	private String userName;
	private String dictionaryName;
	SharedPreferences sp;
	TextToSpeech tts;
	Context mContext;// 移动TTS.apk用
	private ImageButton playVoice;
	int[] optionImage = new int[] { R.drawable.recite_item_index_a,
			R.drawable.recite_item_index_b, R.drawable.recite_item_index_c,
			R.drawable.recite_item_index_d };

	private ProgressBar timerProgress;
	Timer timer;
	Handler handler;
	int count = 0;
	int delaytime;

	public static final int DISPLAY_LENGTH = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_recite_content_righthand);
		SharedPreferences sp = getSharedPreferences("settings",
				Context.MODE_PRIVATE);
		delaytime = sp.getInt("currtime", 20);

		// 装载tts
		tts = new TextToSpeech(this, new OnInitListener() {
			@Override
			public void onInit(int status) {
				// 如果装载TTS引擎成功
				if (status == TextToSpeech.SUCCESS) {
					// 设置使用美式英语朗读
					int result = tts.setLanguage(Locale.JAPANESE);
					// 如果不支持所设置的语言
					if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
							&& result != TextToSpeech.LANG_AVAILABLE) {
						Toast.makeText(RecitedActivity.this,
								"TTS暂时不支持这种语言的朗读。", Toast.LENGTH_LONG).show();

					}
				}
			}

		});

		sp = getSharedPreferences("thefirsttime", MODE_PRIVATE);
		flag = sp.getBoolean("SEVEN_SELECTED", false);
		timerProgress = (ProgressBar) this
				.findViewById(R.id.recite_time_progressbar);
		userName = this.getIntent().getExtras().getString("username");
		dictionaryName = this.getIntent().getExtras().getString("dictionary");
		wordService = new WordService(this, dictionaryName, userName, 1);
		wordbody_tv = (TextView) this.findViewById(R.id.recite_detail_wordbody);
		soundmark_tv = (TextView) this
				.findViewById(R.id.recite_detail_soundmark);
		correct_tv = (TextView) this.findViewById(R.id.correct_answer);
		option_lv = (ListView) this.findViewById(R.id.recite_list_option);
		addToWordnote = (ImageButton) this
				.findViewById(R.id.recite_image_add_to_wordnote);

		addToWordnote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				wordService.addWordToWordnote(keywordWord);
				Toast.makeText(RecitedActivity.this, keywordWord + "成功加入单词本",
						Toast.LENGTH_SHORT).show();
			}
		});
		playVoice = (ImageButton) findViewById(R.id.recite_btn_sound);
		playVoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mContext = arg0.getContext();
				int result = tts.setLanguage(Locale.JAPANESE);
				if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
						&& result != TextToSpeech.LANG_AVAILABLE) {
					copyApkFromAssets(mContext, "N2_TTS_1403331052637.apk",
							Environment.getExternalStorageDirectory()
									.getAbsolutePath()
									+ "/N2_TTS_1403331052637.apk");
					Dialog alertDialog = new AlertDialog.Builder(mContext)
							.setTitle("您尚未安装TTS语音包")
							.setMessage("您需要安装TTS语音包么？")
							.setIcon(R.drawable.ic_launcher)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											Intent intent = new Intent(
													Intent.ACTION_VIEW);
											intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											intent.setDataAndType(
													Uri.parse("file://"
															+ Environment
																	.getExternalStorageDirectory()
																	.getAbsolutePath()
															+ "/N2_TTS_1403331052637.apk"),
													"application/vnd.android.package-archive");
											mContext.startActivity(intent);
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
										}
									}).create();
					alertDialog.show();

				} else {
					// TODO 自动生成的方法存根
					tts.speak(pron, TextToSpeech.QUEUE_ADD, null);
				}
			}
		});
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x1233) {
					count += 1;
					timerProgress.setProgress(count);
				}
				if (count >= 30) {
					count = 0;
					timer.cancel();
					answerFaultOpt();

				}
			}

		};

		getNextQuestion();

	}

	private void getNextQuestion() {
		if (timer != null)
			timer.cancel();
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(0x1233);
			}

		}, 0, delaytime * 10);

		// 选择四选项 false，七选项 true
		Cursor cursor = wordService.getQuestion(flag);
		wordList = convertToList(cursor);
		if (wordList.isEmpty()) {
			// 学习结束的操作,弹出dialog返回课程选择界面

		}
		keyword = wordList.get(0);
		keywordWord = keyword.getWord();
		String[] strs = keyword.getWord().split("%");
		body = strs[0];
		pron = strs[1];
		wordbody_tv.setText(body);
		soundmark_tv.setText(pron);
		for (int i = 0; i < wordList.size(); i++) {
			String word = wordList.get(i).getWord();
			word = word.split("%")[0];
			wordList.get(i).setWord(word);
		}
		// 乱序
		Collections.shuffle(wordList);

		// 真正数据库返回的是四选项，还是七选项 ，选择四选项 false，七选项 true
		actual = wordList.size() == 7 ? true : false;
		// 7选项布局
		if (actual) {
			// 显示七选项
			layout7Question();
			firstAnswer = true;
			option_lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (position == 3) {
						firstAnswer = true;
						// 循坏判断option前三个选项是否正确，若前三个选项有正确的则跳至回答错误操作，若前三个操作无正确的则跳转回答正确操作
						for (int i = 0; i < 3; i++) {
							if (wordList.get(i) == keyword) {
								firstAnswer = false;
								break;
							}
						}
						if (firstAnswer == true) {
							// 重新布局选项列表
							for (int i = 0; i < 4; i++) {
								// 更改选项信息
								listItems.get(i).put("option",
										wordList.get(i + 3).getExplanation_1());
							}
							adapter.notifyDataSetChanged();
							option_lv
									.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> parent,
												View view, int position, long id) {
											Word optionChose = wordList
													.get(position + 3);
											if (optionChose == keyword) {
												anwserCorrectOpt(view);
											} else {
												answerFaultOpt(view);
											}
										}
									});

						} else {
							answerFaultOpt(view);
						}

					} else {
						if (wordList.get(position) == keyword) {
							anwserCorrectOpt(view);
						} else
							answerFaultOpt(view);
					}
				}
			});
		}
		// 4选项布局
		if (!actual) {
			layout4Question();
			option_lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Word optionChose = wordList.get(position);
					if (optionChose == keyword) {
						anwserCorrectOpt(view);
					} else {
						answerFaultOpt(view);
					}
				}
			});
		}
		count = 0;
	}

	private void answerFaultOpt() {
		if (timer != null)
			timer.cancel();
		
		wordService.submitResult(false);
		correct_tv.setText(keyword.getExplanation_1());
		correct_tv.setVisibility(View.VISIBLE);

		option_lv.setOnItemClickListener(null);

		// 此处可以发音
		tts.speak(pron, TextToSpeech.QUEUE_ADD, null);

		// 延迟一秒后进入下一个单词的学习
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				correct_tv.setText("");
				getNextQuestion();
			}
		}, DISPLAY_LENGTH);
	}

	// 问题回答错误后操作
	private void answerFaultOpt(View view) {
		TextView answerFaultOpt_textView = (TextView) view
				.findViewById(R.id.recite_item_text);
		answerFaultOpt_textView.setTextColor(Color.parseColor("#F63B3B"));
		if (timer != null)
			timer.cancel();
		wordService.submitResult(false);
		correct_tv.setText(keyword.getExplanation_1());
		correct_tv.setVisibility(View.VISIBLE);

		option_lv.setOnItemClickListener(null);

		// 此处可以发音
		tts.speak(pron, TextToSpeech.QUEUE_ADD, null);

		// 延迟一秒后进入下一个单词的学习
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				correct_tv.setText("");
				getNextQuestion();
			}
		}, DISPLAY_LENGTH);

	}

	// 问题回答正确后操作
	private void anwserCorrectOpt(View view) {
		TextView anwserCorrectOpt_textView = (TextView) view
				.findViewById(R.id.recite_item_text);
		anwserCorrectOpt_textView.setTextColor(Color.parseColor("#56925F"));
		if (timer != null)
			timer.cancel();
		// Toast.makeText(this, "correct", Toast.LENGTH_SHORT).show();
		wordService.submitResult(true);

		option_lv.setOnItemClickListener(null);
		// 此处可以发音
		tts.speak(pron, TextToSpeech.QUEUE_ADD, null);

		// 延迟一秒后进入下一个单词的学习
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				getNextQuestion();
			}
		}, DISPLAY_LENGTH);
	}

	private void layout4Question() {
		// 设置adapter的对应表
		listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 4; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("optionImage", optionImage[i]);
			map.put("option", wordList.get(i).getExplanation_1());
			listItems.add(map);
		}
		adapter = new SimpleAdapter(this, listItems,
				R.layout.recited_optionitem, new String[] { "optionImage",
						"option" }, new int[] { R.id.recite_item_image,
						R.id.recite_item_text });
		option_lv.setAdapter(adapter);
	}

	private void layout7Question() {
		// 设置adapter的对应表
		listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 3; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("optionImage", optionImage[i]);
			map.put("option", wordList.get(i).getExplanation_1());
			listItems.add(map);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("optionImage", optionImage[3]);
		map.put("option", "以上选择均不正确");
		listItems.add(map);
		adapter = new SimpleAdapter(this, listItems,
				R.layout.recited_optionitem, new String[] { "optionImage",
						"option" }, new int[] { R.id.recite_item_image,
						R.id.recite_item_text });
		option_lv.setAdapter(adapter);
	}

	private List<Word> convertToList(Cursor cursor) {
		List<Word> list = new ArrayList<Word>();
		while (cursor.moveToNext()) {
			Word word = new Word();
			word.setWord(cursor.getString(1));
			word.setExplanation_1(cursor.getString(2));
			word.setExplanation_2(cursor.getString(3));
			word.setExplanation_3(cursor.getString(4));
			list.add(word);
		}
		return list;
	}

	// 将TTS从assets文件夹中移动至手机存储卡中
	private boolean copyApkFromAssets(Context context, String fileName,
			String path) {
		boolean copyIsFinish = false;
		try {
			InputStream is = context.getAssets().open(fileName);
			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			copyIsFinish = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return copyIsFinish;
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		if (tts != null) {
			tts.shutdown();
		}
		timer.cancel();
		super.onDestroy();
	}

}
