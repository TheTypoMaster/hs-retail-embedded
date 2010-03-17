package com.tobacco.main.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CurrentUserManager extends Activity {

	public static final String TAG = "CurUsrManager";

	public static final String ACTION_START = "com.tobacco.main.antivity.CurrentUserManager.START";
	public static final String ACTION_STOP = "com.tobacco.main.antivity.CurrentUserManager.STOP";

	public static final String ACTION_QUERY_USER = "com.tobacco.main.antivity.CurrentUserManager.QUERY_USER";

	public static final String ACTION_USER_LOGON = "com.tobacco.main.antivity.CurrentUserManager.USER_LOGON";
	public static final String ACTION_USER_LOGOFF = "com.tobacco.main.antivity.CurrentUserManager.USER_LOGOFF";

	public static final String ACTION_USER_ACTIVE = "com.tobacco.main.antivity.CurrentUserManager.USER_ACTIVE";
	public static final String ACTION_USER_DEACTIVE = "com.tobacco.main.antivity.CurrentUserManager.USER_DEACTIVE";

	private CurrentUser currentUser = new CurrentUser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		// super.onStart(intent, startId);
		Log.i(TAG, "onStart()");

		String action = intent.getAction();
		Bundle userBundle = intent.getExtras();
		// if user is newly logged on
		if (action.equals(ACTION_USER_LOGON)) {
			String userName = userBundle.getString("curUserName");

			currentUser = new CurrentUser(userBundle.getString("curUserId"),
					userBundle.getString("curUserName"), userBundle
							.getString("curUserPriv"), userBundle
							.getString("curUserStatus"));

			Log.i(TAG, "User session stored: " + userName);

		}

		else if (action.equals(ACTION_QUERY_USER)) {

		}

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private class CurrentUser {

		public CurrentUser() {

		}

		public CurrentUser(String userId, String userName, String userPriv,
				String userStatus) {
			super();
			this.userId = userId;
			this.userName = userName;
			this.userPriv = userPriv;
			this.userStatus = userStatus;

		}

		private String userId;
		private String userName;
		private String userPriv;
		private String userStatus;

	}

}
