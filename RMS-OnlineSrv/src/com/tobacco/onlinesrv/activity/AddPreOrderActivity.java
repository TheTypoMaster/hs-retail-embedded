package com.tobacco.onlinesrv.activity;

import com.tobacco.onlinesrv.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class AddPreOrderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preorder);

		Intent intent = getIntent();


		

		EditText dateEdt = (EditText) this.findViewById(R.id.EditText04);
		dateEdt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent tempIntent = new Intent();
				tempIntent.setClass(AddPreOrderActivity.this,
						DatePickActivity.class);
				startActivity(tempIntent);
			}
		});
		if(intent.getClass().equals(DatePickActivity.class))
		{
			dateEdt.setText("asd");
		}
	}

}
