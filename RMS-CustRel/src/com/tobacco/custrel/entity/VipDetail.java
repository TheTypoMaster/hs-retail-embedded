package com.tobacco.custrel.entity;

import com.tobacco.custrel.provider.CustomerProvider;
import com.tobacco.custrel.provider.VipInfoProvider;

import android.net.Uri;

/**
 * @author Fatal1tyV
 * VIP具体数据实体类
 * 
 */
public class VipDetail {
	

	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ VipInfoProvider.CONTENT_URI + "/vipdetail");
	public static final String DEFAULT_SORT_ORDER = "modified DESC";

	public static final String _ID = "id";
	public static final String GENDER = "gender";
	public static final String BIRTHDAY = "birthday";
	public static final String CRED_CATA = "credCata";
	public static final String CRED_ID = "credId";
	public static final String HOMECITY = "homecity";
	public static final String EDU_DEGREE = "eduDegree";
	public static final String JOB_TITLE = "jobTitle";
	public static final String WORK_EXP = "workExp";
	public static final String PHONE = "phone";
	public static final String MAIL = "mail";
	public static final String ADDR = "addr";
	public static final String POSTCODE = "postcode";
	public static final String RES_1 = "res1";
	public static final String RES_2 = "res2";
	public static final String RES_3 = "res3";

}
