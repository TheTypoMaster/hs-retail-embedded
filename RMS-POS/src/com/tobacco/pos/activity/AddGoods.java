package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.tobacco.R;
import com.tobacco.pos.contentProvider.GoodsKindCPer;
import com.tobacco.pos.contentProvider.ManufacturerCPer;

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
	private TextView addGoodsInfo;
	private Button addGoodsOk;
	private Button addGoodsReset;
	
	private ManufacturerCPer mCPer;
	private GoodsKindCPer goodsKindCPer;
	
	private Vector allPrice = new Vector();//一件商品的所有价格，allPrice由singlePrice组成，singlePrice由单位，条形码，进货价，售价组成。
	
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
						Vector temp = (Vector) allPrice.get(i);
						existingUnit.add(temp.get(0).toString());
					}
					intent.putStringArrayListExtra("existingUnit", existingUnit);
			
					AddGoods.this.startActivityForResult(intent, 0);
				}
			
			}
			
		});
		addGoodsInfo = (TextView)this.findViewById(R.id.addGoodsInfo);
		addGoodsOk = (Button)this.findViewById(R.id.addGoodsOk);
		addGoodsOk.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(goodsNameEText.getText().toString().trim().equals("") || goodsNameEText.getText().toString().trim() == null){
					Toast.makeText(AddGoods.this, "请输入商品的名字", Toast.LENGTH_SHORT).show();
				}
				else{
					Intent intent = new Intent(AddGoods.this, PurchaseManagement.class);
					intent.putExtra("GoodsName", goodsNameEText.getText().toString());
					AddGoods.this.setResult(RESULT_OK, intent);
				}
			}
			
		});
		addGoodsReset = (Button)this.findViewById(R.id.addGoodsReset);
		addGoodsReset.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				addGoodsInfo.setText("");
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
			
			Vector singlePrice = new Vector();
			singlePrice.add(unitName);
			singlePrice.add(barcode);
			singlePrice.add(inPrice);
			singlePrice.add(outPrice);
			
			allPrice.add(singlePrice);
			
						
		}
	}
	

}
