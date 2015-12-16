package com.ricoh.japanesestudy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ricoh.R;

public class RankViewActivity extends Activity
{

	private TextView nameView;
	private TextView timeView;
	private TextView countView;

	private ListView weeklistView;
	private ListView monthlistView;

	private ArrayList<HashMap<String, Object>> weeklist;
	private ArrayList<HashMap<String, Object>> monthlist;

	private String[] rank_indexs = new String[]
	{ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
	private String[] user_names = new String[]
	{ "username", "username", "username", "username", "username", "username", "username", "username", "username", "username" };
	private int[] images = new int[]
	{ R.drawable.rank_person, R.drawable.rank_person, R.drawable.rank_person, R.drawable.rank_person, R.drawable.rank_person, R.drawable.rank_person, R.drawable.rank_person, R.drawable.rank_person, R.drawable.rank_person, R.drawable.rank_person };
	private String[] rank_times = new String[]
	{ "1小时49分", "1小时49分", "1小时49分", "1小时49分", "1小时49分", "1小时49分", "1小时49分", "1小时49分", "1小时49分", "1小时49分" };
	private int[] imv_rank_counts = new int[]
	{ R.drawable.rank_count, R.drawable.rank_count, R.drawable.rank_count, R.drawable.rank_count, R.drawable.rank_count, R.drawable.rank_count, R.drawable.rank_count, R.drawable.rank_count, R.drawable.rank_count, R.drawable.rank_count };
	private String[] txv_rank_counts = new String[]
	{ "上升100名", "上升100名", "上升100名", "上升100名", "上升100名", "上升100名", "上升100名", "上升100名", "上升100名", "上升100名" };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(com.ricoh.R.layout.main_rankview);
		initWidget();
	}

	public void initWidget()
	{
		nameView = (TextView) findViewById(R.id.txv_rank_name);
		timeView = (TextView) findViewById(R.id.txv_rank_time);
		countView = (TextView) findViewById(R.id.txv_rank_count);
		weeklistView = (ListView) findViewById(R.id.slv_rank_week);
		monthlistView = (ListView) findViewById(R.id.slv_rank_month);
//		weeklist = new ArrayList<HashMap<String, Object>>();
//		monthlist = new ArrayList<HashMap<String, Object>>();

		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, getweeklist());
		// weeklist.setAdapter(adapter);
		// monthlist.setAdapter(adapter);

		ArrayList<HashMap<String, Object>> listitems = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 10; i++)
		{
			HashMap<String, Object> listitem = new HashMap<String, Object>();
			listitem.put("rank_index", rank_indexs[i]);
			listitem.put("rank_portrait", images[i]);
			listitem.put("rank_name", user_names[i]);
			listitem.put("rank_time", rank_times[i]);
			listitem.put("imv_rank_count", imv_rank_counts[i]);
			listitem.put("txv_rank_count", txv_rank_counts[i]);
			listitems.add(listitem);
		}

		SimpleAdapter weekadapter = new SimpleAdapter(this, listitems, R.layout.rank_item_view, new String[]
		{ "rank_index", "rank_portrait", "rank_name", "rank_time", "imv_rank_count", "txv_rank_count" }, new int[]
		{ R.id.txv_rank_index, R.id.imv_rank_portrait, R.id.txv_rank_name, R.id.txv_rank_time, R.id.imv_rank_count, R.id.txv_rank_count });
		SimpleAdapter monthadapter = new SimpleAdapter(this, listitems, R.layout.rank_item_view, new String[]
		{ "rank_index", "rank_portrait", "rank_name", "rank_time", "imv_rank_count", "txv_rank_count" }, new int[]
		{ R.id.txv_rank_index, R.id.imv_rank_portrait, R.id.txv_rank_name, R.id.txv_rank_time, R.id.imv_rank_count, R.id.txv_rank_count });

		weeklistView.setAdapter(weekadapter);
		monthlistView.setAdapter(monthadapter);

	}

	// 本方法通过网络返回一个排行榜数组（应该使用异步）
	// 现阶段返回一个静态数组
	private ArrayList<HashMap<String, Object>> getweeklist()
	{
		ArrayList<HashMap<String, Object>> arr = new ArrayList<HashMap<String, Object>>();

		return arr;
	}

}
