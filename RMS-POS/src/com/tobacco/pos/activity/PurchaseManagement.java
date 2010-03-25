package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PurchaseManagement extends Activity {
	private Loginer loginer = null;
	private GoodsCPer gCPer = null;
	private PurchaseBillCPer pBillCPer = null;
	private PurchaseItemCPer pItemCPer = null;
	private ManufacturerCPer mCPer = null;
	private GoodsKindCPer gKindCPer = null;
	private GoodsPriceCPer gPriceCPer = null;
	private UnitCPer unitCPer = null;
	
	private TextView purchaseWelcome;
	
	private LinearLayout addGoodsTable = null;
	private LinearLayout addGoodsIntoPBillTable = null;
	
	private String userName = "";
	
	private int selectedPBillId = -1;//选择的进货单的ID
	
	private List<Vector> goodsVector;
	private List<Vector> newGoods;
	private List<Integer> newGoodsIds;//新增加的商品ID
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.purchasemanagement);
	
		loginer = new Loginer(PurchaseManagement.this);
		
		userName = this.getIntent().getStringExtra("userName");
		purchaseWelcome = (TextView) this.findViewById(R.id.purchaseWelcome);
		purchaseWelcome.setText("你好:" + userName);
	 
	
		addGoodsTable = (LinearLayout)this.findViewById(R.id.addGoodsTable);
		addGoodsIntoPBillTable = (LinearLayout)this.findViewById(R.id.addGoodsIntoPBillTable);
		
		addGoodsIntoPBillTable.setVisibility(8);
		addGoodsTable.setVisibility(8);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "添加商品信息");
		menu.add(0, 1, 1, "添加进货商品");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {//接受从AddGoods传来的数据
		
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 0){//如果成功接收到数据。
			
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
					
					goodsVector = new ArrayList<Vector>();
					newGoods = new ArrayList<Vector>();
					newGoodsIds = new ArrayList<Integer>();

					for(int i=1;i<newGoodsTable.getChildCount()-1;i++){
					
						TableRow row = (TableRow)newGoodsTable.getChildAt(i);
						Vector t = new Vector();
						TextView name = (TextView) row.getChildAt(0);
						TextView m = (TextView)row.getChildAt(1);
						TextView kind = (TextView)row.getChildAt(2);
						t.add(name.getText().toString());
						t.add(m.getText().toString());
						t.add(kind.getText().toString());
						goodsVector.add(t);
				
					}
					
					for(int i=0;i<goodsVector.size();i++){
						Vector temp = goodsVector.get(i);
						if(!newGoods.contains(temp))
							newGoods.add(temp);
					}
				
					for(int i=0;i<newGoods.size();i++){//开始存储商品信息，商品名字，厂家，种类
						Vector temp = newGoods.get(i);

						int newGoodsId =  gCPer.addGoods(temp.get(0).toString(),
								mCPer.getMIdByMName(temp.get(1).toString()), "", 
								gKindCPer.getGoodsKindIdByGoodsKindName(temp.get(2).toString()));
						
						if(newGoodsId!=-1)
							newGoodsIds.add(newGoodsId);//新增商品成功
						else{
							Toast.makeText(PurchaseManagement.this, "增加新商品:"+temp.get(0).toString()+" 失败", Toast.LENGTH_SHORT).show();
							break;
						}
						
					}

					for(int i=1;i<newGoodsTable.getChildCount()-1;i++){//取得新加商品的ID
						TableRow row = (TableRow)newGoodsTable.getChildAt(i);
						TextView name = (TextView) row.getChildAt(0);
						TextView m = (TextView)row.getChildAt(1);
						TextView kind = (TextView)row.getChildAt(2);
						TextView barcode = (TextView)row.getChildAt(3);
						TextView unit = (TextView)row.getChildAt(4);
						TextView inPrice = (TextView)row.getChildAt(5);
						TextView outPrice = (TextView)row.getChildAt(6);
						
						Vector t = new Vector();
						t.add(name.getText().toString());
						t.add(m.getText().toString());
						t.add(kind.getText().toString());
						
						if(newGoods.contains(t)){
							int theGoodsId = newGoodsIds.get(newGoods.indexOf(t));
							int theUnitId = unitCPer.getUnitIdByUnitName(unit.getText().toString());
							String theBarcode = barcode.getText().toString();
							double theInPrice = Double.parseDouble(inPrice.getText().toString());
							double theOutPrice = Double.parseDouble(outPrice.getText().toString());
							
							gPriceCPer.addGoodsPrice(theGoodsId, theUnitId, theBarcode, theInPrice, theOutPrice);//往商品价格表里增加数据
							
						}
						
					}
					
					newGoodsTable.removeViews(1, newGoodsTable.getChildCount()-1);

				}
				
			});
			Button addNewGoodsReset = new Button(this);
			addNewGoodsReset.setText("取消");
			addNewGoodsReset.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					goodsVector.clear();
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

			switch (item.getItemId()) {
			case 0://添加商品
				addGoods();
				break;
			case 1://添加进货商品
				addGoodsIntoPBill();
				break;
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


	public void addGoods(){
		addGoodsTable.setVisibility(0);
		addGoodsIntoPBillTable.setVisibility(8);
		
		Intent intent = new Intent(PurchaseManagement.this, AddGoods.class);
		this.startActivityForResult(intent, 0);
	}
	
	public void addGoodsIntoPBill(){
		addGoodsIntoPBillTable.setVisibility(0);
		addGoodsTable.setVisibility(8);
		
		AlertDialog.Builder addPBillTip = new AlertDialog.Builder(this);
		addPBillTip.setTitle("提示");
		addPBillTip.setMessage("是否添加新的进货单？");
		addPBillTip.setPositiveButton("是", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				addPBill();
			}
			
		});
		addPBillTip.setNegativeButton("否", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
			
				selectPBill();
			}
			
		});
		addPBillTip.show();
	}
	private void addPBill(){//添加新的进货单

		Intent intent = new Intent(PurchaseManagement.this, AddPBill.class);
		intent.putExtra("userName", userName);
		this.startActivityForResult(intent, 1);
	}
	private void selectPBill(){//选择进货单
		AlertDialog.Builder selectPBillDialog = new AlertDialog.Builder(this);
		selectPBillDialog.setTitle("选择进货单");
		
		pBillCPer = new PurchaseBillCPer();
		final Spinner allPBillSpinner = new Spinner(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pBillCPer.getAllPBill());
		allPBillSpinner.setAdapter(adapter);
		
		selectPBillDialog.setView(allPBillSpinner);
		selectPBillDialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				String selected = allPBillSpinner.getSelectedItem().toString();
				String selectedPNum = selected.substring(0, selected.indexOf(":"));
				selectedPBillId = pBillCPer.getPBillIdByPBillNum(selectedPNum);
			
			}
			
		});
		selectPBillDialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
			
			}
			
		});
		selectPBillDialog.show();
	
	}
}
;