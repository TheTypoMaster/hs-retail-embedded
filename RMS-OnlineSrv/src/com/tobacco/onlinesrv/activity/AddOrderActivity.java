package com.tobacco.onlinesrv.activity;

import java.util.Calendar;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.Order;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddOrderActivity extends Activity {
	private int mYear;
	private int mMonth;
	private int mDay;
	private EditText brandEdt;
	private EditText countEdt;
	private EditText dateEdt;
	private EditText fomartEdt;
	private EditText amountEdt;
	private EditText agencyEdt;
	private EditText vipEdt;
	private EditText descEdt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);

		brandEdt = (EditText) this.findViewById(R.id.OrderEditText01);
		countEdt = (EditText) this.findViewById(R.id.OrderEditText02);
		fomartEdt = (EditText) this.findViewById(R.id.OrderEditText03);
		dateEdt = (EditText) this.findViewById(R.id.OrderEditText04);
		amountEdt = (EditText) this.findViewById(R.id.OrderEditText05);
		agencyEdt = (EditText) this.findViewById(R.id.OrderEditText06);
		vipEdt = (EditText) this.findViewById(R.id.OrderEditText07);
		descEdt = (EditText) this.findViewById(R.id.OrderEditText08);
		dateEdt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(0);
			}
		});
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

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case 0:
			mYear = Calendar.getInstance().get(Calendar.YEAR);
			mMonth = Calendar.getInstance().get(Calendar.MONTH);
			mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}

		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			setDateFiled();
		}

		private void setDateFiled() {
			// TODO Auto-generated method stub
			dateEdt.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
		}
	};
}
