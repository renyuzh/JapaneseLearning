package android.ricohkana.fq.db;

import android.ricohkana.fq.models.Dict;

public class KanaObject {
	private int category;
	private int id;
	private String kana;
	private String pronunciationName;
	private String romeword;
	private String info;

	private int row;
	private int section;
	private int type;

	public int color;
	public Dict dicts;
	public String memoryHint;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public Dict getDicts() {
		return dicts;
	}

	public void setDicts(Dict dicts) {
		this.dicts = dicts;
	}

	public String getMemoryHint() {
		return memoryHint;
	}

	public void setMemoryHint(String memoryHint) {
		this.memoryHint = memoryHint;
	}

	public int getCategory() {
		return this.category;
	}

	public int getId() {
		return this.id;
	}

	public String getKana() {
		return this.kana;
	}

	public String getPronunciationName() {
		return this.pronunciationName;
	}

	public String getRomeword() {
		return this.romeword;
	}

	public int getRow() {
		return this.row;
	}

	public int getSection() {
		return this.section;
	}

	public int getType() {
		return this.type;
	}

	public void setCategory(int paramInt) {
		this.category = paramInt;
	}

	public void setId(int paramInt) {
		this.id = paramInt;
	}

	public void setKana(String paramString) {
		this.kana = paramString;
	}

	public void setPronunciationName(String paramString) {
		this.pronunciationName = paramString;
	}

	public void setRomeword(String paramString) {
		this.romeword = paramString;
	}

	public void setRow(int paramInt) {
		this.row = paramInt;
	}

	public void setSection(int paramInt) {
		this.section = paramInt;
	}

	public void setType(int paramInt) {
		this.type = paramInt;
	}
}