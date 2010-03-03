package com.tobacco.pos.util;

import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Loginer extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "POS.db";// ���ݿ�����

	private static final int DATABASE_VERSION = 1;

	private Cursor c = null;

	public Loginer(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public boolean verify(SQLiteDatabase db) {// �жϹ���Ա�Ƿ�����
		c = db.query("UserInfo", null, null, null, null, null, null);
		c.moveToFirst();

		for (int i = 0; i < c.getCount(); i++) {
			if (c.getInt(3) == 0) {
				if (c.getInt(4) == 1)// 1Ϊ��½
					return true;
				else
					return false;
			}
			c.moveToNext();
		}
		return false;
	}

	public boolean login(String userName, String password, SQLiteDatabase db) {

		if (!userName.equals("laoda"))
			return false;
		else {
			c = db.query("UserInfo", null, null, null, null, null, null);
			c.moveToFirst();
			for (int i = 0; i < c.getCount(); i++) {
				if (c.getString(1).equals(userName)
						&& c.getString(2).equals(password)) {// ����ҵ���Ӧ���û�
					db.execSQL("update UserInfo set status = 1 where " + _ID
							+ " = " + c.getInt(0));
					return true;
				}
				c.moveToNext();
			}

			return false;// �Ҳ�����Ӧ���û�
		}
	}
	public boolean logout(SQLiteDatabase db){
		
		db.execSQL("update UserInfo set status = 0 where status =1 ");
		
		return true;
	}
}
