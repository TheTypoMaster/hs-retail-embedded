package com.tobacco.pos.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KindDAO  extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "POS.db";// 数据库名称

	private static final int DATABASE_VERSION = 1;
	
	private Cursor c;
	
	public KindDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
	 
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	 
	}

	public String[] getAllKind(SQLiteDatabase db) {//获取所有的种类
		c = db.query("GoodsKind", null, null, null, null, null, null);
		String[] allKindName = new String[c.getCount()];
		c.moveToFirst();
		for (int i = 0; i < c.getCount(); i++) {
			allKindName[i] = c.getString(1);
			c.moveToNext();
		}
		return allKindName;
	}
	public int getKindIdByName(String kindName, SQLiteDatabase db){
		 
		c = db.query("GoodsKind", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getString(1).equals(kindName))
				return c.getInt(0);
			
			else
				c.moveToNext();
		}
		
		 
		return 0;
	}

}
