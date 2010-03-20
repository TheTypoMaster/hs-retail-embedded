package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.Date;

import com.tobacco.R;

import com.tobacco.pos.contentProvider.GoodsCPer;
import com.tobacco.pos.contentProvider.GoodsKindCPer;
import com.tobacco.pos.contentProvider.GoodsPriceCPer;
import com.tobacco.pos.contentProvider.Loginer;
import com.tobacco.pos.contentProvider.ManufacturerCPer;
import com.tobacco.pos.contentProvider.PurchaseBillCPer;
import com.tobacco.pos.contentProvider.PurchaseItemCPer;
import com.tobacco.pos.contentProvider.UnitCPer;
import com.tobacco.pos.entity.SinglePrice;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class PurchaseManagement extends TabActivity {
	private Loginer loginer = null;
	private GoodsCPer gCPer = null;
	private PurchaseBillCPer pBillCPer = null;
	private PurchaseItemCPer pItemCPer = null;
	private ManufacturerCPer mCPer = null;
	private GoodsKindCPer gKindCPer = null;
	private GoodsPriceCPer gPriceCPer = null;
	private UnitCPer unitCPer = null;
	
	private TextView purchaseWelcome;

	private TextView pBillIdTView;
	private TextView pBillTimeTView;
	private EditText pBillCommentEText;
	
	private Button savePBillButton;
	
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
			
			final TableLayout newGoodsTable = (TableLayout)this.findViewById(R.id.newGoodsTable);
			if(newGoodsTable.getChildCount()>=2)
				newGoodsTable.removeViewAt(newGoodsTable.getChildCount()-1);
			final ArrayList<SinglePrice> allPrice = data.getParcelableArrayListExtra("allPrice");
			final String goodsName = data.getStringExtra("goodsName");
			final String mName = data.getStringExtra("mName");
			final String goodsKind = data.getStringExtra("goodsKind");
			
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
						((TableLayout)v.getParent().getParent()).removeView((TableRow)v.getParent());
						if(newGoodsTable.getChildCount()==2){
							newGoodsTable.removeViewAt(1);
						}
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
				
				
				newGoodsTable.addView(content);
			}
			TableRow lastRow = new TableRow(this);
			TextView[] blank = new TextView[6];
			for(int i=0;i<6;i++){
				blank[i] = new TextView(this);
				blank[i].setText("");
			}
			for(int i=0;i<5;i++){
				lastRow.addView(blank[i]);
			}
			Button addNewGoodsOk = new Button(this);
			addNewGoodsOk.setText("确定");
			addNewGoodsOk.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {

					gCPer = new GoodsCPer();
					mCPer = new ManufacturerCPer();
					gKindCPer = new GoodsKindCPer();
					gPriceCPer = new GoodsPriceCPer();
					unitCPer = new UnitCPer();
					
					int mId = mCPer.getMIdByMName(mName);
					int kindId = gKindCPer.getGoodsKindIdByGoodsKindName(goodsKind);
					int newGoodsId = gCPer.addGoods(goodsName, mId, "", kindId);//如果成功添加商品的话会返回新加的商品ID
					
					if(newGoodsId!=-1){
						boolean flag = true;
						for(int i=0;i<allPrice.size();i++)
						{
							SinglePrice temp = allPrice.get(i);
							int unitId = unitCPer.getUnitIdByUnitName(temp.get("unitName"));
							flag = gPriceCPer.addGoodsPrice(newGoodsId, unitId, temp.get("barcode"), Double.parseDouble(temp.get("inPrice")), Double.parseDouble(temp.get("outPrice")));
							if(!flag){
								Toast.makeText(PurchaseManagement.this, "增加新商品失败", Toast.LENGTH_SHORT).show();
								break;
							}
							
//							Toast.makeText(PurchaseManagement.this, temp.get("unitName")+":"+temp.get("barcode")+":"+temp.get("inPrice")+":"+temp.get("outPrice"), Toast.LENGTH_LONG).show();
						}
						if(flag){
							newGoodsTable.removeViews(1, newGoodsTable.getChildCount()-1);//添加完进货项后将记录删除
							Toast.makeText(PurchaseManagement.this, "增加新商品成功", Toast.LENGTH_SHORT).show();
						}
						
					}
					else{
						Toast.makeText(PurchaseManagement.this, "增加新商品失败", Toast.LENGTH_SHORT).show();
					}
				}
				
			});
			Button addNewGoodsReset = new Button(this);
			addNewGoodsReset.setText("取消");
			addNewGoodsReset.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					newGoodsTable.removeViews(1, newGoodsTable.getChildCount()-1);
				}
				
			});
			lastRow.addView(addNewGoodsOk);
			lastRow.addView(addNewGoodsReset);
			lastRow.addView(blank[5]);
			newGoodsTable.addView(lastRow);
			
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