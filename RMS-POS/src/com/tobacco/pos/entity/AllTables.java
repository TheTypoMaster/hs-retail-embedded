package com.tobacco.pos.entity;

import android.net.Uri;
import android.provider.BaseColumns;

public class AllTables {

	// 用户信息表
	public static final class UserInfo implements BaseColumns {
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.tobacco.contentProvider.UserInfoCPer");

		// 表的各个列
		public static final String userName = "userName";

		public static final String password = "password";

		public static final String permission = "permission";

		public static final String status = "status";
	}

	// 商品类别表
	public static final class GoodsKind implements BaseColumns {
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.tobacco.contentProvider.GoodsKindCPer");

		public static final String name = "name";

		public static final String parent = "parent";

		public static final String level = "level";

		public static final String comment = "comment";

	}

	// 单位表
	public static final class Unit implements BaseColumns {
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.tobacco.contentProvider.UnitCPer");

		public static final String name = "name";

	}
	
	// 商品表
	public static final class Goods implements BaseColumns {
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.tobacco.contentProvider.GoodsCPer");

		public static final String goodsCode = "goodsCode";

		public static final String goodsName = "goodsName";

		public static final String manufacturerId = "manufacturerId";

		public static final String goodsFormat = "goodsFormat";
		
		public static final String kindId = "kindId";

	}
	
	// 商品价格表
	public static final class GoodsPrice implements BaseColumns {
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.tobacco.contentProvider.GoodsPriceCPer");
		
		public static final String goodsId = "goodsId";

		public static final String unitId = "unitId";

		public static final String barcode = "barcode";

		public static final String inPrice = "inPrice";
		
		public static final String outPrice = "outPrice";

	}
	
	// 会员表
	public static final class VIPInfo implements BaseColumns {
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.tobacco.contentProvider.VIPInfoCPer");

		public static final String VIPNum = "VIPNum";

		public static final String VIPName = "VIPName";

		public static final String VIPDiscount = "VIPDiscount";

	}
	
	// 销售单表
	public static final class SalesBill implements BaseColumns {
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.tobacco.contentProvider.SalesBillCPer");

		public static final String sBillNum = "sBillNum";

		public static final String operId = "operId";

		public static final String time = "time";
		
		public static final String VIPId = "VIPId";

	}
	
	// 销售项表
	public static final class SalesItem implements BaseColumns {
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.tobacco.contentProvider.SalesItemCPer");

		public static final String salesBillId = "salesBillId";

		public static final String sGoodsNum = "sGoodsNum";

		public static final String barcode = "barcode";
		
		public static final String inPrice = "inPrice";
		
		public static final String outPrice = "outPrice";
	
	}
	
	// 进货单表
	public static final class PurchaseBill implements BaseColumns {
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.tobacco.contentProvider.PurchaseBillCPer");

		public static final String pBillNum = "pBillNum";

		public static final String operId = "operId";

		public static final String time = "time";

		public static final String comment = "comment";
	
	}
	// 进货项表
	public static final class PurchaseItem implements BaseColumns {
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.tobacco.contentProvider.PurchaseItemCPer");

		public static final String purchaseBillId = "purchaseBillId";

		public static final String pGoodsNum = "pGoodsNum";

		public static final String pPriceId = "pPriceId";

	}
	
	// 进货项表
	public static final class Manufacturer implements BaseColumns {
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.tobacco.contentProvider.ManufacturerCPer");

		public static final String mName = "mName";
	}

	public static class Consume implements BaseColumns{

		
		public static final String AUTHORITY = "com.tobacco.contentProvider.ConsumeCPer";
		/**
	    * The content:// style URL for this table
	    */
	    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/consumes");

	   /**
	    * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
	    */
	    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.consume";

	   /**
	    * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
	    */
	    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.consume";

	   /**
	    * The default sort order for this table
	    */
	    public static final String DEFAULT_SORT_ORDER = "modified DESC";

	   /**
	    * The number of the consume goods
	    * <P>Type: INTEGER</P>
	    */
	    public static final String NUMBER = "number";

	   /**
	    * The number of the operater
	    * <P>Type: INTEGER</P>
	    */
	    public static final String OPERATOR = "operator";
	    
	    /**
	     * The number of the goods
	     * <P>Type: INTEGER</P>
	     */
	    public static final String GOODS = "goods";

	   /**
	    * The timestamp for when the consume was created
	    * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
	    */
	    public static final String CREATED_DATE = "creat_day";
	    
	    /**
	     * The flag to identify the consume type.
	     * <P>Type: BOLLEAN</P>
	     */
	    public static final String FLAG = "flag";
	    
	    /**
	     * The comment to show the reason
	     * <P>Type: TEXT</P>
	     */
	    public static final String COMMENT = "comment";
	}
	
	public static class Complaint implements BaseColumns{
		
		public static final String AUTHORITY = "com.tobacco.contentProvider.ComplaintCPer";
		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/complaints");
		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.complaint";
		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
		 */
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.complaint";
		/**
		 * The id of the operator
		 * <p>TypE: INTEGER</>
		 */
		public static final String OPER_ID = "operId";
		/**
		 * The time complaint create;
		 * <p>TypE: TEXT</>
		 */
		public static final String CREATE_DATE = "createDay";
		/**
		 * The id of the VIP customer
		 * <p>TypE: INTEGER</>
		 */
		public static final String VIP_ID = "vipId";
		/**
		 * The id of the goods
		 * <p>TypE: INTEGER</>
		 */
		public static final String GOODS_ID = "goodsId";
		/**
		 * The reason for complaint
		 * <p>TypE: TEXT</>
		 */
		public static final String CONTENT = "content";
		/**
		 * The comment for this complaint
		 * <p>TypE: TEXT</>
		 */
		public static final String COMMENT = "comment";
		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "modified DESC";
		
	}
	
public static class Return implements BaseColumns{
		
		public static final String AUTHORITY = "com.tobacco.contentProvider.ReturnCPer";
		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/returns");
		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.return";
		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
		 */
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.return";
		/**
		 * The id of the operator
		 * <p>TypE: INTEGER</>
		 */
		public static final String OPER_ID = "operId";
		/**
		 * The time complaint create;
		 * <p>TypE: TEXT</>
		 */
		public static final String CREATE_DATE = "createDay";
		/**
		 * The id of the VIP customer
		 * <p>TypE: INTEGER</>
		 */
		public static final String VIP_ID = "vipId";
		/**
		 * The id of the goods
		 * <p>TypE: INTEGER</>
		 */
		public static final String GOODS_ID = "goodsId";	
		/**
		    * The number of the consume goods
		    * <P>Type: INTEGER</P>
		    */
		public static final String NUMBER = "number";
		/**
		 * The reason for complaint
		 * <p>TypE: TEXT</>
		 */
		public static final String CONTENT = "content";
		/**
		 * The comment for this complaint
		 * <p>TypE: TEXT</>
		 */
		public static final String COMMENT = "comment";
		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "modified DESC";
		
	}
}
