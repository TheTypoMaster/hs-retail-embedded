package com.tobacco.onlinesrv.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

import com.tobacco.onlinesrv.R;

public class DatePickActivity extends Activity {
	public String dateStr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date);

		final Intent intent = getIntent();
		final DatePicker datePicker = (DatePicker) this
				.findViewById(R.id.DatePicker01);
		Button okBtn = (Button) this.findViewById(R.id.okBtn);
		okBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent tempIntent = new Intent();
				tempIntent.setClass(DatePickActivity.this,
						AddPreOrderActivity.class);
				dateStr = datePicker.getYear() + "-" + datePicker.getMonth()
						+ datePicker.getDayOfMonth();
				intent.putExtra("date", dateStr);
				startActivity(tempIntent);
			}
		});
	}
}