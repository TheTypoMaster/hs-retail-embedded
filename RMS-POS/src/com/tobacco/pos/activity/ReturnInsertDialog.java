package com.tobacco.pos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.tobacco.R;
import com.tobacco.pos.entity.AllTables.Consume;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.Return;
import com.tobacco.pos.entity.AllTables.VIPInfo;

public class ReturnInsertDialog extends Activity{

	EditText vipNum ;
	CheckBox check ;
	EditText goodsBarcode ;
	EditText goodsNumber;
	EditText content ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle("增加退货");
		this.setContentView(R.layout.return_insert_dialog);
		vipNum = (EditText)findViewById(R.id.returnInsertEditTextVIP);
		goodsBarcode = (EditText)findViewById(R.id.returnInsertEditTextBarcode);
		goodsNumber = (EditText)findViewById(R.id.returnInsertEditTextNumber);
		content = (EditText)findViewById(R.id.returnInsertEditTextContent);
		check = (CheckBox)findViewById(R.id.returnInsertCheckBox);
		check.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				vipNum.setEnabled(isChecked);
			}			
		});
		Button okButton = (Button)findViewById(R.id.returnInsertButtonConfirm);
		Button cancelButton = (Button)findViewById(R.id.returnInsertButtonCancel);
		okButton.setOnClickListener(onClickListener);
		cancelButton.setOnClickListener(onClickListener);
	}
	
	protected OnClickListener onClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.returnInsertButtonCancel:
				setResult(RESULT_CANCELED,new Intent());
				finish();
				break;
			case R.id.returnInsertButtonConfirm:
				if(check()){
					Intent intent = new Intent();
					if(check.isChecked()){
						intent.putExtra(Return.VIP_ID, vipNum.getText().toString());
					}else{
						intent.putExtra(Return.VIP_ID, "common");
					}
					intent.putExtra(GoodsPrice.barcode, goodsBarcode.getText().toString());
					intent.putExtra(Return.NUMBER, goodsNumber.getText().toString());
					intent.putExtra(Return.CONTENT, content.getText().toString());
					setResult(RESULT_OK,intent);
					finish();
				}
				break;			
			}
		}
		
	};
	
	protected boolean check(){
		if(goodsNumber.getText().toString().equals("")){
			goodsNumber.setText("1");
		}
		if(check.isChecked()&&vipNum.getText().toString().equals("")){
			Toast.makeText(this, "VIP号为空", Toast.LENGTH_SHORT).show();
			return false;
		}else if(goodsBarcode.getText().toString().equals("")){
			Toast.makeText(this, "条形码为空", Toast.LENGTH_SHORT).show();
			return false;
		}else if(content.getText().toString().equals("")){
			Toast.makeText(this, "退货原因为空", Toast.LENGTH_SHORT).show();
			return false;
		} 
		return true;
	}
}
