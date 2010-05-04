package com.tobacco.onlinesrv.entities;

import com.tobacco.onlinesrv.provider.PreOrderProvider;

import android.net.Uri;

public class PreOrder {
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PreOrderProvider.CONTENT_URI + "/preorder");
	public static final String KEY_ID = "id";
	public static final String KEY_PREORDER_ID = "orderid";
	public static final String KEY_BRANDCODE = "brandcode";
	public static final String KEY_BRANDCOUNT = "brandcount";
	public static final String KEY_PREDATE = "date";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_VIPID = "vipid";
	public static final String KEY_FORMAT = "format";
	public static final String KEY_AMOUNT = "amount";
	public static final String KEY_AGENTCYID = "agencyid";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_STATUS = "status";
}
