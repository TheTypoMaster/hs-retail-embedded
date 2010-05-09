package com.tobacco.pos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class GetDiscountActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String VIPNum = this.getIntent().getStringExtra("VIPNum");
		
		Intent discountResultIntent = new Intent(this, PaymentManagement.class);
		discountResultIntent.putExtra("VIPId", 2);
		discountResultIntent.putExtra("VIPDiscount", 0.6+"");
		discountResultIntent.putExtra("VIPName", "腾讯TM");
	 
		this.setResult(RESULT_OK, discountResultIntent);
		finish();
	}
}
