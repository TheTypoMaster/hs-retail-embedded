package com.tobacco.pos.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.tobacco.R;
import com.tobacco.pos.entity.ComplaintModel;
import com.tobacco.pos.handler.ComplaintHandler;
import com.tobacco.pos.util.InputCheck;
import com.tobacco.pos.util.RegexCheck;

public class ComplaintInsertDialog extends Activity{

	EditText vipNum ;
	CheckBox check ;
	EditText goodsBarcode ;
	EditText content ;
	private ComplaintHandler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle("增加投诉");
		this.setContentView(R.layout.complaint_insert);
		vipNum = (EditText)findViewById(R.id.complaintInsertEditTextVIP);
		goodsBarcode = (EditText)findViewById(R.id.complaintInsertEditTextBarcode);
		content = (EditText)findViewById(R.id.complaintInsertEditTextContent);
		check = (CheckBox)findViewById(R.id.complaintInsertCheckBox);
		check.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				vipNum.setEnabled(isChecked);
			}			
		});
		Button okButton = (Button)findViewById(R.id.complaintInsertButtonConfirm);
		Button cancelButton = (Button)findViewById(R.id.complaintInsertButtonCancel);
		okButton.setOnClickListener(onClickListener);
		cancelButton.setOnClickListener(onClickListener);
		
		handler = new ComplaintHandler(this);
	}
	
	protected OnClickListener onClickListener = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.complaintInsertButtonConfirm:
				if(insertComplaint()){
					Toast.makeText(ComplaintInsertDialog.this, "投诉保存成功", Toast.LENGTH_SHORT).show();
					finish();
				}				
				break;
			case R.id.complaintInsertButtonCancel:
				finish();
				break;
			}
		}
		
	};
	
	protected boolean insertComplaint(){
		
		if(check()){
			
			ComplaintModel goods = handler.fillVacancy(goodsBarcode.getText().toString(), 
					vipNum.getText().toString(), content.getText().toString());
			
			handler.insert(goods);
			
//			ContentValues values = new ContentValues();
//			GoodsPriceCPer goodsPriceCPer = new GoodsPriceCPer();
//			VIPInfoCPer vipInfoCPer = new VIPInfoCPer();
//			String vipId = vipInfoCPer.getAttributeByAttribute(VIPInfo._ID, VIPInfo.VIPNum, vipNum.getText().toString());
////			int goodsPriceId = goodsPriceCPer.getGoodsPriceIdByBarcode(goodsBarcode.getText().toString());
//			String goodsPriceId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice._ID, GoodsPrice.barcode, goodsBarcode.getText().toString());
//			
//			if(check.isChecked()){
//				values.put(Complaint.VIP_ID, vipId);
//			}
//			
//			values.put(Complaint.GOODS_ID, goodsPriceId);
//			values.put(Complaint.CONTENT, content.getText().toString());
//			this.getContentResolver().insert(Complaint.CONTENT_URI, values);
			return true;
		}
		return false;
	}
	
	protected boolean check(){
		
		String vipValue = InputCheck.checkVIP(this,vipNum.getText().toString());
		if(check.isChecked()&&vipValue!=null){
			Toast.makeText(this, vipValue, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		String barcodeValue = InputCheck.checkBarcode(this,goodsBarcode.getText().toString());
		if(barcodeValue!=null){
			Toast.makeText(this, barcodeValue, Toast.LENGTH_SHORT).show();
			return false;
		}

		if(content.getText().toString().equals("")){
			Toast.makeText(this, "投诉理由不得为空", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}
}
