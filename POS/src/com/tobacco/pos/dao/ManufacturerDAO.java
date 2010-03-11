package com.tobacco.pos.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ManufacturerDAO extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "POS.db";// 数据库名称

	private static final int DATABASE_VERSION = 1;
	
	private Cursor c = null;
	
	public ManufacturerDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public int getManufacturerIdByName(String mName, SQLiteDatabase db) {
		
		c = db.query("Manufacturer", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getString(1).equals(mName))
			  return c.getInt(0);
			
			else
				c.moveToNext();
		}
		
	 
		return 0;
	}
	public String getManufacturerNameById(int mId, SQLiteDatabase db){
		c = db.query("Manufacturer", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getInt(0) == mId){
				return c.getString(1);
			}
			c.moveToNext();
		}
		return "";
	}
	public String[] getAllManufacturer(SQLiteDatabase db) {
		c = db.query("Manufacturer", null, null, null, null, null, null);
		String[] allManufacturer = new String[c.getCount()];
		c.moveToFirst();
		for (int i = 0; i < c.getCount(); i++) {
			allManufacturer[i] = c.getString(1);// 获取所有厂家的名称
			c.moveToNext();
		}
		return allManufacturer;
	}

}
