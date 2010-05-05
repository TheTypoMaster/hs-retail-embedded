package com.tobacco.pos.Module;

import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;

import com.tobacco.pos.Module.AllTables.Complaint;
import com.tobacco.pos.util.DateTool;

public class ComplaintModel extends BaseModel {

	private int id;
	
	private String operator;
	
	private String customer;
	
	private int customerId;
	
	private int goodsPriceId;
	
	private String goodsName;
	
	private Date createDate;
	
	private String content;

	public ComplaintModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ComplaintModel(int customerId, int goodsPriceId, String content) {
		super();
		this.customerId = customerId;
		this.goodsPriceId = goodsPriceId;
		this.content = content;
	}

	public ComplaintModel(String operator, String customer, int goodsPriceId,
			String goodsName, Date createDate, String content) {
		super();
		this.operator = operator;
		this.customer = customer;
		this.goodsPriceId = goodsPriceId;
		this.goodsName = goodsName;
		this.createDate = createDate;
		this.content = content;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public ContentValues genContentValues(){
		Date today = Calendar.getInstance().getTime();
		String now = DateTool.formatDateToString(today);
		
		ContentValues values = new ContentValues();
//		values.put(Complaint.COMMENT,number);
		values.put(Complaint.CONTENT,content);
		values.put(Complaint.CREATE_DATE,now);
		values.put(Complaint.GOODS_ID,goodsPriceId);
//		values.put(Complaint.OPER_ID,);
		values.put(Complaint.VIP_ID,customerId);
		return values;
	}
	
}
