package com.tobacco.main.service;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class CurrentUserService extends Service {

	public static final String TAG = "CurrentUserService";

	public static final String ACTION_START = "com.tobacco.main.service.CurrentUserService.START";
	public static final String ACTION_BIND = "com.tobacco.main.service.CurrentUserService.BIND";
	public static final String ACTION_QUERY_USER = "com.tobacco.main.service.CurrentUserService.QUERY_USER";

	private ArrayList<CurrentUser> userList = new ArrayList<CurrentUser>();

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Service created...");

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.i(TAG, "Service started...");
		Bundle userBundle = intent.getExtras();

		CurrentUser cu = new CurrentUser(userBundle.getString("curUserName"),
				userBundle.getString("curUserPriv"), userBundle
						.getString("curUserStatus"));

		userList.add(cu);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "Service bound");
		// TODO Auto-generated method stub
		return null;
	}

	private class CurrentUser {

		public CurrentUser() {

		}

		public CurrentUser(String userName, String userPriv, String userStatus) {
			super();
			this.userName = userName;
			this.userPriv = userPriv;
			this.userStatus = userStatus;

			Log.i(TAG, "Registered user: " + userName + ", Priv: " + userPriv);

		}

		private String userName;
		private String userPriv;
		private String userStatus;

	}

}
