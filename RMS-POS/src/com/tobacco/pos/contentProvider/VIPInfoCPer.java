package com.tobacco.pos.contentProvider;

import android.database.Cursor;
import android.net.Uri;

public class VIPInfoCPer extends BaseContentProvider {

	private static final String TABLE_NAME = DatabaseHelper.VIPINFO;
	public static final Uri CONTENT_URI = Uri
			.parse("content://com.tobacco.pos.contentProvider.UnitCPer");

	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		this.tableName = TABLE_NAME;
		return super
				.query(uri, projection, selection, selectionArgs, sortOrder);
	}

	public String getVIPNameByVIPId(int VIPId) {
		Cursor c = this.query(CONTENT_URI, null, " _id = " + VIPId, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getString(2);
		}
		return "";
	}

	public int getVIPIdByVIPNum(String VIPNum) {
		Cursor c = this.query(CONTENT_URI, null, " VIPNum = ? ",
				new String[] { VIPNum }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return -1;
	}

	public String getAttributeById(String attribute, String id) {
		Cursor c = this.query(CONTENT_URI, new String[] { attribute }, "_id = "
				+ "'" + id + "'", null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getString(0);
		} else {
			return null;
		}
	}

	public String getAttributeByAttribute(String attribute, String attribute2,
			String value) {
		Cursor c = this.query(CONTENT_URI, new String[] { attribute },
				attribute2 + " = " + "'" + value + "'", null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getString(0);
		} else {
			return null;
		}
	}
}
