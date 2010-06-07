package com.tobacco.pos.entity;

import android.net.Uri;
import android.provider.BaseColumns;

import com.tobacco.pos.entity.AllTables;
import com.tobacco.pos.entity.AllTables.Complaint;
import com.tobacco.pos.entity.AllTables.Consume;
import com.tobacco.pos.entity.AllTables.Goods;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.Unit;
import com.tobacco.pos.entity.AllTables.UserInfo;
import com.tobacco.pos.entity.AllTables.VIPInfo;

public class ComplaintFull implements BaseColumns{
	
	public static final String AUTHORITY = "com.tobacco.contentProvider.ComplaintCPer";
	/**
	 * The content:// style URL for this table
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/complaints_full");
	public static final String TABLES = "Complaint,VIPInfo,Goods,GoodsPrice";//UserInfo,
	
	public static final String append1 = "Complaint."+Complaint.GOODS_ID+" = GoodsPrice." + GoodsPrice._ID;
	public static final String append2 = "GoodsPrice."+GoodsPrice.goodsId+" =Goods."+Goods._ID;
	public static final String append4 = "Complaint."+Complaint.VIP_ID+" =VIPInfo."+VIPInfo._ID;
//	public static final String append5 = "Complaint."+Complaint.OPER_ID+" =UserInfo."+UserInfo._ID;
	
	public static final String APPEND_WHERE = append1+" AND "+append2+" AND "+append4;//+" AND "+append5

//	/**
//	 * The id of the operator
//	 * <p>TypE: INTEGER</>
//	 */
//	public static final String OPER_ID = "operId";
	/**
	 * The id of the operator
	 * <p>TypE: INTEGER</>
	 */
	public static final String OPER_NAME = "Complaint."+Complaint.OPERATOR;
//	
//	/**
//	 * The id of the VIP customer
//	 * <p>TypE: INTEGER</>
//	 */
//	public static final String VIP_ID = "vipId";
	/**
	 * The id of the VIP customer
	 * <p>TypE: INTEGER</>
	 */
	public static final String VIP_NAME = "VIPInfo."+VIPInfo.VIPName;
	/**
	 * The id of the goodsPrice
	 * <p>TypE: INTEGER</>
	 */
	public static final String GOODS_PRICE_ID = "Complaint."+Complaint.GOODS_ID;
	
	public static final String IS_CIGARETTE = "GoodsPrice."+GoodsPrice.isCigarette;
	/**
	 * The id of the goods
	 * <p>TypE: INTEGER</>
	 */
	public static final String GOODS_NAME = "Goods."+Goods.goodsName;
	
	public static final String ID = "Complaint."+Complaint._ID;
	/**
	 * The time complaint create;
	 * <p>TypE: TEXT</>
	 */
	public static final String CREATE_DATE = "Complaint."+Complaint.CREATE_DATE;
	/**
	 * The reason for complaint
	 * <p>TypE: TEXT</>
	 */
	public static final String COMMENT = "Complaint."+Complaint.COMMENT;
	/**
	 * The default sort order for this table
	 */
	public static final String DEFAULT_SORT_ORDER = "modified DESC";
	
	public static final String[] PROJECTION = new String[]{ID, GOODS_NAME,VIP_NAME,CREATE_DATE,OPER_NAME,COMMENT,GOODS_PRICE_ID,IS_CIGARETTE};
}
