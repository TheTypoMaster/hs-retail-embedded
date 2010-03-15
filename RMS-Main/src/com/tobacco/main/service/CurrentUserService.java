package com.tobacco.main.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.tobacco.main.entities.User;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class CurrentUserService extends Service {

	public static final String TAG = "CurrentUserService";

	public static final String ACTION_START = "com.tobacco.main.service.CurrentUserService.START";
	public static final String ACTION_BIND = "com.tobacco.main.service.CurrentUserService.BIND";
	public static final String ACTION_UNBIND = "com.tobacco.main.service.CurrentUserService.UNBIND";
	public static final String ACTION_STOP = "com.tobacco.main.service.CurrentUserService.STOP";

	public static final String ACTION_QUERY_USER = "com.tobacco.main.service.CurrentUserService.QUERY_USER";

	public static final String ACTION_USER_LOGON = "com.tobacco.main.service.CurrentUserService.USER_LOGON";
	public static final String ACTION_USER_LOGOFF = "com.tobacco.main.service.CurrentUserService.USER_LOGOFF";

	public static final String ACTION_USER_ACTIVE = "com.tobacco.main.service.CurrentUserService.USER_ACTIVE";
	public static final String ACTION_USER_DEACTIVE = "com.tobacco.main.service.CurrentUserService.USER_DEACTIVE";

	private HashMap<String, CurrentUser> suspendUserPool = new HashMap<String, CurrentUser>();
	private CurrentUser currentUser = null;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate()");

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
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
