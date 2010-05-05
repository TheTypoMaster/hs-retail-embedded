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
import com.tobacco.pos.entity.AllTables.Return;
import com.tobacco.pos.util.InputCheck;
import com.tobacco.pos.util.RegexCheck;

public class ReturnInsertDialog extends Activity{

	EditText goodsBarcode ;
	EditText goodsNumber;
	EditText content ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle("增加退货");
		this.setContentView(R.layout.return_insert_dialog);

		goodsBarcode = (EditText)findViewById(R.id.returnInsertEditTextBarcode);
		goodsNumber = (EditText)findViewById(R.id.returnInsertEditTextNumber);
		content = (EditText)findViewById(R.id.returnInsertEditTextContent);

		Button okButton = (Button)findViewById(R.id.returnInsertButtonConfirm);
		Button cancelButton = (Button)findViewById(R.id.returnInsertButtonCancel);
		okButton.setOnClickListener(onClickListener);
		cancelButton.setOnClickListener(onClickListener);
	}
	
	protected OnClickListener onClickListener = new OnClickListener(){

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
		}else if(!RegexCheck.checkInteger(goodsNumber.getText().toString())){
			Toast.makeText(this, "数量输入无效", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		String barcodeValue = InputCheck.checkBarcode(this,goodsBarcode.getText().toString());
		if(barcodeValue!=null){
			Toast.makeText(this, barcodeValue, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(content.getText().toString().equals("")){
			Toast.makeText(this, "退货原因不可为空", Toast.LENGTH_SHORT).show();
			return false;
		} 
		return true;
	}
}
