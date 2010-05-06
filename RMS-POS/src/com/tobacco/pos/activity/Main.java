package com.tobacco.pos.activity;

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
import com.tobacco.main.activity.view.RMSBaseView;
import com.tobacco.pos.contentProvider.Loginer;
import com.tobacco.pos.handler.InventoryBillHandler;

public class Main extends RMSBaseView {

	private int REQUEST_PAYMENT = 0;
	private int REQUEST_CONSUME = 1;
	private int REQUEST_COMPLAINT = 2;
	private int REQUEST_RETURN = 3;
	private int REQUEST_KIND = 4;
	private int REQUEST_PURCHASE = 5;
	private int REQUEST_REPORT = 6;
	private int REQUEST_INVENTORY = 7;
	
	private String[] saleFunctions = {"收银管理","溢耗管理","投诉管理","退货管理"};
	private String[] adminFunctions = {"类别管理","进货管理","报表功能","库存管理"};
	 
	private int state;
	private InventoryBillHandler inventoryHandler;
	
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
		
		inventoryHandler = new InventoryBillHandler(this); 
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		state = inventoryHandler.checkState();
		super.onResume();
	}

	public OnItemClickListener listener = new OnItemClickListener(){
		
		 
		public void onItemClick(AdapterView<?> list, View view, int position,
				long id) { 
			Intent intent;
			ListView listView = (ListView)list;
			
			if(listView.getId()==R.id.listSaleMan){
				switch(position){
				case 0:
					if(state == InventoryBillHandler.STATE_PAUSE){
						new AlertDialog.Builder(Main.this)
						.setMessage("存在未完成的盘点，请先完成或取消该盘点。").show();
					}else{// 开始付款管理
						String screenName = Main.this.currentUserBO.getScreenName();
						intent = new Intent(Main.this, PaymentManagement.class);
						intent.putExtra("userName", screenName);
				
						startActivity(intent);

					}
					break;
				case 1:
					if(state == InventoryBillHandler.STATE_PAUSE){
						new AlertDialog.Builder(Main.this)
						.setMessage("存在未完成的盘点，请先完成或取消该盘点。").show();
					}else{
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
								
								startActivityForResult(intent,REQUEST_CONSUME);
							}
							
						}).create().show();
					}		
					
					break;
				case 2:
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
							
							startActivityForResult(intent,REQUEST_COMPLAINT);
						}
						
					}).create().show();
					break;
				case 3:
					if(state == InventoryBillHandler.STATE_PAUSE){
						new AlertDialog.Builder(Main.this)
						.setMessage("存在未完成的盘点，请先完成或取消该盘点。").show();
					}else{
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
								
								startActivityForResult(intent,REQUEST_RETURN);
							}
							
						}).create().show();
					}		
					break;
				}
			}else if(listView.getId()==R.id.listAdmin){
				switch(position){
				case 0:
					if(state == InventoryBillHandler.STATE_PAUSE){
						new AlertDialog.Builder(Main.this)
						.setMessage("存在未完成的盘点，请先完成或取消该盘点。").show();
					}else{
						String screenName = Main.this.currentUserBO.getScreenName();
						intent = new Intent(Main.this, KindManagement.class);
						intent.putExtra("userName", screenName);
						
						
						startActivity(intent);
					}	
					break;
				case 1:
					if(state == InventoryBillHandler.STATE_PAUSE){
						new AlertDialog.Builder(Main.this)
						.setMessage("存在未完成的盘点，请先完成或取消该盘点。").show();
					}else{//开始进货管理
						String screenName = Main.this.currentUserBO.getScreenName();
						intent = new Intent(Main.this, PurchaseManagement.class);
						intent.putExtra("userName", screenName);
				
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

					 
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = null;
							if(which == 0){
								intent = new Intent("com.tobacco.pos.activity.InventoryInsert");
							}else{
								intent = new Intent("com.tobacco.pos.activity.InventoryBillSelect");
							}
							
							startActivityForResult(intent,REQUEST_INVENTORY);
						}
						
					}).create().show();
					break;
				}
			}
		}
		
	};
}