package android.ricohkana.fq.utils;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Config {

	/**
	 * 
	 * 读取assets的文件内容
	 * 
	 * @param filePath
	 * 
	 * @return
	 */

	private static Config config;

	public static Config getConfig() {
		if (config == null) {
			config = new Config();
		}
		return config;
	}

	public static String getDataFromAssets(Context context, String filePath) {
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();

		try {
			br = new BufferedReader(new InputStreamReader(context.getAssets()
					.open(filePath)));
			String line;

			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}
}
