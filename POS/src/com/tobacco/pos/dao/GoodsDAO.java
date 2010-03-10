package com.tobacco.pos.dao;

import java.util.Vector;

import com.tobacco.pos.dao.KindDAO;
import com.tobacco.pos.dao.ManufacturerDAO;
import com.tobacco.pos.dao.UnitDAO;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GoodsDAO extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "POS.db";// 数据库名称

	private static final int DATABASE_VERSION = 1;

	private ManufacturerDAO manufacturerDAO = null;
	
	private KindDAO kindDAO = null;
	
	private UnitDAO unitDAO = null;
	
	private Cursor c = null;

	public GoodsDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		manufacturerDAO = new ManufacturerDAO(context);
		kindDAO = new KindDAO(context);
		unitDAO = new UnitDAO(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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

	public String[] getAllGoods(SQLiteDatabase db){
		c = db.query("Goods", null, null, null, null, null, null);
		String[] allGoodsName = new String[c.getCount()];
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			allGoodsName[i] = c.getString(1)+":"+c.getString(2);//将商品的编号（不是ID）和商品名同时返回
			c.moveToNext();
		}
		return allGoodsName;
	}

	
	public int getMaxGoodId(SQLiteDatabase db){//获取最大的商品Id
		c = db.query("Goods", null, null, null, null, null, null);
		c.moveToLast();
		return c.getInt(0);
	}
	/**
	 * 
	 * @param goodsName 商品的名称
	 * @param mName 厂家的名称
	 * @param kindName 类别的名称
	 * @param allPrice 该商品的所有价格，allPrice存储的是向量，每个向量由单位名称，条形码，进货价，售价构成
	 * @return 添加商品的结果
	 */
	public String addGoods(String goodsName, String mName, String kindName, Vector allPrice, SQLiteDatabase db){
		
		
		if(goodsName.trim().length() == 0 || goodsName == null)
			return "请输入商品名称";
		else if(allPrice.size() == 0)
			return "请为该商品添加单位以及价格";
		else{
			ContentValues value = new ContentValues();
			
			value.clear();
			value.put("goodsCode", "G"+goodsName);
			value.put("goodsName", goodsName);
		    value.put("manufacturerId", manufacturerDAO.getManufacturerIdByName(mName, db));
			value.put("kindId", kindDAO.getKindIdByName(kindName, db));
		    value.put("codeForShort", "111");
			value.put("goodsFormat", "222");
			
			db.insertOrThrow("Goods", null, value);
			
			int maxGoodId = getMaxGoodId(db);
	
			for(int i=0;i<allPrice.size();i++){

				value.clear();
				value.put("goodsId", maxGoodId);
				
				Vector temp = (Vector) allPrice.get(i);
				int unitId = unitDAO.getUnitIdByName(temp.get(0)+"", db);
				value.put("goodsId", maxGoodId);
				value.put("unitId", unitId);
				value.put("barcode", temp.get(1)+"");
				value.put("inPrice", temp.get(2)+"");
				value.put("outPrice", temp.get(3)+"");
				
				db.insertOrThrow("GoodsPrice", null, value);
			}
			db.close();
			
			return "成功添加商品" + goodsName;
		}
		
	}
	public String getGoodsNameByGoodsId(int goodsId, SQLiteDatabase db){//根据商品的ID查找商品的详细信息
	
		c = db.query("Goods", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getInt(0) == goodsId){
				return c.getString(2);//商品名字
					
			}
			c.moveToNext();
		}
		return "";
	}
}
