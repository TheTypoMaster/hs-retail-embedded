package com.tobacco.pos.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GoodsPriceDAO  extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "POS.db";// 数据库名称

	private static final int DATABASE_VERSION = 1;
	
	private UnitDAO uDAO = null;
	private Cursor c;
	
	public GoodsPriceDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		uDAO = new UnitDAO(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	 
	}
	
	public List<String> getPriceByGoodsCode(String goodsCode, SQLiteDatabase db){//根据商品的代码查找对应的所有价格
		int goodsId = 0;//根据商品的代码查找的ID
		List<String> allPrice = new ArrayList<String>();;//一件商品对应的所有价格
		c = db.query("Goods", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getString(1).equals(goodsCode))
			{
				goodsId = c.getInt(0);//查找到相应商品的ID
				break;
			}
			else
				c.moveToNext();
		}
		 
		c = db.query("GoodsPrice", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getInt(1) == goodsId){//找到相应的商品
				String uName = uDAO.getUnitNameById(c.getInt(2), uDAO.getReadableDatabase());
				
				String price = c.getInt(0) + ":" + uName + ": 进货价(" + c.getDouble(4) + ")售价(" + c.getDouble(5) + ")";
				allPrice.add(price);
			}
			c.moveToNext();
		}
		return allPrice;		
	}
	public int getGoodsIdByBarcode(String barcode, SQLiteDatabase db){
		c = db.query("GoodsPrice", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getString(3).equals(barcode)){
				return c.getInt(1);
			}
			c.moveToNext();
		}
		return -1;
	}
	public int getUnitIdByBarcode(String barcode, SQLiteDatabase db){
		c = db.query("GoodsPrice", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getString(3).equals(barcode)){
				return c.getInt(2);
			}
			c.moveToNext();
		}
		return -1;
	}
	public double getOutPriceByBarcode(String barcode, SQLiteDatabase db){
		c = db.query("GoodsPrice", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getString(3).equals(barcode)){
				return c.getDouble(5);
			}
			c.moveToNext();
		}
		return 0;
	}
	
	public int getGoodsPriceIdByBarcode(String barcode, SQLiteDatabase db){
		c = db.query("GoodsPrice", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getString(3).equals(barcode)){
				return c.getInt(0);
			}
			c.moveToNext();
		}
		return -1;
	}

}
