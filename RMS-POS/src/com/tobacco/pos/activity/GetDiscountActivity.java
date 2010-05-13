package com.tobacco.pos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GetDiscountActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String VIPNum = this.getIntent().getStringExtra("VIPNum");
		
		Intent discountResultIntent = new Intent(this, PaymentManagement.class);
		discountResultIntent.putExtra("VIPId", 2);
//		try
//		{
//			discountResultIntent.putExtra("VIPId", Integer.parseInt(InputCheck.checkVIP(this, VIPNum)));
//		}
//		catch(Exception e){
//			discountResultIntent.putExtra("VIPId", -1);
//		}
		discountResultIntent.putExtra("VIPDiscount", 0.6+"");
		discountResultIntent.putExtra("VIPName", "腾讯TM");
	 
		this.setResult(RESULT_OK, discountResultIntent);
		finish();
	}
}
