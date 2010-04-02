package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

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
	private TableLayout goodsIntoPBillTable = null;
	
	private String userName = "";
	
	private int selectedPBillId = -1;//选择的进货单的ID
	private int newPBillId = -1;//新建的进货单的ID
	
	private int selectedGoodsId = -1;//在入货的时候选择的商品ID
	private int selectedUnitId = -1;//在入货的时候根据商品选择的单位ID
	
	private List<Integer> priceIdList  = new ArrayList<Integer>();//存储将要添加到进货单里的商品的价格项Id
	private List<Integer> countList = new ArrayList<Integer>();//存储将要添加到进货单里的商品的数量
	
	private List<Vector> goodsVector;
	private List<Vector> newGoods;
	private List<Integer> newGoodsIds;//新增加的商品ID
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.purchasemanagement);
	
		loginer = new Loginer(PurchaseManagement.this);
		gCPer = new GoodsCPer();
		pBillCPer = new PurchaseBillCPer();
		pItemCPer = new PurchaseItemCPer();
		mCPer = new ManufacturerCPer();
		gKindCPer = new GoodsKindCPer();
		gPriceCPer = new GoodsPriceCPer();
		unitCPer = new UnitCPer();
		
		userName = this.getIntent().getStringExtra("userName");
		purchaseWelcome = (TextView) this.findViewById(R.id.purchaseWelcome);
		purchaseWelcome.setText("你好:" + userName);
	 
	
		addGoodsTable = (LinearLayout)this.findViewById(R.id.addGoodsTable);
		addGoodsIntoPBillTable = (LinearLayout)this.findViewById(R.id.addGoodsIntoPBillTable);
		goodsIntoPBillTable = (TableLayout)this.findViewById(R.id.goodsIntoPBillTable);
		
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
				
					boolean addGoodsFlag = true;
					for(int i=0;i<newGoods.size();i++){//开始存储商品信息，商品名字，厂家，种类
						Vector temp = newGoods.get(i);

						int newGoodsId =  gCPer.addGoods(temp.get(0).toString(),
								mCPer.getMIdByMName(temp.get(1).toString()), "", 
								gKindCPer.getGoodsKindIdByGoodsKindName(temp.get(2).toString()));
						
						if(newGoodsId!=-1)
							newGoodsIds.add(newGoodsId);//新增商品成功
						else{
							addGoodsFlag = false;
							Toast.makeText(PurchaseManagement.this, "增加新商品:"+temp.get(0).toString()+" 失败", Toast.LENGTH_SHORT).show();
							break;
						}
						
					}
					if(addGoodsFlag)
						Toast.makeText(PurchaseManagement.this, "成功增加所有新商品", Toast.LENGTH_SHORT).show();

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
		if(requestCode == 1){
			newPBillId = data.getIntExtra("newPBillId", -1);
			Log.d("lyq", "(P) The new PBill's id is " + newPBillId);
			if(newPBillId != -1){

				List<Integer> pIdList = new ArrayList<Integer>();
				List<Integer> cList = new ArrayList<Integer>();
				for(int i=0;i<priceIdList.size();i++){
					if(!pIdList.contains(priceIdList.get(i))){
						pIdList.add(priceIdList.get(i));
						cList.add(countList.get(i));
					}
					else{
						int theP = pIdList.indexOf(priceIdList.get(i));
						int original = cList.get(theP);
					
						cList.set(theP, original+countList.get(i));
					}
				}
				boolean wholeFlag = true;
				for(int i=0;i<pIdList.size();i++){
					boolean flag = pItemCPer.addPItem(newPBillId, pIdList.get(i), cList.get(i));
					if(!flag){
						Toast.makeText(PurchaseManagement.this, "添加进货项失败", Toast.LENGTH_SHORT).show();
						wholeFlag = false;
					}
				}
				if(wholeFlag){
					String newPBillNum = pBillCPer.getPBillNumByPBillId(newPBillId);
					Toast.makeText(PurchaseManagement.this, "恭喜，成功往新进货单:" + newPBillNum + "添加进货项", Toast.LENGTH_SHORT).show();
				}
				
				goodsIntoPBillTable.removeViews(1, goodsIntoPBillTable.getChildCount()-1);
				priceIdList.clear();
				countList.clear();
			}

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
		
		TableLayout goodsIntoPBillTable = (TableLayout)this.findViewById(R.id.goodsIntoPBillTable);
		if(goodsIntoPBillTable.getChildCount()>1)
			goodsIntoPBillTable.removeViews(1, goodsIntoPBillTable.getChildCount()-1);
		
		this.addPItem(goodsIntoPBillTable);
	}
	private void addPItem(final TableLayout goodsIntoPBillTable){//增加进货项
		if(goodsIntoPBillTable.getChildCount()>1)
			goodsIntoPBillTable.removeViewAt(goodsIntoPBillTable.getChildCount()-1);
		final Map<Integer, String> allGoodsMap = gCPer.getAllGoodsName();//取得所有的商品名字，格式是商品ID+商品名字。
		List<String> allGoodsName = new ArrayList<String>();//取得所有商品的名字（不包含商品ID）。
		
		for(Object o : allGoodsMap.keySet()){
			allGoodsName.add(allGoodsMap.get(o));
		}
		
		Spinner allGoodsSpinner = new Spinner(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allGoodsName);
		allGoodsSpinner.setAdapter(adapter);
			
		final Spinner unitSpinner = new Spinner(this);
		
		allGoodsSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int index = 0;
				for(Object o : allGoodsMap.keySet()){
					
					if(index==arg2){
						selectedGoodsId = (Integer)o;
						ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(PurchaseManagement.this, android.R.layout.simple_spinner_item, gPriceCPer.getUnitNameByGoodsId(selectedGoodsId));
					
						unitSpinner.setAdapter(unitAdapter);
					}
					index++;
				}
		
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			
			}
			
		});
		final ImageView add = new ImageView(this);
		final EditText countEText = new EditText(this);
		countEText.setWidth(7);
		countEText.setSingleLine(true);
		countEText.addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable s) {
				String theCount = countEText.getText().toString();
				if(theCount.length()>0){
					try{
						Integer.parseInt(theCount);
					}
					catch(Exception e){
						s.clear();
						Toast.makeText(PurchaseManagement.this, "数量格式不正确", Toast.LENGTH_SHORT).show();
					
					}
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
			}
			
		});//输入的内容有改变时

		countEText.setOnKeyListener(new OnKeyListener(){

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
					if(((TextView)v).getText().toString().equals("") || ((TextView)v).getText().toString() == null)
						;
					else{
						priceIdList.add(gPriceCPer.getPriceIdByGoodsIdAndUnitId(selectedGoodsId, selectedUnitId));
						countList.add(Integer.parseInt(countEText.getText().toString()));//将该进货项的数量加入到countList中。
					
						return true;
					}
				}
				return false;
			}
			
		});

		final TextView inPriceTView = new TextView(this);
		final TextView outPriceTView = new TextView(this);
	
		unitSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String selectedUnitName = ((TextView)arg1).getText().toString();
				selectedUnitId = unitCPer.getUnitIdByUnitName(selectedUnitName);
				inPriceTView.setText(gPriceCPer.getInPriceByGoodsIdAndUnitId(selectedGoodsId, selectedUnitId)+"");
				outPriceTView.setText(gPriceCPer.getOutPriceByGoodsIdAndUnitId(selectedGoodsId, selectedUnitId)+"");
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
		
		ImageView delete = new ImageView(this);
		delete.setImageResource(R.drawable.delete);
		delete.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				TableLayout t = (TableLayout)v.getParent().getParent();
				
				priceIdList.remove(t.indexOfChild((TableRow)v.getParent())-1);
				countList.remove(t.indexOfChild((TableRow)v.getParent())-1);

				
				t.removeView((TableRow)v.getParent());
			
				if(t.getChildCount()==2){
					t.removeViewAt(1);
					priceIdList.clear();
					countList.clear();
				}
				
			
			}
			
		});
		
		TableRow row = new TableRow(this);
		row.addView(allGoodsSpinner);
		
		row.addView(unitSpinner);
		row.addView(countEText);
		row.addView(inPriceTView);
		row.addView(outPriceTView);
	
		row.addView(delete);
		
		goodsIntoPBillTable.addView(row);
		
		
		add.setImageResource(R.drawable.add);
		
		
		add.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(countEText.getText().toString() == null || countEText.getText().toString().trim().equals("")){
					Toast.makeText(PurchaseManagement.this, "请输入商品的进货量", Toast.LENGTH_SHORT).show();
				}
				else{
					addPItem(goodsIntoPBillTable);
				}
			}
			
		});
		
		
		Button addGoodsIntoPBillOk = new Button(this);//确定
		addGoodsIntoPBillOk.setText("确定");
		addGoodsIntoPBillOk.setOnClickListener(new OnClickListener(){

			public void onClick(final View v) {
				if(countEText.getText().toString() == null || countEText.getText().toString().trim().equals("")){
					Toast.makeText(PurchaseManagement.this, "请输入商品的进货量", Toast.LENGTH_SHORT).show();
				}
				else{
					AlertDialog.Builder addPBillTip = new AlertDialog.Builder(PurchaseManagement.this);
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
			}
		});
		
		Button addGoodsIntoPBillReset = new Button(this);//取消
		addGoodsIntoPBillReset.setText("取消");
		addGoodsIntoPBillReset.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				((TableLayout)v.getParent().getParent()).removeViews(1, ((TableLayout)v.getParent().getParent()).getChildCount()-1);
			}
			
		});
		
		TableRow theLastRow = new TableRow(this);
		theLastRow.addView(new TextView(this));
		theLastRow.addView(new TextView(this));
		
		theLastRow.addView(add);
		theLastRow.addView(addGoodsIntoPBillOk);
		theLastRow.addView(addGoodsIntoPBillReset);
		
		goodsIntoPBillTable.addView(theLastRow);
		
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
			
				Log.d("lyq", "The selected PBill id is " + selectedPBillId);

				List<Integer> pIdList = new ArrayList<Integer>();
				List<Integer> cList = new ArrayList<Integer>();
				for(int i=0;i<priceIdList.size();i++){
					if(!pIdList.contains(priceIdList.get(i))){
						pIdList.add(priceIdList.get(i));
						cList.add(countList.get(i));
					}
					else{
						int theP = pIdList.indexOf(priceIdList.get(i));
						int original = cList.get(theP);
					
						cList.set(theP, original+countList.get(i));
					}
				}
				boolean wholeFlag = true;
				for(int i=0;i<pIdList.size();i++){
					boolean flag = pItemCPer.addPItem(selectedPBillId, pIdList.get(i), cList.get(i));
					if(!flag){
						Toast.makeText(PurchaseManagement.this, "添加进货项失败", Toast.LENGTH_SHORT).show();
						wholeFlag = false;
					}
				}
				if(wholeFlag){
					String selectedPBillNum = pBillCPer.getPBillNumByPBillId(selectedPBillId);
					Toast.makeText(PurchaseManagement.this, "恭喜，成功往进货单:"+selectedPBillNum+" 添加进货项", Toast.LENGTH_SHORT).show();
				}
				pIdList.clear();
				cList.clear();
				goodsIntoPBillTable.removeViews(1, goodsIntoPBillTable.getChildCount()-1);
				priceIdList.clear();
				countList.clear();
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