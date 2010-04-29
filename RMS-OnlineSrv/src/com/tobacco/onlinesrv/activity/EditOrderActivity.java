package com.tobacco.onlinesrv.activity;

import com.tobacco.onlinesrv.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class EditOrderActivity extends Activity {

	private TextView orderIdTxt;
	private TextView brandCodeTxt;
	private TextView brandCountTxt;
	private TextView formatTxt;
	private TextView amountTxt;
	private TextView unitTxt;
	private TextView vipTxt;
	private TextView dateTxt;
	private TextView descTxt;
	private TextView statusTxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_info);
		Intent intent = getIntent();
		orderIdTxt = (TextView) this.findViewById(R.id.orderIdView);
		orderIdTxt.setText(intent.getExtras().get("orderId").toString());
		brandCodeTxt = (TextView) this.findViewById(R.id.brandCodeView);
		brandCodeTxt.setText(intent.getExtras().get("brandCode").toString());
		brandCountTxt = (TextView) this.findViewById(R.id.brandCountView);
		brandCountTxt.setText(intent.getExtras().get("brandCount").toString());
		formatTxt =(TextView)  this.findViewById(R.id.formatView);
		formatTxt.setText(intent.getExtras().get("format").toString());
		amountTxt = (TextView) this.findViewById(R.id.amountView);
		amountTxt.setText(intent.getExtras().get("amount").toString());
		unitTxt = (TextView) this.findViewById(R.id.unitView);
		unitTxt.setText(intent.getExtras().get("agency").toString());
		vipTxt =(TextView)  this.findViewById(R.id.vipView);
		vipTxt.setText(intent.getExtras().get("vip").toString());
		dateTxt =(TextView)  this.findViewById(R.id.dateView);
		dateTxt.setText(intent.getExtras().get("date").toString());
		descTxt = (TextView) this.findViewById(R.id.descView);
		descTxt.setText(intent.getExtras().get("desc").toString());
		statusTxt =(TextView)  this.findViewById(R.id.statusView);
		statusTxt.setText(intent.getExtras().get("status").toString());
	}

}
