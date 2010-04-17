package com.tobacco.pos.contentProvider;

import android.database.Cursor;
import android.net.Uri;

public class UserInfoCPer extends BaseContentProvider {

	private static final String TABLE_NAME = DatabaseHelper.USERINFO;
	public static final Uri CONTENT_URI = Uri
			.parse("content://com.tobacco.pos.contentProvider.UnitCPer");

	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		this.tableName = TABLE_NAME;
		return super
				.query(uri, projection, selection, selectionArgs, sortOrder);
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
