package com.tobacco.pos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper; 

public class SalesBillDAO  extends SQLiteOpenHelper{
	

	private static final String DATABASE_NAME = "POS.db";// 数据库名称

	private static final int DATABASE_VERSION = 1;
	
	private Cursor c = null;
	
	public SalesBillDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	 
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
	 		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
	}
	public int addSBill(int operId, String time, int VIPId, SQLiteDatabase db){//增加销售单，如果增加成功的话返回最大的ID
		c = db.query("SalesBill", null, null, null, null, null, null);	
		String sBillNum = "S" + (c.getCount() + 1) ;

		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("sBillNum", sBillNum);
		value.put("operId", operId);
		value.put("time", time);
		value.put("VIPId", VIPId);
		
		db.insertOrThrow("SalesBill", null, value);
		
		c = db.query("SalesBill", null, null, null, null, null, null);	
		c.moveToLast();
		return c.getInt(0);
	}
	
	public String getSBillNumBySBillId(int sBillId, SQLiteDatabase db){
		c = db.query("SalesBill", null, null, null, null, null, null);	
		c.moveToFirst();
		
		for(int i=0;i<c.getCount();i++){
			if(c.getInt(0) == sBillId){
				return c.getString(1);
			}
			c.moveToNext();
		}
		
		return "";
	}
}
