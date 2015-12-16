package android.ricohkana.fq.service;

import android.content.Context;
import android.database.Cursor;
import android.ricohkana.fq.db.*;
import android.ricohkana.fq.utils.Constant;
import android.ricohkana.fq.utils.*;

import java.util.ArrayList;
import java.util.List;

public class KanaService {
	private Context mContext;

	public KanaService(Context paramContext) {
		this.mContext = paramContext;
	}

	public List<KanaObject> getKanaFromType(int yin, int type) {
		ArrayList<KanaObject> kanaObjects = new ArrayList<KanaObject>();
		DBService localDBService = new DBService(this.mContext);

		try {
			localDBService.openDatabase(this.mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String selectSql = "select row,section,kana,romeword,color from Kana where category = ? and type = ?";
		String[] arrayOfString = new String[2];
		arrayOfString[0] = String.valueOf(Constant.yin);
		arrayOfString[1] = String.valueOf(Constant.type);
		Cursor localCursor = null;

		try {
			localCursor = localDBService.query(selectSql, arrayOfString);
			localCursor.moveToFirst();
			
			while (!localCursor.isAfterLast()) {
				KanaObject kanaObject = new KanaObject();

				kanaObject.setRow(localCursor.getInt(localCursor
						.getColumnIndex("row")));
				kanaObject.setSection(localCursor.getInt(localCursor
						.getColumnIndex("section")));
				kanaObject.setRomeword(localCursor.getString(localCursor
						.getColumnIndex("romeword")));
				kanaObject.setColor(localCursor.getInt(localCursor
						.getColumnIndex("color")));
				kanaObject.setKana(localCursor.getString(localCursor
						.getColumnIndex("kana")));

				kanaObjects.add(kanaObject);
				localCursor.moveToNext();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			localDBService.close();
			if(localCursor!=null)
				localCursor.close();
		}

		return kanaObjects;
	}
	
	  public void getMemories()
	  {
	    String str1 = Config.getConfig().getDataFromAssets(this.mContext, "hiragana.xml");
	    new HiraganaXmlParser().parse(str1);
	    
	    String str2 = Config.getConfig().getDataFromAssets(this.mContext, "katakana.xml");
	    new HiraganaXmlParser().parse(str2);
	  }
}