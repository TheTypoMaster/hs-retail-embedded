package com.tobacco.pos.entity;

import android.net.Uri;
import android.provider.BaseColumns;

import com.tobacco.pos.entity.AllTables.Consume;
import com.tobacco.pos.entity.AllTables.Goods;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.Unit;
import com.tobacco.pos.entity.AllTables.UserInfo;

public class ConsumeFull implements BaseColumns {
	public static final String AUTHORITY = "com.tobacco.contentProvider.ConsumeCPer";
	/**
	 * The content:// style URL for this table
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/consumes_full");
	public static final String TABLES = "Consume,Unit,Goods,GoodsPrice"; //UserInfo,
	
	public static final String append1 = "Consume."+Consume.GOODS+" = GoodsPrice." + GoodsPrice._ID;
	public static final String append2 = "GoodsPrice."+GoodsPrice.goodsId+" =Goods."+Goods._ID;
	public static final String append3 = "GoodsPrice."+GoodsPrice.unitId+" =Unit."+Unit._ID;
//	public static final String append4 = "Consume."+Consume.OPERATOR+" =UserInfo."+UserInfo._ID;
	
	public static final String APPEND_WHERE = append1+" AND "+append2+" AND "+append3; //+" AND "+append4
	
	/**
	 * The id of the operator
	 * <p>TypE: INTEGER</>
	 */
	public static final String OPER_NAME = "Consume."+Consume.OPERATOR;
	/**
	 * The name of the Unit 
	 * <p>TypE: TEXT</>
	 */
	public static final String UNIT_NAME = "Unit."+Unit.name;
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
	 * The time complaint create;
	 * <p>TypE: TEXT</>
	 */
	public static final String CREATE_DATE = "Consume."+Consume.CREATED_DATE;
	/**
	 * The reason for complaint
	 * <p>TypE: TEXT</>
	 */
	public static final String COMMENT = "Consume."+Consume.COMMENT;
	/**
	 * The count for Consume
	 * <p>TypE: TEXT</>
	 */
	public static final String NUMBER = "Consume."+Consume.NUMBER;
	/**
	 * The type for Consume
	 * <p>TypE: TEXT</>
	 */
	public static final String FLAG = "Consume."+Consume.FLAG;
	/**
	 * The default sort order for this table
	 */
	public static final String DEFAULT_SORT_ORDER = "modified DESC";
	
	public static final String[] PROJECTION = new String[]{GOODS_NAME,UNIT_NAME,CREATE_DATE,OPER_NAME,COMMENT,GOODS_PRICE_ID,NUMBER,FLAG};
}
