package com.ricoh.slidemenutest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tan.richo.myview.TasksCompletedView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.ricohkana.fq.dailyexpression.ContentActivity;
import android.ricohkana.fq.dailyexpression.DailyExpressionActivity;
import android.ricohkana.fq.hiragana.KanaDetailActivity;
import android.ricohkana.fq.hiragana.ShowKanasActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.ricoh.slidemenutest.FixedSpeedScroller;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.ricoh.R;
import com.ricoh.japanesestudy.ChooseNewCourceActivity;
import com.ricoh.japanesestudy.CourceListActivity;
import com.ricoh.japanesestudy.RankViewActivity;
import com.ricoh.japanesestudy.RecitedActivity;
import com.ricoh.japanesestudy.Setting;
import com.ricoh.japanesestudy.WordLearning;
import com.ricoh.yunsang.DBService.WordService;
import com.ricoh.yunsang.login.LoginActivity;
import com.ricoh.japanesestudy.User_message;

;

public class MainActivity extends Activity
{
	SlidingMenu slideMenu;
	ListView listview;
	SharedPreferences sp;
	String dictionaryName;
	String userName;
	TextView totalWords;
	TextView redictedWords;
	private String[] names = new String[]
	{ "五十音图", "单词学习", "日常用语", "排行榜", "单词本", "设置", "退出" };
	private int[] images = new int[]
	{ R.drawable.slide_syllabany, R.drawable.slide_word, R.drawable.slide_language, R.drawable.slide_rank, R.drawable.slide_notebook, R.drawable.slide_setting, R.drawable.slide_logoff };
	ImageButton start;
	private ImageButton buttonTest0, buttonTest1, buttonTest2;

	private TasksCompletedView mTasksView;
	private int mTotalProgress;
	private int mCurrentProgress;
	private int login_days = 0;

	private int diffLevel = 1;

	WordService wordService;
	private TextView tx;

	private List<View> views;
	private ViewPager vp;
	private TextView everyTextView;
	private TextView translationView;
	private TextView anlysisView;

	private Handler mHandler;
	private static int curImg;
	private static boolean iscontinue;
	private String[] jpsentence;
	private String[] cnsentence;
	private String[] anlysentence;

	int startYear;
	int startMonth;
	int startDay;
	int first_register;

	private long firstTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_mainview);
		// 接收注册界面传来的用户名
		userName = getIntent().getExtras().getString("username");
		Toast.makeText(this, userName, Toast.LENGTH_SHORT).show();
		sp = getSharedPreferences("thefirsttime", MODE_PRIVATE);
		dictionaryName = sp.getString("COURSE", "");

		first_register = getIntent().getIntExtra("first_Register", 0);

		if (first_register == 1)
		{
			startYear = getIntent().getIntExtra("startYear", 2014);
			startMonth = getIntent().getIntExtra("startMonth", 01);
			startDay = getIntent().getExtras().getInt("startDay", 01);
		}

		// wordService = new WordService(this, dictionaryName, userName, 1);
		//
		// totalWords = (TextView) findViewById(R.id.appMessage1);
		// redictedWords = (TextView) findViewById(R.id.appMessage2);
		//
		// totalWords.setText("全部词汇:" + wordService.allWordCount() + "");
		// redictedWords.setText("已完成:" + wordService.redictedWordCount() + "");

		// 设置slidingmenu的宽度
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int dp = metrics.densityDpi;
		// 动画
		CanvasTransformer mTransformer = new CanvasTransformer()
		{
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen)
			{
				// TODO Auto-generated method stub
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth() / 2, canvas.getHeight() / 2);
			}
		};

		slideMenu = new SlidingMenu(this);
		slideMenu.setMode(SlidingMenu.LEFT);// 从左面划出
		slideMenu.setBehindOffset(dp);// 设置菜单宽度
		slideMenu.setTouchModeAbove(SlidingMenu.LEFT);// 设置全屏点

		slideMenu.setFadeEnabled(true);
		slideMenu.setFadeDegree(0.35f);
		slideMenu.setMenu(R.layout.slide_menu);// 设置侧滑菜单ui
		slideMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);// 加入到activity
		slideMenu.setBehindCanvasTransformer(mTransformer);

		listFill();

		// start = (ImageButton) findViewById(R.id.start);
		// start.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View arg0)
		// {
		// Intent intent = new Intent();
		// intent.setClass(MainActivity.this, RecitedActivity.class);
		// intent.putExtra("username", userName);
		// intent.putExtra("dictionary", dictionaryName);
		// startActivity(intent);
		//
		// }
		// });

		initVariable();

		everyTextView = (TextView) findViewById(R.id.everyday_text_jp);
		translationView = (TextView) findViewById(R.id.everyday_text_cn);
		anlysisView = (TextView) findViewById(R.id.everyday_text_anly);

		loadViewPager();
		initData();
		mHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				vp.setCurrentItem(curImg, true);
				everyTextView.setText(jpsentence[curImg]);
				translationView.setText(cnsentence[curImg]);
				anlysisView.setText(anlysentence[curImg]);
			}

		};
		curImg = 0;
		iscontinue = true;
		startAnim();

		// initView();

		/*
		 * 设置用户登陆云裳天数
		 */
		// TextView user_activity_button2 = (TextView)
		// findViewById(R.id.user_activity_button2);
		// login_days = login_days + getIntent().getIntExtra("login_days", 0);
		// user_activity_button2.setText("已在云裳 ： " + login_days + "天");

		// 确认是否是第一次登陆云裳程序
		// int first_Register = getIntent().getIntExtra("first_Register", 0);
		//
		// /*
		// * 初始化用户背单词时间
		// */
		// if (first_Register == 1)
		// {
		//
		// int year = getIntent().getIntExtra("startYear", 2014);
		// int month = getIntent().getIntExtra("startMonth", 1);
		// int day = getIntent().getIntExtra("startDay", 1);
		//
		// TextView time_start = (TextView) findViewById(R.id.time_start);
		// TextView time_end = (TextView) findViewById(R.id.time_end);
		// time_start.setText(year + "年" + month + "月" + day + "日");
		// time_end.setText(year + "年" + month + "月" + day + "日");
		//
		// }

	}

	private void initVariable()
	{
		mTotalProgress = 100;
		mCurrentProgress = 0;
	}

	// private void initView()
	// {
	// mTasksView = (TasksCompletedView) findViewById(R.id.tasks_view);
	// mTasksView.setProgress(mCurrentProgress);
	// }

	private void listFill()
	{

		tx = (TextView) findViewById(R.id.mytextviewname);
		tx.setText(userName.split("@")[0]);

		List<Map<String, Object>> listitems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < names.length; i++)
		{
			Map<String, Object> listitem = new HashMap<String, Object>();
			listitem.put("icon", images[i]);
			listitem.put("label", names[i]);
			listitems.add(listitem);
		}

		LinearLayout user_message = (LinearLayout) this.findViewById(R.id.user_message);
		user_message.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent_user = new Intent();
				intent_user.setClass(MainActivity.this, User_message.class);
				intent_user.putExtra("username", userName);
				intent_user.putExtra("dictionary", dictionaryName);
				if (first_register == 1)
				{
					intent_user.putExtra("startYear", startYear);
					intent_user.putExtra("startMonth", startMonth);
					intent_user.putExtra("startDay", startDay);
				}
				startActivity(intent_user);
			}

		});

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listitems, R.layout.listitem, new String[]
		{ "icon", "label" }, new int[]
		{ R.id.icon_img, R.id.label_text });
		listview = (ListView) this.findViewById(R.id.listview_slidingmenu);
		listview.setDivider(null);
		listview.setAdapter(simpleAdapter);

		listview.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				int kind = arg2;
				switch (kind)
				{
				case 0:

					Intent intent0 = new Intent();
					intent0.setClass(MainActivity.this, ShowKanasActivity.class);
					intent0.putExtra("username", userName);
					intent0.putExtra("dictionary", dictionaryName);
					startActivity(intent0);

					// showShare();

					break;
				case 1:// 进入到排行版界面

					Intent intent1 = new Intent();
					intent1.setClass(MainActivity.this, WordLearning.class);
					intent1.putExtra("username", userName);
					startActivity(intent1);
					// Intent intent = new Intent();
					// intent.setClass(MainActivity.this,
					// RankViewActivity.class);
					// startActivity(intent);

					break;
				case 2: // 提醒设置

					Intent intent2 = new Intent();
					intent2.setClass(MainActivity.this, DailyExpressionActivity.class);
					startActivity(intent2);

					// Dialog dialog = new
					// NotificationDialog(MainActivity.this);
					// // dialog.setTitle("设置提醒时间");
					// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					// dialog.getWindow().setBackgroundDrawable(
					// new BitmapDrawable());
					// dialog.show();
					// WindowManager windowManager = getWindowManager();
					// WindowManager.LayoutParams windowParams = dialog
					// .getWindow().getAttributes();
					// Display d = windowManager.getDefaultDisplay(); //
					// 获取屏幕宽、高用
					// windowParams.width = (int) (d.getWidth() * 0.9); // 设置宽度
					// windowParams.height = (int) (d.getHeight() * 0.75); //
					// 设置高度
					// dialog.getWindow().setAttributes(windowParams);
					// dialog.findViewById(R.id.button1);

					break;
				case 3:// 难度设置

					Intent intent3 = new Intent();
					intent3.setClass(MainActivity.this, RankViewActivity.class);
					startActivity(intent3);

					// Dialog dialog2 = new DifficultDialog(MainActivity.this);
					// // dialog2.setTitle("设置难度");
					// dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
					// dialog2.getWindow().setBackgroundDrawable(
					// new BitmapDrawable());
					// dialog2.show();
					// WindowManager windowManager2 = getWindowManager();
					// WindowManager.LayoutParams windowParams2 = dialog2
					// .getWindow().getAttributes();
					// Display d2 = windowManager2.getDefaultDisplay(); //
					// 获取屏幕宽、高用
					// windowParams2.width = (int) (d2.getWidth() * 0.9); //
					// 设置宽度
					// windowParams2.height = (int) (d2.getHeight() * 0.5); //
					// 设置高度
					// dialog2.getWindow().setAttributes(windowParams2);

					break;
				case 4:

					Intent intent4 = new Intent(MainActivity.this, CourceListActivity.class);
					Bundle bundle = new Bundle();
					// 此处为测试
					bundle.putString("username", userName);
					intent4.putExtras(bundle);
					startActivity(intent4);

					// Dialog dialog3 = new TimerDialog(MainActivity.this);
					// // dialog2.setTitle("设置难度");
					// dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
					// dialog3.getWindow().setBackgroundDrawable(
					// new BitmapDrawable());
					// dialog3.show();
					// WindowManager windowManager3 = getWindowManager();
					// WindowManager.LayoutParams windowParams3 = dialog3
					// .getWindow().getAttributes();
					// Display d3 = windowManager3.getDefaultDisplay(); //
					// 获取屏幕宽、高用
					// windowParams3.width = (int) (d3.getWidth() * 0.9); //
					// 设置宽度
					// windowParams3.height = (int) (d3.getHeight() * 0.5); //
					// 设置高度
					// dialog3.getWindow().setAttributes(windowParams3);
					break;
				case 5:

					Intent intent5 = new Intent();
					intent5.setClass(MainActivity.this, Setting.class);
					startActivity(intent5);

					// 单词本显示
					// Intent intent5 = new Intent(MainActivity.this,
					// CourceListActivity.class);
					// Bundle bundle = new Bundle();
					// // 此处为测试
					// bundle.putString("username", userName);
					// intent5.putExtras(bundle);
					// startActivity(intent5);
					break;
				case 6:// 注销

					dialog();

					break;

				default:
					break;

				}

			}

		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		// TODO Auto-generated method stub
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_MENU:
			slideMenu.toggle(true);
			break;
		case KeyEvent.KEYCODE_BACK:

			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000)
			{ // 如果两次按键时间间隔大于2秒，则不退出
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// 更新firstTime
				return true;
			} else
			{ // 两次按键小于2秒时，退出应用
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(startMain);
				System.exit(0);// 退出程序
			}

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 加载ViewPager
	 */
	public void loadViewPager()
	{
		views = new ArrayList<View>();

		ImageView im = new ImageView(this);
		im.setBackgroundResource(R.drawable.vp3);
		ImageView im2 = new ImageView(this);
		im2.setBackgroundResource(R.drawable.vp2);
		ImageView im3 = new ImageView(this);
		im3.setBackgroundResource(R.drawable.vp1);
		ImageView im4 = new ImageView(this);
		im4.setBackgroundResource(R.drawable.vp4);

		views.add(im);
		views.add(im2);
		views.add(im3);
		views.add(im4);

		vp = (ViewPager) findViewById(R.id.viewpager);
		PagerAdapter pa = new PagerAdapter()
		{

			@Override
			public int getCount()
			{
				// TODO Auto-generated method stub
				return views.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1)
			{
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			@Override
			public Object instantiateItem(View container, int position)
			{
				// TODO Auto-generated method stub
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}

			@Override
			public void destroyItem(View container, int position, Object object)
			{
				// TODO Auto-generated method stub
				((ViewPager) container).removeView(views.get(position));
			}
		};
		vp.setAdapter(pa);
		vp.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				// TODO Auto-generated method stub
				switch (event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
				{
					iscontinue = false;
					break;
				}
				case MotionEvent.ACTION_UP:
				{
					iscontinue = true;
					break;
				}
				case MotionEvent.ACTION_MOVE:
				{
					iscontinue = false;
					break;
				}
				default:
				{
					iscontinue = true;
					break;
				}

				}
				return false;
			}
		});

		try
		{
			Field mScroller;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			Interpolator sInterpolator = new AccelerateInterpolator();
			FixedSpeedScroller scroller = new FixedSpeedScroller(vp.getContext(), sInterpolator);
			// scroller.setFixedDuration(5000);
			mScroller.set(vp, scroller);
		} catch (NoSuchFieldException e)
		{
		} catch (IllegalArgumentException e)
		{
		} catch (IllegalAccessException e)
		{
		}

	}

	/**
	 * 加载数据
	 */
	public void initData()
	{
		jpsentence = new String[]
		{ "1いつも親とけんかばかりして、ついには家出までする始末だ。", "2いつも親とけんかばかりして、ついには家出までする始末だ。／いつもおやとけんかばかりして、ついにはいえでまでするしまつだ。", "3／いつもおやとけんかばかりして、ついにはいえでまでするしまつだ。", "4いつも親とけんかばかりして、ついには家出までする始末だ。", "5いつも親とけん出までする始末だ。" };
		cnsentence = new String[]
		{ "[中文解释] 经常和父母吵架，走的结果。", "[中文解释] 经常和父母吵架，最终导致了离家出走的结果。", "[中文解释] 最终导致了离家出走的结果。", "[中文解释] 经常和父母吵架的结果。", "[中文解释] 最终导致了离家出走,经常和父母吵架的结果。" };
		anlysentence = new String[]
		{ "[单词及语法解说] 可以用于同辈间。", "[单词及语法解说] 诉说烦恼。", "[单词及语法解说] 可以用于同辈间诉说烦恼。", "[单词及语法解说] 可以用诉说烦恼。", "[单词及语法解说] 于同辈间诉说烦可以用诉说烦恼。" };
	}

	/**
	 * 开始动画
	 */
	public void startAnim()
	{
		new Thread()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				while (true)
				{
					if (iscontinue)
					{
						try
						{
							Thread.sleep(3000);
							if (curImg < 3)
							{
								curImg++;
							} else
							{
								curImg = 0;
							}
							mHandler.sendEmptyMessage(0);
							Log.i("curimg", "" + curImg);
						} catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

		}.start();
	}

	protected void dialog()
	{
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage("确定要退出云裳吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(startMain);
				System.exit(0);// 退出程序
			}
		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

}
