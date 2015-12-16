package android.ricohkana.fq.dailyexpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.ricoh.R;
import com.ricoh.slidemenutest.MainActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.ricohkana.fq.hiragana.ShowKanasActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DailyExpressionActivity extends Activity {

	private long exitTime = 0;
	private DaoAdapter daoAdapter;
	private ListView groupListView;
	private List<GroupBean> groupList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group);
		// 数据库操作对象
		daoAdapter = DaoAdapter.getInstance(this);
		// 取得ListView
		groupListView = (ListView) this.findViewById(R.id.categoryLV);
		// 添加适配器
		groupListView.setAdapter(creatGroupView());

		groupListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(
							AdapterView<?> paramAnonymousAdapterView,
							View paramAnonymousView, int paramAnonymousInt,
							long paramAnonymousLong) {
						@SuppressWarnings("rawtypes")
						HashMap localHashMap = (HashMap) ((Adapter) paramAnonymousAdapterView
								.getAdapter()).getItem(paramAnonymousInt);
						int id = ((Integer) localHashMap.get("groupId"))
								.intValue();
						String groupName = ((String) localHashMap
								.get("typeName"));
						Intent localIntent = new Intent();
						Log.i("sdsds", "dsd--" + id);
						if (id != -1) {
							localIntent.putExtra("group_id", id);
							localIntent.putExtra("groupName", groupName);
							localIntent.setClass(DailyExpressionActivity.this,
									ContentActivity.class);
						} else {
							localIntent.setClass(DailyExpressionActivity.this,
									CollectActivity.class);
						}
						startActivity(localIntent);
					}
				});


	}

	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if ((intent.getFlags() & Intent.FLAG_ACTIVITY_CLEAR_TOP) != 0) {
			finish();
		} else {
			
		}
	}
	
	// 分类列表适配器
	public SimpleAdapter creatGroupView() {
		// 取得分类信息
		groupList = daoAdapter.getGroup();
		Iterator<GroupBean> localIterator = this.groupList.iterator();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map;
		// 适配器数据添加
		while (localIterator.hasNext()) {
			GroupBean group = (GroupBean) localIterator.next();
			map = new HashMap<String, Object>();
			switch (group.getGroupId()) {
			case 1:
				map.put("img", R.drawable.greetings);
				map.put("typeName", group.getGroupName());
				map.put("groupId", group.getGroupId());
				break;
			case 2:
				map.put("img", R.drawable.dating);
				map.put("typeName", group.getGroupName());
				map.put("groupId", group.getGroupId());
				break;
			case 3:
				map.put("img", R.drawable.eatdrink);
				map.put("typeName", group.getGroupName());
				map.put("groupId", group.getGroupId());
				break;
			case 4:
				map.put("img", R.drawable.emergency);
				map.put("typeName", group.getGroupName());
				map.put("groupId", group.getGroupId());
				break;
			case 5:
				map.put("img", R.drawable.shopping);
				map.put("typeName", group.getGroupName());
				map.put("groupId", group.getGroupId());
				break;
			case 6:
				map.put("img", R.drawable.hobbies);
				map.put("typeName", group.getGroupName());
				map.put("groupId", group.getGroupId());
				break;
			default:
				map.put("img", R.drawable.greetings);
				map.put("typeName", group.getGroupName());
				map.put("groupId", group.getGroupId());
				break;
			}
			list.add(map);
		}
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.collect);
		map.put("typeName", "收藏 (共有" + daoAdapter.getCounts() + "句)");
		map.put("groupId", Integer.valueOf("-1"));
		list.add(map);
		String[] arrayOfString = { "img", "typeName", "groupId" };
		int[] arrayOfInt = new int[3];
		arrayOfInt[0] = R.id.img;
		arrayOfInt[1] = R.id.typeName;
		SimpleAdapter localSimpleAdapter = new SimpleAdapter(this, list,
				R.layout.group_item, arrayOfString, arrayOfInt);
		return localSimpleAdapter;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// 刷新分类信息
	// protected void onRestart() {
	// super.onRestart();
	// groupListView.setAdapter(creatGroupView());
	// }

//	// 按两次程序退出
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			Intent intent = new Intent();
//			intent.setClass(DailyExpressionActivity.this, MainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			this.startActivity(intent);
//			finish();
//			return false;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

//	public void exit() {
//		if ((System.currentTimeMillis() - exitTime) > 2000) {
//			Toast.makeText(getApplicationContext(), "再按一次退出程序",
//					Toast.LENGTH_SHORT).show();
//			exitTime = System.currentTimeMillis();
//		} else {
//			//finish();
//			//System.exit(0);
//			Intent intent = new Intent();
//			intent.setClass(DailyExpressionActivity.this, ShowKanasActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			this.startActivity(intent);
//			finish();
//		}
//	}

}
