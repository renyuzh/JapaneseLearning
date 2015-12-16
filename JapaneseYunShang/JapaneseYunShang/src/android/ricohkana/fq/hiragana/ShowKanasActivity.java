package android.ricohkana.fq.hiragana;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.ricohkana.fq.dailyexpression.DailyExpressionActivity;
import android.ricohkana.fq.db.KanaObject;
import android.ricohkana.fq.service.KanaService;
import android.ricohkana.fq.utils.CommonFunction;
import android.ricohkana.fq.utils.Constant;
import android.ricohkana.fq.utils.SlideMenu_yintu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ricoh.R;
import com.ricoh.slidemenutest.MainActivity;

public class ShowKanasActivity extends Activity implements View.OnClickListener
{
	GridViewAdapter adapter;
	Context context;
	GridView gvKana;
	KanaService kanaService;
	public int kana_type;
	List<KanaObject> listKana;
	private Button listFirstBtn;
	private Button listSecondBtn;
	private SlideMenu_yintu slideMenu;
	private ImageView kanaChangeBtn;
	private ImageView kanaChangeXiaoguoBtn;
	private ImageView arrowImageview;
	private Button titleBtn;
	private LinearLayout kanaListLinearlayout;
	private boolean isout = false;

	MediaPlayer mediaPlayer;

	SharedPreferences sp;
	String dictionaryName;
	String userName;

	private int getRealColor(int paramInt)
	{
		switch (paramInt)
		{
		case 0:
			return getResources().getColor(R.color.c_grey);
		case 1:
			return getResources().getColor(R.color.c_red);
		default:
			return -1;
		}
	}

	private void refreshData()
	{
		this.listKana = this.kanaService.getKanaFromType(Constant.yin, Constant.type);
		this.adapter = new GridViewAdapter(this, this.listKana);
		this.gvKana.setAdapter(this.adapter);
	}

	private int listBtnClicked(int paramInt)
	{
		hide();
		int i = Constant.titleBtnTextId;
		Constant.titleBtnTextId = paramInt;
		this.titleBtn.setText(Constant.titleBtnTextId);

		switch (Constant.titleBtnTextId)
		{
		case R.string.qing_yin:
			Constant.yin = 0;
			break;
		case R.string.zhuo_yin:
			Constant.yin = 1;
			break;
		case R.string.ao_yin:
			Constant.yin = 2;
			break;
		default:
		}

		setGvKana();

		return i;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent = new Intent();
			intent.setClass(ShowKanasActivity.this, MainActivity.class);
			intent.putExtra("username", userName);
			this.startActivity(intent);
		}
		return true;
	}

	private void findViews()
	{

		this.titleBtn = ((Button) findViewById(R.id.title_btn));
		this.titleBtn.setText(Constant.titleBtnTextId);

		this.arrowImageview = ((ImageView) findViewById(R.id.arrow_imageview));
		this.kanaChangeBtn = ((ImageView) findViewById(R.id.kana_change_btn));
		if (Constant.type == 0)
		{
			this.kanaChangeBtn.setImageResource(R.drawable.show_pian);
		} else
		{
			this.kanaChangeBtn.setImageResource(R.drawable.show_ping);
		}

		this.slideMenu = ((SlideMenu_yintu) findViewById(R.id.slide_menu));
		this.kanaChangeXiaoguoBtn = ((ImageView) findViewById(R.id.kana_change_btn_xiaoguo));
		this.kanaListLinearlayout = ((LinearLayout) findViewById(R.id.kana_list_linearlayout));
		this.listFirstBtn = ((Button) findViewById(R.id.list_first_btn));
		this.listSecondBtn = ((Button) findViewById(R.id.list_second_btn));
		this.context = this;

		this.kanaChangeXiaoguoBtn.setOnClickListener(this);
		this.titleBtn.setOnClickListener(this);
		this.listFirstBtn.setOnClickListener(this);
		this.listSecondBtn.setOnClickListener(this);

		// 获取形象记忆资料
		this.kanaService = new KanaService(this);
		Constant.memoriesMap = new HashMap<String, String>();
		this.kanaService.getMemories();

		setGvKana();
	}

	private void setGvKana()
	{
		if ((0 == Constant.yin) || (1 == Constant.yin))
		{
			if (null != this.gvKana)
			{
				this.gvKana.setVisibility(View.GONE);
			}
			this.gvKana = ((GridView) findViewById(R.id.gvKana));
			this.gvKana.setVisibility(View.VISIBLE);
		} else
		{
			if (null != this.gvKana)
			{
				this.gvKana.setVisibility(View.GONE);
			}
			this.gvKana = ((GridView) findViewById(R.id.gvKanaao));
			this.gvKana.setVisibility(View.VISIBLE);
		}

		refreshData();

		this.gvKana.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
			{

				int row = 0;
				int section = 0;
				if ((0 == Constant.yin) || (1 == Constant.yin))
				{
					row = paramAnonymousInt / 5;
					section = paramAnonymousInt % 5;
				} else
				{
					row = paramAnonymousInt / 3;
					section = paramAnonymousInt % 3;
				}

				// 排除清音表上几个空格
				if (!((0 == Constant.yin && 7 == row && 1 == section) || (0 == Constant.yin && 7 == row && 3 == section) || (0 == Constant.yin && 9 == row && 1 == section) || (0 == Constant.yin && 9 == row && 2 == section) || (0 == Constant.yin && 9 == row && 3 == section)))
				{

					Constant.row = row;
					Constant.section = section;

					Intent localIntent = new Intent();
					localIntent.setClass(ShowKanasActivity.this, KanaDetailActivity.class);
					localIntent.putExtra("username", userName);
					localIntent.putExtra("dictionary", dictionaryName);
					ShowKanasActivity.this.startActivity(localIntent);
				}
			}
		});
	}

	public void onCreate(Bundle paramBundle)
	{
		super.onCreate(paramBundle);
		setContentView(R.layout.kana_main);

		if(getIntent() != null && getIntent().getExtras() != null){
			userName = getIntent().getExtras().getString("username");
			sp = getSharedPreferences("thefirsttime", MODE_PRIVATE);
			dictionaryName = sp.getString("COURSE", "");
		}
		

		if (!CommonFunction.InitFlag)
		{
			CommonFunction.InitFlag = true;

			// 关于声音池的一些初始化
			final Field[] fields = R.raw.class.getFields();
			CommonFunction.poolMap = new HashMap<String, Integer>();

			// 实例化SoundPool，大小为3
			CommonFunction.pool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);

			CommonFunction.pool.setOnLoadCompleteListener(new OnLoadCompleteListener()
			{

				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
				{
					// 当前装载完成ID为map的最大值，即为最后一次装载完成,fields.length要减1是因为kana_data.db不算
					if (sampleId == fields.length - 1)
					{
						// Toast.makeText(ShowKanasActivity.this,
						// "加载声音池完成!", Toast.LENGTH_SHORT).show();
						CommonFunction.SoundLoadedFlag = true;
					}
				}
			});
		}

		if (!CommonFunction.SoundLoadedFlag)
		{
			LoadSoundTask loadSoundTask = new LoadSoundTask(ShowKanasActivity.this);
		}

		findViews();
		mediaPlayer = new MediaPlayer();
	}

	public void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		if ((intent.getFlags() & Intent.FLAG_ACTIVITY_CLEAR_TOP) != 0)
		{
			// finish();
		} else
		{
			// slideMenu.closeMenu();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_file, menu);
		Intent localIntent1 = new Intent(this, InfoActivity.class);
		localIntent1.putExtra("fromActivity", "ShowKanasActivity");
		menu.findItem(R.id.info_menu).setIntent(localIntent1);
		return super.onCreateOptionsMenu(menu);
	}

	private void hide()
	{
		this.kanaListLinearlayout.setVisibility(View.INVISIBLE);
		this.arrowImageview.setImageResource(R.drawable.arrow_right);
		this.isout = false;
	}

	public void onClick(View paramView)
	{
		switch (paramView.getId())
		{
		case R.id.kana_change_btn_xiaoguo:
			switch (Constant.type)
			{
			case 0:
				this.kanaChangeBtn.setImageResource(R.drawable.show_ping);
				Constant.type = 1;
				break;
			case 1:
				this.kanaChangeBtn.setImageResource(R.drawable.show_pian);
				Constant.type = 0;
				break;
			}
			refreshData();
			break;
		case R.id.title_btn:
			if (!this.isout)
			{
				this.kanaListLinearlayout.setVisibility(View.VISIBLE);
				this.arrowImageview.setImageResource(R.drawable.arrow_down);
				this.isout = true;
				this.listFirstBtn.setText(Constant.firstBtnTextId);
				this.listSecondBtn.setText(Constant.secondBtnTextId);
				this.kanaListLinearlayout.bringToFront();
			} else
			{
				this.kanaListLinearlayout.setVisibility(View.INVISIBLE);
				this.arrowImageview.setImageResource(R.drawable.arrow_right);
				this.isout = false;
			}
			return;
		case R.id.list_first_btn:
			Constant.firstBtnTextId = listBtnClicked(Constant.firstBtnTextId);
			return;
		case R.id.list_second_btn:
			Constant.secondBtnTextId = listBtnClicked(Constant.secondBtnTextId);
			return;
		}
	}

	protected void onDestroy()
	{
		super.onDestroy();
		System.exit(0);

		// 或者下面这种方式
		// android.os.Process.killProcess(android.os.Process.myPid());
	}

	protected void onResume()
	{
		super.onResume();
		refreshData();
	}

	private class GridViewAdapter extends BaseAdapter
	{
		Context context;
		private List<KanaObject> lstKana;

		public GridViewAdapter(Context context, List<KanaObject> paramList)
		{
			this.context = context;
			this.lstKana = paramList;
		}

		public int getCount()
		{
			return this.lstKana.size();
		}

		public Object getItem(int paramInt)
		{
			return this.lstKana.get(paramInt);
		}

		public long getItemId(int paramInt)
		{
			return paramInt;
		}

		public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
		{
			ShowKanasActivity.ViewHolder localViewHolder = null;
			if (paramView == null)
			{
				paramView = LayoutInflater.from(this.context).inflate(R.layout.kana_item, null);
				localViewHolder = new ShowKanasActivity.ViewHolder();
				localViewHolder.tvKana = ((TextView) paramView.findViewById(R.id.tvKana));
				localViewHolder.tvRomaji = ((TextView) paramView.findViewById(R.id.tvRomaji));
				localViewHolder.layoutColor = ((RelativeLayout) paramView.findViewById(R.id.layoutColor));
				paramView.setTag(localViewHolder);
			}

			KanaObject localKana = (KanaObject) this.lstKana.get(paramInt);
			localViewHolder = (ShowKanasActivity.ViewHolder) paramView.getTag();

			if (localKana != null)
			{
				int i = localKana.getColor();

				localViewHolder.tvKana.setText(localKana.getKana());
				localViewHolder.tvRomaji.setText(localKana.getRomeword());
				localViewHolder.layoutColor.setBackgroundColor(ShowKanasActivity.this.getRealColor(i));
			}
			return paramView;
		}
	}

	class ViewHolder
	{
		public RelativeLayout layoutColor;
		public TextView tvKana;
		public TextView tvRomaji;

		ViewHolder()
		{
		}
	}

}

class LoadSoundTask extends Thread
{
	Context context;

	public LoadSoundTask(Context context)
	{
		this.context = context;
		start();
	}

	public void run()
	{

		Field[] fields = R.raw.class.getFields();
		for (Field f : fields)
		{
			if (!f.getName().equals("kana_data"))
			{
				int resid = context.getResources().getIdentifier(f.getName(), "raw", context.getPackageName());

				if (null == CommonFunction.poolMap.get(f.getName()))
				{
					int soundID = CommonFunction.pool.load(context, resid, 1);
					CommonFunction.poolMap.put(f.getName(), soundID);
				}
			}
		}
	}
}
