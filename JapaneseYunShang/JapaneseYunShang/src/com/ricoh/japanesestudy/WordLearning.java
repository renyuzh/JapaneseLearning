package com.ricoh.japanesestudy;

import tan.richo.myview.TasksCompletedView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ricoh.R;
import com.ricoh.slidemenutest.MainActivity;
import com.ricoh.yunsang.DBService.WordService;

public class WordLearning extends Activity
{

	SharedPreferences sp;
	String dictionaryName;
	String userName;
	WordService wordService;

	TextView totalWords;
	TextView redictedWords;
	Button start;
	ImageButton Button_a;
	ImageButton Button_b;

	private TasksCompletedView mTasksView;
	private int mCurrentProgress;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_mainview_new);

		userName = getIntent().getExtras().getString("username");
		sp = getSharedPreferences("thefirsttime", MODE_PRIVATE);
		dictionaryName = sp.getString("COURSE", "");

		totalWords = (TextView) findViewById(R.id.appMessage2);
		redictedWords = (TextView) findViewById(R.id.appMessage1);
		start = (Button) findViewById(R.id.start);
		Button_a = (ImageButton) findViewById(R.id.a);
		Button_b = (ImageButton) findViewById(R.id.b);

		wordService = new WordService(this, dictionaryName, userName, 1);

		totalWords.setText(wordService.allWordCount() + "");
		redictedWords.setText(wordService.redictedWordCount() + "");

		start.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent0 = new Intent();
				intent0.setClass(WordLearning.this, RecitedActivity.class);
				intent0.putExtra("username", userName);
				intent0.putExtra("dictionary", dictionaryName);
				startActivity(intent0);
			}
		});

		Button_a.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent1 = new Intent(WordLearning.this, CourceListActivity.class);
				Bundle bundle = new Bundle();
				// ´Ë´¦Îª²âÊÔ
				bundle.putString("username", userName);
				intent1.putExtras(bundle);
				startActivity(intent1);

			}
		});

		Button_b.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent2 = new Intent(WordLearning.this, ChooseNewCourceActivity.class);
				intent2.putExtra("username", userName);
				intent2.putExtra("dictionary", dictionaryName);
				startActivity(intent2);

			}
		});

		initView();

	}

	private void initView()
	{
		mTasksView = (TasksCompletedView) findViewById(R.id.tasks_view);
		mTasksView.setProgress(mCurrentProgress);
	}

}
