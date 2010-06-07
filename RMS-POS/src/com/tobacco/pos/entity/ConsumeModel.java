package com.tobacco.pos.entity;

import java.util.Calendar;
import java.util.Date;

import com.tobacco.pos.entity.AllTables.Consume;
import com.tobacco.pos.util.DateTool;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ConsumeModel extends BaseModel{
	
	private static String TAG = "ConsumeModel";
	
	private int id;
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
	
	/**
	 * the type of the consume
	 */
	private String type;
	
	public ConsumeModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ConsumeModel(int number, int goodsPriceId, String comment, String type) {
		super(comment);
		this.number = number;
		this.goodsPriceId = goodsPriceId;
		this.type = type;
	}
	
	public ConsumeModel(int number, String goodsName,
			String unitName, int goodsPriceId, double inPrice, String comment, String type) {
		super(comment);
		this.number = number;
		this.goodsName = goodsName;
		this.unitName = unitName;
		this.goodsPriceId = goodsPriceId;
		this.inPrice = inPrice;
		this.type = type;
	}

	public ConsumeModel(int id, String operator, int number, String goodsName,
			String unitName, int goodsPriceId, String comment,String createDate, String type) {
		super(createDate,comment);
		this.id = id;
		this.operator = operator;
		this.number = number;
		this.goodsName = goodsName;
		this.unitName = unitName;
		this.goodsPriceId = goodsPriceId;
//		this.inPrice = inPrice;
		this.type = type;
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
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ContentValues genContentValues(){
		Date today = Calendar.getInstance().getTime();
		String now = DateTool.formatDateToString(today);
		
		this.createDate = now;
		
		ContentValues values = new ContentValues();
		values.put(Consume.NUMBER,number);
		values.put(Consume.GOODS,goodsPriceId);
		values.put(Consume.CREATED_DATE,now);
		values.put(Consume.OPERATOR,operator);
		values.put(Consume.FLAG,(type.equals("溢")?1:0));
		values.put(Consume.COMMENT,comment);
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
		dest.writeInt(number);
		dest.writeInt(goodsPriceId);
		dest.writeString(createDate);
		dest.writeString(operator);
		dest.writeInt((type.equals("溢")?1:0));
		dest.writeString(comment);
	}
	
	public static final Parcelable.Creator<ConsumeModel> CREATOR = new Parcelable.Creator<ConsumeModel>() {
		
		public ConsumeModel createFromParcel(Parcel in) {
			Log.i(TAG, "createFromParcel()");
			return new ConsumeModel(in);
		}

		public ConsumeModel[] newArray(int size) {
			return new ConsumeModel[size];
		}
	};
	
	private ConsumeModel(Parcel in) {
		Log.i(TAG, "ConsumeModel()");
		
		number = in.readInt();
		goodsPriceId = in.readInt();
		createDate = in.readString();
		operator = in.readString();
		type = (in.readInt()==1)?"溢":"耗";
		comment = in.readString();
	}

}