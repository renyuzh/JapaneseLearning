package com.ricoh.japanesestudy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ricoh.R;
import com.ricoh.slidemenutest.MainActivity;
import com.ricoh.yunsang.DBService.WordService;
import com.ricoh.yunsang.Data.Word;

public class WordnoteAcitivity extends Activity {
	ListView wordlist;
	Button backButton;
	Button deleteFromWordnote;
	WordService wordService;
	List<Word> arraylist;
	BaseAdapter adapter;
	ProgressBar pro;
	
	public static final int DISPLAY_LENGTH = 2000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.ricoh.R.layout.wordnotebook);
		wordlist = (ListView) this.findViewById(R.id.wordnotelist);
		deleteFromWordnote = (Button)this.findViewById(R.id.deletewordfromwordnote);
		//获得启动该activity的intent，该intent应该包含username-string  dictionary - string两个键值对
		Intent intent = this.getIntent();
		String username = intent.getStringExtra("username");
		String dictionaryname = intent.getStringExtra("dictionary");
		wordService = new WordService(this,dictionaryname,username,1);
		Cursor cursor = wordService.seeWordnote();
		arraylist = convertToList(cursor);
		adapter = new WordlistAdapter();
		wordlist.setAdapter(adapter);
		wordlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//跳转至worddisplay
				Intent intent = new Intent(WordnoteAcitivity.this,WordDisplayActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("word", arraylist.get(position));
				intent.putExtras(bundle);
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
	//返回word列表
	List<Word> convertToList(Cursor cursor){
		List<Word> list = new ArrayList<Word>();
		while(cursor.moveToNext()){
			Word word = new Word(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
			list.add(word);
		}
		return list;
	}
	
	class WordlistAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return arraylist.size();
		}

		@Override
		public Object getItem(int position) {
			return arraylist.get(position).getWord();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);  
			View view = inflater.inflate(R.layout.wordnoteitemlayout,null);
			Button deleteWordnote = (Button) view.findViewById(R.id.deletewordfromwordnote);
			deleteWordnote.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog(position);
				}
			});
			TextView tv = (TextView) view.findViewById(R.id.wordnoteitemtextview);
			String tempString = arraylist.get(position).getWord().replace("%", " ");
			tv.setText(tempString);
			return view;
		}
		
		
		protected void dialog(final int position) {  
	        AlertDialog.Builder builder = new Builder(WordnoteAcitivity.this);  
	        builder.setMessage("确定要删除选定单词吗?");
	        builder.setTitle("提示");  
	        builder.setPositiveButton("确认",  
	        new android.content.DialogInterface.OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	            	wordService.deleteWordFromWordnote(arraylist.get(position).getWord());
					arraylist.remove(position);
					adapter.notifyDataSetChanged();
	            }  
	        });  
	        builder.setNegativeButton("取消",  
	        new android.content.DialogInterface.OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	                dialog.dismiss();  
	            }  
	        });  
	        builder.create().show();  
	    }
		
	}
	
}
