package com.tobacco.onlinesrv.activity;

import com.tobacco.onlinesrv.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OptionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Button preOrderBtn = (Button)this.findViewById(R.id.Button01);
		preOrderBtn.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(OptionActivity.this, AddPreOrderActivity.class);
				startActivity(intent);
			}});
	}

}
