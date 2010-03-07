package com.tobacco.pos.activity;

import com.tobacco.pos.util.Loginer;
import com.tobacco.pos.util.TableCreater;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Main extends Activity {
	private Button b1;// 收银按钮
	private Button b2;// 类别管理按钮
	private Button b3;// 进货按钮
	private Button b4;// 报表按钮
	private TableCreater tableHelper = null;
	private Loginer loginer = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tableHelper = new TableCreater(this);// 新建数据库帮助类
		tableHelper.createTable(tableHelper.getWritableDatabase());

		b1 = (Button) this.findViewById(R.id.payment);
		b2 = (Button) this.findViewById(R.id.kind);
		b3 = (Button) this.findViewById(R.id.purchase);
		b4 = (Button) this.findViewById(R.id.report);

		b1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
//				AlertDialog.Builder loginPaymentDialog = new AlertDialog.Builder(
//						Main.this);
//				loginPaymentDialog.setTitle("登陆");
//				TableLayout loginPaymentLayout = new TableLayout(Main.this);
//				TableRow loginPaymentRow1 = new TableRow(Main.this);
//				TextView nameTView = new TextView(Main.this);
//				nameTView.setText("用户名:");
//				loginPaymentRow1.addView(nameTView);
//
//				EditText nameEText = new EditText(Main.this);
//				nameEText.setWidth(200);
//				loginPaymentRow1.addView(nameEText);
//				loginPaymentLayout.addView(loginPaymentRow1);
//				loginPaymentDialog.setView(loginPaymentLayout);
//
//				loginPaymentDialog.setPositiveButton("确定",
//						new DialogInterface.OnClickListener() {
//
//							public void onClick(DialogInterface dialog,
//									int which) {
//
//							}
//						});
//				loginPaymentDialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
//
//					public void onClick(DialogInterface dialog, int which) {
//				 
//					}
//					
//				});
//				loginPaymentDialog.show();
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
			
//				Intent intent = new Intent(Main.this, PaymentManagement.class);
//				startActivity(intent);
			}

		});

		b2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Main.this, KindManagement.class);
				startActivity(intent);
			}

		});

		b3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {// 先验证是否有在线在用户，如果没有的话需要用管理员的账户登入

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