package com.tobacco.pos.entity;

import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.tobacco.pos.entity.AllTables.Complaint;
import com.tobacco.pos.util.DateTool;

public class ComplaintModel extends BaseModel {

	private static String TAG = "ComplaintModel";
	/**
	 * the id of this object
	 */
	private int id;
	
	/**
	 * the operator who deal with this complaint operation.
	 */
	private String operator;
	
	/**
	 * the name of the customer who complaint the goods. maybe a vip or not
	 */
	private String customer;
	
	/**
	 * the id of the customer who complaint the goods. maybe a vip or not
	 */
	private int customerId;
	
	/**
	 * the id of the complainted goods.
	 */
	private int goodsPriceId;
	
	/**
	 * the name of the complainted goods.
	 */
	private String goodsName;
	
	/**
	 * the cigarette flag.
	 */
	private boolean isCigarette;


	public ComplaintModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ComplaintModel(int customerId, int goodsPriceId, boolean isCigarette, String comment) {
		super(comment);
		this.customerId = customerId;
		this.goodsPriceId = goodsPriceId;
		this.isCigarette = isCigarette;
	}

	public ComplaintModel(int id, String operator, String customer, int goodsPriceId, 
			String goodsName, String createDate, String comment) {
		super(createDate,comment);
		this.id = id;
		this.operator = operator;
		this.customer = customer;
		this.goodsPriceId = goodsPriceId;
		this.goodsName = goodsName;

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
		values.put(Complaint.COMMENT,comment);
		values.put(Complaint.CREATE_DATE,now);
		values.put(Complaint.GOODS_ID,goodsPriceId);
		values.put(Complaint.OPERATOR,operator);
		values.put(Complaint.VIP_ID,customerId);
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
		dest.writeString(operator);
		dest.writeInt(customerId);
	}
	
	public static final Parcelable.Creator<ComplaintModel> CREATOR = new Parcelable.Creator<ComplaintModel>() {
		
		public ComplaintModel createFromParcel(Parcel in) {
			Log.i(TAG, "createFromParcel()");
			return new ComplaintModel(in);
		}

		public ComplaintModel[] newArray(int size) {
			return new ComplaintModel[size];
		}
	};
	
	private ComplaintModel(Parcel in) {
		Log.i(TAG, "ComplaintModel()");
		
		comment = in.readString();
		createDate = in.readString();
		goodsPriceId = in.readInt();	
		operator = in.readString();
		customerId = in.readInt();
		
	}
	
}
