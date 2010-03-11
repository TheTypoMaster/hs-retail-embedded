package com.tobacco.pos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SalesItemDAO  extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME = "POS.db";// Êý¾Ý¿âÃû³Æ

	private static final int DATABASE_VERSION = 1;
	
	private Cursor c = null;
	
	public SalesItemDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		 
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	 
	}
	
	public boolean addSalesItem(int salesBillId, int sGoodsNum, int sPriceId, SQLiteDatabase db){
		
		ContentValues value = new ContentValues();
		value.put("salesBillId", salesBillId);
		value.put("sGoodsNum", sGoodsNum);
		value.put("sPriceId", sPriceId);
		
		db.insertOrThrow("SalesItem", null, value);
	
		return true;
	}
	

}
