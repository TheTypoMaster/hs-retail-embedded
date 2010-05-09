package com.tobacco.pos.entity;

import java.util.Date;
import com.tobacco.pos.util.DateTool;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class BaseModel implements Parcelable{

	protected static String TAG = "BaseModule";
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

	 
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	 
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		Log.i(TAG, "writeToParcel()");
		dest.writeString(DateTool.formatDateToString(createDate));
		dest.writeString(comment);
	}
	
	public static final Parcelable.Creator<BaseModel> CREATOR = new Parcelable.Creator<BaseModel>() {
		
		public BaseModel createFromParcel(Parcel in) {
			Log.i(TAG, "createFromParcel()");
			return new BaseModel(in);
		}

		public BaseModel[] newArray(int size) {
			return new BaseModel[size];
		}
	};
	
	private BaseModel(Parcel in) {
		
		Log.i(TAG, "BaseModel()");
		createDate = DateTool.formatStringToDate(in.readString());
		comment = in.readString();

	}
	
}