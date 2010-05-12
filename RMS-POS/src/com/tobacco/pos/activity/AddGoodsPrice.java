package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tobacco.R;
import com.tobacco.pos.contentProvider.UnitCPer;
import com.tobacco.pos.service.ScanInputService;

public class AddGoodsPrice extends Activity {
	private Spinner unitNameSpinner = null;
	private EditText barcodeEText = null;
	private EditText inPriceEditText = null;
	private EditText outPriceEditText = null;
	private Button addGoodsPriceOk = null;
	private Button addGoodsPriceReset = null;
	
	private UnitCPer unitCPer = null;
	
	private boolean updateAccordingBarcode ;
	private ArrayList<String> existingBarcode;

	protected void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addgoodsprice);
		
		IntentFilter filter = new IntentFilter("com.tobacco.action.scan");
		this.registerReceiver(new ScanReceiver(), filter);
	
		this.startService(new Intent(this,ScanInputService.class));
		
		unitCPer = new UnitCPer();
		
		this.setTitle("增加商品价格");
		
		unitNameSpinner = (Spinner)this.findViewById(R.id.unitNameSpinner);
		ArrayList<String> existingUnit = getIntent().getStringArrayListExtra("existingUnit");
		existingBarcode = getIntent().getStringArrayListExtra("existingBarcode");
	
		List<String> allUnitName = unitCPer.getAllUnitName();
		List<String> remainingUnitName = new ArrayList<String>();
		for(int i=0;i<allUnitName.size();i++){//剔除掉已经有价格与之对应的单位
			
			if(!existingUnit.contains(allUnitName.get(i))){
				remainingUnitName.add(allUnitName.get(i));
			}
		}
		
		ArrayAdapter<String> allUnitNameAdapter = new ArrayAdapter<String>(AddGoodsPrice.this,android.R.layout.simple_spinner_item,remainingUnitName);
		unitNameSpinner.setAdapter(allUnitNameAdapter);
		
		barcodeEText = (EditText)this.findViewById(R.id.barcodeEText);
		barcodeEText.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(existingBarcode.contains(barcodeEText.getText().toString())){//如果条形码和之前的发生冲突，提示是否更新
						AlertDialog.Builder addPriceTip = new AlertDialog.Builder(AddGoodsPrice.this);
						
						addPriceTip.setTitle("提示");
						addPriceTip.setMessage("该条形码已有对于的商品，\n是否要更新商品的信息？");
						addPriceTip.setPositiveButton("是", new DialogInterface.OnClickListener(){

							public void onClick(DialogInterface dialog, int which) {
								updateAccordingBarcode = true;
							
							}
							
						});
						addPriceTip.setNegativeButton("否", new DialogInterface.OnClickListener(){
							
							public void onClick(DialogInterface dialog, int which) {
								barcodeEText.setText("");
							}
						});
						addPriceTip.show();
					}
				
				}
			}
			
		});
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

					if(barcodeEText.getText().toString().equals("") || barcodeEText.getText() == null)
						Toast.makeText(AddGoodsPrice.this, "请输入条形码", Toast.LENGTH_SHORT).show();
					else{
						Intent intent = new Intent(AddGoodsPrice.this, AddGoods.class);
						
						intent.putExtra("unitName", unitNameSpinner.getSelectedItem().toString());
						intent.putExtra("barcode", barcodeEText.getText().toString());
						intent.putExtra("inPrice", inPrice);
						intent.putExtra("outPrice", outPrice);
						
						if(updateAccordingBarcode)
							intent.putExtra("updateAccordingBarcode", true);
						else
							intent.putExtra("updateAccordingBarcode", false);
					
						AddGoodsPrice.this.setResult(RESULT_OK, intent);
						
						finish();
					}

				
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
	

	public class ScanReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
 
			String barcode = intent.getStringExtra("BARCODE");
		
			barcodeEText.setText("" + barcode);
			
			
		}
		
	}
}
