package android.ricohkana.fq.hiragana;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import com.ricoh.R;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.ricohkana.fq.utils.*;
import android.ricohkana.fq.db.*;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class KanaDetailActivity extends Activity
{

	DBService localDBService;

	public MediaPlayer mediaPlayer;
	public MediaPlayer mpeg;

	boolean isNoError = true;

	public String romewordStr = null;
	public String kanaStr = null;

	String API = "http://translate.google.com/translate_tts?ie=UTF-8&&tl=ja&&q=%s";

	// 这个是自己项目包路径
	// public static final String PACKAGE_NAME = "android.ricohkana.fq";
	public static final String PACKAGE_NAME = "com.ricoh";

	// 获取存储位置地址
	public static final String AUDIO_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME + "/SimpleGojuon/";

	private String animFileName = "";
	private AnimationDrawable animationDrawable = null;
	private int biShunNum = 0;
	private ImageView biShunImageView = null;

	private AudioManager audio;
	private ImageButton backBtn = null;
	private ImageView backBtnXiaoguo;
	private ImageButton kanachangeButton;

	private Button biShunBtn = null;
	private Button markColorBtn = null;
	private Button rememberBtn = null;

	private LinearLayout btnLinearLayout = null;
	private RelativeLayout kanaRelativeLayout = null;
	private RelativeLayout markColorLinearLayout = null;
	private RelativeLayout rememberLinearLayout = null;

	private Button clearBtn = null;
	private TextView exampleTextView = null;
	private int duration = 0;
	private Handler animHandler = null;
	private int height = 0;
	private ImageButton leftBtn = null;
	private ProgressBar pb;
	private String pictureFileName = "";
	private Bitmap practiceBitmap = null;
	private Button practiceBtn = null;
	private PracticeImageView practiceImageView = null;
	private int practicePicPosition = 0;
	static String pronunciationName = ""; // private？
	private ImageButton rightBtn = null;
	private TextView romeWordTextView = null;
	private TextView tvKanaTextView = null;
	private TextView tvRememberKana = null;
	private TextView tvRemember = null;

	private int section = 0;
	private String selectSql = "";
	private ImageButton soundBtn = null;
	private ImageButton wordsoundBtn = null;
	private TextView titleTextView = null;
	private Toast toast;
	private int width = 0;

	SharedPreferences sp;
	String dictionaryName;
	String userName;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		localDBService = new DBService(this);
		try
		{
			localDBService.openDatabase(this);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.selectSql = "select kana,romeword,information,pronunciationName,animFileName,practicePicId,color from Kana where type = ? and row = ? and section = ? and category = ?";
		requestWindowFeature(1);
		setContentView(R.layout.kana_detail);

		userName = getIntent().getExtras().getString("username");
		sp = getSharedPreferences("thefirsttime", MODE_PRIVATE);
		dictionaryName = sp.getString("COURSE", "");

		findViews();

		mediaPlayer = new MediaPlayer();
		startSound(Constant.row, Constant.section, -1);
		setVolume();
		this.audio = ((AudioManager) getSystemService("audio"));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.menu_file, menu);
		Intent localIntent1 = new Intent(this, InfoActivity.class);
		localIntent1.putExtra("fromActivity", "KanaDetailActivity");
		menu.findItem(R.id.info_menu).setIntent(localIntent1);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 数据库查询以及一些控件的设置
	 * 
	 * @param paramInt1
	 *            假名所在行
	 * @param paramInt2
	 *            假名所在段
	 */
	private void selectDBs(int paramInt1, int paramInt2)
	{

		String[] arrayOfString = new String[4];
		arrayOfString[0] = String.valueOf(Constant.type);
		arrayOfString[1] = String.valueOf(paramInt1);
		arrayOfString[2] = String.valueOf(paramInt2);
		arrayOfString[3] = String.valueOf(Constant.yin);
		Cursor localCursor = null;

		try
		{
			localCursor = localDBService.query(this.selectSql, arrayOfString);

			if (localCursor.moveToFirst())
			{

				int color = localCursor.getInt(localCursor.getColumnIndex("color"));
				if (1 == color)
				{
					this.tvKanaTextView.setTextColor(getResources().getColor(R.color.c_red));
					this.romeWordTextView.setTextColor(getResources().getColor(R.color.c_red));
				} else if (0 == color)
				{
					this.tvKanaTextView.setTextColor(getResources().getColor(R.color.c_grey));
					this.romeWordTextView.setTextColor(getResources().getColor(R.color.c_grey));
				} else
				{
					System.out.println("KanaDetailActivity: color error");
				}

				this.kanaStr = localCursor.getString(localCursor.getColumnIndex("kana"));
				this.tvKanaTextView.setText(this.kanaStr);
				this.romewordStr = localCursor.getString(localCursor.getColumnIndex("romeword"));
				this.romeWordTextView.setText(this.romewordStr);

				this.tvRememberKana.setText(this.kanaStr);
				this.tvRemember.setText(Constant.memoriesMap.get(this.kanaStr));

				this.animFileName = localCursor.getString(localCursor.getColumnIndex("animFileName"));
				this.practicePicPosition = localCursor.getInt(localCursor.getColumnIndex("practicePicId"));
				this.pronunciationName = localCursor.getString(localCursor.getColumnIndex("pronunciationName"));
			}

			String sampleString = getSample();
			exampleTextView.setText(sampleString);

			if (sampleString.indexOf("(") == -1)
			{
				exampleTextView.setVisibility(View.GONE);
				wordsoundBtn.setVisibility(View.GONE);
			} else
			{
				exampleTextView.setVisibility(View.VISIBLE);
				wordsoundBtn.setVisibility(View.VISIBLE);
			}

			if (this.clearBtn.getVisibility() == 0)
			{
				this.practiceImageView.clear();
				setPracticePicture();
			}

			if (0 == Constant.row && 0 == Constant.section)
			{
				this.leftBtn.setEnabled(false);
				this.leftBtn.setVisibility(View.INVISIBLE);
			} else
			{
				this.leftBtn.setEnabled(true);
				this.leftBtn.setVisibility(View.VISIBLE);
			}

			// 两个“||”分开的是清音、浊音、拗音假名表最后一个假名
			if ((0 == Constant.yin && 10 == Constant.row && 0 == Constant.section) || (1 == Constant.yin && 4 == Constant.row && 4 == Constant.section) || (2 == Constant.yin && 10 == Constant.row && 2 == Constant.section))
			{
				this.rightBtn.setEnabled(false);
				this.rightBtn.setVisibility(View.INVISIBLE);
			} else
			{
				this.rightBtn.setEnabled(true);
				this.rightBtn.setVisibility(View.VISIBLE);
			}
		} catch (Exception localException)
		{
			localException.printStackTrace();
		} finally
		{
			localCursor.close();
		}
	}

	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
	{

		boolean bool = true;

		// 用户按了降低音量键
		if (paramInt == 25)
		{
			this.audio.adjustStreamVolume(3, -1, 5);
			bool = true;
		} else if (paramInt == 24)
		{
			// 用户按了提高音量键
			this.audio.adjustStreamVolume(3, 1, 5);
			bool = true;
		} else if (paramInt == KeyEvent.KEYCODE_BACK)
		{
			// 按下键盘上返回按钮
			// Intent localIntent = new Intent(this, ShowKanasActivity.class);
			finish();
			// startActivity(localIntent);
			bool = true;
		} else
		{
			bool = super.onKeyDown(paramInt, paramKeyEvent);
		}

		return bool;
	}

	/**
	 * 播放音频
	 * 
	 * @param paramInt1
	 *            当前行
	 * @param paramInt2
	 *            当前段
	 * @param paramInt3
	 *            等于0表示该函数是通过点击向左箭头进来的，等于1该函数是通过点击向右箭头进来的，-1表示其它情况
	 */
	private void startSound(int paramInt1, int paramInt2, int paramInt3)
	{
		if (-1 == paramInt2)
		{// paramInt2 == -1表示点击向左箭头时已到该行行首
			// 转到上一行
			Constant.row = paramInt1 - 1;

			// 这个地方要注意，因为拗音是三个假名一行
			if (2 != Constant.yin)
			{
				Constant.section = 4;
			} else
			{
				Constant.section = 2;
			}

		} else if ((2 != Constant.yin && 5 == paramInt2) || (2 == Constant.yin && 3 == paramInt2))
		{// 点击向右箭头时已到该行末尾
			// 转到下一行
			Constant.row = paramInt1 + 1;
			Constant.section = 0;
		} // 跳过轻音假名表中几个空格
		else if (0 == Constant.yin && 7 == paramInt1 && 1 == paramInt2)
		{
			if (0 == paramInt3)
			{
				Constant.row = paramInt1;
				Constant.section = paramInt2 - 1;
			} else if (1 == paramInt3)
			{
				Constant.row = paramInt1;
				Constant.section = paramInt2 + 1;
			}
		} else if (0 == Constant.yin && 7 == paramInt1 && 3 == paramInt2)
		{
			if (0 == paramInt3)
			{
				Constant.row = paramInt1;
				Constant.section = paramInt2 - 1;
			} else if (1 == paramInt3)
			{
				Constant.row = paramInt1;
				Constant.section = paramInt2 + 1;
			}
		} else if (0 == Constant.yin && 9 == paramInt1 && 1 == paramInt2)
		{
			Constant.row = paramInt1;
			Constant.section = paramInt2 + 3;
		} else if (0 == Constant.yin && 9 == paramInt1 && 3 == paramInt2)
		{
			Constant.row = paramInt1;
			Constant.section = paramInt2 - 3;
		} else
		{
			Constant.row = paramInt1;
			Constant.section = paramInt2;
		}

		selectDBs(Constant.row, Constant.section);

		String str = KanaDetailActivity.this.pronunciationName;
		if (str.equals("do.mp3"))
		{
			str = "doo.mp3";
		}

		CommonFunction.playMusic(str.split("\\.")[0], KanaDetailActivity.this, mediaPlayer);
	}

	public void onClick(View paramView)
	{
		switch (paramView.getId())
		{
		case R.id.back_btn:
			Intent localIntent = new Intent(this, ShowKanasActivity.class);
			localIntent.putExtra("username", userName);
			localIntent.putExtra("dictionary", dictionaryName);
			finish();
			startActivity(localIntent);
			break;
		case R.id.back_btn_xiaoguo:
			Intent localIntent2 = new Intent(this, ShowKanasActivity.class);
			localIntent2.putExtra("username", userName);
			localIntent2.putExtra("dictionary", dictionaryName);
			finish();
			startActivity(localIntent2);
			break;
		case R.id.clear_btn:
			this.practiceImageView.clear();
			break;
		case R.id.btnPrev:
			this.section = (Constant.section - 1);
			// 排除第一行第一个假名的情况
			startSound(Constant.row, this.section, 0);
			changeKana();
			break;
		case R.id.btnNext:
			this.section = (1 + Constant.section);
			startSound(Constant.row, this.section, 1);
			changeKana();
			break;
		case R.id.sound_btn:
			startSound(Constant.row, Constant.section, -1);
			break;
		case R.id.markcolor_btn:
			markColorLinearLayout.setVisibility(View.VISIBLE);
			kanaRelativeLayout.setVisibility(View.GONE);
			rememberLinearLayout.setVisibility(View.GONE);
			this.titleTextView.setText(R.string.markcolor);
			break;
		case R.id.bi_shun_btn:
			markColorLinearLayout.setVisibility(View.GONE);
			kanaRelativeLayout.setVisibility(View.VISIBLE);
			rememberLinearLayout.setVisibility(View.GONE);
			this.titleTextView.setText(R.string.bi_shun);
			this.practiceImageView.setVisibility(View.INVISIBLE);
			this.biShunImageView.setVisibility(View.VISIBLE);
			this.clearBtn.setVisibility(View.INVISIBLE);
			setBitmap(this.animFileName);
			startAnimation();
			break;
		case R.id.practice_btn:
			markColorLinearLayout.setVisibility(View.GONE);
			kanaRelativeLayout.setVisibility(View.VISIBLE);
			rememberLinearLayout.setVisibility(View.GONE);
			this.titleTextView.setText(R.string.practice);
			this.clearBtn.setVisibility(View.VISIBLE);
			this.biShunImageView.setVisibility(View.INVISIBLE);
			this.practiceImageView.setVisibility(View.VISIBLE);
			this.practiceImageView.clear();
			setPracticePicture();
			break;
		case R.id.remember_btn:
			markColorLinearLayout.setVisibility(View.GONE);
			kanaRelativeLayout.setVisibility(View.GONE);
			rememberLinearLayout.setVisibility(View.VISIBLE);
			this.titleTextView.setText(R.string.remember);
			break;
		case R.id.markcolor_hard_btn:
			changeKanaColor(1, R.color.c_red);
			break;
		case R.id.markcolor_easy_btn:
			changeKanaColor(0, R.color.c_grey);
			break;
		case R.id.kanachange_btn:
			// 平假名、片假名切换
			Constant.type = 1 - Constant.type;
			System.out.println("Constant.type: " + Constant.type);
			startSound(Constant.row, Constant.section, -1);
			changeKana();
			break;
		default:

		}
	}

	/**
	 * 更改颜色：更改textview颜色同时更新数据库
	 * 
	 * @param color
	 *            颜色，1为红色red，0为灰色grey
	 * @param id
	 *            颜色资源id 红色R.color.c_red 灰色R.color.c_grey
	 */
	private void changeKanaColor(int color, int id)
	{
		localDBService.execSQL("update Kana set color=? where type=? and row=? and section=? and category=?", new Object[]
		{ color, Constant.type, Constant.row, Constant.section, Constant.yin });
		this.tvKanaTextView.setTextColor(getResources().getColor(id));
		this.romeWordTextView.setTextColor(getResources().getColor(id));
	}

	/**
	 * 保存方法
	 * 
	 * @param bm
	 * @param picName
	 *            文件名
	 */
	public void saveBitmap(Bitmap bm, String picName)
	{
		String BITMAP_PATH = "/data/data/" + DBService.PACKAGE_NAME + "/bitmaps"; // 获取存储位置地址

		File dir = new File(BITMAP_PATH);

		if (!dir.exists())
		{
			dir.mkdir();
		}

		File bitmapFile = new File(dir, picName);

		if (!bitmapFile.exists())
		{
			try
			{
				bitmapFile.createNewFile();
				FileOutputStream out = new FileOutputStream(bitmapFile);
				bm.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.flush();
				out.close();
			} catch (Exception e)
			{
				// TODO: handle exception
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void setBitmap(String paramString)
	{
		this.biShunImageView.setImageBitmap(null);
		BitmapDrawable localBitmapDrawable = new BitmapDrawable(Bitmap.createBitmap(new BinReader().readImage(paramString, 0L), 0, 0, 250, 250));
		this.biShunImageView.setBackgroundDrawable(localBitmapDrawable);
	}

	@SuppressWarnings("deprecation")
	private void setPracticePicture()
	{
		this.practiceBitmap = getPracticedBinBitmap("practice_ping.bin", "practice_pian.bin", this.practicePicPosition);
		BitmapDrawable localBitmapDrawable = new BitmapDrawable(this.practiceBitmap);
		this.practiceImageView.setBackgroundDrawable(localBitmapDrawable);
	}

	/**
	 * 
	 * @param paramString1
	 * @param paramString2
	 * @param paramInt
	 * @return
	 */
	private Bitmap getPracticedBinBitmap(String paramString1, String paramString2, int paramInt)
	{
		// Constant.type == 0表示此时应该显示平假名;Constant.type == 1表示应该显示片假名
		if (Constant.type == 0)
		{
			this.pictureFileName = paramString1;
		} else if (Constant.type == 1)
		{
			this.pictureFileName = paramString2;
		}

		Bitmap localBitmap = new BinReader().readImage(this.pictureFileName, paramInt);
		return localBitmap;
	}

	private void setVolume()
	{
		this.pb = new ProgressBar(this, null, 16842872);
		this.pb.setMax(10);
		this.pb.setProgress(2);
		this.pb.setLayoutParams(new LinearLayout.LayoutParams(300, -2));
		this.toast = Toast.makeText(this, R.string.volume_control, 0);
		this.toast.setGravity(17, 0, 0);
		LinearLayout localLinearLayout = (LinearLayout) this.toast.getView();
		localLinearLayout.setGravity(17);
		localLinearLayout.addView(this.pb, 1);
	}

	private void startAnimation()
	{
		this.animHandler.postDelayed(new Runnable()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				startBiShun();
			}
		}, 500L);
	}

	@SuppressWarnings("deprecation")
	private void startBiShun()
	{

		// 获取包含所有笔顺中间片段图片的总体图片
		Bitmap localBitmap1 = new BinReader().readImage(this.animFileName, 0L);

		// 获取笔顺中间片段图片张数，每张宽度250
		this.biShunNum = (localBitmap1.getWidth() / 250);

		saveBitmap(localBitmap1, "WholeBitmap.jpg");

		this.animationDrawable = new AnimationDrawable();

		for (int i = 0;; ++i)
		{
			if (i >= this.biShunNum)
			{
				// setOneShot里的值若为false表示循环播放，true表示只播放一次
				this.animationDrawable.setOneShot(true);
				this.biShunImageView.setImageDrawable(this.animationDrawable);
				this.animationDrawable.stop();
				// 注意，这里才开始播放动画
				this.animationDrawable.start();
				return;
			}

			Bitmap localBitmap2 = Bitmap.createBitmap(localBitmap1, i * 250, 0, 250, 250);
			// 通过addFrame 方法把每一帧要显示的内容添加进去，并设置播放间隔时间
			this.animationDrawable.addFrame(new BitmapDrawable(localBitmap2), 100);
		}
	}

	private void changeKana()
	{
		if (this.titleTextView.getText() == getString(R.string.practice))
		{
			setPracticePicture();
		} else if (this.titleTextView.getText() == getString(R.string.bi_shun))
		{
			setBitmap(this.animFileName);
			startAnimation();
		} else if (this.titleTextView.getText() == getString(R.string.remember))
		{

		}
	}

	@SuppressWarnings("deprecation")
	private void findViews()
	{
		this.kanaRelativeLayout = ((RelativeLayout) findViewById(R.id.kana_relativelayout));
		this.btnLinearLayout = ((LinearLayout) findViewById(R.id.btn_linearlayout));
		this.markColorLinearLayout = ((RelativeLayout) findViewById(R.id.kana_markcolor_relativelayout));
		this.rememberLinearLayout = ((RelativeLayout) findViewById(R.id.kana_remember_relativelayout));

		this.backBtn = ((ImageButton) findViewById(R.id.back_btn));
		this.backBtnXiaoguo = ((ImageView) findViewById(R.id.back_btn_xiaoguo));
		this.kanachangeButton = (ImageButton) findViewById(R.id.kanachange_btn);
		this.clearBtn = ((Button) findViewById(R.id.clear_btn));
		this.leftBtn = ((ImageButton) findViewById(R.id.btnPrev));
		this.rightBtn = ((ImageButton) findViewById(R.id.btnNext));
		this.soundBtn = ((ImageButton) findViewById(R.id.sound_btn));
		this.wordsoundBtn = ((ImageButton) findViewById(R.id.word_sound_btn));

		this.markColorBtn = (Button) findViewById(R.id.markcolor_btn);
		this.biShunBtn = ((Button) findViewById(R.id.bi_shun_btn));
		this.practiceBtn = ((Button) findViewById(R.id.practice_btn));
		this.rememberBtn = (Button) findViewById(R.id.remember_btn);

		this.titleTextView = ((TextView) findViewById(R.id.title_textview));
		this.biShunImageView = ((ImageView) findViewById(R.id.bi_shun_imageview));
		this.practiceImageView = ((PracticeImageView) findViewById(R.id.practice_imageview));
		this.exampleTextView = ((TextView) findViewById(R.id.tvExample));
		this.romeWordTextView = ((TextView) findViewById(R.id.tvRomaji));
		this.tvKanaTextView = ((TextView) findViewById(R.id.tvKana));

		tvRememberKana = ((TextView) findViewById(R.id.tvRememberKana));
		tvRemember = ((TextView) findViewById(R.id.tvRemember));

		this.wordsoundBtn.setOnClickListener(new SpeakListener());

		// 浊音和拗音没有笔画演示和手写练习
		if (1 == Constant.yin || 2 == Constant.yin)
		{
			this.markColorBtn.setEnabled(false);
			this.markColorBtn.setVisibility(View.INVISIBLE);

			this.biShunBtn.setEnabled(false);
			this.biShunBtn.setVisibility(View.INVISIBLE);

			this.practiceBtn.setEnabled(false);
			this.practiceBtn.setVisibility(View.INVISIBLE);

			this.rememberBtn.setEnabled(false);
			this.rememberBtn.setVisibility(View.INVISIBLE);
		}

		this.animHandler = new Handler();

		Display localDisplay = getWindowManager().getDefaultDisplay();

		Constant.paintStrokeWidth = 1 * localDisplay.getHeight() / 32;
	}

	public String getSample()
	{
		String exampleStr = "";
		if (0 == Constant.type)
		{
			InputStream inputStream = super.getClass().getResourceAsStream("/assets/kanasample/" + "h" + this.romewordStr);
			if (null != inputStream)
			{
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				try
				{
					if (null != bufferedReader)
					{
						exampleStr = bufferedReader.readLine();
					}
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (1 == Constant.type)
		{
			InputStream inputStream = super.getClass().getResourceAsStream("/assets/kanasample/" + "k" + this.romewordStr);
			if (null != inputStream)
			{
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				try
				{
					if (null != bufferedReader)
					{
						exampleStr = bufferedReader.readLine();
					}
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return exampleStr;
	}

	boolean hasAudio(String paramString)
	{
		return new File(AUDIO_PATH + paramString + ".mp3").exists();
	}

	public void mkdir()
	{
		File localFile = new File(AUDIO_PATH);
		if (!localFile.exists())
		{
			localFile.mkdir();
		}
	}

	public String simplify(String paramString)
	{
		if ((paramString.indexOf('无') != -1) || (paramString.indexOf("empty") != -1))
		{
			paramString = "";
		} else
		{
			// 注意这里两个左括号不一样，一个中文字符，一个英文字符
			int i = paramString.indexOf("(");
			int j = paramString.indexOf("（");
			if (i != -1)
			{
				return paramString.substring(0, i);
			} else if (j != -1)
			{
				return paramString.substring(0, j);
			} else
			{
				paramString = "";
			}
		}
		return paramString;
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		localDBService.close();
	}

	class SpeakListener implements View.OnClickListener
	{
		String s;

		public SpeakListener()
		{
		}

		public void onClick(View paramView)
		{
			this.s = KanaDetailActivity.this.getSample();
			String simplyString = KanaDetailActivity.this.simplify(this.s);
			if (!simplyString.equals(""))
			{
				KanaDetailActivity.SpeakTask localSpeakTask = new KanaDetailActivity.SpeakTask(simplyString);
				String[] arrayOfString = new String[1];
				arrayOfString[0] = simplyString;
				localSpeakTask.execute(arrayOfString);
			}
		}
	}

	public static void makeRootDirectory(String filePath)
	{
		File file = null;
		try
		{
			file = new File(filePath);
			if (!file.exists())
			{
				file.mkdir();
			}
		} catch (Exception e)
		{

		}
	}

	class SpeakTask extends AsyncTask<String, String, String>
	{
		String text;

		public SpeakTask(String paramString)
		{
			this.text = paramString;
		}

		protected String doInBackground(String... paramVarArgs)
		{
			System.out.println("prams[0]:" + paramVarArgs[0]);
			if (KanaDetailActivity.this.hasAudio(paramVarArgs[0]))
			{
				File localFile2 = new File(AUDIO_PATH + paramVarArgs[0] + ".mp3");
				KanaDetailActivity.this.mpeg = new MediaPlayer();
				try
				{
					FileInputStream fis = new FileInputStream(localFile2);
					KanaDetailActivity.this.mpeg.setDataSource(fis.getFD());
					KanaDetailActivity.this.mpeg.prepare();
					fis.close();
				} catch (IllegalArgumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				KanaDetailActivity.this.mpeg.start();
				KanaDetailActivity.this.mpeg.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
				{
					public void onCompletion(MediaPlayer paramAnonymousMediaPlayer)
					{
						KanaDetailActivity.this.mpeg.release();
					}
				});
			} else
			{
				try
				{
					System.out.println("http://translate.google.com/translate_tts?tl=ja&q=" + URLEncoder.encode(paramVarArgs[0], "utf-8"));
					URL localURL = new URL(new URI("http://translate.google.com/translate_tts?tl=ja&q=" + URLEncoder.encode(paramVarArgs[0], "utf-8")).toASCIIString());
					HttpURLConnection localHttpURLConnection = (HttpURLConnection) localURL.openConnection();
					localHttpURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0");
					localHttpURLConnection.setRequestMethod("GET");
					localHttpURLConnection.setDoOutput(true);
					localHttpURLConnection.connect();

					// 注意，这里要先创建路径才行，就是说要先有文件夹才能在它里面创建文件
					makeRootDirectory(AUDIO_PATH);
					File localFile1 = new File(AUDIO_PATH + paramVarArgs[0] + ".mp3");
					if (!localFile1.exists())
						localFile1.createNewFile();

					FileOutputStream localFileOutputStream = new FileOutputStream(localFile1);
					InputStream localInputStream = localHttpURLConnection.getInputStream();
					byte[] arrayOfByte = new byte[1024];

					int len = 0;
					while ((len = localInputStream.read(arrayOfByte)) > 0)
					{
						localFileOutputStream.write(arrayOfByte, 0, len);
					}
					localFileOutputStream.close();

					KanaDetailActivity.this.mpeg = new MediaPlayer();
					FileInputStream fis2 = new FileInputStream(localFile1);
					KanaDetailActivity.this.mpeg.setDataSource(fis2.getFD());
					KanaDetailActivity.this.mpeg.prepare();
					KanaDetailActivity.this.mpeg.start();
					KanaDetailActivity.this.mpeg.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
					{
						public void onCompletion(MediaPlayer paramAnonymousMediaPlayer)
						{
							KanaDetailActivity.this.mpeg.release();
						}
					});

				} catch (MalformedURLException localMalformedURLException)
				{
					KanaDetailActivity.this.isNoError = false;
					localMalformedURLException.printStackTrace();
				} catch (ProtocolException localProtocolException)
				{
					KanaDetailActivity.this.isNoError = false;
					localProtocolException.printStackTrace();
				} catch (FileNotFoundException localFileNotFoundException)
				{
					KanaDetailActivity.this.isNoError = false;
					localFileNotFoundException.printStackTrace();
				} catch (IOException localIOException)
				{
					KanaDetailActivity.this.isNoError = false;
					localIOException.printStackTrace();
				} catch (URISyntaxException localURISyntaxException)
				{
					KanaDetailActivity.this.isNoError = false;
					localURISyntaxException.printStackTrace();
				}
			}

			return paramVarArgs[0];
		}

		protected void onPostExecute(String paramString)
		{
			if (KanaDetailActivity.this.isNoError)
			{
				Toast.makeText(KanaDetailActivity.this, "成功获取音频", Toast.LENGTH_SHORT).show();
			} else
			{
				Toast.makeText(KanaDetailActivity.this, "获取音频失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}
		}

		public void onPreExecute()
		{
			System.out.println("text:" + this.text);
			Toast.makeText(KanaDetailActivity.this, "获取音频中....", Toast.LENGTH_SHORT).show();
			KanaDetailActivity.this.isNoError = true;
		}
	}
}
