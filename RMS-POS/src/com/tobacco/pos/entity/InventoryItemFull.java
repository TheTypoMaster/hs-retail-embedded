package com.tobacco.pos.entity;

import android.net.Uri;
import android.provider.BaseColumns;

import com.tobacco.pos.entity.AllTables.Goods;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.InventoryBill;
import com.tobacco.pos.entity.AllTables.InventoryItem;
import com.tobacco.pos.entity.AllTables.Unit;

public class InventoryItemFull implements BaseColumns{
	
	public static final String AUTHORITY = "com.tobacco.contentProvider.InventoryItemCPer";
	/**
	 * The content:// style URL for this table
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/inventoryItems_full");
	public static final String TABLES = "InventoryItem,Unit,Goods,GoodsPrice";
	
	public static final String append1 = "InventoryItem."+InventoryItem.GOODS_PRICE_ID+" = GoodsPrice." + GoodsPrice._ID;
	public static final String append2 = "GoodsPrice."+GoodsPrice.goodsId+" =Goods."+Goods._ID;
	public static final String append3 = "GoodsPrice."+GoodsPrice.unitId+" =Unit."+Unit._ID;
	
	public static final String APPEND_WHERE = append1+" AND "+append2+" AND "+append3;
	
	/**
	 * The name of the goods
	 * <p>TypE: INTEGER</>
	 */
	public static final String GOODS_NAME = "Goods."+Goods.goodsName;
	/**
	 * The id of the goodsPrice
	 * <p>TypE: INTEGER</>
	 */
	public static final String GOODS_PRICE_ID = "GoodsPrice."+GoodsPrice._ID;
	/**
	 * The inPrice of the goods
	 * <p>TypE: DOUBLE</>
	 */
	public static final String GOODS_INPRICE = "GoodsPrice."+GoodsPrice.inPrice;
	/**
	 * The name of the Unit 
	 * <p>TypE: TEXT</>
	 */
	public static final String UNIT_NAME = "Unit."+Unit.name;
	/**
	 * Goods' expect count in the system
	 * <p>TypE: INTEGER</>
	 */
	public static final String EXPECT_NUM = "InventoryItem."+InventoryItem.EXPECT_NUM;	
	/**
	 * Goods' real count in the stock
	 * <P>Type: INTEGER</P>
	 */
	public static final String REAL_NUM = "InventoryItem."+InventoryItem.REAL_NUM;	
	/**
	 * Goods' inventory result
	 * <p>Type: DOUBLE</> 
	 */
	public static final String ITEM_RESULT = "InventoryItem."+InventoryItem.ITEM_RESULT;
	/**
	 * Inventory bill's id
	 * <p>Type: INTEGER</>
	 */
	public static final String IBILL_ID = "InventoryItem."+InventoryItem.IBILL_ID;

}
