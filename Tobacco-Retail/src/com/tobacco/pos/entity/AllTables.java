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

		public static final String sPriceId = "sPriceId";
	
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

}
