package com.tobacco.pos.entity;

public class InventoryItemObject {//库存项对象，由序号-商品名称-厂家-种类-剩余数量-单位-进价-售价组成

	private int index;
	
	private String goodsName;//商品名称
	
	private String mName;//厂家
	
	private String kind;//种类
	
	private String surplusNum;//剩余数量
	
	private String unitName;//单位名称
	
	private String inPrice;//进价
	
	private String outPrice;//售价
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		
		this.index = index;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getMName() {
		return mName;
	}

	public void setMName(String name) {
		mName = name;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getSurplusNum() {
		return surplusNum;
	}

	public void setSurplusNum(String surplusNum) {
		this.surplusNum = surplusNum;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getInPrice() {
		return inPrice;
	}

	public void setInPrice(String inPrice) {
		this.inPrice = inPrice;
	}

	public String getOutPrice() {
		return outPrice;
	}

	public void setOutPrice(String outPrice) {
		this.outPrice = outPrice;
	}
	
	
}
