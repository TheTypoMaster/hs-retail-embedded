package com.tobacco.pos.entity;

import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.tobacco.pos.entity.AllTables.Return;
import com.tobacco.pos.util.DateTool;

public class ReturnModel extends BaseModel {

	private static String TAG = "ReturnModel";
	
	/**
	 * the id of this object
	 */
	private int id;

	/**
	 * the operator who deal with this return operation.
	 */
	private String operator;

	/**
	 * the name of the customer who return the goods. maybe a vip or not
	 */
	private String customer;

	/**
	 * the id of the customer who return the goods. maybe a vip or not
	 */
	private int customerId;
	
	/**
	 * the unit of the returned goods.
	 */
	private String unit;

	/**
	 * the id of the rturned goods.
	 */
	private int goodsPriceId;

	/**
	 * the name of the rturned goods.
	 */
	private String goodsName;
	
	/**
	 * the number of the returned goods.
	 */
	private int number;

	/**
	 * the inPrice of the returned goods.
	 */
	private double inPrice;

	/**
	 * the cigarette flag.
	 */
	private boolean isCigarette;

	public ReturnModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReturnModel(String unit, int goodsPriceId, String goodsName, String comment,
			int number,double inPrice, boolean isCigarette) {
		super(comment);
		this.unit = unit;
		this.goodsPriceId = goodsPriceId;
		this.goodsName = goodsName;
		this.number = number;
		this.inPrice = inPrice;
		this.isCigarette = isCigarette;
	}

	public ReturnModel(int id, String operator, String customer, int goodsPriceId,
			String goodsName, String createDate, String comment, int number) {
		super(createDate,comment);
		this.id = id;
		this.operator = operator;
		this.customer = customer;
		this.goodsPriceId = goodsPriceId;
		this.goodsName = goodsName;
		this.number = number;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getGoodsPriceId() {
		return goodsPriceId;
	}

	public void setGoodsPriceId(int goodsPriceId) {
		this.goodsPriceId = goodsPriceId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	public double getInPrice() {
		return inPrice;
	}

	public void setInPrice(double inPrice) {
		this.inPrice = inPrice;
	}

	public boolean isCigarette() {
		return isCigarette;
	}

	public void setCigarette(boolean isCigarette) {
		this.isCigarette = isCigarette;
	}
	
	public ContentValues genContentValues(){
		Date today = Calendar.getInstance().getTime();
		String now = DateTool.formatDateToString(today);
		
		this.createDate = now;
		
		ContentValues values = new ContentValues();
		values.put(Return.COMMENT,comment);
		values.put(Return.CREATE_DATE,now);
		values.put(Return.GOODS_ID,goodsPriceId);
		values.put(Return.NUMBER,number);
		values.put(Return.OPERATOR,operator);
		values.put(Return.VIP_ID,customerId);
		return values;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		Log.i(TAG, "writeToParcel()");
		dest.writeString(comment);
		dest.writeString(createDate);
		dest.writeInt(goodsPriceId);
		dest.writeInt(number);
		dest.writeString(operator);
		dest.writeInt(customerId);
	}
	
	public static final Parcelable.Creator<ReturnModel> CREATOR = new Parcelable.Creator<ReturnModel>() {
		
		public ReturnModel createFromParcel(Parcel in) {
			Log.i(TAG, "createFromParcel()");
			return new ReturnModel(in);
		}

		public ReturnModel[] newArray(int size) {
			return new ReturnModel[size];
		}
	};
	
	private ReturnModel(Parcel in) {
		Log.i(TAG, "ReturnModel()");
		
		comment = in.readString();
		createDate = in.readString();
		goodsPriceId = in.readInt();
		number = in.readInt();
		operator = in.readString();
		customerId = in.readInt();
	}
}
