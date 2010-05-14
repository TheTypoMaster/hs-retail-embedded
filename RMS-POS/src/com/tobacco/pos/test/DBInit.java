package com.tobacco.pos.test;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.tobacco.pos.entity.AllTables.Complaint;
import com.tobacco.pos.entity.AllTables.Consume;
import com.tobacco.pos.entity.AllTables.Return;
import com.tobacco.pos.util.db.POSDbHelper;

public class DBInit extends Activity {

	private static final String TAG = "DBInit";

	public static final String ACTION_INIT_DB = "com.tobacco.pos.test.DBInit.INIT_DB";
	
	POSDbHelper dbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// auto generate log statement
		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		Log.i(TAG, "onStart()");

		super.onStart();

		dbHelper = new POSDbHelper(this);
		dbHelper.initDB();

		initConsume();
		initComplaint();
		initReturn();

		setResult(RESULT_OK);

		finish();

	}

	@Override
	protected void onStop() {
		Log.i(TAG, "onStop()");
		dbHelper.close();
		super.onStop();

	}
	
	private void initComplaint() {
		
		Log.i(TAG, "initComplaint()");

		Uri complaintUri = Complaint.CONTENT_URI;
		ContentValues value = new ContentValues();

		value.clear();
		value.put(Complaint.VIP_ID, 1);
		value.put(Complaint.GOODS_ID, 1);
		value.put(Complaint.COMMENT, "init");
		getContentResolver().insert(complaintUri, value);

		value.clear();

		value.put(Complaint.VIP_ID, 2);
		value.put(Complaint.GOODS_ID, 2);
		value.put(Complaint.COMMENT, "init");
		getContentResolver().insert(complaintUri, value);

		value.clear();
		value.put(Complaint.VIP_ID, 3);
		value.put(Complaint.GOODS_ID, 3);
		value.put(Complaint.COMMENT, "init");
		getContentResolver().insert(complaintUri, value);

		value.clear();
		value.put(Complaint.VIP_ID, 4);
		value.put(Complaint.GOODS_ID, 4);
		value.put(Complaint.COMMENT, "init");
		getContentResolver().insert(complaintUri, value);

		value.clear();
		value.put(Complaint.VIP_ID, 5);
		value.put(Complaint.GOODS_ID, 5);
		value.put(Complaint.COMMENT, "init");
		getContentResolver().insert(complaintUri, value);

		value.clear();

		value.put(Complaint.VIP_ID, 6);
		value.put(Complaint.GOODS_ID, 6);
		value.put(Complaint.COMMENT, "init");
		getContentResolver().insert(complaintUri, value);

		value.clear();
		value.put(Complaint.VIP_ID, 1);
		value.put(Complaint.GOODS_ID, 7);
		value.put(Complaint.COMMENT, "init");
		getContentResolver().insert(complaintUri, value);

		value.clear();

		value.put(Complaint.VIP_ID, 2);
		value.put(Complaint.GOODS_ID, 8);
		value.put(Complaint.COMMENT, "init");
		getContentResolver().insert(complaintUri, value);

		value.clear();
		value.put(Complaint.VIP_ID, 3);
		value.put(Complaint.GOODS_ID, 9);
		value.put(Complaint.COMMENT, "init");
		getContentResolver().insert(complaintUri, value);

		value.clear();
		value.put(Complaint.VIP_ID, 4);
		value.put(Complaint.GOODS_ID, 10);
		value.put(Complaint.COMMENT, "init");
		getContentResolver().insert(complaintUri, value);

		value.clear();
		value.put(Complaint.VIP_ID, 5);
		value.put(Complaint.GOODS_ID, 11);
		value.put(Complaint.COMMENT, "init");
		getContentResolver().insert(complaintUri, value);

		value.clear();
		value.put(Complaint.VIP_ID, 6);
		value.put(Complaint.GOODS_ID, 12);
		value.put(Complaint.COMMENT, "init");
		getContentResolver().insert(complaintUri, value);
		

	}
	
	private void initReturn() {
		
		Log.i(TAG, "initReturn()");

		Uri returnUri = Return.CONTENT_URI;
		ContentValues value = new ContentValues();

		value.clear();
		value.put(Return.VIP_ID, 1);
		value.put(Return.GOODS_ID, 1);
		value.put(Return.NUMBER, 1);
		value.put(Return.COMMENT, "init");
		getContentResolver().insert(returnUri, value);
		
		value.clear();
		value.put(Return.VIP_ID, 2);
		value.put(Return.GOODS_ID, 2);
		value.put(Return.NUMBER, 2);
		value.put(Return.COMMENT, "init");
		getContentResolver().insert(returnUri, value);
		
		value.clear();
		value.put(Return.VIP_ID, 3);
		value.put(Return.GOODS_ID, 3);
		value.put(Return.NUMBER, 3);
		value.put(Return.COMMENT, "init");
		getContentResolver().insert(returnUri, value);
		
		value.clear();
		value.put(Return.VIP_ID, 4);
		value.put(Return.GOODS_ID, 4);
		value.put(Return.NUMBER, 4);
		value.put(Return.COMMENT, "init");
		getContentResolver().insert(returnUri, value);
		
		value.clear();
		value.put(Return.VIP_ID, 5);
		value.put(Return.GOODS_ID, 5);
		value.put(Return.NUMBER, 5);
		value.put(Return.COMMENT, "init");
		getContentResolver().insert(returnUri, value);
		
		value.clear();
		value.put(Return.VIP_ID, 6);
		value.put(Return.GOODS_ID, 6);
		value.put(Return.NUMBER, 6);
		value.put(Return.COMMENT, "init");
		getContentResolver().insert(returnUri, value);
		
		value.clear();
		value.put(Return.VIP_ID, 1);
		value.put(Return.GOODS_ID, 7);
		value.put(Return.NUMBER, 7);
		value.put(Return.COMMENT, "init");
		getContentResolver().insert(returnUri, value);
		
		value.clear();
		value.put(Return.VIP_ID, 2);
		value.put(Return.GOODS_ID, 8);
		value.put(Return.NUMBER, 8);
		value.put(Return.COMMENT, "init");
		getContentResolver().insert(returnUri, value);
		
		value.clear();
		value.put(Return.VIP_ID, 3);
		value.put(Return.GOODS_ID, 9);
		value.put(Return.NUMBER, 9);
		value.put(Return.COMMENT, "init");
		getContentResolver().insert(returnUri, value);
		
		value.clear();
		value.put(Return.VIP_ID, 4);
		value.put(Return.GOODS_ID, 10);
		value.put(Return.NUMBER, 10);
		value.put(Return.COMMENT, "init");
		getContentResolver().insert(returnUri, value);
		
		value.clear();
		value.put(Return.VIP_ID, 5);
		value.put(Return.GOODS_ID, 11);
		value.put(Return.NUMBER, 11);
		value.put(Return.COMMENT, "init");
		getContentResolver().insert(returnUri, value);
		
		value.clear();
		value.put(Return.VIP_ID, 6);
		value.put(Return.GOODS_ID, 12);
		value.put(Return.NUMBER, 12);
		value.put(Return.COMMENT, "init");
		getContentResolver().insert(returnUri, value);

	}
	
	private void initConsume() {
		
		Log.i(TAG, "initConsume()");

		Uri consumeUri = Consume.CONTENT_URI;
		ContentValues value = new ContentValues();

		value.clear();
		value.put(Consume.NUMBER, 1);
		value.put(Consume.GOODS, 1);
		value.put(Consume.FLAG, 1);
		value.put(Consume.COMMENT, "init");
		getContentResolver().insert(consumeUri, value);
		
		value.clear();
		value.put(Consume.NUMBER, 2);
		value.put(Consume.GOODS, 2);
		value.put(Consume.FLAG, 0);
		value.put(Consume.COMMENT, "init");
		getContentResolver().insert(consumeUri, value);
		
		value.clear();
		value.put(Consume.NUMBER, 3);
		value.put(Consume.GOODS, 3);
		value.put(Consume.FLAG, 1);
		value.put(Consume.COMMENT, "init");
		getContentResolver().insert(consumeUri, value);
		
		value.clear();
		value.put(Consume.NUMBER, 4);
		value.put(Consume.GOODS, 4);
		value.put(Consume.FLAG, 0);
		value.put(Consume.COMMENT, "init");
		getContentResolver().insert(consumeUri, value);
		
		value.clear();
		value.put(Consume.NUMBER, 5);
		value.put(Consume.GOODS, 6);
		value.put(Consume.FLAG, 1);
		value.put(Consume.COMMENT, "init");
		getContentResolver().insert(consumeUri, value);
		
		value.clear();
		value.put(Consume.NUMBER, 6);
		value.put(Consume.GOODS, 6);
		value.put(Consume.FLAG, 0);
		value.put(Consume.COMMENT, "init");
		getContentResolver().insert(consumeUri, value);
		
		value.clear();
		value.put(Consume.NUMBER, 7);
		value.put(Consume.GOODS, 7);
		value.put(Consume.FLAG, 1);
		value.put(Consume.COMMENT, "init");
		getContentResolver().insert(consumeUri, value);
		
		value.clear();
		value.put(Consume.NUMBER, 8);
		value.put(Consume.GOODS, 8);
		value.put(Consume.FLAG, 0);
		value.put(Consume.COMMENT, "init");
		getContentResolver().insert(consumeUri, value);
		
		value.clear();
		value.put(Consume.NUMBER, 9);
		value.put(Consume.GOODS, 9);
		value.put(Consume.FLAG, 1);
		value.put(Consume.COMMENT, "init");
		getContentResolver().insert(consumeUri, value);
		
		value.clear();
		value.put(Consume.NUMBER, 10);
		value.put(Consume.GOODS, 10);
		value.put(Consume.FLAG, 0);
		value.put(Consume.COMMENT, "init");
		getContentResolver().insert(consumeUri, value);
		
		value.clear();
		value.put(Consume.NUMBER, 11);
		value.put(Consume.GOODS, 11);
		value.put(Consume.FLAG, 1);
		value.put(Consume.COMMENT, "init");
		getContentResolver().insert(consumeUri, value);
		
		value.clear();
		value.put(Consume.NUMBER, 12);
		value.put(Consume.GOODS, 12);
		value.put(Consume.FLAG, 0);
		value.put(Consume.COMMENT, "init");
		getContentResolver().insert(consumeUri, value);

	}
	
}
