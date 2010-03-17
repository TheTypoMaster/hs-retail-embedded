package com.tobacco.pos.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
		
		ArrayAdapter<String> allUnitNameAdapter = new ArrayAdapter<String>(AddGoodsPrice.this,android.R.layout.simple_spinner_item,unitCPer.getAllUnitName());
		unitNameSpinner.setAdapter(allUnitNameAdapter);
		
		barcodeEText = (EditText)this.findViewById(R.id.barcodeEText);
		inPriceEditText = (EditText)this.findViewById(R.id.inPriceEditText);
		outPriceEditText = (EditText)this.findViewById(R.id.outPriceEditText);
		
		addGoodsPriceOk = (Button)this.findViewById(R.id.addGoodsOk);
		addGoodsPriceOk.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				
			}
			
		});
		addGoodsPriceReset = (Button)this.findViewById(R.id.addGoodsReset);
		addGoodsPriceReset.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				finish();
			}
			
		});
		
		
	}
}
