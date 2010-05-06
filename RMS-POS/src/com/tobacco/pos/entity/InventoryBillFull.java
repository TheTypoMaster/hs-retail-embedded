package com.tobacco.pos.entity;

import android.net.Uri;
import android.provider.BaseColumns;

import com.tobacco.pos.entity.AllTables.InventoryBill;
import com.tobacco.pos.entity.AllTables.UserInfo;

public class InventoryBillFull implements BaseColumns{
	
	public static final String AUTHORITY = "com.tobacco.contentProvider.InventoryBillCPer";
	/**
	 * The content:// style URL for this table
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/inventoryBills_full");
	public static final String TABLES = "InventoryBill";//,UserInfo
//	public static final String append1 = "InventoryBill."+InventoryBill.OPERATOR+" = UserInfo." + UserInfo._ID;
	public static final String APPEND_WHERE = null;
	
	/**
	 * The name of the operator
	 * <p>TypE: INTEGER</>
	 */
	public static final String OPER_NAME = InventoryBill.OPERATOR; //"InventoryBill."+
	/**
	 * The id of the inventory bill.
	 * <p>TypE: INTEGER</>
	 */
	public static final String IBILL_ID = InventoryBill._ID;
	/**
	 * The number of the inventory bill.
	 * <p>TypE: TEXT</>
	 */
	public static final String IBILL_NUM = InventoryBill.IBILL_NUM;	
	/**
	 * Flag identify whether the inventory bill finished.
	 * <P>Type: BOOLEAN</P>
	 */
	public static final String FINISHED = InventoryBill.FINISHED;	
	/**
	 * Inventory result.
	 * <p>Type: DOUBLE</> 
	 */
	public static final String RESULT = InventoryBill.RESULT;
	/**
	 * Inventory bill's create time.
	 * <p>Type: TEXT</>
	 */
	public static final String CREATE_DATE = InventoryBill.CREATE_DATE;

	/**
	 * Projection for the selection of InventoryBill.
	 * <p>Type: String[]</>
	 */
	public static final String[] PROJECTION = new String[]{IBILL_ID,IBILL_NUM,FINISHED,RESULT,CREATE_DATE,OPER_NAME};
}
