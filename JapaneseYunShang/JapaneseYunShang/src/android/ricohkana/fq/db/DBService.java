package android.ricohkana.fq.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.ricoh.R;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DBService extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "kana_data.db";
	private static final int DATABASE_VISION = 1;

	 // 这个是自己项目包路径
//	public static final String PACKAGE_NAME = "android.ricohkana.fq";
	public static final String PACKAGE_NAME = "com.ricoh";
	
	// 获取存储位置地址
	public static final String DATABASE_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME+"/databases";

	public DBService(Context paramContext) {
		super(paramContext, "kana_data.db", null, 1);
	}

	public static void openDatabase(Context context) throws Exception {
		String databaseFilename = DATABASE_PATH + "/" + DATABASE_NAME;

		File dir = new File(DATABASE_PATH);

		if (!dir.exists()) {
			dir.mkdir();
		}

		File databasefile = new File(dir,DATABASE_NAME);

		if(!databasefile.exists())
		{
			databasefile.createNewFile();

			//第一次创建数据库的时候要从raw文件夹把kana_data.db的数据拷过来，以后就不用了，不然会把用户设置的颜色值都覆盖掉
			InputStream is = context.getResources().openRawResource(
					R.raw.kana_data);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(databasefile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] buffer = new byte[8192];
			int count = 0;
			try {
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				fos.close();
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
				databaseFilename, null);
		database.close();
	}

	public void update(String table, ContentValues values, String whereClause, String[] whereArgs)
	{
		getWritableDatabase().update(table, values, whereClause, whereArgs);
	}

	public void execSQL(String paramString) {
		getWritableDatabase().execSQL(paramString);
	}

	public void execSQL(String paramString, Object[] paramArrayOfObject) {
		getWritableDatabase().execSQL(paramString, paramArrayOfObject);
	}

	public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
	}

	public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1,
			int paramInt2) {
	}

	public Cursor query(String paramString, String[] paramArrayOfString) {
		return getWritableDatabase().rawQuery(paramString, paramArrayOfString);
	}
}