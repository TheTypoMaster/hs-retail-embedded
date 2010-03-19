package com.tobacco.custrel.activity;

import com.tobacco.main.activity.CurrentUserManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class TestCallerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Intent i = new Intent(this, TestActivity.class);
		// i.putExtra("curUser", "Zwd");

		Intent i = new Intent();
		i.setAction(CurrentUserManager.ACTION_QUERY_USER);
		i.putExtra("userName", "zwd");

		startActivityForResult(i, 0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		Log.i("Queried User Status: ", data.getStringExtra("curUserName").toString() + data.getStringExtra("curUserStatus"));

	}

}
