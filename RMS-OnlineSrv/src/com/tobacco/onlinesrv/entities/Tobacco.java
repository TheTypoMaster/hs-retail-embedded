package com.tobacco.onlinesrv.entities;

import com.tobacco.onlinesrv.provider.TobaccoProvider;

import android.net.Uri;

public class Tobacco {
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ TobaccoProvider.CONTENT_URI + "/tobacco");
	public static final String KEY_ID = "id";
	public static final String KEY_NAME = "name";
	public static final String KEY_PACKET_PRICE = "packetprice";
	public static final String KEY_ITEM_PRICE = "itemprice";
	public static final String KEY_PACKET_PRICE_SALE = "packetsaleprice";
	public static final String KEY_ITEM_PRICE_SALE = "itemsaleprice";
	public static final String KEY_MANUFACTORY = "manufactory";
}
