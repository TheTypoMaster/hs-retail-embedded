package com.tobacco.pos.contentProvider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class PurchaseBillCPer extends BaseContentProvider {

	private static final String TABLE_NAME = "PurchaseBill";
	public static final Uri CONTENT_URI = Uri
			.parse("content://com.tobacco.pos.contentProvider.PurchaseBillCPer");

	public boolean onCreate() {
		 
		ctx = getContext();
		dbHelper = new DatabaseHelper(getContext());
		this.tableName = PurchaseBillCPer.TABLE_NAME;
		return true;
	}
	public String getMaxPBillNum() {
		Cursor c = this.query(CONTENT_URI, null, null, null, " time ");
		if (c.getCount() > 0) {
			c.moveToLast();
			String lastPBillNum = c.getString(1);
			return lastPBillNum.substring(1);
		} else
			return "";
	}

	public int addPBill(String pBillNum, int operId, String time, String comment) {// 添加进货单，成功的话返回新加的进货单号，否则返回-1
		ContentValues value = new ContentValues();

		value.clear();
		value.put("pBillNum", pBillNum);
		value.put("operId", operId);
		value.put("time", time);
		value.put("comment", comment);

		Uri uri = this.getContext().getContentResolver().insert(CONTENT_URI, value);
		if (uri != null) {
			Cursor c = this.query(CONTENT_URI, null, null, null, " time ");
			if (c.getCount() > 0) {
				c.moveToLast();
				return c.getInt(0);
			} else
				return -1;
		} else
			return -1;
	}

	public String[] getAllPBill() {// 获取所有的进货单，时间最晚的排在最前，格式是：进货单号+时间
		String str[];
		Cursor c = this.query(CONTENT_URI, null, null, null, " time ");

		str = new String[c.getCount()];
		c.moveToLast();
		for (int i = 0; i < c.getCount(); i++) {
			str[i] = c.getString(1) + ":" + c.getString(3);
			c.moveToPrevious();
		}
		return str;
	}

	public int getPBillIdByPBillNum(String pBillNum) {
		Cursor c = this.query(CONTENT_URI, null, " pBillNum = ? ",
				new String[] { pBillNum }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return -1;
	}

	public String getPBillNumByPBillId(int pBillId) {
		Cursor c = this.query(CONTENT_URI, null, " _id = ?",
				new String[] { pBillId + "" }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getString(1);

		}
		return "";
	}

	public String getTimeByPBillId(int pBillId) {// 根据进货单的ID查找时间
		Cursor c = this.query(CONTENT_URI, null, " _id = ?",
				new String[] { pBillId + "" }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getString(3);

		}
		return "";
	}

	public int getPBillIdByTimeAndPBillNum(String startTime, String endTime,
			String pBillNum) {
		int pBillId = this.getPBillIdByPBillNum(pBillNum);
		if (pBillId == -1)
			return -1;
		else {// 找到相应编号的进货单，开始时间比较
			if (startTime.equals("开始时间") && endTime.equals("结束时间"))
				return pBillId;
			else if (startTime.equals("开始时间") && !endTime.equals("结束时间")) {
				String theTime = this.getTimeByPBillId(pBillId);

				endTime += " 23:59:59";

				if (endTime.compareTo(theTime) >= 0)
					return pBillId;
				else
					return -1;
			} else if (!startTime.equals("开始时间") && endTime.equals("结束时间")) {
				String theTime = this.getTimeByPBillId(pBillId);

				startTime += " 00:00:00";

				if (startTime.compareTo(theTime) <= 0)
					return pBillId;
				else
					return -1;
			} else {
				String theTime = this.getTimeByPBillId(pBillId);

				startTime += " 00:00:00";
				endTime += " 23:59:59";

				if ((startTime.compareTo(theTime) < 0 && theTime
						.compareTo(endTime) < 0)
						|| startTime.equals(theTime) || endTime.equals(theTime))
					return pBillId;
				else
					return -1;
			}
		}
	}

}
