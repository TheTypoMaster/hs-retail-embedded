package com.tobacco.pos.activity;

import com.tobacco.R;
import com.tobacco.pos.contentProvider.GoodsKindCPer;
import com.tobacco.pos.contentProvider.ManufacturerCPer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
					Intent intent = new Intent(AddGoods.this, AddGoodsPrice.class);
					AddGoods.this.startActivity(intent);
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

}
