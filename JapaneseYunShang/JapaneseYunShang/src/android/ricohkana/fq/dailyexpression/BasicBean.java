package android.ricohkana.fq.dailyexpression;
//基本单词语句信息类
//常用单词信息
public class BasicBean {

	private int id=-1;
	private int type=0;
	private String romanization="";
	private String japanese="";
	private String chinese="";
	private int isFavorite=0;
	private String soundAdress="";
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRomanization() {
		return romanization;
	}
	public void setRomanization(String romanization) {
		this.romanization = romanization;
	}
	public String getJapanese() {
		return japanese;
	}
	public void setJapanese(String japanese) {
		this.japanese = japanese;
	}
	public String getChinese() {
		return chinese;
	}
	public void setChinese(String chinese) {
		this.chinese = chinese;
	}
	public int getIsFavorite() {
		return isFavorite;
	}
	public void setIsFavorite(int isFavorite) {
		this.isFavorite = isFavorite;
	}
	public String getSoundAdress() {
		return soundAdress;
	}
	public void setSoundAdress(String soundAdress) {
		this.soundAdress = soundAdress;
	}
	
	
	
}
