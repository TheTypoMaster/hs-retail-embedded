package com.tobacco.pos.entity;

import android.net.Uri;
import android.provider.BaseColumns;

import com.tobacco.pos.entity.AllTables.Complaint;
import com.tobacco.pos.entity.AllTables.Consume;
import com.tobacco.pos.entity.AllTables.Goods;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.Return;
import com.tobacco.pos.entity.AllTables.Unit;
import com.tobacco.pos.entity.AllTables.UserInfo;
import com.tobacco.pos.entity.AllTables.VIPInfo;

public class ReturnFull implements BaseColumns{
	
	public static final String AUTHORITY = "com.tobacco.contentProvider.ReturnCPer";
	/**
	 * The content:// style URL for this table
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/returns_full");
	public static final String TABLES = "Return,VIPInfo,Goods,GoodsPrice";//UserInfo,
	
	public static final String append1 = "Return."+Return.GOODS_ID+" = GoodsPrice." + GoodsPrice._ID;
	public static final String append2 = "GoodsPrice."+GoodsPrice.goodsId+" =Goods."+Goods._ID;
//	public static final String append3 = "GoodsPrice."+GoodsPrice.unitId+" =Unit."+Unit._ID;
	public static final String append4 = "Return."+Return.VIP_ID+" =VIPInfo."+VIPInfo._ID;
//	public static final String append5 = "Return."+Return.OPER_ID+" =UserInfo."+UserInfo._ID;
	
	public static final String APPEND_WHERE = append1+" AND "+append2+" AND "+append4;//+" AND "+append5

	/**
	 * The id of the operator
	 * <p>TypE: INTEGER</>
	 */
	public static final String OPER_NAME = "Return."+Return.OPERATOR;
	/**
	 * The id of the VIP customer
	 * <p>TypE: INTEGER</>
	 */
	public static final String VIP_NAME = "VIPInfo."+VIPInfo.VIPName;
	/**
	 * The id of the goodsPrice
	 * <p>TypE: INTEGER</>
	 */
	public static final String GOODS_PRICE_ID = "GoodsPrice."+GoodsPrice._ID;
	/**
	 * The id of the goods
	 * <p>TypE: INTEGER</>
	 */
	public static final String GOODS_NAME = "Goods."+Goods.goodsName;
	/**
	 * The name of the Unit 
	 * <p>TypE: TEXT</>
	 */
//	public static final String UNIT_NAME = "Unit."+Unit.name;
	/**
	 * The time Return create;
	 * <p>TypE: TEXT</>
	 */
	public static final String CREATE_DATE = "Return."+Return.CREATE_DATE;
	/**
	 * The reason for Return
	 * <p>TypE: TEXT</>
	 */
	public static final String COMMENT = "Return."+Return.COMMENT;
	/**
	 * The count for Return
	 * <p>TypE: TEXT</>
	 */
	public static final String NUMBER = "Return."+Return.NUMBER;
	/**
	 * The default sort order for this table
	 */
	public static final String DEFAULT_SORT_ORDER = "modified DESC";
	
	public static final String[] PROJECTION = new String[]{GOODS_NAME,VIP_NAME,CREATE_DATE,OPER_NAME,COMMENT,GOODS_PRICE_ID,NUMBER};
}
