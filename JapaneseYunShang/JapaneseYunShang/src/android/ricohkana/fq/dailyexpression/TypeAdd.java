package android.ricohkana.fq.dailyexpression;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;





import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
//个人定制
public class TypeAdd {

  private String url;
  private String groupName;
  private Context context;
  private ProgressDialog dialog;
  private DaoAdapter daoAdapter;
  private HttpDownloader downloader;
  private String filePath;
  private String path="jp/add/";
  private String fileName="a.xml";
 
  private static final int Fail=-1;
  public TypeAdd(String group,String url,Context context)
  {
	  this.url=url;
	  this.groupName=group;
	  this.context=context;
	  daoAdapter=DaoAdapter.getInstance(context);
	  downloader=new HttpDownloader();
	  this.filePath=Environment.getExternalStorageDirectory() + "/"+path+fileName;
  }
  
  public void dataAdd() 
  {
	  if(!checkInternet(context))
  	{
  		 Toast.makeText(context, "quxiaolianwang", Toast.LENGTH_SHORT).show();
  		 return;
  	}
	if(daoAdapter.isExist(groupName))
	{
		 Toast.makeText(context, "内容已存在", Toast.LENGTH_SHORT).show();
		 return;
	}
	  dialog=new ProgressDialog(context);
	  dialog.setTitle("正在联网下载数据...");
	  dialog.setMessage("请稍候");
	  dialog.show();
	  new Thread(runnable).start();	   
	  
  }
  
  
  @SuppressLint("HandlerLeak")
 	private Handler  handler = new Handler(){
 	    public void handleMessage(Message msg) {
 	        super.handleMessage(msg);
 	        Bundle data = msg.getData();
 	        int status=data.getInt("value");	           	
 	        	if(status==Fail)
 	        	{
 	        		Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
 	        		return;
 	        	}
 	        	
 	         File file=new File(filePath);
 	   		 InputStream in;
			try {
				in = new FileInputStream(file);
				SaxParser sax;
				sax = new SaxParser(in);
				 List<XmlBean> info=sax.parse();
				 int id=daoAdapter.insertGroup(groupName);  
				 System.out.println("ssssss"+id);
				 daoAdapter.insertData(info, id);
			    }catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
            
 	        dialog.cancel(); 		        	
 	    
 	    }
     };
     
     
   private Runnable runnable = new Runnable(){
 	    @Override
 	    public void run() {
 	        Message msg = new Message();
 	        Bundle data = new Bundle();
 			int status=downloader.downfile(url, path, fileName);
 	        data.putInt("value",status);
 	        msg.setData(data);
 	        handler.sendMessage(msg);
 	    }
 	
   };
 	
   //检测网络是否连接
  	public boolean  checkInternet(Context context)
  	{
      if (context != null)
      { 
  		   	ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
  			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
  			if (mNetworkInfo != null)
  			{ 	
  			      return mNetworkInfo.isAvailable(); 
  		     } 
  		} 
  			return false; 
  			  			
  	}
  	
	
  }
