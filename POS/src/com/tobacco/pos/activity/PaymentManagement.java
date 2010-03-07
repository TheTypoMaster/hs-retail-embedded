package com.tobacco.pos.activity;

import com.tobacco.pos.util.Loginer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class PaymentManagement extends Activity {
	private Loginer loginer = null;
	
	private TextView paymentWelcome;
	private ImageButton paymentreturn;//返回首页的按钮
	
	private String userName = "";//登陆用户的名字
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentmanagement);
    	
        userName = this.getIntent().getStringExtra("userName");
        
        paymentWelcome = (TextView)this.findViewById(R.id.paymentWelcome);
        paymentWelcome.setText("你好:" + userName);
        
        paymentreturn = (ImageButton)this.findViewById(R.id.paymentreturn);
        paymentreturn.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				loginer = new Loginer(PaymentManagement.this);
				if(loginer.logout(userName, loginer.getWritableDatabase()))// 登出
				{
					Intent intent = new Intent(PaymentManagement.this, Main.class);
					PaymentManagement.this.startActivity(intent);
				}
			}
        	
        });
    }

	@Override
	protected void onPause() {
		super.onPause();
		
		loginer = new Loginer(PaymentManagement.this);
		if(loginer.logout(userName, loginer.getWritableDatabase()))// 登出
		{
			Intent intent = new Intent(PaymentManagement.this, Main.class);
			PaymentManagement.this.startActivity(intent);
		}
	}
    
}
