package com.tobacco.pos.contentProvider;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class GoodsPriceCPer extends BaseContentProvider {

	private UnitCPer unitCPer = null;
	private GoodsCPer gCPer = null;

	private static final String TABLE_NAME = DatabaseHelper.GOODSPRICE;
	public static final Uri CONTENT_URI = Uri
			.parse("content://com.tobacco.pos.contentProvider.GoodsPriceCPer");

    public boolean onCreate() {
	 
		ctx = getContext();
		dbHelper = new DatabaseHelper(getContext());
		this.tableName = GoodsPriceCPer.TABLE_NAME;
		return true;
	}

	public int getGoodsIdByBarcode(String barcode) {
		Cursor c = this.query(CONTENT_URI, null, " barcode = ? ",
				new String[] { barcode }, null);

		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(1);
		} else
			return -1;
	}

	public int getUnitIdByBarcode(String barcode) {
		Cursor c = this.query(CONTENT_URI, null, " barcode = ? ",
				new String[] { barcode }, null);

		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(2);
		}
		return -1;
	}

	public double getOutPriceByBarcode(String barcode) {
		Cursor c = this.query(CONTENT_URI, null, " barcode = ? ",
				new String[] { barcode }, null);

		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getDouble(5);
		} else
			return 0;
	}

	public double getInPriceByBarcode(String barcode) {
		Cursor c = this.query(CONTENT_URI, null, " barcode = ? ",
				new String[] { barcode }, null);

		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getDouble(4);
		} else
			return 0;
	}

	public int getGoodsPriceIdByBarcode(String barcode) {
		Cursor c = this.query(CONTENT_URI, null, " barcode = ? ",
				new String[] { barcode }, null);

		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		} else
			return 0;
	}

	public List<Integer> getGoodsPriceIdByGoodsId(int goodsId) {
		Cursor c = this.query(CONTENT_URI, null, " goodsId = ? ",
				new String[] { goodsId + "" }, null);
		if (c.getCount() > 0) {
			List<Integer> priceIdList = new ArrayList<Integer>();
			c.moveToFirst();
			for (int i = 0; i < c.getCount(); i++) {
				priceIdList.add(c.getInt(0));
				c.moveToNext();
			}
			return priceIdList;
		}
		return new ArrayList<Integer>();
	}

	public boolean addGoodsPrice(int goodsId, int unitId, String barcode,
			double inPrice, double outPrice) {
		ContentValues value = new ContentValues();
		value.clear();
		value.put("goodsId", goodsId);
		value.put("unitId", unitId);
		value.put("barcode", barcode);
		value.put("inPrice", inPrice);
		value.put("outPrice", outPrice);

	
		if (this.getContext().getContentResolver().insert(CONTENT_URI, value) != null)
			return true;
		else
			return false;
	}

	public List<String> getUnitNameByGoodsId(int goodsId) {// 根据商品的ID查找单位的名字（一件商品可能有多种单位，比如红塔山的烟有”包“，”条“
		Cursor c = this.query(CONTENT_URI, null, " goodsId = ? ",
				new String[] { goodsId + "" }, null);
		List<String> allUnitByGoodsId = new ArrayList<String>();

		unitCPer = new UnitCPer();

		if (c.getCount() > 0) {
			c.moveToFirst();
			for (int i = 0; i < c.getCount(); i++) {
				allUnitByGoodsId.add(unitCPer.getUnitNameById(c.getInt(2)));
				c.moveToNext();
			}

			return allUnitByGoodsId;
		}
		return new ArrayList<String>();
	}

	// 根据商品的Id和单位Id查找进货价
	public double getInPriceByGoodsIdAndUnitId(int goodsId, int unitId) {
		Cursor c = this.query(CONTENT_URI, null,
				" goodsId = ? and unitId = ? ", new String[] { goodsId + "",
						unitId + "" }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getDouble(4);
		}
		return 0;
	}

	// 根据商品的Id和单位Id查找售价
	public double getOutPriceByGoodsIdAndUnitId(int goodsId, int unitId) {
		Cursor c = this.query(CONTENT_URI, null,
				" goodsId = ? and unitId = ? ", new String[] { goodsId + "",
						unitId + "" }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getDouble(5);
		}
		return 0;
	}

	// 根据商品的Id和单位Id查找价格项Id
	public int getPriceIdByGoodsIdAndUnitId(int goodsId, int unitId) {
		Cursor c = this.query(CONTENT_URI, null,
				" goodsId = ? and unitId = ? ", new String[] { goodsId + "",
						unitId + "" }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return -1;
	}

	public List<String> getInfoByGPriceId(int goodsPriceId) {
		Cursor c = this.query(CONTENT_URI, null, " _id = ? ",
				new String[] { goodsPriceId + "" }, null);
		if (c.getCount() > 0) {
			gCPer = new GoodsCPer();
			unitCPer = new UnitCPer();
			c.moveToFirst();
			List<String> info = new ArrayList<String>();

			info.add(gCPer.getGoodsNameByGoodsId(c.getInt(1)));
			info.add(gCPer.getUnitNameByGoodsId(c.getInt(1)));
			info.add(unitCPer.getUnitNameById(c.getInt(2)));
			info.add("" + c.getDouble(4));
			info.add("" + c.getDouble(5));

			return info;
		}
		return null;
	}

	public String getBarcodeIdByGoodsPriceId(String id) {
		Cursor c = this.query(CONTENT_URI, null, " _id = ? ",
				new String[] { id }, null);

		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getString(3);
		} else
			return null;
	}

	public int getGoodsIdByGoodsPriceId(int priceId) {// 根据商品价格的Id查找商品Id
		Cursor c = this.query(CONTENT_URI, null, " _id = ? ",
				new String[] { priceId + "" }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(1);
		}
		return -1;
	}

	public int getUnitIdByGoodsPriceId(int priceId) {
		Cursor c = this.query(CONTENT_URI, null, " _id = ? ",
				new String[] { priceId + "" }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(2);
		}
		return -1;
	}

	public double getInPriceByGoodsPriceId(int priceId) {
		Cursor c = this.query(CONTENT_URI, null, " _id = ? ",
				new String[] { priceId + "" }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getDouble(4);
		}
		return 0;
	}

	public double getOutPriceByGoodsPriceId(int priceId) {
		Cursor c = this.query(CONTENT_URI, null, " _id = ? ",
				new String[] { priceId + "" }, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getDouble(5);
		}
		return 0;
	}

	public String getAttributeById(String attribute, String id) {
		Cursor c = this.query(CONTENT_URI, new String[] { attribute }, "_id = "
				+ "'" + id + "'", null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getString(0);
		} else {
			return null;
		}
	}

	public String getAttributeByAttribute(String attribute, String attribute2,
			String value) {
		Cursor c = this.query(CONTENT_URI, new String[] { attribute },
				attribute2 + " = " + "'" + value + "'", null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getString(0);
		} else {
			return null;
		}
	}

}
