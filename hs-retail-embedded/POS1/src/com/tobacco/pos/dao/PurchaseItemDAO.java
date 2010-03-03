package com.tobacco.pos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PurchaseItemDAO  extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "POS.db";// 数据库名称

	private static final int DATABASE_VERSION = 1;
	
	private Cursor c = null;
	
	public PurchaseItemDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	 
	}
	
	public boolean addPurchaseItem(int purchaseBillId, int pGoodsNum, int pPriceId, SQLiteDatabase db){

		c = db.query("PurchaseItem", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){//查找选择的进货单里是否有一样的商品，如果有的话更新数量
			if(c.getInt(1) == purchaseBillId && c.getInt(3) == pPriceId){
				int afterCount = c.getInt(2) + pGoodsNum;
				db.execSQL("update PurchaseItem set pGoodsNum = " + afterCount + " where purchaseBillId = " + purchaseBillId + " and pPriceId = " + pPriceId);
				return true;
			}
			c.moveToNext();
		}
		ContentValues value = new ContentValues();
		value.put("purchaseBillId", purchaseBillId);
		value.put("pGoodsNum", pGoodsNum);
		value.put("pPriceId", pPriceId);
		
		db.insertOrThrow("PurchaseItem", null, value);
		
		return true;
	}

}
