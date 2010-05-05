package com.tobacco.pos.Module;

import android.net.Uri;
import android.provider.BaseColumns;

import com.tobacco.pos.Module.AllTables;
import com.tobacco.pos.Module.AllTables.Complaint;
import com.tobacco.pos.Module.AllTables.Goods;
import com.tobacco.pos.Module.AllTables.GoodsPrice;
import com.tobacco.pos.Module.AllTables.Unit;
import com.tobacco.pos.Module.AllTables.UserInfo;
import com.tobacco.pos.Module.AllTables.VIPInfo;

public class ComplaintFull implements BaseColumns{
	
	public static final String AUTHORITY = "com.tobacco.contentProvider.ComplaintCPer";
	/**
	 * The content:// style URL for this table
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/complaints_full");
	public static final String TABLES = "Complaint,UserInfo,VIPInfo,Goods,GoodsPrice";
	
	public static final String append1 = "Complaint."+Complaint.GOODS_ID+" = GoodsPrice." + GoodsPrice._ID;
	public static final String append2 = "GoodsPrice."+GoodsPrice.goodsId+" =Goods."+Goods._ID;
	public static final String append4 = "Complaint."+Complaint.VIP_ID+" =VIPInfo."+VIPInfo._ID;
	public static final String append5 = "Complaint."+Complaint.OPER_ID+" =UserInfo."+UserInfo._ID;
	
	public static final String APPEND_WHERE = append1+" AND "+append2+" AND "+append4+" AND "+append5;

//	/**
//	 * The id of the operator
//	 * <p>TypE: INTEGER</>
//	 */
//	public static final String OPER_ID = "operId";
	/**
	 * The id of the operator
	 * <p>TypE: INTEGER</>
	 */
	public static final String OPER_NAME = "UserInfo."+UserInfo.userName;
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
	public static final String CREATE_DATE = "Complaint."+Complaint.CREATE_DATE;
	/**
	 * The reason for complaint
	 * <p>TypE: TEXT</>
	 */
	public static final String CONTENT = "Complaint."+Complaint.CONTENT;
	/**
	 * The default sort order for this table
	 */
	public static final String DEFAULT_SORT_ORDER = "modified DESC";
	
	public static final String[] PROJECTION = new String[]{GOODS_NAME,VIP_NAME,CREATE_DATE,OPER_NAME,CONTENT,GOODS_PRICE_ID};
}
