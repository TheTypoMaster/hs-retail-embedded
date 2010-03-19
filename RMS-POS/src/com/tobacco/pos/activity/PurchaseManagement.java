package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.Date;

import com.tobacco.R;

import com.tobacco.pos.contentProvider.Loginer;
import com.tobacco.pos.contentProvider.PurchaseBillCPer;
import com.tobacco.pos.contentProvider.PurchaseItemCPer;
import com.tobacco.pos.entity.SinglePrice;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
				
				displayPBill();
				if(tabId.equals("pBill")){
					
				}
				else{
				
				/*	wholePBill = (Spinner)PurchaseManagement.this.findViewById(R.id.wholePBill);
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
					);*/

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
			TableLayout pItemTable = (TableLayout)this.findViewById(R.id.pItemTable);
//			if(pItemTable.getChildCount()==0){
//				TableRow title = new TableRow(this);
//			
//				TextView nameTView = new TextView(this);
//				nameTView.setText("名字");
//				
//				TextView mTView = new TextView(this);
//				mTView.setText("厂家");
//				
//				TextView kindTView = new TextView(this);
//				kindTView.setText("类别");
//				
//				TextView barcodeTView = new TextView(this);
//				barcodeTView.setText("条形码");
//				
//				TextView unitTView = new TextView(this);
//				unitTView.setText("单位");
//				
//				TextView inPriceTView = new TextView(this);
//				inPriceTView.setText("进货价");
//				
//				TextView outPriceTView = new TextView(this);
//				outPriceTView.setText("售价");
//				
//				title.addView(nameTView);
//				title.addView(mTView);
//				title.addView(kindTView);
//				title.addView(barcodeTView);
//				title.addView(unitTView);
//				title.addView(inPriceTView);
//				title.addView(outPriceTView);
//				
//				pItemTable.addView(title);
//				
//			}
			ArrayList<SinglePrice> allPrice = data.getParcelableArrayListExtra("allPrice");
			String goodsName = data.getStringExtra("goodsName");
			String mName = data.getStringExtra("mName");
			String goodsKind = data.getStringExtra("goodsKind");
			
			for(int i=0;i<allPrice.size();i++){
				TableRow content = new TableRow(this);
				
				TextView nameTV = new TextView(this);
				nameTV.setText(goodsName);
				
				TextView mTV = new TextView(this);
				mTV.setText(mName);
				
				TextView kindTV = new TextView(this);
				kindTV.setText(goodsKind);
				
				SinglePrice temp = allPrice.get(i);
				
				TextView barcodeTV = new TextView(this);
				barcodeTV.setText(temp.get("barcode"));
				
				TextView unitTV = new TextView(this);
				unitTV.setText(temp.get("unitName"));
				
				TextView inPriceTV = new TextView(this);
				inPriceTV.setText(temp.get("inPrice"));
				
				TextView outPriceTV = new TextView(this);
				outPriceTV.setText(temp.get("outPrice"));
				
				ImageView deleteImage = new ImageView(PurchaseManagement.this);
				deleteImage.setImageResource(R.drawable.delete);
				deleteImage.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
//						((TableLayout)v.getParent().getParent()).removeView(r);
//						if(salesBillTable.getChildCount()==2){
//							salesBillTable.removeViewAt(1);
//						}
					}
					
				});
				
				content.addView(nameTV);
				content.addView(mTV);
				content.addView(kindTV);
				content.addView(barcodeTV);
				content.addView(unitTV);
				content.addView(inPriceTV);
				content.addView(outPriceTV);
				content.addView(deleteImage);
				
				
				pItemTable.addView(content);
			}
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
;