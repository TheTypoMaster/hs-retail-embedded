package com.tobacco.pos.Module;

import java.util.Calendar;
import java.util.Date;

import com.tobacco.pos.Module.AllTables.Consume;
import com.tobacco.pos.util.DateTool;

import android.content.ContentValues;

public class ConsumeModel extends BaseModel{
	
	/**
	 * the operator's name who deal with this consume goods.
	 */
	private String operator;
	
	/**
	 * the number of the consumes goods
	 */
	private int number;
	
	/**
	 * the name of the consume goods.	
	 */
	private String goodsName;
	
	/**
	 * the unit of the consume goods.
	 */
	private String unitName;
	
	/**
	 * the id of the record of the goods's price 
	 */
	private int goodsPriceId;
	
	/**
	 * the inPrice of the goods
	 */
	private double inPrice;
	
	public ConsumeModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ConsumeModel(int number, String goodsName,
			String unitName, int goodsPriceId, double inPrice,String comment) {
		super(comment);
		this.number = number;
		this.goodsName = goodsName;
		this.unitName = unitName;
		this.goodsPriceId = goodsPriceId;
		this.inPrice = inPrice;
	}

	public ConsumeModel(String operator, int number, String goodsName,
			String unitName, int goodsPriceId, double inPrice,String comment,Date createDate) {
		super(createDate,comment);
		this.operator = operator;
		this.number = number;
		this.goodsName = goodsName;
		this.unitName = unitName;
		this.goodsPriceId = goodsPriceId;
		this.inPrice = inPrice;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getGoodsPriceId() {
		return goodsPriceId;
	}

	public void setGoodsPriceId(int goodsPriceId) {
		this.goodsPriceId = goodsPriceId;
	}

	public double getInPrice() {
		return inPrice;
	}

	public void setInPrice(double inPrice) {
		this.inPrice = inPrice;
	}
	
	public ContentValues genContentValues(){
		Date today = Calendar.getInstance().getTime();
		String now = DateTool.formatDateToString(today);
		
		ContentValues values = new ContentValues();
		values.put(Consume.NUMBER,number);
		values.put(Consume.GOODS,goodsPriceId);
		values.put(Consume.CREATED_DATE,now);
//		values.put(Consume.OPERATOR,);
//		values.put(Consume.FLAG,);
		values.put(Consume.COMMENT,comment);
		return values;
	}
	
	

}
