package com.tobacco.pos.activity;

import android.app.Activity;
import android.content.ContentValues;
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
import com.tobacco.pos.contentProvider.GoodsPriceCPer;
import com.tobacco.pos.contentProvider.VIPInfoCPer;
import com.tobacco.pos.entity.AllTables.Complaint;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.VIPInfo;

public class ComplaintInsertDialog extends Activity{

	EditText vipNum ;
	CheckBox check ;
	EditText goodsBarcode ;
	EditText content ;
	
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

			@Override
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
	}
	
	protected OnClickListener onClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.complaintInsertButtonConfirm:
				if(insertComplaint()){
					Toast.makeText(ComplaintInsertDialog.this, "投诉已成功保存", Toast.LENGTH_SHORT).show();
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
			ContentValues values = new ContentValues();
			GoodsPriceCPer goodsPriceCPer = new GoodsPriceCPer();
			VIPInfoCPer vipInfoCPer = new VIPInfoCPer();
			String vipId = vipInfoCPer.getAttributeByAttribute(VIPInfo._ID, VIPInfo.VIPNum, vipNum.getText().toString());
//			int goodsPriceId = goodsPriceCPer.getGoodsPriceIdByBarcode(goodsBarcode.getText().toString());
			String goodsPriceId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice._ID, GoodsPrice.barcode, goodsBarcode.getText().toString());
			
			if(check.isChecked()){
				values.put(Complaint.VIP_ID, vipId);
			}
			
			values.put(Complaint.GOODS_ID, goodsPriceId);
			values.put(Complaint.CONTENT, content.getText().toString());
			this.getContentResolver().insert(Complaint.CONTENT_URI, values);
			return true;
		}
		return false;
	}
	
	protected boolean check(){
		if(check.isChecked()&&vipNum.getText().toString().equals("")){
			Toast.makeText(this, "VIP号为空", Toast.LENGTH_SHORT).show();
			return false;
		}else if(goodsBarcode.getText().toString().equals("")){
			Toast.makeText(this, "条形码为空", Toast.LENGTH_SHORT).show();
			return false;
		}else if(content.getText().toString().equals("")){
			Toast.makeText(this, "投诉内容为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}
