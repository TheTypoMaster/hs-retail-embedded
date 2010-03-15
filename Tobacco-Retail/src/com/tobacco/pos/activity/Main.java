package com.tobacco.pos.activity;

import com.tobacco.pos.util.Loginer;
import com.tobacco.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
	private Button b1;//销售
	private Button b2;//种类管理
	private Button b3;//进货管理
	private Button b4;//报表
	
	private Loginer loginer = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		b1 = (Button) this.findViewById(R.id.payment);
		b2 = (Button) this.findViewById(R.id.kind);
		b3 = (Button) this.findViewById(R.id.purchase);
		b4 = (Button) this.findViewById(R.id.report);

		b1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				loginer = new Loginer(Main.this);
				String userName = loginer.verifyIsSBOnline(loginer.getReadableDatabase());
				if(userName == null)
				{
					Intent intent = new Intent(Main.this, Login.class);
					intent.putExtra("purpose", "PaymentManagement");
					startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(Main.this, PaymentManagement.class);
					intent.putExtra("userName", userName);
					startActivity(intent);
				}
			}

		});

		b2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Main.this, KindManagement.class);
				startActivity(intent);
			}

		});

		b3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {//
				
				loginer = new Loginer(Main.this);
				String userName = loginer.verify(loginer.getReadableDatabase());
				if (userName != null) {
					Intent intent = new Intent(Main.this,
							PurchaseManagement.class);
					intent.putExtra("userName", userName);
					startActivity(intent);
				} else {
					Intent intent = new Intent(Main.this, Login.class);
					intent.putExtra("purpose", "PurchaseManagement");
			
					startActivity(intent);
				}
			}

		});

		b4.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Main.this, ReportManagement.class);
				startActivity(intent);
			}

		});
	}
}