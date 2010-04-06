package com.tobacco.pos.activity;

import com.tobacco.pos.contentProvider.Loginer;
import com.tobacco.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
	private Button b5;//溢耗管理
	private Button b6;//投诉管理
	private Button b7;//退货管理
	private Button b8;//库存管理
	
	private Loginer loginer = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		b1 = (Button) this.findViewById(R.id.payment);
		b2 = (Button) this.findViewById(R.id.kind);
		b3 = (Button) this.findViewById(R.id.purchase);
		b4 = (Button) this.findViewById(R.id.report);
		b5 = (Button) this.findViewById(R.id.consume);
		b6 = (Button) this.findViewById(R.id.complaint);
		b7 = (Button) this.findViewById(R.id.returnGoods);
		b8 = (Button) this.findViewById(R.id.inventory);

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
		
		b5.setOnClickListener(new OnClickListener(){

		 
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(Main.this)
				.setTitle("选择功能")
				.setItems(R.array.select_consume_function_items, new DialogInterface.OnClickListener(){

					 
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = null;
						if(which == 0){
							intent = new Intent("com.tobacco.pos.activity.CosumeInsert");
						}else{
							intent = new Intent("com.tobacco.pos.activity.ConsumeSelect");
						}
						
						startActivity(intent);
					}
					
				}).create().show();
			}
        	
        });
		
		b6.setOnClickListener(new OnClickListener(){

			 
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(Main.this)
				.setTitle("选择功能")
				.setItems(R.array.select_complaint_function_items, new DialogInterface.OnClickListener(){

				 
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = null;
						if(which == 0){
							intent = new Intent("com.tobacco.pos.activity.ComplaintInsertDialog");
						}else{
							intent = new Intent("com.tobacco.pos.activity.ComplaintSelect");
						}
						
						startActivity(intent);
					}
					
				}).create().show();
			}
			
		});
		
		b7.setOnClickListener(new OnClickListener(){

			 
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(Main.this)
				.setTitle("选择功能")
				.setItems(R.array.select_return_function_items, new DialogInterface.OnClickListener(){

					 
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = null;
						if(which == 0){
							intent = new Intent("com.tobacco.pos.activity.ReturnInsert");
						}else{
							intent = new Intent("com.tobacco.pos.activity.ReturnSelect");
						}
						
						startActivity(intent);
					}
					
				}).create().show();
			}
			
		});
		
		b8.setOnClickListener(new OnClickListener(){

			 
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(Main.this)
				.setTitle("选择功能")
				.setItems(R.array.select_inventory_function_items, new DialogInterface.OnClickListener(){

				 
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = null;
						if(which == 0){
							intent = new Intent("com.tobacco.pos.activity.InventoryInsert");
						}else{
							intent = new Intent("com.tobacco.pos.activity.InventoryBillSelect");
						}
						
						startActivity(intent);
					}
					
				}).create().show();
			}
			
		});
	}
}