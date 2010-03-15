package com.tobacco.main.activity;

import com.tobacco.main.R;
import com.tobacco.main.entities.User;
import com.tobacco.main.provider.AccountProvider;
import com.tobacco.main.service.CurrentUserService;
import com.tobacco.main.util.MD5Hasher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {

	private static final String TAG = "Login";

	private String userName;
	private String userPwd;

	/**
	 * starting method call
	 * 
	 * */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// start CurUserService

		// Intent intent = new Intent();
		// intent.setAction(CurrentUserService.ACTION_START);
		// startService(intent);

		setContentView(R.layout.login_dialog);

		ContentProvider provider = new AccountProvider();

		Button okBtn = (Button) findViewById(R.id.login_Ok);
		okBtn.setOnClickListener(loginClickListener);

	}

	private OnClickListener loginClickListener = new OnClickListener() {

		public void onClick(View v) {

			EditText uName = (EditText) findViewById(R.id.login_username);
			EditText uPwd = (EditText) findViewById(R.id.login_password);

			userName = uName.getText().toString();
			userPwd = uPwd.getText().toString();

			// start next activity if login succesfully
			if (verifyUser(userName, userPwd) == true) {
				Log.i(TAG, "Login success!");

				// Intent intent = new Intent();
				// intent.setAction("com.tobacco.custrel.CUSTREL_START");
				// startActivity(intent);
			}

		}

	};

	/**
	 * verify user & register current user if success
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	private boolean verifyUser(String userName, String password) {

		boolean ifSuccess = false;
		String[] projection = new String[] { User._ID, User.USERNAME,
				User.PASSWORD, User.PRIV, User.STATUS };
		String curUserId = null;
		String curUserName = null;
		String curUserPwd = null;
		String curUserPriv = null;
		String curUserStatus = null;

		// retrieve from db
		Uri user = User.CONTENT_URI;
		Cursor cursor = managedQuery(user, projection, User.USERNAME + "= ?",
				new String[] { userName }, null);

		// verify if user exists
		if (cursor.getCount() == 0)
			openfailDialog();

		// verify pwd
		if (cursor.moveToFirst()) {
			
			int idColumn = cursor.getColumnIndex(User._ID);
			int nameColumn = cursor.getColumnIndex(User.USERNAME);
			int pwdColumn = cursor.getColumnIndex(User.PASSWORD);
			int privColumn = cursor.getColumnIndex(User.PRIV);
			int statusColumn = cursor.getColumnIndex(User.STATUS);

			curUserId = cursor.getString(idColumn);
			curUserName = cursor.getString(nameColumn);
			curUserPwd = cursor.getString(pwdColumn);
			curUserPriv = cursor.getString(privColumn);
			curUserStatus = cursor.getString(statusColumn);

			if (MD5Hasher.hash(password).equals(curUserPwd)) {
				ifSuccess = true;
				
				Intent i = new Intent();
				i.setAction(CurrentUserService.ACTION_START);
				i.putExtra("curUserId", curUserId);
				i.putExtra("curUserName", curUserName);
				i.putExtra("curUserPriv", curUserPriv);
				i.putExtra("curUserStatus", curUserStatus);

				// bindService(i, curUserSvcConn,
				// CurrentUserService.BIND_AUTO_CREATE);
				startService(i);

			} else
				openfailDialog();

		}

		return ifSuccess;

	}

	/**
	 * method template for creating a dialogue
	 */
	private void openfailDialog() {
		// Toast.makeText(Login.this, "����һ��Toast����",
		// Toast.LENGTH_SHORT).show();
		new AlertDialog.Builder(Login.this).setTitle("登录失败").setMessage(
				"您的用户名或密码有误！").setPositiveButton("重新登录",
				new DialogInterface.OnClickListener() {
					public void onClick(

					DialogInterface dialoginterface, int i) {
					}
				}).show();
	}

}
