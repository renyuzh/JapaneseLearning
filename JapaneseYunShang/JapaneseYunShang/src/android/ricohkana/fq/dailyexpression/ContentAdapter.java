package android.ricohkana.fq.dailyexpression;
//单词语句信息适配器
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ricoh.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentAdapter extends BaseAdapter {

	private static final int WORD=1;
	private static final int SENTENCE=2;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> mData;
	private DaoAdapter daoAdapter=null;
	private int type;
	private Context context;
	//单词语句适配器构造
	public ContentAdapter(Context context,int type,int groupId){
		this.mInflater = LayoutInflater.from(context);
		this.context=context;
		this.type=type;
		daoAdapter=DaoAdapter.getInstance(context);
        dataInit(groupId);
	}
	
	//收藏适配器构造
	public ContentAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
		this.context=context;
		daoAdapter=DaoAdapter.getInstance(context);
        collectdataInit();
	}
	//搜索适配器构造
	public ContentAdapter(Context context, List<BasicBean> data)
	{
		this.mInflater = LayoutInflater.from(context);
		this.context=context;
		searchDataInit(data);
	}
	//单词和句子数据的加载
	public void dataInit(int groupId)
	{
	    List<BasicBean> list =null;	
	    mData=new ArrayList<Map<String,Object>>();
	    Map<String, Object> map=null;

	
	    if(type==WORD)
		{
			list=daoAdapter.getWords(groupId);
		}
		if(type==SENTENCE)
		{
			list=daoAdapter.getSentence(groupId);
		}		
		for(BasicBean basicBean : list) 
		{
			  map=new HashMap<String, Object>();
			  map.put("id", basicBean.getId());
			  map.put("roma", basicBean.getRomanization());
			  map.put("japanese", basicBean.getJapanese());
			  map.put("sound", basicBean.getSoundAdress());
			  map.put("chinese", basicBean.getChinese());
			  map.put("favorite", basicBean.getIsFavorite());
			  map.put("type", basicBean.getType());
			  Log.i("id", String.valueOf(basicBean.getId()));
			  Log.i("chinese",basicBean.getChinese());
			  this.mData.add(map);
		}
	}
   	
	
	//收藏数据的加载
	public void collectdataInit() {
		
	    List<BasicBean> list =null;	
	    mData=new ArrayList<Map<String,Object>>();
	    Map<String, Object> map=null;
		list=daoAdapter.getCollection();
		for(BasicBean basicBean : list) 
		{
			  map=new HashMap<String, Object>();
			  map.put("id", basicBean.getId());
			  map.put("roma", basicBean.getRomanization());
			  map.put("japanese", basicBean.getJapanese());
			  map.put("sound", basicBean.getSoundAdress());
			  map.put("chinese", basicBean.getChinese());
			  map.put("favorite", basicBean.getIsFavorite());
			  map.put("type", basicBean.getType());
			  Log.i("id", String.valueOf(basicBean.getId()));
			  Log.i("chinese",basicBean.getChinese());
			  this.mData.add(map);
		}
	
	} 
	
	public void searchDataInit(List<BasicBean> list)
	{
		mData=new ArrayList<Map<String,Object>>();
	    Map<String, Object> map=null;
		for(BasicBean basicBean : list) 
		{
			  map=new HashMap<String, Object>();
			  map.put("id", basicBean.getId());
			  map.put("roma", basicBean.getRomanization());
			  map.put("japanese", basicBean.getJapanese());
			  map.put("sound", basicBean.getSoundAdress());
			  map.put("chinese", basicBean.getChinese());
			  map.put("favorite", basicBean.getIsFavorite());
			  map.put("type", basicBean.getType());
			  Log.i("id", String.valueOf(basicBean.getId()));
			  Log.i("chinese",basicBean.getChinese());
			  this.mData.add(map);
		}
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if (convertView == null) {
			
			holder=new ViewHolder();  
			
			convertView = mInflater.inflate(R.layout.basic_list_item, null);
			holder.roma = (TextView)convertView.findViewById(R.id.text1);
			holder.japanese = (TextView)convertView.findViewById(R.id.text2);
			holder.sound = (ImageView)convertView.findViewById(R.id.imgPlay);
			holder.chinese = (TextView)convertView.findViewById(R.id.text3);
			holder.collect=(CheckBox)convertView.findViewById(R.id.favorite);
			convertView.setTag(holder);
			
		}else {
			
			holder = (ViewHolder)convertView.getTag();
		}
		
		
		holder.roma.setText((String)mData.get(position).get("roma"));
		holder.japanese.setText((String)mData.get(position).get("japanese"));
		holder.sound.setTag(((String)mData.get(position).get("sound")));
		holder.chinese.setText((String)mData.get(position).get("chinese"));
		int id=((Integer)mData.get(position).get("id")).intValue();
		int type=((Integer)mData.get(position).get("type")).intValue();
		List<Integer> info=new ArrayList<Integer>();
        info.add(id);
        info.add(type);
		holder.collect.setTag(info);
		int flag=((Integer)mData.get(position).get("favorite")).intValue();
		//收藏状态的显示
		if(flag==0)
		{
			holder.collect.setChecked(false);
		}
		else
		{
			holder.collect.setChecked(true);
		}
		
		//对收藏按钮添加事件
		holder.collect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				
				@SuppressWarnings("rawtypes")
				List tm=(List)arg0.getTag();
				if(isChecked)
				{
					daoAdapter.addCollection(((Integer)tm.get(1)).intValue(), ((Integer)tm.get(0)).intValue());
				}
				else 
				{
					daoAdapter.delCollection(((Integer)tm.get(1)).intValue(), ((Integer)tm.get(0)).intValue());
				}
				
			}
		});
		
	    holder.sound.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	
            	String address=(String)v.getTag();
                PlaySound playSound=new PlaySound(address, context);
                try {
					playSound.play();
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

       });
		
		
		
		return convertView;
	}

	
	public final class ViewHolder{
		public TextView roma;
		public TextView japanese;
		public ImageView sound;
		public TextView chinese;
		public CheckBox collect;
	}
	
	
}
