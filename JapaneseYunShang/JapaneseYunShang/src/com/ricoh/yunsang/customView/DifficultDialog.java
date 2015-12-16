package com.ricoh.yunsang.customView;

import com.ricoh.R;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

public class DifficultDialog extends Dialog
{

	private Context context;
	private Switch switch1;
	SharedPreferences sp;

	public DifficultDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		// TODO 自动生成的构造函数存根
	}

	public DifficultDialog(Context context)
	{
		super(context);

		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.difficult_dialog);
		sp = getContext().getSharedPreferences("thefirsttime", Context.MODE_PRIVATE);

		switch1 = (Switch) findViewById(R.id.switch1);
		Boolean flag = sp.getBoolean("SEVEN_SELECTED", false);

		switch1.setChecked(!flag);

		switch1.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean ischecked)
			{

				if (ischecked)
				{
					Editor editor = sp.edit();
					editor.putBoolean("SEVEN_SELECTED", false);
					editor.commit();
					Toast.makeText(context, "4选项", Toast.LENGTH_SHORT).show();
				} else
				{
					Editor editor = sp.edit();
					editor.putBoolean("SEVEN_SELECTED", true);
					editor.commit();
					Toast.makeText(context, "7选项", Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

}
