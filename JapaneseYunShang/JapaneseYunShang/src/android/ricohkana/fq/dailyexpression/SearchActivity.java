package android.ricohkana.fq.dailyexpression;
//搜索界面


import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity {

    private DaoAdapter daoAdapter;
    private ImageButton searchButton;
    private ImageButton clearButton;
    private ListView  listView;
    private TextView textView;
	private EditText editText;
	
	
	public SearchActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		daoAdapter=DaoAdapter.getInstance(this);	
		searchButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 String str = editText.getText().toString();	
				 if(str==null||str.equals(""))
				 {
					 Toast.makeText(SearchActivity.this, "请输入关键字",Toast.LENGTH_SHORT).show();
			         return;
				 }
				 editText.clearFocus();
				 List<BasicBean> data=daoAdapter.getSearch(str);
				 textView.setText("共检索到：" + data.size() + "条记录");
				 ContentAdapter myAdapter=new ContentAdapter(getApplicationContext(),data);
				 listView.setAdapter(myAdapter);
			}
		
		
		});
		
	    clearButton.setOnClickListener(new View.OnClickListener()
	    {
	      public void onClick(View paramAnonymousView)
	      {
	        editText.setText(null);
	      }
	    });
		
		
		
	}
	
}
