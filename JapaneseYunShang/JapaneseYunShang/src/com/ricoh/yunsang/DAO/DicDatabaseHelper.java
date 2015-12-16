package com.ricoh.yunsang.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DicDatabaseHelper extends SQLiteOpenHelper {

	public DicDatabaseHelper(Context context, String name,
			 int version) {
		super(context, name,null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}

}
