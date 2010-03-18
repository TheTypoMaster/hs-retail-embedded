package com.tobacco.onlinesrv.activity;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.Order;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddOrderActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);

		final EditText brandEdt = (EditText) this
				.findViewById(R.id.OrderEditText01);
		final EditText countEdt = (EditText) this
				.findViewById(R.id.OrderEditText02);
		final EditText fomartEdt = (EditText) this
				.findViewById(R.id.OrderEditText03);
		final EditText dateEdt = (EditText) this
				.findViewById(R.id.OrderEditText04);
		final EditText amountEdt = (EditText) this
				.findViewById(R.id.OrderEditText05);
		final EditText agencyEdt = (EditText) this
				.findViewById(R.id.OrderEditText06);
		final EditText vipEdt = (EditText) this
				.findViewById(R.id.OrderEditText07);
		final EditText descEdt = (EditText) this
				.findViewById(R.id.OrderEditText08);
		Button okBtn = (Button) this.findViewById(R.id.orderOkBtn);
		okBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				ContentValues values = new ContentValues();
				values.put(Order.KEY_BRANDCODE, brandEdt.getText().toString());
				values.put(Order.KEY_BRANDCOUNT, Integer.parseInt(countEdt
						.getText().toString()));
				values.put(Order.KEY_DATE, dateEdt.getText().toString());
				values.put(Order.KEY_FORMAT, fomartEdt.getText().toString());
				values.put(Order.KEY_AMOUNT, Float.parseFloat(amountEdt
						.getText().toString()));
				values.put(Order.KEY_AGENTCYID, agencyEdt.getText().toString());
				values.put(Order.KEY_USERNAME, "cry");
				values.put(Order.KEY_VIPID, Integer.parseInt(vipEdt.getText()
						.toString()));
				values.put(Order.KEY_DESCRIPTION, descEdt.getText().toString());
				values.put(Order.KEY_STATUS, "0");
				Uri uri = getContentResolver()
						.insert(Order.CONTENT_URI, values);
				if (uri != null) {
					Log.i("add orderinfo", "success");
					finish();
				}

			}
		});
		Button cancelBtn = (Button) this.findViewById(R.id.orderCancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
