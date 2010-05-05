package com.tobacco.pos.entity;

import java.util.Date;

public class BaseModel {

	/**
	 * the create time of this object.
	 */
	protected Date createDate;
	
	/**
	 * the comment for this object.
	 */
	protected String comment;	
	
	public BaseModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BaseModel(String comment) {
		super();
		this.comment = comment;
	}

	public BaseModel(Date createDate, String comment) {
		super();
		this.createDate = createDate;
		this.comment = comment;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
