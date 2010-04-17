package com.tobacco.pos.contentProvider;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class PurchaseItemCPer extends BaseContentProvider {

	private static final String TABLE_NAME = DatabaseHelper.PURCHASEITEM;
	public static final Uri CONTENT_URI = Uri
			.parse("content://com.tobacco.pos.contentProvider.PurchaseBillCPer");

	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		this.tableName = TABLE_NAME;
		return super
				.query(uri, projection, selection, selectionArgs, sortOrder);
	}

	public boolean addPItem(int pBillId, int priceId, int count) {
		Cursor c = this.query(CONTENT_URI, null,
				" purchaseBillId = ? and pPriceId = ? ", new String[] {
						pBillId + "", priceId + "" }, null);
		if (c.getCount() == 0) {

			ContentValues value = new ContentValues();
			value.put("purchaseBillId", pBillId);
			value.put("pGoodsNum", count);
			value.put("pPriceId", priceId);

			Uri uri =this.getContext().getContentResolver().insert(CONTENT_URI, value);
			if (uri != null)
				return true;
			return false;
		} else {
			c.moveToFirst();
			int originalCount = c.getInt(2);
			ContentValues value = new ContentValues();
			value.put("purchaseBillId", pBillId);
			value.put("pGoodsNum", originalCount + count);
			value.put("pPriceId", priceId);
			int updateCount = this.update(CONTENT_URI, value,
					" purchaseBillId = ? and pPriceId = ? ", new String[] {
							pBillId + "", priceId + "" });
			if (updateCount > 0)
				return true;

			else
				return false;

		}
	}

	public List<Integer> getAllPCountByPBillId(int pBillId) {
		Cursor c = this.query(CONTENT_URI, null, " purchaseBillId = ? ",
				new String[] { pBillId + "" }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			List<Integer> allCountId = new ArrayList<Integer>();
			for (int i = 0; i < c.getCount(); i++) {
				allCountId.add(c.getInt(2));
				c.moveToNext();
			}
			return allCountId;
		}
		return null;
	}

	public List<Integer> getAllPriceIdByPBillId(int pBillId) {
		Cursor c = this.query(CONTENT_URI, null, " purchaseBillId = ? ",
				new String[] { pBillId + "" }, null);

		if (c.getCount() > 0) {
			c.moveToFirst();
			List<Integer> allPriceId = new ArrayList<Integer>();
			for (int i = 0; i < c.getCount(); i++) {
				allPriceId.add(c.getInt(3));
				c.moveToNext();
			}
			return allPriceId;
		}
		return new ArrayList<Integer>();
	}

	public ArrayList<ArrayList<Integer>> getPItemByPriceId(int priceId) {
		Cursor c = this.query(CONTENT_URI, null, " pPriceId = ? ",
				new String[] { priceId + "" }, null);
		if (c.getCount() > 0) {

			c.moveToFirst();
			ArrayList<ArrayList<Integer>> allPItem = new ArrayList<ArrayList<Integer>>();
			for (int i = 0; i < c.getCount(); i++) {
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(c.getInt(0));
				temp.add(c.getInt(1));
				temp.add(c.getInt(2));
				temp.add(c.getInt(3));

				allPItem.add(temp);

				c.moveToNext();
			}
			return allPItem;
		}
		return new ArrayList<ArrayList<Integer>>();
	}
}
