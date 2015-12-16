package android.ricohkana.fq.hiragana;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;

public class BinReader
{
	/**
	 * 
	 * @param paramString
	 * @param paramLong
	 * @return
	 */
	public Bitmap readImage(String paramString, long paramLong)
	{
	    Object localObject = ((byte[])null);
	    try
	    {
	      InputStream localInputStream = super.getClass().getResourceAsStream("/assets/" + paramString);
	      localInputStream.skip(paramLong);
	      byte[] arrayOfByte = new byte[(0xFF & localInputStream.read()) << 24 | (0xFF & localInputStream.read()) << 16 | (0xFF & localInputStream.read()) << 8 | (0xFF & localInputStream.read())];
	      localInputStream.read(arrayOfByte, 0, arrayOfByte.length);
	      localInputStream.close();
	      Bitmap localBitmap = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
	      int i = localBitmap.getWidth();
	      if (i != 0)
	        localObject = localBitmap;
	    }
	    catch (IOException localIOException)
	    {
	      localIOException.printStackTrace();
	    }
	    return (Bitmap) localObject;
  }
}