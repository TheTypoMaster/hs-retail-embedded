package com.tobacco.pos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SendCigaretteMoneyActivity extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent i = getIntent();
		
		Log.d("lyq", "..." + i.getIntExtra("vipId",1));
		Log.d("lyq", "..." + i.getDoubleExtra("cigaretteMoney", 0));
		
		Intent intent = new Intent(this, PaymentManagement.class);
		
		this.setResult(11, intent);
		finish();
	}
}
