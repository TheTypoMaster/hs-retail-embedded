package com.tobacco.pos.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VIPInfoDAO  extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "POS.db";// Êý¾Ý¿âÃû³Æ

	private static final int DATABASE_VERSION = 1;
	
	private Cursor c;
	
	public VIPInfoDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 
	}

	public int getVIPIdByVIPNum(String VIPNum, SQLiteDatabase db){
		c = db.query("VIPInfo", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getString(1).equals(VIPNum)){
				return c.getInt(0);
			}
			c.moveToNext();
		}
		return -1;
	}
}
