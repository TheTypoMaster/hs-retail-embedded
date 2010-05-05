//package com.tobacco.pos.activity;
//
//import java.util.ArrayList;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.Window;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.tobacco.R;
//import com.tobacco.pos.entity.AllTables.Consume;
//import com.tobacco.pos.entity.AllTables.Goods;
//import com.tobacco.pos.entity.AllTables.GoodsPrice;
//import com.tobacco.pos.entity.AllTables.Unit;
//import com.tobacco.pos.util.QueryDataFromTables;
//
//public class ConsumeInsertDialog extends Activity{
//
//	private String[] projection ;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		projection = getIntent().getStringArrayExtra("projection");
//        requestWindowFeature(Window.FEATURE_LEFT_ICON);
//        setContentView(R.layout.consume_insert_dialog);
//        
//        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, 
//                android.R.drawable.ic_dialog_alert);
//        
//        Button cancelButton = (Button)findViewById(R.id.consume_insert_cancel);
//        Button confirmButton = (Button)findViewById(R.id.consume_insert_confirm);
//        
//        cancelButton.setOnClickListener(buttonListener);
//        confirmButton.setOnClickListener(buttonListener);
//        
//	}
//	
//	private OnClickListener buttonListener = new OnClickListener(){
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			switch(v.getId()){
//			
//			case R.id.consume_insert_cancel:
//				setResult(RESULT_CANCELED,new Intent());
//				finish();
//				break;
//			case R.id.consume_insert_confirm:
//				EditText barcodeText = (EditText)findViewById(R.id.barcodeTextConsume);
//				EditText numberText = (EditText)findViewById(R.id.numberTextConsume);
//				if(numberText.getText().toString().equals("")){
//					numberText.setText("1");
//				}				
//				if(!barcodeText.getText().toString().equals("")){
//					getData(barcodeText.getText().toString());
//				}else{
//					Toast.makeText(ConsumeInsertDialog.this, "条形码框为空", Toast.LENGTH_SHORT).show();
//				}
//				
//				
////				EditText numberText = (EditText)findViewById(R.id.numberTextConsume);
////				Intent intent = new Intent();
////				intent.putExtra("BARCODE", barcodeText.getText().toString());
////				intent.putExtra(Consume.NUMBER, numberText.getText().toString());
////				setResult(RESULT_OK,intent);
////				ContentValues values = new ContentValues();
////				values.put(Consume.GOODS, barcodeText.getText().toString());
////				values.put(Consume.NUMBER, numberText.getText().toString());
////			    getContentResolver().insert(getIntent().getData(), values);
////				finish();
//				break;
//			}
//		}
//		
//	};
//	
//	private static final int GET_DATA = 1;
//	
//	protected void getData(String Barcode){
//		String arg1 = QueryDataFromTables.TWO_TABLE+";"+"GoodsPriceCPer"+";"+GoodsPrice.barcode+";"+Barcode+";"+GoodsPrice.goodsId+";"+Goods._ID+";"+"GoodsCPer"+";"+Goods.goodsName;
//		String arg2 = QueryDataFromTables.TWO_TABLE+";"+"GoodsPriceCPer"+";"+GoodsPrice.barcode+";"+Barcode+";"+GoodsPrice.unitId+";"+Unit._ID+";"+"UnitCPer"+";"+Unit.name;
//		String arg3 = QueryDataFromTables.ONE_TABLE+";"+"GoodsPriceCPer"+";"+GoodsPrice.barcode+";"+Barcode+";"+GoodsPrice.inPrice;
//		String arg4 = QueryDataFromTables.ONE_TABLE+";"+"GoodsPriceCPer"+";"+GoodsPrice.barcode+";"+Barcode+";"+GoodsPrice._ID;
//		
////		String arg1 = "GoodsPrice"+";"+GoodsPrice.barcode+";"+Barcode+";"+GoodsPrice.goodsId+";"+Goods._ID+";"+"Goods"+";"+Goods.goodsName;
////		String arg2 = "GoodsPrice"+";"+GoodsPrice.barcode+";"+Barcode+";"+GoodsPrice.unitId+";"+Unit._ID+";"+"Unit"+";"+Unit.name;
////		String arg3 = "GoodsPrice"+";"+GoodsPrice.barcode+";"+Barcode+";"+"null"+";"+"null"+";"+"null"+";"+GoodsPrice.inPrice;
////		String arg4 = "GoodsPrice"+";"+GoodsPrice.barcode+";"+Barcode+";"+"null"+";"+"null"+";"+"null"+";"+GoodsPrice._ID;
//		//projection = new String[]{"Goods"+":"+Goods.goodsName,"Unit"+":"+Unit.name,Consume.NUMBER,"GoodsPrice"+":"+GoodsPrice.inPrice,"Total"};
//		
//		Intent intent = new Intent("com.tobacco.pos.activity.QueryDataFromTables");
//		String[] args = new String[]{arg1,arg2,arg3,arg4};
//		//String[] args = new String[]{arg1,arg2};
//		intent.putExtra("args", args);
//		//startActivity(intent);
//		startActivityForResult(intent, GET_DATA);
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		if(requestCode == GET_DATA)
//		{
//			if(resultCode == RESULT_CANCELED){
//				new AlertDialog.Builder(ConsumeInsertDialog.this).setMessage("Failed").show();
//			}
//			else{
//				EditText numberText = (EditText)findViewById(R.id.numberTextConsume);
//				ArrayList<String> list = data.getExtras().getStringArrayList("data");
//				String[] targetValue = new String[list.size()];
//				targetValue = list.toArray(targetValue);
//				
//				Intent intent = new Intent();
//				//intent.putExtra("projection", projection);
//				intent.putExtra(projection[0], targetValue[0]);
//				intent.putExtra(projection[1], targetValue[1]);
//				intent.putExtra(projection[2], numberText.getText().toString());
//				intent.putExtra(projection[3], targetValue[2]);
//				intent.putExtra(projection[4], String.valueOf(Integer.valueOf(numberText.getText().toString())*Integer.valueOf(targetValue[2])));
//				intent.putExtra("GoodsPrice:_ID", targetValue[3]);
//				
//				setResult(RESULT_OK,intent);
//				finish();
//			}
//		}
//	}
//
//	
//	
//}

package com.tobacco.pos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tobacco.R;
import com.tobacco.pos.entity.AllTables.Consume;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.util.InputCheck;
import com.tobacco.pos.util.RegexCheck;

public class ConsumeInsertDialog extends Activity{

	EditText barcodeText ;
	EditText numberText ;
	EditText commentText ;
	Button cancelButton ;
    Button confirmButton ;
    RadioGroup radioGroup;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.consume_insert_dialog);
        this.setTitle("输入溢耗商品：");
        
        barcodeText = (EditText)findViewById(R.id.barcodeTextConsume);
		numberText = (EditText)findViewById(R.id.numberTextConsume);
		commentText = (EditText)findViewById(R.id.commentTextConsume);
        cancelButton = (Button)findViewById(R.id.consume_insert_cancel);
        confirmButton = (Button)findViewById(R.id.consume_insert_confirm);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupConsume);
        
        cancelButton.setOnClickListener(buttonListener);
        confirmButton.setOnClickListener(buttonListener);       

		Intent intent = getIntent();
		String barcode = intent.getStringExtra(GoodsPrice.barcode);
		if(barcode!=null)
			barcodeText.setText(barcode);
        
	}
	
	private OnClickListener buttonListener = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			
			case R.id.consume_insert_cancel:
				setResult(RESULT_CANCELED,new Intent());
				finish();
				break;
			case R.id.consume_insert_confirm:
				if(check()){
					Intent intent = new Intent();
//					Log.i("radio", ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString());
					String type = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
					intent.putExtra(Consume.FLAG, type);
					intent.putExtra(GoodsPrice.barcode, barcodeText.getText().toString());
					intent.putExtra(Consume.NUMBER, numberText.getText().toString());
					intent.putExtra(Consume.COMMENT, commentText.getText().toString());
					setResult(RESULT_OK,intent);
					finish();
					break;
				}			
			}
		}
		
	};
	
	protected boolean check(){
		
		if(numberText.getText().toString().equals("")){
			numberText.setText("1");
		}else if(!RegexCheck.checkInteger(numberText.getText().toString())){
			Toast.makeText(this, "数量输入无效", Toast.LENGTH_SHORT).show();
			return false;
		}

		String barcodeValue = InputCheck.checkBarcode(this,barcodeText.getText().toString());
		if(barcodeValue!=null){
			Toast.makeText(this, barcodeValue, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(commentText.getText().toString().equals("")){
			Toast.makeText(this, "溢耗原因不得为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
}
