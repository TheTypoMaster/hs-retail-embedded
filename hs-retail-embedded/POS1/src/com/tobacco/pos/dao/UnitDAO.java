package com.tobacco.pos.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UnitDAO  extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "POS.db";// ���ݿ�����

	private static final int DATABASE_VERSION = 1;
	
	private Cursor c;
	
	public UnitDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	 	
	}
	public List<String> getAllUnit(SQLiteDatabase db) {//��ȡ���еĵ�λ����
		c = db.query("Unit", null, null, null, null, null, null);
		List<String> allUnitName = new ArrayList<String>();
		c.moveToFirst();
		for (int i = 0; i < c.getCount(); i++) {
			allUnitName.add(c.getString(1));

			c.moveToNext();
		}
		return allUnitName;
	}
	/**
	 * ͨ����λ��ID��������
	 * @param unitId Ҫ���ҵ�λ��ID
	 * @param db
	 * @return ������Ӧ��λ������
	 */
	public String getUnitNameById(int unitId, SQLiteDatabase db){
		c = db.query("Unit", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getInt(0) == unitId)
				return c.getString(1);
			
			else
				c.moveToNext();
		}
		return null;
	}
	public int getUnitIdByName(String unitName, SQLiteDatabase db){
		c = db.query("Unit", null, null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			if(c.getString(1).equals(unitName))
				return c.getInt(0);
			
			else
				c.moveToNext();
		}
		 
		return 0;
	}
}
