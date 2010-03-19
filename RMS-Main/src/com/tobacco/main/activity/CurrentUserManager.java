package com.tobacco.main.activity;

import com.tobacco.main.entities.User;
import com.tobacco.main.util.MD5Hasher;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.util.Log;

public class CurrentUserManager extends Activity {

	public static final String TAG = "CurUsrManager";

	public static final String ACTION_START = "com.tobacco.main.activity.CurrentUserManager.START";
	public static final String ACTION_STOP = "com.tobacco.main.activity.CurrentUserManager.STOP";

	public static final String ACTION_QUERY_USER = "com.tobacco.main.activity.CurrentUserManager.QUERY_USER";

	public static final String ACTION_USER_LOGON = "com.tobacco.main.activity.CurrentUserManager.USER_LOGON";
	public static final String ACTION_USER_LOGOFF = "com.tobacco.main.activity.CurrentUserManager.USER_LOGOFF";

	public static final String ACTION_USER_ACTIVE = "com.tobacco.main.activity.CurrentUserManager.USER_ACTIVE";
	public static final String ACTION_USER_DEACTIVE = "com.tobacco.main.activity.CurrentUserManager.USER_DEACTIVE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "onStart()");
		Intent intent = getIntent();
		// super.onStart(intent, startId);

		String action = intent.getAction();
		// if user is newly logged on
		if (action.equals(ACTION_USER_LOGON)) {
			userLogon(intent);
		}

		else if (action.equals(ACTION_QUERY_USER)) {
			userQuery(intent);
		}

	}

	private void userLogon(Intent intent) {

		Bundle userBundle = intent.getExtras();

		String userId = userBundle.getString("curUserId");
		String userName = userBundle.getString("curUserName");

		ContentValues cValues = new ContentValues();

		cValues.put("status", "online");

		ContentResolver cr = getContentResolver();
		Uri uri = Uri.withAppendedPath(User.CONTENT_URI, userId);
		cr.update(uri, cValues, "id = '" + userId + "'", null);

		Log.i(TAG, "User session stored: " + userName);

	}

	/**
	 * @param intent
	 */
	private void userQuery(Intent intent) {

		// get user that wanna be queried
		String userName = intent.getStringExtra("userName");
		Log.i(TAG, "Query user:" + userName);
		Log.i(TAG, "Caller: "+ getCallingActivity().getClassName());

		String[] projection = new String[] { User._ID, User.USERNAME,
				User.PASSWORD, User.PRIV, User.STATUS };
		String queriedUserId = null;
		String queriedUserName = null;
		String queriedUserPriv = null;
		String queriedUserStatus = null;

		// retrieve from db
		Uri user = User.CONTENT_URI;
		Cursor cursor = managedQuery(user, projection, User.USERNAME + "= '"
				+ userName + "'", null, null);

		// set up result intent
		Intent resultIntent = new Intent(this,getCallingActivity().getClass());

		// query user via username

		if (cursor.moveToFirst()) {

			int idColumn = cursor.getColumnIndex(User._ID);
			int nameColumn = cursor.getColumnIndex(User.USERNAME);
			int privColumn = cursor.getColumnIndex(User.PRIV);
			int statusColumn = cursor.getColumnIndex(User.STATUS);

			queriedUserId = cursor.getString(idColumn);
			queriedUserName = cursor.getString(nameColumn);
			queriedUserPriv = cursor.getString(privColumn);
			queriedUserStatus = cursor.getString(statusColumn);

		}

		resultIntent.putExtra("curUserId", queriedUserId);
		resultIntent.putExtra("curUserName", queriedUserName);
		resultIntent.putExtra("curUserPriv", queriedUserPriv);
		resultIntent.putExtra("curUserStatus", queriedUserStatus);

		setResult(RESULT_OK, resultIntent);
		finish();

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
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
