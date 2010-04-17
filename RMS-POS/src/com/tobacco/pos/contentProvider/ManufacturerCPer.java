package com.tobacco.pos.contentProvider;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.net.Uri;

public class ManufacturerCPer extends BaseContentProvider {

	private static final String TABLE_NAME = DatabaseHelper.MANUFACTURER;

	public static final Uri CONTENT_URI = Uri
			.parse("content://com.tobacco.pos.contentProvider.ManufacturerCPer");
	
	public boolean onCreate() {
			 
			ctx = getContext();
			dbHelper = new DatabaseHelper(getContext());
			this.tableName = ManufacturerCPer.TABLE_NAME;
			return true;
		}

	public List<String> getAllManufacturerName() {
		Cursor c = this.query(CONTENT_URI, null, null, null, null);
		if (c.getCount() > 0) {
			List<String> allManufacturer = new ArrayList<String>();
			c.moveToFirst();
			for (int i = 0; i < c.getCount(); i++) {
				allManufacturer.add(c.getString(1));
				c.moveToNext();
			}
			return allManufacturer;
		}
		return null;
	}

	public int getMIdByMName(String mName) {// 根据厂家的名字查找该厂家的ID
		Cursor c = this.query(CONTENT_URI, null, " mName = ? ",
				new String[] { mName }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		} else
			return -1;
	}

	public String getMNameByMId(int mId) {
		Cursor c = this.query(CONTENT_URI, null, " _id = ? ",
				new String[] { mId + "" }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getString(1);
		}
		return "";
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
