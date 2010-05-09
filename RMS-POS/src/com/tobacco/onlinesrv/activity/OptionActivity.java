package com.tobacco.onlinesrv.activity;

import com.tobacco.R;
import com.tobacco.onlinesrv.entities.Tobacco;
import com.tobacco.onlinesrv.util.FieldSupport;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class OptionActivity extends Activity{
	private Button tobaccoBtn;
	private Button addOrderBtn;
	private Button queryOrderBtn;
	private Button returnBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onlinemain);
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

		returnBtn = (Button) this.findViewById(R.id.returnBtn);
		returnBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		fillBrandInfo();
	}

	private void fillBrandInfo() {
		Cursor cursor = this.managedQuery(Tobacco.CONTENT_URI, null, null,
				null, null);
		String tempStr = "";
		String tempStr2 = "";
		String tempStr3 = "";
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			do {
				tempStr += "," + cursor.getString(1);
				tempStr2 += "," + cursor.getString(2);
				tempStr3 += "," + cursor.getString(4);
			} while (cursor.moveToNext());
			FieldSupport.brandType = tempStr.substring(1, tempStr.length()).split(",");
			FieldSupport.packetPrice = tempStr2.substring(1, tempStr2.length()).split(",");
			FieldSupport.itemPrice = tempStr3.substring(1, tempStr3.length()).split(",");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if(resultCode == RESULT_OK)
			System.out.println("back success");
	}

}
