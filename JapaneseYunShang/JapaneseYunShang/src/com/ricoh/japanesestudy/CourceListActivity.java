package com.ricoh.japanesestudy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ricoh.R;
import com.ricoh.yunsang.DBService.WordService;

public class CourceListActivity extends Activity
{
	private ListView courceList;
	private String username;
	private String dictionary;
	ArrayList<String> cources;
	ProgressBar pro;

	public static final int DISPLAY_LENGTH = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(com.ricoh.R.layout.cource_list);

		courceList = (ListView) this.findViewById(R.id.cource_list_listview);
		username = this.getIntent().getExtras().getString("username");
		cources = (ArrayList<String>) WordService.getWordnote(username);
		for (int i = 0; i < cources.size(); i++)
		{
			String s = cources.get(i);
			String[] ss = s.split("/");
			String tempStr = ss[ss.length - 1];
			cources.set(i, tempStr);
		}
		ArrayList<String> listCource = new ArrayList<String>();

		for (int i = 0; i < cources.size(); i++)
		{

			listCource.add("日语"+(i+1)+"级");
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cource_list_item, listCource);
		courceList.setAdapter(adapter);
		courceList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				int position = arg2;
				dictionary = cources.get(position);
				Intent intent = new Intent(CourceListActivity.this, WordnoteAcitivity.class);
				intent.putExtra("username", username);
				intent.putExtra("dictionary", dictionary);
				startActivity(intent);
			}
		});
		
		//进入界面后ProgressBar加载两秒钟后结束
		pro = (ProgressBar) findViewById(R.id.progressBar1);
		new Handler().postDelayed(new Runnable()
		{

			@Override
			public void run()
			{
				pro.setVisibility(View.GONE);

			}
		}, DISPLAY_LENGTH);

	}
}
