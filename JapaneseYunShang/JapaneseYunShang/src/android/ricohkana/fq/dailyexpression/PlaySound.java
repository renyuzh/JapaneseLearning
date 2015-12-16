package android.ricohkana.fq.dailyexpression;
//声音播放

import java.io.IOException;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;



public class PlaySound {

	private String address="/jp/word/1.mp3";
	private String path="jp/word/";
	private String fileName="1.mp3";
	private HttpDownloader downloader;
	private static final int Fail=-1;
	private String filePath;
	private static final String url="http://172.27.35.7:8080";
	private Context context;
	private ProgressDialog dialog; 
	
	public PlaySound(String address,Context context) {
		// TODO Auto-generated constructor stub
//		this.address=address;
//		String[] tmp=address.split("/");
//		this.path=tmp[0];
//		this.fileName=tmp[1];
		this.context=context;
		this.filePath=Environment.getExternalStorageDirectory() + "/"+path+fileName;
		downloader=new HttpDownloader();	
		
	}
	
	
	public void play() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException 
	{
		if(!downloader.isExist(path+fileName))
		{
			if(!checkInternet(context))
        	{
        		 Toast.makeText(context, "此功能需要网络连接", Toast.LENGTH_SHORT).show();
        		 return;
        	}
			  dialog=new ProgressDialog(context);
			  dialog.setTitle("正在联网下载数据...");
			  dialog.setMessage("请稍候");
			  dialog.show();
			  new Thread(runnable).start();	 
		}
		else 
		{
			media(filePath);
		}
	}
	   
	 
    @SuppressLint("HandlerLeak")
	private Handler  handler = new Handler(){
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        dialog.cancel();
	        Bundle data = msg.getData();
	        int status=data.getInt("value");	           	
	        	if(status==Fail)
	        	{
	        		Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
	        		return;
	        	}
	        
	        try {
	        	
				media(filePath);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	
    };
    
    
  private Runnable runnable = new Runnable(){
	    @Override
	    public void run() {
	        Message msg = new Message();
	        Bundle data = new Bundle();
	        
			int status=downloader.downfile(url+address, path, fileName);
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
   	
   	//播放录音
   	public void  media(String path) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
   	{
          MediaPlayer  player = new MediaPlayer();

          player.setDataSource(path);

          player.prepare();

          player.start();
	}

}
