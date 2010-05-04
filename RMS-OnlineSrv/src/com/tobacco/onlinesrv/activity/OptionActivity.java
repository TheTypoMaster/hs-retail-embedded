package com.tobacco.onlinesrv.activity;

import com.tobacco.main.activity.view.RMSBaseView;
import com.tobacco.onlinesrv.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OptionActivity extends RMSBaseView{
	private Button addOrderBtn;
	private Button queryOrderBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		addOrderBtn = (Button) this.findViewById(R.id.addOrderBtn);
		addOrderBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(OptionActivity.this, AddOrderActivity.class);
				startActivity(intent);
			}
		});
		queryOrderBtn = (Button) this.findViewById(R.id.queryOrderBtn);
		queryOrderBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(OptionActivity.this,
						QueryOrderActivity.class);
				startActivity(intent);
			}
		});

		
	}

}
