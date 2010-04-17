package com.tobacco.pos.contentProvider;

import android.database.Cursor;
import android.net.Uri;

public class UserInfoCPer extends BaseContentProvider {

	private static final String TABLE_NAME = DatabaseHelper.USERINFO;
	public static final Uri CONTENT_URI = Uri
			.parse("content://com.tobacco.pos.contentProvider.UnitCPer");


	public boolean onCreate() {
		 
		ctx = getContext();
		dbHelper = new DatabaseHelper(getContext());
		this.tableName = UserInfoCPer.TABLE_NAME;
		return true;
	}

	public int getUserIdByUserName(String userName) {
		Cursor c = this.query(CONTENT_URI, null, " userName = ? ",
				new String[] { userName }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		} else
			return -1;
	}

	public String getUserNameByUserId(String UserId) {
		Cursor c = this.query(CONTENT_URI, null, " _id = ? ",
				new String[] { UserId }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getString(1);
		} else
			return null;
	}
}
