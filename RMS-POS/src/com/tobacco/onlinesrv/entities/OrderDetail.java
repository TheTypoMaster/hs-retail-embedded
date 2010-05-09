package com.tobacco.onlinesrv.entities;

import com.tobacco.onlinesrv.provider.OrderDetailProvider;
import com.tobacco.onlinesrv.provider.OrderProvider;
import android.net.Uri;

public class OrderDetail {
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ OrderDetailProvider.CONTENT_URI + "/orderdetail");
	public static final String KEY_ID = "id";
	public static final String KEY_PREORDER_ID = "preorderid";
	public static final String KEY_ORDER_ID = "orderid";
	public static final String KEY_BRANDCODE = "brandcode";
	public static final String KEY_BRANDCOUNT = "brandcount";
	public static final String KEY_FORMAT = "format";
	public static final String KEY_PRICE = "price";
	public static final String KEY_AMOUNT = "amount";
}
