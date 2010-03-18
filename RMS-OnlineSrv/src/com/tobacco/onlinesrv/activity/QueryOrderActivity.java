package com.tobacco.onlinesrv.activity;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.PreOrder;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class QueryOrderActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);

	
	}

	private void openfailDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(QueryOrderActivity.this).setTitle("")
				.setMessage("fail").show();
	}

}