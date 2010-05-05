package com.tobacco.onlinesrv.activity;

import com.tobacco.onlinesrv.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OptionActivity extends Activity{
	private Button tobaccoBtn;
	private Button addOrderBtn;
	private Button queryOrderBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tobaccoBtn = (Button) this.findViewById(R.id.tobaccoManageBtn);
		tobaccoBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("android.intent.action.Tobacco");
				startActivity(intent);
			}
		});
		addOrderBtn = (Button) this.findViewById(R.id.addOrderBtn);
		addOrderBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("android.intent.action.AddOrder");
				startActivityForResult(intent, RESULT_OK);
			}
		});
		
		queryOrderBtn = (Button) this.findViewById(R.id.queryOrderBtn);
		queryOrderBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("android.intent.action.QueryOrder");
				startActivity(intent);
			}
		});

		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if(resultCode == RESULT_OK)
			System.out.println("back success");
	}

}
