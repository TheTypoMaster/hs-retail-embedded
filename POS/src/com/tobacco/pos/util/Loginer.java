package com.tobacco.pos.util;

import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Loginer extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "POS.db";// 数据库名称

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

	public String verify(SQLiteDatabase db) {// 判断管理员是否上线,有的话返回管理员的名字，没有的话返回空
		c = db.query("UserInfo", null, null, null, null, null, null);
		c.moveToFirst();

		for (int i = 0; i < c.getCount(); i++) {
			if (c.getInt(3) == 0) {
				if (c.getInt(4) == 1)// 1为登陆
					return c.getString(1);
				else
					return null;
			}
			c.moveToNext();
		}
		return null;
	}

	public String verifyIsSBOnline(SQLiteDatabase db) {// 检查是否有人在线，有的话返回该用户的名字，没有的话返回空
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
	 * @param userName登陆时的用户名
	 * @param password密码
	 * @param
	 *            purpose登陆的目的，比如PaymentManagement，PurchaseManagement，ReportManagement等
	 * @param db
	 * @return是否有权限
	 */
	public boolean login(String userName, String password, String purpose,
			SQLiteDatabase db) {
		if (purpose.equals("PurchaseManagement")) {// 进货时的权限认证
			if (!userName.equals("laoda"))
				return false;
			else {
				c = db.query("UserInfo", null, null, null, null, null, null);
				c.moveToFirst();
				for (int i = 0; i < c.getCount(); i++) {
					if (c.getString(1).equals(userName)
							&& c.getString(2).equals(password)) {// 如果找到对应的用户
						db.execSQL("update UserInfo set status = 1 where "
								+ _ID + " = " + c.getInt(0));
						return true;
					}
					c.moveToNext();
				}

				return false;// 找不到对应的用户
			}
		} else if (purpose.equals("PaymentManagement")) {// 销售时的权限认证
			c = db.query("UserInfo", null, null, null, null, null, null);
			c.moveToFirst();
			for (int i = 0; i < c.getCount(); i++) {
				if (c.getString(1).equals(userName)
						&& c.getString(2).equals(password)) {// 如果找到对应的用户
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
