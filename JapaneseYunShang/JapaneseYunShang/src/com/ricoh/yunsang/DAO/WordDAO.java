package com.ricoh.yunsang.DAO;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ricoh.yunsang.DBService.WordService;

public class WordDAO {
	public String wordReserve = null;
	private DicDatabaseHelper dbhelper;
	// 问题来自recited表false 来自underlearn ture;
	public boolean flagOfQuestionFrom = false;

	public WordDAO(DicDatabaseHelper dbhelper) {
		this.dbhelper = dbhelper;
	}

	private int getWordId(String word) {
		Cursor cursor = dbhelper.getReadableDatabase().rawQuery(
				"select _id from dictionary where word=?",
				new String[] { word });
		cursor.moveToNext();
		return cursor.getInt(0);
	}

	// copy
	public boolean copyToUnderlearn() {
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		Cursor tempcursor = db.rawQuery("select _id from dictionary", null);
		while (tempcursor.moveToNext()) {
			int temp = tempcursor.getInt(0);
			db.execSQL("insert into underlearn values(null,0,?)",
					new Integer[] { temp });

		}
		return true;

	}

	// question opt
	public Cursor getQuestion() {
		Cursor cursor = null;
		int count = 0;
		while (true) {
			cursor = getQuestionByRecited();
			if (cursor.getCount() != 0)
				flagOfQuestionFrom = false;
			else {
				cursor = getQuestionByUnderlearn();
				flagOfQuestionFrom = true;
			}
			if (cursor == null || cursor.getCount() == 0) {
				changeRecitedSign();
			} else {
				return cursor;
			}
			count++;
			if (count > 20) {
				return null;
			}
		}

	}

	// 辅助函数，当两张表中全无符合条件时操作
	private void changeRecitedSign() {
		dbhelper.getWritableDatabase().execSQL(
				"update recited set ebmsnhaus_sign = ebmsnhaus_sign + 1");
	}

	private Cursor getQuestionByUnderlearn() {
		Cursor cur1 = dbhelper
				.getReadableDatabase()
				.rawQuery(
						"select * from dictionary where dictionary._id in (select ul.dictionary_wordid from underlearn as ul where ul.learn_time < 5 order by random() limit 1)",
						null);
		return cur1;
	}

	private Cursor getQuestionByRecited() {
		Cursor cur2 = dbhelper
				.getReadableDatabase()
				.rawQuery(
						"select * from dictionary where dictionary._id in (select r.dictionary_wordid from recited as r where "
								+ "r.ebmsnhaus_sign in (1,2,3,5,7,9,11,13)"
								+ " and review_info < 3 order by random() limit 1)",
						null);
		return cur2;
	}

	// 取得其他干扰项
	public Cursor getOtherOption(int count) {
		wordReserve = new String(WordService.WordReserve);
		if (count == 4) {
			return dbhelper
					.getReadableDatabase()
					.rawQuery(
							"select * from dictionary where word <> ? order by random() limit 3",
							new String[] { wordReserve });
		} else {
			return dbhelper
					.getReadableDatabase()
					.rawQuery(
							"select * from dictionary where word <> ? order by random() limit 6",
							new String[] { wordReserve });
		}
	}

	// 根据结果修改数据库数据
	public void changeRecitedByResult(Boolean result, String wordReserve) {
		if (result) {
			dbhelper.getWritableDatabase()
					.execSQL(
							"update recited set review_info = review + 1 where dictionary_wordid in (select _id from dictionary whre word = ?)",
							new String[] { wordReserve });
		}
	}

	public void changeUnderlearnByResult(Boolean result, String wordReserve) {
		dbhelper.getWritableDatabase()
				.execSQL(
						"update underlearn set learn_time = learn_time + 1 where dictionary_wordid in (select _id from dictionary where word = ?)",
						new String[] { wordReserve });
		Cursor cursorResult = dbhelper
				.getReadableDatabase()
				.rawQuery(
						"select dictionary_wordid from underlearn where learn_time >=5",
						null);
		if (cursorResult.getCount() != 0) {
			while (cursorResult.moveToNext()) {
				insertIntoRecited(cursorResult.getInt(0));
			}
		}
	}

	// word note opt
	public void insertIntoWordnote(String word) {
		int id = getWordId(word);
		dbhelper.getWritableDatabase().execSQL(
				"insert into word_note values(null,?)", new Integer[] { id });
	}

	public void deleteFromWordnote(String word) {
		int id = getWordId(word);
		dbhelper.getWritableDatabase().execSQL(
				"delete from word_note where dictionary_wordid = ?",
				new Integer[] { id });
	}

	public Cursor seeWordnote() {
		Cursor cursor = dbhelper
				.getReadableDatabase()
				.rawQuery(
						"select word_note._id,dictionary.word,dictionary.explanation_1,dictionary.explanation_2,dictionary.explanation_3 from dictionary,word_note where dictionary._id = word_note.dictionary_wordid",
						null);
		return cursor;
	}

	// 同步单词本数据
	public Cursor getWordnoteWordId() {
		Cursor cursor = dbhelper.getReadableDatabase().rawQuery(
				"select _id from wordnote", null);
		return cursor;

	}

	// recited opt
	void insertIntoRecited(int wordid) {
		dbhelper.getWritableDatabase().execSQL(
				"insert into Recited values(null,0,0,?)",
				new Integer[] { wordid });
	}

	// under learn opt
	public void deleteFromUnderlearn(String word) {
		int id = getWordId(word);
		dbhelper.getWritableDatabase().execSQL(
				"delete from Underlearn where dictionary_wordid = ?",
				new Integer[] { id });
	}

	public void clearTable(String table) {
		dbhelper.getWritableDatabase().execSQL("delete from ?",
				new String[] { table });
	}
	
	public int getWords(){
		
		return 0;
	}
	public int getAllWordCount(){
		Cursor cursor= 	dbhelper.getWritableDatabase().rawQuery("select * from dictionary",null);
		int number = cursor.getCount();
		cursor.close();
		return number;
	}

	public int getRecitedWordCount() {
		Cursor cursor= 	dbhelper.getWritableDatabase().rawQuery("select * from recited",null);
		int number = cursor.getCount();
		cursor.close();
		return number;
	}


}
