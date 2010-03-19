package com.tobacco.onlinesrv.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.PreOrder;

public class AddPreOrderActivity extends Activity {

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
		setContentView(R.layout.preorder);

		Intent intent = getIntent();
		brandEdt = (EditText) this.findViewById(R.id.EditText01);
		countEdt = (EditText) this.findViewById(R.id.EditText02);
		fomartEdt = (EditText) this.findViewById(R.id.EditText03);
		dateEdt = (EditText) this.findViewById(R.id.EditText04);
		amountEdt = (EditText) this.findViewById(R.id.EditText05);
		agencyEdt = (EditText) this.findViewById(R.id.EditText06);
		vipEdt = (EditText) this.findViewById(R.id.EditText07);
		descEdt = (EditText) this.findViewById(R.id.EditText08);

		dateEdt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(0);
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
					openSuccessDialog();
				} else
					openFailDialog();

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

	private void openSuccessDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(AddPreOrderActivity.this).setTitle("")
				.setMessage("添加成功").setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								finish();
							}
						}).show();
	}

	private void openFailDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(AddPreOrderActivity.this).setTitle("")
				.setMessage("添加失败").setPositiveButton("返回",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								finish();
							}
						}).show();
	}
}
