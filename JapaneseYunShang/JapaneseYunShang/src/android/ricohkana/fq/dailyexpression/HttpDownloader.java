package android.ricohkana.fq.dailyexpression;
//文件下载
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpDownloader {
	private URL url=null;
	FileUtils fileUtils=new FileUtils();
	
	
	public int downfile( String urlStr, String path, String fileName)
	{		
		try {
			InputStream input=null;
			input = getInputStream(urlStr);
			File resultFile=fileUtils.write2SDFromInput(path, fileName, input);
			input.close();
			if(resultFile==null)
			{
				return -1;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return 0;
	}
  //由于得到一个InputStream对象是所有文件处理前必须的操作，所以将这个操作封装成了一个方法
       public InputStream getInputStream(String urlStr) throws IOException
       {     
    	   InputStream is=null;
    	    try {
				url=new URL(urlStr);
				HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();
				is=urlConn.getInputStream();
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
    	    return is;
       }
       
       
     public boolean isExist(String path)
       {
    	   if(fileUtils.isFileExist(path))
			  return true;
    	   else {
			 return false;
		}
      }
       
}