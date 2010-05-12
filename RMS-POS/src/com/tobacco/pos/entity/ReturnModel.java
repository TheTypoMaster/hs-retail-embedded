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
	
	private int id;
	
	private String operator;
	
	private String customer;
	
	private int customerId;
	
	private String unit;
	
	private int goodsPriceId;
	
	private String goodsName;
	
	private Date createDate;
	
	private String comment;
	
	private int number;

	private double inPrice;

	public ReturnModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReturnModel(String unit, int goodsPriceId, double inPrice, String goodsName, String comment,
			int number) {
		super(comment);
		this.unit = unit;
		this.goodsPriceId = goodsPriceId;
		this.goodsName = goodsName;
//		this.content = content;
		this.number = number;
		this.inPrice = inPrice;
	}

	public ReturnModel(String operator, String customer, int goodsPriceId,
			String goodsName, Date createDate, String comment, int number,double inPrice) {
		super(comment);
		this.operator = operator;
		this.customer = customer;
		this.goodsPriceId = goodsPriceId;
		this.goodsName = goodsName;
		this.createDate = createDate;
//		this.content = content;
		this.number = number;
		this.inPrice = inPrice;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

//	public String getContent() {
//		return content;
//	}
//
//	public void setContent(String content) {
//		this.content = content;
//	}

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

	public ContentValues genContentValues(){
		Date today = Calendar.getInstance().getTime();
		String now = DateTool.formatDateToString(today);
		
		ContentValues values = new ContentValues();
//		values.put(Return.COMMENT,number);
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
		dest.writeString(DateTool.formatDateToString(createDate));
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
		createDate = DateTool.formatStringToDate(in.readString());
		goodsPriceId = in.readInt();
		number = in.readInt();
		operator = in.readString();
		customerId = in.readInt();
	}
}
