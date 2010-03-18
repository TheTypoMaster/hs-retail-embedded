package com.tobacco.onlinesrv.activity;

import com.tobacco.onlinesrv.R;
import android.app.Activity;
import android.content.ContentProvider;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OptionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button addPreBtn = (Button) this.findViewById(R.id.Button01);
		addPreBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(OptionActivity.this, AddPreOrderActivity.class);
				startActivity(intent);
			}
		});
		Button queryPreBtn = (Button) this.findViewById(R.id.Button02);
		queryPreBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(OptionActivity.this,
						QueryPreOrderActivity.class);
				startActivity(intent);
			}
		});

		Button addOrderBtn = (Button) this.findViewById(R.id.Button03);
		addOrderBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(OptionActivity.this, AddOrderActivity.class);
				startActivity(intent);
			}
		});

		Button queryOrderBtn = (Button) this.findViewById(R.id.Button04);
		queryOrderBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(OptionActivity.this, QueryOrderActivity.class);
				startActivity(intent);
			}
		});
	}

}
