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

	public String verify(SQLiteDatabase db) {// �жϹ���Ա�Ƿ�����,�еĻ����ع���Ա�����֣�û�еĻ����ؿ�
		c = db.query("UserInfo", null, null, null, null, null, null);
		c.moveToFirst();

		for (int i = 0; i < c.getCount(); i++) {
			if (c.getInt(3) == 0) {
				if (c.getInt(4) == 1)// 1Ϊ��½
					return c.getString(1);
				else
					return null;
			}
			c.moveToNext();
		}
		return null;
	}

	public String verifyIsSBOnline(SQLiteDatabase db) {// ����Ƿ��������ߣ��еĻ����ظ��û������֣�û�еĻ����ؿ�
		c = db.query("UserInfo", null, null, null, null, null, null);
		c.moveToFirst();

		for (int i = 0; i < c.getCount(); i++) {
			if (c.getInt(4) == 1) {
				return c.getString(1);
			}
			c.moveToNext();
		}
		return null;
	}

	/**
	 * 
	 * @param userName��½ʱ���û���
	 * @param password����
	 * @param
	 *            purpose��½��Ŀ�ģ�����PaymentManagement��PurchaseManagement��ReportManagement��
	 * @param db
	 * @return�Ƿ���Ȩ��
	 */
	public boolean login(String userName, String password, String purpose,
			SQLiteDatabase db) {
		if (purpose.equals("PurchaseManagement")) {// ����ʱ��Ȩ����֤
			if (!userName.equals("laoda"))
				return false;
			else {
				c = db.query("UserInfo", null, null, null, null, null, null);
				c.moveToFirst();
				for (int i = 0; i < c.getCount(); i++) {
					if (c.getString(1).equals(userName)
							&& c.getString(2).equals(password)) {// ����ҵ���Ӧ���û�
						db.execSQL("update UserInfo set status = 1 where "
								+ _ID + " = " + c.getInt(0));
						return true;
					}
					c.moveToNext();
				}

				return false;// �Ҳ�����Ӧ���û�
			}
		} else if (purpose.equals("PaymentManagement")) {// ����ʱ��Ȩ����֤
			c = db.query("UserInfo", null, null, null, null, null, null);
			c.moveToFirst();
			for (int i = 0; i < c.getCount(); i++) {
				if (c.getString(1).equals(userName)
						&& c.getString(2).equals(password)) {// ����ҵ���Ӧ���û�
					db.execSQL("update UserInfo set status = 1 where "
							+ _ID + " = " + c.getInt(0));
					return true;
				}
				c.moveToNext();
			}
			return false;
		} else
			return false;
	}

	public boolean logout(String userName, SQLiteDatabase db) {

		db.execSQL("update UserInfo set status = 0 where userName = '" + userName + "'");

		return true;
	}
}
