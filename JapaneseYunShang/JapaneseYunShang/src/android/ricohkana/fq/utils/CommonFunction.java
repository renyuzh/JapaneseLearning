package android.ricohkana.fq.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.ricohkana.fq.db.DBService;
import android.ricohkana.fq.db.KanaObject;

import java.io.IOException;
import java.util.Map;

public class CommonFunction {
	public static SoundPool pool;
	public static Map<String, Integer> poolMap;

	public static boolean InitFlag = false;
	public static boolean SoundLoadedFlag = false;

	/**
	 * 播放音频
	 * @param paramString 文件名
	 * @param paramContext
	 * @param mediaPlayer
	 */
	public static void playMusic(String paramString, Context paramContext, MediaPlayer mediaPlayer) {
		if (!SoundLoadedFlag) {
			 new SpeechTask(paramContext, mediaPlayer).execute(new String[] { paramString
			 +".mp3" });
		} else {
			CommonFunction.pool.play(CommonFunction.poolMap.get(paramString),
					1.0f, 1.0f, 0, 0, 1.0f);
		}
	}

	public static String selectKanaById(int paramInt, Context paramContext) {
		DBService localDBService = new DBService(paramContext);
		String str1 = "select kana from Kana where id = " + paramInt;
		Cursor localCursor = null;
		try {
			localCursor = localDBService.query(str1, null);
			localCursor.moveToFirst();
			String str2 = localCursor.getString(0);
			return str2;
		} catch (Exception localException) {
			localException.printStackTrace();
			return null;
		} finally {
			localDBService.close();
			localCursor.close();
		}
	}

	public static KanaObject selectKanaObjectById(int paramInt,
			Context paramContext) {
		KanaObject localKanaObject = new KanaObject();
		localKanaObject.setId(paramInt);
		DBService localDBService = new DBService(paramContext);
		String str = "select * from Kana where id = " + paramInt;
		Cursor localCursor = null;
		try {
			localCursor = localDBService.query(str, null);
			localCursor.moveToFirst();
			
			localKanaObject.setCategory(localCursor.getInt(localCursor
					.getColumnIndex("category")));
			localKanaObject.setType(localCursor.getInt(localCursor
					.getColumnIndex("type")));
			localKanaObject.setRow(localCursor.getInt(localCursor
					.getColumnIndex("row")));
			localKanaObject.setSection(localCursor.getInt(localCursor
					.getColumnIndex("section")));
			localKanaObject.setKana(localCursor.getString(localCursor
					.getColumnIndex("kana")));
			localKanaObject.setRomeword(localCursor.getString(localCursor
					.getColumnIndex("romeword")));
			localKanaObject
					.setPronunciationName(localCursor.getString(localCursor
							.getColumnIndex("pronunciationName")));
			return localKanaObject;
		} catch (Exception localException) {
			localException.printStackTrace();
			return localKanaObject;
		} finally {
			localDBService.close();
			localCursor.close();
		}
	}

	public static String selectRomewordById(int paramInt, Context paramContext) {
		DBService localDBService = new DBService(paramContext);
		String str1 = "select romeword from Kana where id = " + paramInt;
		Cursor localCursor = null;
		try {
			localCursor = localDBService.query(str1, null);
			localCursor.moveToFirst();
			String str2 = localCursor.getString(0);
			return str2;
		} catch (Exception localException) {
			localException.printStackTrace();
			return null;
		} finally {
			localDBService.close();
			localCursor.close();
		}
	}
}
