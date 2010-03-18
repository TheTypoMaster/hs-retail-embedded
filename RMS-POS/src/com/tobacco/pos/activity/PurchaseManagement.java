package com.tobacco.pos.activity;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.tobacco.R;

import com.tobacco.pos.contentProvider.GoodsKindCPer;
import com.tobacco.pos.contentProvider.Loginer;
import com.tobacco.pos.contentProvider.ManufacturerCPer;
import com.tobacco.pos.contentProvider.PurchaseBillCPer;
import com.tobacco.pos.contentProvider.PurchaseItemCPer;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TabHost.OnTabChangeListener;

public class PurchaseManagement extends TabActivity {
	private Loginer loginer = null;
	private PurchaseBillCPer pBillCPer = null;
	private PurchaseItemCPer pItemCPer = null;
	
	private TextView purchaseWelcome;

	private TextView pBillIdTView;
	private TextView pBillTimeTView;
	private EditText pBillCommentEText;
	
	private Button savePBillButton;
	
	private Spinner wholePBill;

	private String pBillCode = "";
	private int selectedPBillId = 0;
	private int selectedPriceId = 0;
	
	private TabHost mTabHost;
	
	private String userName = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.purchasemanagement);
	
		loginer = new Loginer(PurchaseManagement.this);
		
		userName = this.getIntent().getStringExtra("userName");
		purchaseWelcome = (TextView) this.findViewById(R.id.purchaseWelcome);
		purchaseWelcome.setText("你好:" + userName);
	 
		
		mTabHost = getTabHost();
	
		mTabHost.addTab(mTabHost.newTabSpec("pBill").setIndicator("H1", getResources().getDrawable(R.drawable.addpbill)).setContent(R.id.lay1));
	    mTabHost.addTab(mTabHost.newTabSpec("pItem").setIndicator("H2", getResources().getDrawable(R.drawable.addpitem)).setContent(R.id.lay2));
	    mTabHost.setCurrentTab(0);
	    
	    displayPBill();
		
	    mTabHost.setOnTabChangedListener(new OnTabChangeListener(){

			public void onTabChanged(String tabId) {
			
				if(tabId.equals("pBill")){
					displayPBill();
				}
				else{
					displayPBill();
					wholePBill = (Spinner)PurchaseManagement.this.findViewById(R.id.wholePBill);
					String allPBill [] = pBillCPer.getAllPBill();
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(PurchaseManagement.this,android.R.layout.simple_spinner_item,allPBill);
					wholePBill.setAdapter(adapter);

					wholePBill.setOnItemSelectedListener(new OnItemSelectedListener(){
						public void onItemSelected(AdapterView<?> arg0,View arg1, int arg2, long arg3) {
							String temp = ((TextView)arg1).getText()+"";
							pBillCode = temp.substring(0, temp.indexOf(":"));
							selectedPBillId = pBillCPer.getPBillIdByPBillNum(pBillCode);
						}

						public void onNothingSelected(AdapterView<?> arg0) {}
						}
					);

			}
	    }}
	);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "添加商品");
		menu.add(0, 1, 1, "添加进货商品");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {//接受从AddGoods传来的数据
		
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){//如果成功接收到数据。
			String goodsName = data.getStringExtra("GoodsName");
			Toast.makeText(this, ""+goodsName, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(mTabHost.getCurrentTab() == 1){//如果当前的Tab是"添加进货商品"
			switch (item.getItemId()) {
			case 0://添加商品
				Intent intent = new Intent(PurchaseManagement.this, AddGoods.class);
				this.startActivityForResult(intent, 0);
				
				break;
			case 1://添加进货商品

				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
//	
//	private void addGoods(){
//		ScrollView addGoodsDialog;
//		EditText goodsNameEText;
//		Spinner allManufacturerSpinner;
//		Spinner allKindSpinner;
//		Button addPriceButton;
//		TextView addGoodsInfo;
//
//		
//		ManufacturerCPer mCPer = new ManufacturerCPer();
//		GoodsKindCPer goodsKindCPer = new GoodsKindCPer();
//		
//		addGoodsDialog = (ScrollView)findViewById(R.id.addGoodsDialog);
//
//		goodsNameEText = (EditText)this.findViewById(R.id.goodsNameEText);
//		
////		allManufacturerSpinner = (Spinner)this.findViewById(R.id.allManufacturerSpinner);
////		ArrayAdapter<String> allManufacturerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mCPer.getAllManufacturerName());
////		allManufacturerSpinner.setAdapter(allManufacturerAdapter);
////		
////		allKindSpinner = (Spinner)this.findViewById(R.id.allKindSpinner);
////		ArrayAdapter<String> allGoodsKindAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,goodsKindCPer.getAllGoodsKindName());
////		allKindSpinner.setAdapter(allGoodsKindAdapter);
////		
//		addPriceButton = (Button)this.findViewById(R.id.addPriceButton);
//		addGoodsInfo = (TextView)this.findViewById(R.id.addGoodsInfo);
//		
//		AlertDialog.Builder addGDialog = new AlertDialog.Builder(this);
//		addGDialog.setTitle("增加商品");
//		addGDialog.setView(addGoodsDialog);
////		addGDialog.set
//		
//		addGDialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
//
//			public void onClick(DialogInterface dialog, int which) {
//			
//			}
//			
//		});
//		addGDialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
//
//			public void onClick(DialogInterface dialog, int which) {
//				
//			}
//			
//		}).show();
//		
//	}


	@Override
	protected void onStop() {
		super.onPause();

		if(loginer.logout(userName, loginer.getWritableDatabase())){
			Intent intent = new Intent(PurchaseManagement.this, Main.class);
			PurchaseManagement.this.startActivity(intent);
			
		
		}
	}


	private void displayPBill(){
		pBillCPer = new PurchaseBillCPer();
		pBillIdTView = (TextView) PurchaseManagement.this.findViewById(R.id.pBillIdTView);
		pBillTimeTView = (TextView) PurchaseManagement.this.findViewById(R.id.pBillTimeTView);

		pBillIdTView.setText("P" + (Integer.parseInt(pBillCPer.getMaxPBillNum()) + 1));

		pBillTimeTView.setText((new Date()).toLocaleString());

		pBillCommentEText = (EditText) PurchaseManagement.this
							.findViewById(R.id.pBillCommentEText);
		pBillCommentEText.setText("");

		savePBillButton = (Button) PurchaseManagement.this
							.findViewById(R.id.savePBillButton);
		savePBillButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				boolean flag = pBillCPer.addPBill(pBillIdTView.getText().toString(), 1, pBillTimeTView.getText().toString(),pBillCommentEText.getText().toString());
				if(flag){
					Toast.makeText(getApplicationContext(), "添加进货单成功!", Toast.LENGTH_SHORT).show();
					
					pBillIdTView.setText("P" + (Integer.parseInt(pBillCPer.getMaxPBillNum()) + 1));
					pBillTimeTView.setText((new Date()).toLocaleString());
					pBillCommentEText.setText("");

				}
					
				else
					Toast.makeText(getApplicationContext(), "添加进货单失败!", Toast.LENGTH_SHORT).show();
				}
				
			}
		);
	}

}
