package android.ricohkana.fq.dailyexpression;
//数据库操作

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
@SuppressLint("SdCardPath")
public class DaoAdapter {
//	public static final String PACKAGE_NAME = "android.ricohkana.fq";
	public static final String PACKAGE_NAME = "com.ricoh";
	private static final String DB_NAME = "jp.db";
	private static final String DB_PATH = "/data"
				+ Environment.getDataDirectory().getAbsolutePath() + "/"
				+ PACKAGE_NAME+"/databases";
	private static final int WORD=1;
	private static final int SENTENCE=2;
	  private static DaoAdapter daoAdapter = null;
	  private Context context = null;
	  private SQLiteDatabase db = null;

	  private DaoAdapter(Context paramContext)
	  {
	    this.context = paramContext;
	    databaseInit();
	  }
	  
	  public static DaoAdapter getInstance(Context paramContext)
	  {
	    if (daoAdapter == null) {
	      daoAdapter = new DaoAdapter(paramContext);
	    }
	    return daoAdapter;
	  }
	  
	  //打开数据库
	  private void openDB()
	  {
		  this.db=SQLiteDatabase.openOrCreateDatabase(DB_PATH+DB_NAME, null);
	  }
	  
	  //关闭数据库
	  private void  closeDB() 
	  {
		this.db.close();
	  }
	  
	  //返回日常用语分组信息
	  public List<GroupBean> getGroup()
	  {
		  List<GroupBean> groupInfo=new ArrayList<GroupBean>();
		  GroupBean group;
		  openDB();

			  Cursor cursor = db.rawQuery("SELECT * FROM groupJp ", new String[]{});  
			  while (cursor.moveToNext()) {
				  group=new GroupBean();
				  int id=cursor.getInt(cursor.getColumnIndex("groupId"));
				  String name = cursor.getString(cursor.getColumnIndex("groupName"));  
				  group.setGroupId(id);
				  group.setGroupName(name);
				  groupInfo.add(group);
			  }	
			 cursor.close();
        	 closeDB();
         	
		 return groupInfo;
		  
	  }
	  
	  
    private BasicBean getJpInfo(Cursor cursor,int type)
	   {
		     BasicBean jpInfo=new BasicBean();  
			 int id=cursor.getInt(cursor.getColumnIndex("id"));
			 String romanization = cursor.getString(cursor.getColumnIndex("romanization"));
			 String japanese=cursor.getString(cursor.getColumnIndex("japanese"));
			 String chinese=cursor.getString(cursor.getColumnIndex("chinese"));
			 String soundAddress=cursor.getString(cursor.getColumnIndex("soundAdress"));
			 int isFavorite=cursor.getInt(cursor.getColumnIndex("isFavorite"));
			 jpInfo.setId(id);
			 jpInfo.setRomanization(romanization);
			 jpInfo.setJapanese(japanese);
			 jpInfo.setChinese(chinese);
			 jpInfo.setSoundAdress(soundAddress);
			 jpInfo.setIsFavorite(isFavorite);
			 jpInfo.setType(type);
			 return jpInfo;
	   }
	  
	  
	  //返回某一类型的单词信息
	  public List<BasicBean> getWords(int groupId)
	  {
		  
		  List<BasicBean> wordInfo=new ArrayList<BasicBean>();
		  openDB();
		  Cursor cursor=db.rawQuery("SELECT * FROM word WHERE groupId = ?", new String[]{String.valueOf(groupId)});
		  while (cursor.moveToNext())
		  {
			 wordInfo.add(getJpInfo(cursor,WORD));
		   }
		  cursor.close();
		  closeDB();
		  return wordInfo;
	  }
	  
	  
	  
	  //返回某一类型的语句信息
	  public List<BasicBean> getSentence(int groupId)
	  {
		  List<BasicBean> sentenceInfo=new ArrayList<BasicBean>();
		  openDB();
		  Cursor cursor=db.rawQuery("SELECT * FROM sentence WHERE groupId = ?", new String[]{String.valueOf(groupId)});
		  while (cursor.moveToNext()) {

			 sentenceInfo.add(getJpInfo(cursor,SENTENCE));
		}
		  cursor.close();
		  closeDB();
		  return sentenceInfo;
	  }
	  
    //返回收藏的单词或句子信息
	   public List<BasicBean> getCollection()
	   {
		   List<BasicBean> collectionInfo=new ArrayList<BasicBean>();
		   openDB();
		   Cursor cursor;
		   cursor=db.rawQuery("SELECT * FROM word WHERE isFavorite = ?", new String[]{"1"});
		   while (cursor.moveToNext()) {
			   collectionInfo.add(getJpInfo(cursor,WORD));
			}  
		   cursor.close();
		   cursor=db.rawQuery("SELECT * FROM sentence WHERE isFavorite = ?", new String[]{"1"});
		   
		   while (cursor.moveToNext()) {     
				 collectionInfo.add(getJpInfo(cursor,SENTENCE));
			}
		   cursor.close();
		   closeDB();
		   return collectionInfo;
	   }
	  
	   
	   //返回查询的词汇语句信息
	  public List<BasicBean> getSearch(String key) 
	  {
	     List<BasicBean> result=new ArrayList<BasicBean>();
	     openDB();
	     Cursor cursor;
	     cursor=db.rawQuery("SELECT * FROM word WHERE chinese LIKE ? ", new String[]{"%"+key+"%"});
	     while(cursor.moveToNext())
	     {
	    	 result.add(getJpInfo(cursor,WORD));
	     }
	     cursor.close();
	     cursor=db.rawQuery("SELECT * FROM sentence WHERE chinese LIKE ? ", new String[]{"%"+key+"%"});
	     while(cursor.moveToNext())
	     {
	    	 result.add(getJpInfo(cursor,SENTENCE));
	     }
	     cursor.close();
	     closeDB();
	     return result;
      }
	  
	  //单词或语句添加收藏数据库操作
	  public void addCollection(int type,int id)
	  {
		  openDB();
		  if(type==WORD)
		  {
			String sql="UPDATE word SET isFavorite = 1 WHERE id ="+String.valueOf(id);
			db.execSQL(sql);  
		  }
		  if(type==SENTENCE)
		  {
			  String sql="UPDATE sentence SET isFavorite = 1 WHERE id ="+String.valueOf(id);
		      db.execSQL(sql);  
		  }
		  closeDB();
	  }
	 
	  
	  //单词或语句删除收藏数据库操作
	  public void delCollection(int type ,int id )
	  {
		  openDB();
		  if(type==WORD)
		  {
			String sql="UPDATE word SET isFavorite = 0 WHERE id ="+String.valueOf(id);
			db.execSQL(sql);  
		  }
		  if(type==SENTENCE)
		  {
			  String sql="UPDATE sentence SET isFavorite = 0 WHERE id ="+String.valueOf(id);
		      db.execSQL(sql);  
		  }
		  closeDB();
		  
	  }
	  
	    //统计收藏语句的数目
	  
	  public  int getCounts() 
	  {
		  int count=0;
		  openDB();
		  Cursor cursor;
		  cursor=db.rawQuery("SELECT * FROM word WHERE isFavorite = ?", new String[]{"1"});
		  count+=cursor.getCount();
		  cursor.close();
		  cursor=db.rawQuery("SELECT * FROM sentence WHERE isFavorite = ?", new String[]{"1"});
		  count+=cursor.getCount();
		  cursor.close();
		  closeDB();
		  return count;
	   }
	  
	  //判断是否存在一个日语类型
	  public boolean isExist(String gourpName)
	  {
		  
		  openDB();
		  Cursor cursor;
		  cursor=db.rawQuery("SELECT * FROM groupJp WHERE groupName = ?", new String[]{gourpName});
		  int i=cursor.getCount();
		  cursor.close();
		  closeDB();
		  if(i==0)
			  return false;
		  
		  return true;
	  }
    

	  //插入日常用语分组信息
	  public int insertGroup(String groupName)
	  {
		  openDB();
		  String sql="insert into groupJp values(?,?)";
	      db.execSQL(sql,new String[]{null,groupName});
	      Cursor cursor;
		  cursor=db.rawQuery("SELECT * FROM groupJp WHERE groupName = ?", new String[]{groupName});
		  cursor.moveToFirst();
		  int id=cursor.getInt(cursor.getColumnIndex("groupId"));	 
		  cursor.close();
		  closeDB();
	      return id;
	  }
	  
	  //对单词和语句数据进行添加
	  public void insertData(List<XmlBean> data,int groupId)
	  {
		  openDB();
			 for (XmlBean xmlBean : data) {
					if(xmlBean.getType()==1)
					{
						String sql="insert into word values(?,?,?,?,?,?,?)";
						String[] tem=new String[]{null,String.valueOf(groupId),xmlBean.getRomanization(),xmlBean.getJapanese(),xmlBean.getChinese(),xmlBean.getSoundAdress(),"0"};
						db.execSQL(sql,tem);  
					}
					if(xmlBean.getType()==2)
					{
						String sql="insert into sentence values(?,?,?,?,?,?,?)";
						String[] tem=new String[]{null,String.valueOf(groupId),xmlBean.getRomanization(),xmlBean.getJapanese(),xmlBean.getChinese(),xmlBean.getSoundAdress(),"0"};
						db.execSQL(sql,tem);
					}
				}
			 
			 closeDB();
	  }
	  

	  private void databaseInit()
		{
			//databases 目录是准备放 SQLite 数据库的地方，也是 Android 程序默认的数据库存储目录
	        // 数据库名为 test.db
	        // 检查 SQLite 数据库文件是否存在
		   
	        if ((new File(DB_PATH + DB_NAME)).exists() == false) {
	            // 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
	            File f = new File(DB_PATH);
	           
	            // 如 database 目录不存在，新建该目录
	            if (!f.exists()) {
	                f.mkdir();
	            }

	            try {
	                // 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
	                InputStream is = this.context.getAssets().open(DB_NAME);
	                // 输出流
	                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);

	                // 文件写入
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = is.read(buffer)) > 0) {
	                    os.write(buffer, 0, length);
	                }

	                // 关闭文件流
	                os.flush();
	                os.close();
	                is.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
		}
}
