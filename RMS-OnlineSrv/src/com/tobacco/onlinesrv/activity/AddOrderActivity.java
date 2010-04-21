package com.tobacco.onlinesrv.activity;

import java.util.Calendar;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.Order;
import com.tobacco.onlinesrv.entities.PreOrder;

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
import android.widget.RadioButton;

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
	private RadioButton pRadio;
	private RadioButton oRadio;
	private Button okBtn;
	private Button cancelBtn;
	private Uri preorderUri = PreOrder.CONTENT_URI;
	private Uri orderUri = Order.CONTENT_URI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);
		init();
		setListeners();

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

	private void init() {
		brandEdt = (EditText) this.findViewById(R.id.brandCodeText);
		countEdt = (EditText) this.findViewById(R.id.brandCountText);
		fomartEdt = (EditText) this.findViewById(R.id.formatText);
		dateEdt = (EditText) this.findViewById(R.id.dateText);
		amountEdt = (EditText) this.findViewById(R.id.amountText);
		// agencyEdt = (EditText) this.findViewById(R.id.OrderEditText06);
		vipEdt = (EditText) this.findViewById(R.id.vipText);
		descEdt = (EditText) this.findViewById(R.id.descText);
		pRadio = (RadioButton) this.findViewById(R.id.preOrderRadio);
		oRadio = (RadioButton) this.findViewById(R.id.orderRadio);
		okBtn = (Button) this.findViewById(R.id.orderOkBtn);
		cancelBtn = (Button) this.findViewById(R.id.orderCancelBtn);
	}

	private void setListeners() {
		dateEdt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(0);
			}
		});

		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = null;
				if (pRadio.isChecked())
					uri = AddOrder(preorderUri, PreOrder.KEY_PREORDER_ID,
							PreOrder.KEY_BRANDCODE, PreOrder.KEY_BRANDCOUNT,
							PreOrder.KEY_PREDATE, PreOrder.KEY_FORMAT,
							PreOrder.KEY_AMOUNT, PreOrder.KEY_AGENTCYID,
							PreOrder.KEY_USERNAME, PreOrder.KEY_VIPID,
							PreOrder.KEY_DESCRIPTION, PreOrder.KEY_STATUS);
				if (oRadio.isChecked())
					uri = AddOrder(orderUri, Order.KEY_ORDER_ID,
							Order.KEY_BRANDCODE, Order.KEY_BRANDCOUNT,
							Order.KEY_DATE, Order.KEY_FORMAT, Order.KEY_AMOUNT,
							Order.KEY_AGENTCYID, Order.KEY_USERNAME,
							Order.KEY_VIPID, Order.KEY_DESCRIPTION,
							Order.KEY_STATUS);

				if (uri != null) {
					Log.i("add orderinfo", "success");
					finish();
				}

			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private Uri AddOrder(Uri uriType, String orderId, String brandCode,
			String brandCount, String date, String format, String amount,
			String agencyId, String userName, String vipId, String desc,
			String status) {
		ContentValues values = new ContentValues();
		values.put(orderId, "");
		values.put(brandCode, brandEdt.getText().toString());
		values.put(brandCount, Integer.parseInt(countEdt.getText().toString()));
		values.put(date, dateEdt.getText().toString());
		values.put(format, fomartEdt.getText().toString());
		values.put(amount, Float.parseFloat(amountEdt.getText().toString()));
		values.put(agencyId, "1");
		values.put(userName, "cry");
		values.put(vipId, Integer.parseInt(vipEdt.getText().toString()));
		values.put(desc, descEdt.getText().toString());
		values.put(status, "0");
		Uri uri = getContentResolver().insert(uriType, values);
		return uri;
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
