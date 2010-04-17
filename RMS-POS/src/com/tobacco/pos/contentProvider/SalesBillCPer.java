package com.tobacco.pos.contentProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class SalesBillCPer extends BaseContentProvider {

	private static final String TABLE_NAME = DatabaseHelper.SALESBILL;

	public static final Uri CONTENT_URI = Uri
			.parse("content://com.tobacco.pos.contentProvider.SalesBillCPer");

	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		this.tableName = TABLE_NAME;
		return super
				.query(uri, projection, selection, selectionArgs, sortOrder);
	}

	public int addSBill(int operId, int VIPId, double payMoney) {
		SimpleDateFormat dateFormater = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Cursor c = this.query(CONTENT_URI, null, null, null, " sBillNum ");
		String sBillNum;
		if (c.getCount() > 0) {
			c.moveToLast();
			String lastSBillNum = c.getString(1);
			sBillNum = "" + (Integer.parseInt(lastSBillNum) + 1);
		} else
			sBillNum = "1";

		ContentValues value = new ContentValues();

		value.clear();
		value.put("sBillNum", sBillNum);
		value.put("operId", operId);
		Date d = new Date();
		value.put("time", dateFormater.format(d));
		value.put("VIPId", VIPId);
		value.put("payMoney", payMoney);

		this.getContext().getContentResolver().insert(CONTENT_URI, value);

		c = this.query(CONTENT_URI, null, null, null, null);
		c.moveToLast();
		return c.getInt(0);// 获取最后添加的销售单的ID
	}

	public String getSBillNumBySBillId(int newSBillId) {
		Cursor c = this.query(CONTENT_URI, null, " _id = " + newSBillId, null,
				null);

		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getString(1);
		}
		return "";
	}

	public int getSBillIdBySBillNum(String startTime, String endTime,
			String salesBillNum) {// 根据销售单的编号以及时间查找销售单Id,
		Cursor c = null;

		if (startTime.equals("开始时间") && endTime.equals("结束时间"))// 没时间约束
			c = this.query(CONTENT_URI, null, " sBillNum = ? ",
					new String[] { salesBillNum }, null);
		else if (!startTime.equals("开始时间") && endTime.equals("结束时间"))// 限定开始时间
			c = this.query(CONTENT_URI, null, " time >= ? and sBillNum = ? ",
					new String[] { startTime, salesBillNum }, null);
		else if (startTime.equals("开始时间") && !endTime.equals("结束时间"))// 有限定结束时间
			c = this.query(CONTENT_URI, null, " time <= ? and sBillNum = ? ",
					new String[] { endTime, salesBillNum }, null);
		else
			// 有开始时间限制也有结束时间限制
			c = this.query(CONTENT_URI, null,
					" time between ? and ? and sBillNum = ? ", new String[] {
							startTime, endTime, salesBillNum }, null);

		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return -1;
	}

	public String getSTimeBySBillId(int salesBillId) {// 根据销售单的Id查找时间
		Cursor c = this.query(CONTENT_URI, null, " _id = ? ", new String[] { ""
				+ salesBillId }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getString(3);
		}
		return "";
	}

	public int getVIPIdBySBillId(int salesBillId) {// 根据销售单的Id查找客户Id
		Cursor c = this.query(CONTENT_URI, null, " _id = ? ", new String[] { ""
				+ salesBillId }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(4);
		}
		return -1;
	}

	public List<Integer> getSalesBillIdByVIPId(String startTime,
			String endTime, int VIPId) {// 根据时间，客户Id查找满足条件的所有SalesBill的Id
		Cursor c = null;
		if (startTime.equals("开始时间") && endTime.equals("结束时间"))// 没有时间限制
			c = this.query(CONTENT_URI, null, " VIPId = ? ",
					new String[] { VIPId + "" }, null);
		else if (!startTime.equals("开始时间") && endTime.equals("结束时间"))// 有限制开始时间
			c = this.query(CONTENT_URI, null, " time >= ? and VIPId = ? ",
					new String[] { startTime, VIPId + "" }, null);
		else if (startTime.equals("开始时间") && !endTime.equals("结束时间"))// 有设置结束时间
			c = this.query(CONTENT_URI, null, " time <= ? and VIPId = ? ",
					new String[] { endTime, VIPId + "" }, null);
		else
			c = this.query(CONTENT_URI, null,
					" time between ? and ?  and VIPId = ? ", new String[] {
							startTime, endTime, VIPId + "" }, null);
		if (c.getCount() > 0) {
			List<Integer> sBillIdList = new ArrayList<Integer>();
			c.moveToFirst();
			for (int i = 0; i < c.getCount(); i++) {
				sBillIdList.add(c.getInt(0));
				c.moveToNext();
			}
			return sBillIdList;
		} else
			return new ArrayList<Integer>();
	}

}
