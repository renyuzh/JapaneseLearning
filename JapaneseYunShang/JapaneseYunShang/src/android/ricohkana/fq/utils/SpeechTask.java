package android.ricohkana.fq.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;

public class SpeechTask extends AsyncTask<String, String, String> {
	Context context;
	MediaPlayer mediaPlayer;

	public SpeechTask(Context context, MediaPlayer mediaPlayer) {
		this.context = context;
		this.mediaPlayer = mediaPlayer;
	}

	protected String doInBackground(String... paramVarArgs) {

		try {
			int i = context.getResources().getIdentifier(
					paramVarArgs[0].split("\\.")[0], "raw",
					context.getPackageName());
			if (i != 0) {
				mediaPlayer = MediaPlayer.create(
						context, i);
				if (mediaPlayer != null) {
					mediaPlayer.stop();
				}
				mediaPlayer.prepare();
				mediaPlayer.start();

				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				    public void onCompletion(MediaPlayer mp) {
				        mp.release();

				    };
				});
			}
		} catch (Exception localException) {
		}
		return null;
	}
}