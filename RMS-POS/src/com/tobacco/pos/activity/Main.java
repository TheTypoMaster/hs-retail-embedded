package com.tobacco.pos.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.tobacco.R;
import com.tobacco.pos.contentProvider.Loginer;

public class Main extends Activity {

	private String[] saleFunctions = {"收银管理","溢耗管理","投诉管理","退货管理"};
	private String[] adminFunctions = {"类别管理","进货管理","报表功能","库存管理"};
	private Loginer loginer = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ListView saleList = (ListView)findViewById(R.id.listSaleMan);
		ListView adminList = (ListView)findViewById(R.id.listAdmin);
		
		saleList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,saleFunctions));
		adminList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,adminFunctions));
		
		saleList.setOnItemClickListener(listener);
		adminList.setOnItemClickListener(listener);
	}
	
	public OnItemClickListener listener = new OnItemClickListener(){
		
		@Override
		public void onItemClick(AdapterView<?> list, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			String userName;
			Intent intent;
			ListView listView = (ListView)list;
			
			if(listView.getId()==R.id.listSaleMan){
				switch(position){
				case 0:
					loginer = new Loginer(Main.this);
					userName = loginer.verifyIsSBOnline(loginer.getReadableDatabase());
					if(userName == null)
					{
						intent = new Intent(Main.this, Login.class);
						intent.putExtra("purpose", "PaymentManagement");
						startActivity(intent);
					}
					else
					{
						intent = new Intent(Main.this, PaymentManagement.class);
						intent.putExtra("userName", userName);
						startActivity(intent);
					}
					break;
				case 1:
					new AlertDialog.Builder(Main.this)
					.setTitle("选择功能")
					.setItems(R.array.select_consume_function_items, new DialogInterface.OnClickListener(){
	
						@Override
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
					break;
				case 2:
					new AlertDialog.Builder(Main.this)
					.setTitle("选择功能")
					.setItems(R.array.select_complaint_function_items, new DialogInterface.OnClickListener(){
	
						@Override
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
					break;
				case 3:
					new AlertDialog.Builder(Main.this)
					.setTitle("选择功能")
					.setItems(R.array.select_return_function_items, new DialogInterface.OnClickListener(){
	
						@Override
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
					break;
				}
			}else if(listView.getId()==R.id.listAdmin){
				switch(position){
				case 0:
					intent = new Intent(Main.this, KindManagement.class);
					startActivity(intent);
					break;
				case 1:
					loginer = new Loginer(Main.this);
					userName = loginer.verify(loginer.getReadableDatabase());
					if (userName != null) {
						intent = new Intent(Main.this,
								PurchaseManagement.class);
						intent.putExtra("userName", userName);
						startActivity(intent);
					} else {
						intent = new Intent(Main.this, Login.class);
						intent.putExtra("purpose", "PurchaseManagement");
				
						startActivity(intent);
					}
					break;
				case 2:
					AlertDialog.Builder selectReportKindTip = new AlertDialog.Builder(Main.this);
					selectReportKindTip.setTitle("报表类型");
					selectReportKindTip.setItems(R.array.reportKind, new DialogInterface.OnClickListener(){

						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Main.this, ReportManagement.class);
							intent.putExtra("reportKind", which);
							startActivity(intent);
						}
						
					});
					
					selectReportKindTip.show();
					break;
				case 3:
					new AlertDialog.Builder(Main.this)
					.setTitle("选择功能")
					.setItems(R.array.select_inventory_function_items, new DialogInterface.OnClickListener(){

						@Override
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
					break;
				}
			}
//			switch(position){	
//			case 0:
//				loginer = new Loginer(Main.this);
//				userName = loginer.verifyIsSBOnline(loginer.getReadableDatabase());
//				if(userName == null)
//				{
//					intent = new Intent(Main2.this, Login.class);
//					intent.putExtra("purpose", "PaymentManagement");
//					startActivity(intent);
//				}
//				else
//				{
//					intent = new Intent(Main2.this, PaymentManagement.class);
//					intent.putExtra("userName", userName);
//					startActivity(intent);
//				}
//				break;
//			case 1:
//				intent = new Intent(Main2.this, KindManagement.class);
//				startActivity(intent);
//				break;
//			case 2:
//				loginer = new Loginer(Main2.this);
//				userName = loginer.verify(loginer.getReadableDatabase());
//				if (userName != null) {
//					intent = new Intent(Main2.this,
//							PurchaseManagement.class);
//					intent.putExtra("userName", userName);
//					startActivity(intent);
//				} else {
//					intent = new Intent(Main2.this, Login.class);
//					intent.putExtra("purpose", "PurchaseManagement");
//			
//					startActivity(intent);
//				}
//				break;
//			case 3:
//				intent = new Intent(Main2.this, ReportManagement.class);
//				startActivity(intent);
//				break;
//			case 4:
//				new AlertDialog.Builder(Main2.this)
//				.setTitle("选择功能")
//				.setItems(R.array.select_consume_function_items, new DialogInterface.OnClickListener(){
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						Intent intent = null;
//						if(which == 0){
//							intent = new Intent("com.tobacco.pos.activity.CosumeInsert");
//						}else{
//							intent = new Intent("com.tobacco.pos.activity.ConsumeSelect");
//						}
//						
//						startActivity(intent);
//					}
//					
//				}).create().show();
//				break;
//			case 5:
//				new AlertDialog.Builder(Main2.this)
//				.setTitle("选择功能")
//				.setItems(R.array.select_complaint_function_items, new DialogInterface.OnClickListener(){
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						Intent intent = null;
//						if(which == 0){
//							intent = new Intent("com.tobacco.pos.activity.ComplaintInsertDialog");
//						}else{
//							intent = new Intent("com.tobacco.pos.activity.ComplaintSelect");
//						}
//						
//						startActivity(intent);
//					}
//					
//				}).create().show();
//				break;
//			case 6:
//				new AlertDialog.Builder(Main2.this)
//				.setTitle("选择功能")
//				.setItems(R.array.select_return_function_items, new DialogInterface.OnClickListener(){
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						Intent intent = null;
//						if(which == 0){
//							intent = new Intent("com.tobacco.pos.activity.ReturnInsert");
//						}else{
//							intent = new Intent("com.tobacco.pos.activity.ReturnSelect");
//						}
//						
//						startActivity(intent);
//					}
//					
//				}).create().show();
//				break;
//			case 7:
//				new AlertDialog.Builder(Main2.this)
//				.setTitle("选择功能")
//				.setItems(R.array.select_inventory_function_items, new DialogInterface.OnClickListener(){
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						Intent intent = null;
//						if(which == 0){
//							intent = new Intent("com.tobacco.pos.activity.InventoryInsert");
//						}else{
//							intent = new Intent("com.tobacco.pos.activity.InventoryBillSelect");
//						}
//						
//						startActivity(intent);
//					}
//					
//				}).create().show();
//				break;
//			}
		}
		
	};
}