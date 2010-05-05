package com.tobacco.pos.Module;

import android.content.ContentValues;

import com.tobacco.pos.Module.AllTables.InventoryItem;

public class InventoryItemModel extends BaseModel {

	private int id;
	
	private String goodsName;
	
	private int goodsPriceId;
	
	private double goodsInPrice;
	
	private String unitName;
	
	private int expectNum;
	
	private int realNum;
	
	private double itemResult;
	
	private int ibillId;

	public InventoryItemModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InventoryItemModel(String goodsName, int goodsPriceId,
			double goodsPrice, String unitName, int expectNum) {
		super();
		this.goodsName = goodsName;
		this.goodsPriceId = goodsPriceId;
		this.goodsInPrice = goodsPrice;
		this.unitName = unitName;
		this.expectNum = expectNum;
		this.realNum = 0;
		this.itemResult = 0;
		this.ibillId = 0;
	}

	public InventoryItemModel(String goodsName, int goodsPriceId,
			double goodsPrice, String unitName, int expectNum, int realNum,
			double itemResult, int ibillId) {
		super();
//		this.id = id;
		this.goodsName = goodsName;
		this.goodsPriceId = goodsPriceId;
		this.goodsInPrice = goodsPrice;
		this.unitName = unitName;
		this.expectNum = expectNum;
		this.realNum = realNum;
		this.itemResult = itemResult;
		this.ibillId = ibillId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getGoodsPriceId() {
		return goodsPriceId;
	}

	public void setGoodsPriceId(int goodsPriceId) {
		this.goodsPriceId = goodsPriceId;
	}

	public double getGoodsInPrice() {
		return goodsInPrice;
	}

	public void setGoodsInPrice(double goodsInPrice) {
		this.goodsInPrice = goodsInPrice;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getExpectNum() {
		return expectNum;
	}

	public void setExpectNum(int expectNum) {
		this.expectNum = expectNum;
	}

	public int getRealNum() {
		return realNum;
	}

	public void setRealNum(int realNum) {
		this.realNum = realNum;
	}

	public double getItemResult() {
		return itemResult;
	}

	public void setItemResult(double itemResult) {
		this.itemResult = itemResult;
	}

	public int getIbillId() {
		return ibillId;
	}

	public void setIbillId(int ibillId) {
		this.ibillId = ibillId;
	}

	public ContentValues genContentValues(){
		
		ContentValues values = new ContentValues();
		values.put(InventoryItem.COMMENT,comment);
		values.put(InventoryItem.EXPECT_NUM,expectNum);
		values.put(InventoryItem.GOODS_PRICE_ID,goodsPriceId);
		values.put(InventoryItem.IBILL_ID,ibillId);
		values.put(InventoryItem.ITEM_RESULT,itemResult);
		values.put(InventoryItem.REAL_NUM,realNum);
		
		return values;
	}
}
