package com.tobacco.onlinesrv.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.tobacco.main.activity.Login;
import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.PreOrder;
import com.tobacco.onlinesrv.provider.PreOrderProvider;

public class AddPreOrderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preorder);

		Intent intent = getIntent();

		EditText dateEdt = (EditText) this.findViewById(R.id.EditText04);
		dateEdt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent tempIntent = new Intent();
				tempIntent.setClass(AddPreOrderActivity.this,
						DatePickActivity.class);
				startActivity(tempIntent);
			}
		});
		EditText sd = (EditText) this.findViewById(R.id.EditText01);
		Uri preorder = PreOrder.CONTENT_URI;

		Cursor cursor = this.managedQuery(preorder, null, null, null, null);
		if (cursor.getCount() == 0)
			openfailDialog();
		else {
			cursor.moveToFirst();
			String s = cursor.getString(1);
			sd.setText(s);
		}
	}

	private void openfailDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(AddPreOrderActivity.this).setTitle("")
				.setMessage("fail").show();
	}

}
