package android.ricohkana.fq.dailyexpression;
//ËÑË÷½çÃæ
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import com.ricoh.R;

public class CollectActivity extends Activity {

	
	private ListView listView;
	public CollectActivity() {
		// TODO Auto-generated constructor stub
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collect);
		listView=(ListView)findViewById(R.id.collectlist);
		ContentAdapter myaAdapter=new ContentAdapter(this);
		listView.setAdapter(myaAdapter);
		ImageButton back=(ImageButton)findViewById(R.id.CollectBack);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(CollectActivity.this, DailyExpressionActivity.class);
				startActivity(intent);
				
			}
		});
	}
}
