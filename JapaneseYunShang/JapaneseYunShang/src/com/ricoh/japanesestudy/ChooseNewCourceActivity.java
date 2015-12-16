package com.ricoh.japanesestudy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ricoh.R;
import com.ricoh.slidemenutest.MainActivity;
import com.ricoh.yunsang.DBService.WordService;

public class ChooseNewCourceActivity extends Activity
{
	ListView chooseNewCourceList;
	String dictionaryName;
	List<String> arrayList;
	String username;
	SharedPreferences sp;
	
	int startYear;
	int startMonth;
	int startDay;
	int first_register;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_new_cource);
		username = this.getIntent().getExtras().getString("username");
		
		first_register = getIntent().getIntExtra("first_Register", 0);

		if (first_register == 1)
		{
			startYear = getIntent().getIntExtra("startYear", 2014);
			startMonth = getIntent().getIntExtra("startMonth", 01);
			startDay = getIntent().getExtras().getInt("startDay", 01);
		}
		
		chooseNewCourceList = (ListView) this.findViewById(R.id.choose_new_cource_listview);
		String[] files = null;
		try
		{
			files = this.getAssets().list("");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		arrayList = new ArrayList<String>();
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].endsWith("a.db") || files[i].endsWith("b.db"))
				arrayList.add(files[i]);
		}
		String[] courses = new String[arrayList.size()];
		for(int i=0;i<arrayList.size();i++){
			courses[i] = "日语"+(i+1)+"级";
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cource_list_item, courses);
		chooseNewCourceList.setAdapter(adapter);
		chooseNewCourceList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				int position = arg2;
				dictionaryName = arrayList.get(position);
				// 此处应加入XML文件
				sp = getSharedPreferences("thefirsttime", Context.MODE_PRIVATE);
				sp.edit().putString("COURSE", dictionaryName).commit();
				System.out.println(dictionaryName);
				WordService.addNewCourse(ChooseNewCourceActivity.this, dictionaryName, username);
				WordService wordService = new WordService(ChooseNewCourceActivity.this, dictionaryName, username, 1);
				wordService.initializeDictionary();
				Intent intent = new Intent(ChooseNewCourceActivity.this, MainActivity.class);
				intent.putExtra("username", username);
				if (first_register == 1)
				{
					intent.putExtra("first_register",1);
					intent.putExtra("startYear", startYear);
					intent.putExtra("startMonth", startMonth);
					intent.putExtra("startDay", startDay);
				}
				startActivity(intent);
			}

		});
	}
}
