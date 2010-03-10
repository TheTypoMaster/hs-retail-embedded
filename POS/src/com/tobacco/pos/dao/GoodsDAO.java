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
	private static final String DATABASE_NAME = "POS.db";// ���ݿ�����

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
			allManufacturer[i] = c.getString(1);// ��ȡ���г��ҵ�����
			c.moveToNext();
		}
		return allManufacturer;
	}

	public String[] getAllGoods(SQLiteDatabase db){
		c = db.query("Goods", null, null, null, null, null, null);
		String[] allGoodsName = new String[c.getCount()];
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			allGoodsName[i] = c.getString(1)+":"+c.getString(2);//����Ʒ�ı�ţ�����ID������Ʒ��ͬʱ����
			c.moveToNext();
		}
		return allGoodsName;
	}

	
	public int getMaxGoodId(SQLiteDatabase db){//��ȡ������ƷId
		c = db.query("Goods", null, null, null, null, null, null);
		c.moveToLast();
		return c.getInt(0);
	}
	/**
	 * 
	 * @param goodsName ��Ʒ������
	 * @param mName ���ҵ�����
	 * @param kindName ��������
	 * @param allPrice ����Ʒ�����м۸�allPrice�洢����������ÿ�������ɵ�λ���ƣ������룬�����ۣ��ۼ۹���
	 * @return �����Ʒ�Ľ��
	 */
	public String addGoods(String goodsName, String mName, String kindName, Vector allPrice, SQLiteDatabase db){
		
		
		if(goodsName.trim().length() == 0 || goodsName == null)
			return "��������Ʒ����";
		else if(allPrice.size() == 0)
			return "��Ϊ����Ʒ��ӵ�λ�Լ��۸�";
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
			
			return "�ɹ������Ʒ" + goodsName;
		}
		
	}
	public String getGoodsNameByGoodsId(int goodsId, SQLiteDatabase db){//������Ʒ��ID������Ʒ����ϸ��Ϣ
	
		c = db.query("Goods", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getInt(0) == goodsId){
				return c.getString(2);//��Ʒ����
					
			}
			c.moveToNext();
		}
		return "";
	}
}
