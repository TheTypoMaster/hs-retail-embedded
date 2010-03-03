package com.tobacco.pos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PaymentManagement extends Activity {
   
	private Button paymentreturn;//返回首页的按钮
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentmanagement);
        
        paymentreturn = (Button)this.findViewById(R.id.paymentreturn);
        paymentreturn.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent(PaymentManagement.this, Main.class);
				PaymentManagement.this.startActivity(intent);
			}
        	
        });
    }
}
