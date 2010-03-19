package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.tobacco.R;
import com.tobacco.pos.contentProvider.GoodsKindCPer;
import com.tobacco.pos.contentProvider.ManufacturerCPer;
import com.tobacco.pos.entity.SinglePrice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddGoods extends Activity {
	private EditText goodsNameEText;
	private Spinner allManufacturerSpinner;
	private Spinner allKindSpinner;
	private Button addPriceButton;
	private Button addGoodsOk;
	private Button addGoodsReset;
	
	private ManufacturerCPer mCPer;
	private GoodsKindCPer goodsKindCPer;
	
	private ArrayList allPrice = new ArrayList();//一件商品的所有价格，allPrice由singlePrice组成，singlePrice由单位，条形码，进货价，售价组成。
	
	public void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.addgoods);
	
		this.setTitle("增加商品");
		
		goodsKindCPer = new GoodsKindCPer();
		mCPer = new ManufacturerCPer();
		
		goodsNameEText = (EditText)this.findViewById(R.id.goodsNameEText);
		
		allManufacturerSpinner = (Spinner)this.findViewById(R.id.allManufacturerSpinner);
		ArrayAdapter<String> allManufacturerAdapter = new ArrayAdapter<String>(AddGoods.this,android.R.layout.simple_spinner_item,mCPer.getAllManufacturerName());
		allManufacturerSpinner.setAdapter(allManufacturerAdapter);
		
		allKindSpinner = (Spinner)this.findViewById(R.id.allKindSpinner);
		ArrayAdapter<String> allGoodsKindAdapter = new ArrayAdapter<String>(AddGoods.this,android.R.layout.simple_spinner_item,goodsKindCPer.getAllGoodsKindName());
		allKindSpinner.setAdapter(allGoodsKindAdapter);
		
		addPriceButton = (Button)this.findViewById(R.id.addPriceButton);
		addPriceButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(goodsNameEText.getText().toString().trim().equals("") || goodsNameEText.getText().toString().trim() == null){
					Toast.makeText(AddGoods.this, "请输入商品的名字", Toast.LENGTH_SHORT).show();
				}
				else{
					Intent intent = new Intent(AddGoods.this, AddGoodsPrice.class);//开始AddGoodsPrice，接收价格信息。
					ArrayList<String> existingUnit = new ArrayList<String>() ;//取出allPrice中的所有单位，在AddGoodsPrice中就不显示出那些已经有价格的单位
					for(int i=0;i<allPrice.size();i++){
						
						SinglePrice temp = (SinglePrice)allPrice.get(i);
						existingUnit.add(temp.get("unitName").toString());
					}
					intent.putStringArrayListExtra("existingUnit", existingUnit);
			
					AddGoods.this.startActivityForResult(intent, 0);
				}
			
			}
			
		});
		
		addGoodsOk = (Button)this.findViewById(R.id.addGoodsOk);
		addGoodsOk.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(goodsNameEText.getText().toString().trim().equals("") || goodsNameEText.getText().toString().trim() == null){
					Toast.makeText(AddGoods.this, "请输入商品的名字", Toast.LENGTH_SHORT).show();
				}
				else if(allPrice.size() == 0){
					Toast.makeText(AddGoods.this, "请为该商品输入价格", Toast.LENGTH_SHORT).show();
				}
				else{
					Intent intent = new Intent(AddGoods.this, PurchaseManagement.class);
					intent.putParcelableArrayListExtra("allPrice", allPrice);//商品的所有价格
					intent.putExtra("goodsName", goodsNameEText.getText().toString());//商品名称
					intent.putExtra("mName", allManufacturerSpinner.getSelectedItem().toString());//厂家名称
					intent.putExtra("goodsKind", allKindSpinner.getSelectedItem().toString());
					
					AddGoods.this.setResult(RESULT_OK, intent);
					
					finish();
					
				}
			}
			
		});
		addGoodsReset = (Button)this.findViewById(R.id.addGoodsReset);
		addGoodsReset.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				finish();
			}
			
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){//如果成功接收到数据，数据有单位，条形码，进价，售价
			String unitName = data.getStringExtra("unitName");
			String barcode = data.getStringExtra("barcode");
			double inPrice = data.getDoubleExtra("inPrice", 0);
			double outPrice = data.getDoubleExtra("outPrice", 0);
			
			SinglePrice sPrice = new SinglePrice();
			sPrice.put("unitName", unitName);
			sPrice.put("barcode", barcode);
			sPrice.put("inPrice", ""+inPrice);
			sPrice.put("outPrice", ""+outPrice);

			allPrice.add(sPrice);
		}
	}
	

}
