package com.tobacco.pos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PurchaseBillDAO  extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "POS.db";// 数据库名称

	private static final int DATABASE_VERSION = 1;
	
	private Cursor c = null;
	public PurchaseBillDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	 
	}
	
	public int getPBillIdByCode(String pBillCode, SQLiteDatabase db){
		
		c = db.query("PurchaseBill", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getString(1).equals(pBillCode)){//找到相应的进货单
				return c.getInt(0);
			}
			c.moveToNext();
		}
		return 0;
	}
	public String getMaxPBillNum(SQLiteDatabase db){//获取最大的进货单号，增加进货单时在这基础上加1
		c = db.query("PurchaseBill", null, null, null, null, null, null);
		
		c.moveToLast();//将游标移到最后一行，开始读取它的编号pBillNum
		
		return c.getString(1);
	}
	public String [] getAllPBill(SQLiteDatabase db){//以数组的显示返回所有的进货单，格式：进货单号+时间
		String str [];
		c = db.query("PurchaseBill", null, null, null, null, null, null);
		str = new String[c.getCount()];
		c.moveToLast();
		for(int i=0;i<c.getCount();i++){
			str[i] = c.getString(1)+":"+c.getString(3);
			c.moveToPrevious();
		}
		return str;
	}
	public boolean savePBill(String pBillNum, int operId, String time, String comment, SQLiteDatabase db){

		ContentValues value = new ContentValues();
		
		value.clear();
		value.put("pBillNum", pBillNum);
		value.put("operId", operId);
		value.put("time", time);
		value.put("comment", comment);
		
		db.insertOrThrow("PurchaseBill", null, value);
		
		return true;
	}


}
