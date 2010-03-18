package com.tobacco.onlinesrv.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.PreOrder;

public class AddPreOrderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preorder);

		Intent intent = getIntent();
		final EditText brandEdt = (EditText) this.findViewById(R.id.EditText01);
		final EditText countEdt = (EditText) this.findViewById(R.id.EditText02);
		final EditText fomartEdt = (EditText) this
				.findViewById(R.id.EditText03);
		final EditText dateEdt = (EditText) this.findViewById(R.id.EditText04);
		final EditText amountEdt = (EditText) this
				.findViewById(R.id.EditText05);
		final EditText agencyEdt = (EditText) this
				.findViewById(R.id.EditText06);
		final EditText vipEdt = (EditText) this.findViewById(R.id.EditText07);
		final EditText descEdt = (EditText) this.findViewById(R.id.EditText08);
		dateEdt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent tempIntent = new Intent();
				tempIntent.setClass(AddPreOrderActivity.this,
						DatePickActivity.class);
				startActivity(tempIntent);
			}
		});

		Button okBtn = (Button) this.findViewById(R.id.preOkBtn);
		okBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				ContentValues values = new ContentValues();
				values.put(PreOrder.KEY_BRANDCODE, brandEdt.getText()
						.toString());
				values.put(PreOrder.KEY_BRANDCOUNT, Integer.parseInt(countEdt
						.getText().toString()));
				values.put(PreOrder.KEY_PREDATE, dateEdt.getText().toString());
				values.put(PreOrder.KEY_FORMAT, fomartEdt.getText().toString());
				values.put(PreOrder.KEY_AMOUNT, Float.parseFloat(amountEdt
						.getText().toString()));
				values.put(PreOrder.KEY_AGENTCYID, agencyEdt.getText()
						.toString());
				values.put(PreOrder.KEY_USERNAME, "cry");
				values.put(PreOrder.KEY_VIPID, Integer.parseInt(vipEdt
						.getText().toString()));
				values.put(PreOrder.KEY_DESCRIPTION, descEdt.getText()
						.toString());
				values.put(PreOrder.KEY_STATUS, "0");
				Uri uri = getContentResolver().insert(PreOrder.CONTENT_URI,
						values);
				if (uri != null) {
					Log.i("add preorderinfo", "success");
					finish();
				}

			}
		});
		Button cancelBtn = (Button) this.findViewById(R.id.preCancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}
}
