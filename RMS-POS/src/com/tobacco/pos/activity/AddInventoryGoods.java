package com.tobacco.pos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tobacco.R;
import com.tobacco.pos.entity.AllTables.GoodsPrice;

public class AddInventoryGoods extends Activity{

	private EditText barcode ;
	private Button confirm ;
	private Button cancel ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.add_inventory_goods);
		barcode = (EditText)findViewById(R.id.barcodeAddInventoryGoods);
		confirm = (Button)findViewById(R.id.confirmAddInventoryGoods);
		cancel = (Button)findViewById(R.id.cancelAddInventoryGoods);
		confirm.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
	}
	
	private OnClickListener onClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.confirmAddInventoryGoods:
				if(check()){
					Intent intent = new Intent();
					intent.putExtra(GoodsPrice.barcode, barcode.getText().toString());
					setResult(RESULT_OK,intent);
					finish();
				}
				break;
			case R.id.cancelAddInventoryGoods:
				finish();
				break;
			default:	
			}
		}
		
	};
	
	protected boolean check(){
		if(barcode.getText().toString().equals("")){
			Toast.makeText(this, "条形码为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}
