package com.tobacco.pos.activity;

import com.tobacco.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AddGoods extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.addgoods);
	
		this.setTitle("增加商品");
		Log.d("lyq","jinru");
	}

}
