package com.tobacco.pos.entity;

import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;

import com.tobacco.pos.entity.AllTables.InventoryBill;
import com.tobacco.pos.util.DateTool;

public class InventoryBillModel extends BaseModel {

	private String operName;
	
	private int iBillId;
	
	private String iBillNum;
	
	private boolean finished;
	
	private double result;
	
	private Date createDate;

	public InventoryBillModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InventoryBillModel(boolean finished, double result) {
		super();
		this.finished = finished;
		this.result = result;
	}

	public InventoryBillModel(String operName, int iBillId, String iBillNum,
			boolean finished, double result, Date createDate) {
		super();
		this.operName = operName;
		this.iBillId = iBillId;
		this.iBillNum = iBillNum;
		this.finished = finished;
		this.result = result;
		this.createDate = createDate;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public int getiBillId() {
		return iBillId;
	}

	public void setiBillId(int iBillId) {
		this.iBillId = iBillId;
	}

	public String getiBillNum() {
		return iBillNum;
	}

	public void setiBillNum(String iBillNum) {
		this.iBillNum = iBillNum;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public ContentValues genContentValues(){
		Date today = Calendar.getInstance().getTime();
		String now = DateTool.formatDateToString(today);
		
		ContentValues values = new ContentValues();
		values.put(InventoryBill.IBILL_NUM, iBillNum);
//		values.put(InventoryBill.COMMENT, comment);
		values.put(InventoryBill.CREATE_DATE, now);
		values.put(InventoryBill.FINISHED, finished);
//		values.put(InventoryBill.OPER_ID, value);
		values.put(InventoryBill.RESULT, result);
		
		return values;
	}
}
