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
	private Button b1;// ������ť
	private Button b2;// ������ť
	private Button b3;// ������ť
	private Button b4;// ����ť
	private TableCreater tableHelper = null;
	private Loginer loginer = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tableHelper = new TableCreater(this);// �½����ݿ������
		tableHelper.createTable(tableHelper.getWritableDatabase());

		b1 = (Button) this.findViewById(R.id.payment);
		b2 = (Button) this.findViewById(R.id.kind);
		b3 = (Button) this.findViewById(R.id.purchase);
		b4 = (Button) this.findViewById(R.id.report);

		b1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
//				AlertDialog.Builder loginPaymentDialog = new AlertDialog.Builder(
//						Main.this);
//				loginPaymentDialog.setTitle("��½");
//				TableLayout loginPaymentLayout = new TableLayout(Main.this);
//				TableRow loginPaymentRow1 = new TableRow(Main.this);
//				TextView nameTView = new TextView(Main.this);
//				nameTView.setText("�û���:");
//				loginPaymentRow1.addView(nameTView);
//
//				EditText nameEText = new EditText(Main.this);
//				nameEText.setWidth(200);
//				loginPaymentRow1.addView(nameEText);
//				loginPaymentLayout.addView(loginPaymentRow1);
//				loginPaymentDialog.setView(loginPaymentLayout);
//
//				loginPaymentDialog.setPositiveButton("ȷ��",
//						new DialogInterface.OnClickListener() {
//
//							public void onClick(DialogInterface dialog,
//									int which) {
//
//							}
//						});
//				loginPaymentDialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){
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

			public void onClick(View v) {// ����֤�Ƿ����������û������û�еĻ���Ҫ�ù���Ա���˻�����

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