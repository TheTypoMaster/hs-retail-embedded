package com.tobacco.onlinesrv.util;

import com.tobacco.onlinesrv.entities.OrderDetail;

public class FieldSupport {
	public static final String KEY_ID = "id";
	public static final String KEY_ORDER_ID = "orderid";
	public static final String KEY_BRANDCODE = "brandcode";
	public static final String KEY_BRANDCOUNT = "brandcount";
	public static final String KEY_PRICE = "price";
	public static final String KEY_DATE = "date";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_VIPID = "vipid";
	public static final String KEY_FORMAT = "format";
	public static final String KEY_AMOUNT = "amount";
	public static final String KEY_AGENTCYID = "agencyid";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_STATUS = "status";
	public static final String KEY_RECIEVE = "recieve";

	public static String[] brandType;
	public static String[] packetPrice;
	public static String[] itemPrice;
	public static String format[] = { "条", "包" };
	public static String type[] = { "预订单", "订单" };
	public static String orderType[] = { "预订单", "订单" };
	public static String queryType[] = { "单号" };

	public static String fromForOrder[] = new String[] { "count", KEY_ORDER_ID,
			KEY_USERNAME, KEY_DATE, "statusName", KEY_AGENTCYID, KEY_AMOUNT,
			KEY_DESCRIPTION, "recieveName" };

	public static String fromForOrderDetail[] = new String[] {
			OrderDetail.KEY_BRANDCODE, OrderDetail.KEY_BRANDCOUNT,
			OrderDetail.KEY_FORMAT, OrderDetail.KEY_PRICE,
			OrderDetail.KEY_AMOUNT };

	public static String[] KEY_FIELDS = { OrderDetail.KEY_ID,
			OrderDetail.KEY_PREORDER_ID, OrderDetail.KEY_ORDER_ID,
			OrderDetail.KEY_BRANDCODE, OrderDetail.KEY_BRANDCOUNT,
			OrderDetail.KEY_FORMAT, OrderDetail.KEY_PRICE,
			OrderDetail.KEY_AMOUNT };
}
