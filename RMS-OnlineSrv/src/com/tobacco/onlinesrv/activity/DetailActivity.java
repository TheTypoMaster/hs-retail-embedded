package com.tobacco.onlinesrv.activity;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.Agency;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity {

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
		fillAgencyText(intent.getExtras().get("agency").toString());
		vipTxt =(TextView)  this.findViewById(R.id.vipView);
		vipTxt.setText(intent.getExtras().get("vip").toString());
		dateTxt =(TextView)  this.findViewById(R.id.dateView);
		dateTxt.setText(intent.getExtras().get("date").toString());
		descTxt = (TextView) this.findViewById(R.id.descView);
		descTxt.setText(intent.getExtras().get("desc").toString());
		statusTxt =(TextView)  this.findViewById(R.id.statusView);
		statusTxt.setText(intent.getExtras().get("statusName").toString());
	}
	private void fillAgencyText(String agencyId)
	{
		Cursor cursor = this.managedQuery(Agency.CONTENT_URI, null, "id = "+agencyId, null, null);
		if(cursor.getCount()==0)
			Toast.makeText(DetailActivity.this, "找不到您所在单位",
					Toast.LENGTH_SHORT).show();
		else{
			cursor.moveToFirst();
			String agencyName = cursor.getString(1);
			unitTxt.setText(agencyName);
		}		
	}

}
