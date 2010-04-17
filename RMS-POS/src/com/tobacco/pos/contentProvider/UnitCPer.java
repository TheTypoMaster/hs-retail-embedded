package com.tobacco.pos.contentProvider;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.net.Uri;

public class UnitCPer extends BaseContentProvider {

	private static final String TABLE_NAME = DatabaseHelper.UNIT;
	public static final Uri CONTENT_URI = Uri
			.parse("content://com.tobacco.pos.contentProvider.UnitCPer");

	public boolean onCreate() {
		 
		ctx = getContext();
		dbHelper = new DatabaseHelper(getContext());
		this.tableName = UnitCPer.TABLE_NAME;
		return true;
	}

	public String getUnitNameById(int unitId) {
		Cursor c = this
				.query(CONTENT_URI, null, " _id = " + unitId, null, null);

		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getString(1);
		}
		return "";

	}

	public int getUnitIdByUnitName(String unitName) {
		Cursor c = this.query(CONTENT_URI, null, " name = ? ",
				new String[] { unitName }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return -1;
	}

	public List<String> getAllUnitName() {
		Cursor c = this.query(CONTENT_URI, null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			List<String> allUnitName = new ArrayList<String>();
			for (int i = 0; i < c.getCount(); i++) {
				allUnitName.add(c.getString(1));
				c.moveToNext();
			}
			return allUnitName;
		}
		return null;
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
}
