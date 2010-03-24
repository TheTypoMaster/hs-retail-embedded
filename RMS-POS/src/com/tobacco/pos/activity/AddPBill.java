package com.tobacco.pos.activity;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tobacco.R;
import com.tobacco.pos.contentProvider.PurchaseBillCPer;
import com.tobacco.pos.contentProvider.UserInfoCPer;

public class AddPBill extends Activity{
	
	private PurchaseBillCPer pBillCPer = null;
	private UserInfoCPer userInfoCPer = null;
	
	private TextView pBillNumTView = null;
	private TextView pTimeTView = null;
	private EditText pCommentEText = null;
	private Button addPBillOk = null;
	private Button addPBillReset = null;

	private String userName = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.addpbill);
		
		this.setTitle("添加进货单");

		pBillCPer = new PurchaseBillCPer();
		userInfoCPer = new UserInfoCPer();
		
		pBillNumTView = (TextView)this.findViewById(R.id.pBillNumTView);
		String maxPBillNum = pBillCPer.getMaxPBillNum();
		pBillNumTView.setText("P"+(Integer.parseInt(maxPBillNum)+1));
		
		pTimeTView = (TextView)this.findViewById(R.id.pTimeTView);
		pTimeTView.setText((new Date()).toLocaleString());
		pCommentEText = (EditText)this.findViewById(R.id.pCommentEText);
		addPBillOk = (Button)this.findViewById(R.id.addPBillOk);
		addPBillReset = (Button)this.findViewById(R.id.addPBillReset);
		
		addPBillOk.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				userName = getIntent().getStringExtra("userName");
				boolean flag = pBillCPer.addPBill(pBillNumTView.getText().toString(), userInfoCPer.getUserIdByUserName(userName), pTimeTView.getText().toString(), pCommentEText.getText().toString());
				if(flag)
				{
					Toast.makeText(AddPBill.this, "成功添加进货单:"+pBillNumTView.getText(), Toast.LENGTH_SHORT).show();
					finish();
				}
			}
			
		});
		addPBillReset.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				finish();
			}
			
		});
	}

}
