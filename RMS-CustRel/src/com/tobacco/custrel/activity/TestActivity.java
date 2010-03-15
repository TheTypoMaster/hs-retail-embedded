package com.tobacco.custrel.activity;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestActivity extends Activity {

	private CurrentUser curUser = new CurrentUser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Log.i("TestActivity", "Started");

		Intent i = new Intent(this, TestCallerActivity.class);
		i.putExtra("regUser", "Reguser sent back within intent");

		setResult(RESULT_OK, i);
		
		finish();
		

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	public CurrentUser getCurUser() {
		return curUser;
	}

	public void setCurUser(CurrentUser curUser) {
		this.curUser = curUser;
	}

	private class CurrentUser {

		public CurrentUser() {

		}

		public CurrentUser(String userName, String userPriv, String userStatus) {
			super();
			this.userName = userName;
			this.userPriv = userPriv;
			this.userStatus = userStatus;

		}

		private String userName;
		private String userPriv;
		private String userStatus;

	}

}
