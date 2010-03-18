package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tobacco.R;
import com.tobacco.pos.contentProvider.UnitCPer;

public class AddGoodsPrice extends Activity {
	private Spinner unitNameSpinner = null;
	private EditText barcodeEText = null;
	private EditText inPriceEditText = null;
	private EditText outPriceEditText = null;
	private Button addGoodsPriceOk = null;
	private Button addGoodsPriceReset = null;
	
	private UnitCPer unitCPer = null;

	protected void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addgoodsprice);
		
		unitCPer = new UnitCPer();
		
		this.setTitle("增加商品价格");
		
		unitNameSpinner = (Spinner)this.findViewById(R.id.unitNameSpinner);
		ArrayList<String> existingUnit = getIntent().getStringArrayListExtra("existingUnit");
		List<String> allUnitName = unitCPer.getAllUnitName();
		for(int i=0;i<allUnitName.size();i++){
	
				if(existingUnit.contains(allUnitName.get(i))){
					allUnitName.remove(i);
				}
				
			}
		
		ArrayAdapter<String> allUnitNameAdapter = new ArrayAdapter<String>(AddGoodsPrice.this,android.R.layout.simple_spinner_item,allUnitName);
		unitNameSpinner.setAdapter(allUnitNameAdapter);
		
		barcodeEText = (EditText)this.findViewById(R.id.barcodeEText);
		inPriceEditText = (EditText)this.findViewById(R.id.inPriceEditText);
		outPriceEditText = (EditText)this.findViewById(R.id.outPriceEditText);
		
		addGoodsPriceOk = (Button)this.findViewById(R.id.addGoodsPriceOk);
		
		addGoodsPriceOk.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				double inPrice = 0;
				double outPrice = 0;
				boolean flag;
				if(inPriceEditText.getText().toString() == null || outPriceEditText.getText().toString() == null){
					flag = false;
					Toast.makeText(AddGoodsPrice.this, "请填写完价格", Toast.LENGTH_SHORT).show();
				}
				else{
					try{
						inPrice = Double.parseDouble(inPriceEditText.getText().toString());
						outPrice = Double.parseDouble(outPriceEditText.getText().toString());
					
						flag = true;
					}
					catch(Exception e){
						flag = false;
						Toast.makeText(AddGoodsPrice.this, "价格格式不准确，请检查", Toast.LENGTH_SHORT).show();
					}
				}
				if(flag){
					Intent intent = new Intent(AddGoodsPrice.this, AddGoods.class);
					intent.putExtra("unitName", unitNameSpinner.getSelectedItem().toString());
					intent.putExtra("barcode", barcodeEText.getText().toString());
					intent.putExtra("inPrice", inPrice);
					intent.putExtra("outPrice", outPrice);
				
					AddGoodsPrice.this.setResult(RESULT_OK, intent);

					finish();
				}
				
			}
			
		});
		addGoodsPriceReset = (Button)this.findViewById(R.id.addGoodsPriceReset);
		addGoodsPriceReset.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				finish();
			}
			
		});
		
		
	}
}
