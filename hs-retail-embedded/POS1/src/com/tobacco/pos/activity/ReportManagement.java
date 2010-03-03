package com.tobacco.pos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ReportManagement extends Activity {

	private Button reportreturn;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.reportmanagement);
	        
	        reportreturn = (Button)this.findViewById(R.id.reportreturn);
	        reportreturn.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					Intent intent = new Intent(ReportManagement.this, Main.class);
					ReportManagement.this.startActivity(intent);
				}
	        	
	        });
	    }
}
