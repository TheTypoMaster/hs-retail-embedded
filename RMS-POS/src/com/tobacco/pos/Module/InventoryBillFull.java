package com.tobacco.pos.Module;

import android.net.Uri;
import android.provider.BaseColumns;

import com.tobacco.pos.Module.AllTables.InventoryBill;
import com.tobacco.pos.Module.AllTables.UserInfo;

public class InventoryBillFull implements BaseColumns{
	
	public static final String AUTHORITY = "com.tobacco.contentProvider.InventoryBillCPer";
	/**
	 * The content:// style URL for this table
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/inventoryBills_full");
	public static final String TABLES = "InventoryBill,UserInfo";
	public static final String append1 = "InventoryBill."+InventoryBill.OPER_ID+" = UserInfo." + UserInfo._ID;
	public static final String APPEND_WHERE = append1;
	
	/**
	 * The name of the operator
	 * <p>TypE: INTEGER</>
	 */
	public static final String OPER_NAME = "UserInfo."+UserInfo.userName;
	/**
	 * The id of the inventory bill.
	 * <p>TypE: INTEGER</>
	 */
	public static final String IBILL_ID = "InventoryBill."+InventoryBill._ID;
	/**
	 * The number of the inventory bill.
	 * <p>TypE: TEXT</>
	 */
	public static final String IBILL_NUM = "InventoryBill."+InventoryBill.IBILL_NUM;	
	/**
	 * Flag identify whether the inventory bill finished.
	 * <P>Type: BOOLEAN</P>
	 */
	public static final String FINISHED = "InventoryBill."+InventoryBill.FINISHED;	
	/**
	 * Inventory result.
	 * <p>Type: DOUBLE</> 
	 */
	public static final String RESULT = "InventoryBill."+InventoryBill.RESULT;
	/**
	 * Inventory bill's create time.
	 * <p>Type: TEXT</>
	 */
	public static final String CREATE_DATE = "InventoryBill."+InventoryBill.CREATE_DATE;

	/**
	 * Projection for the selection of InventoryBill.
	 * <p>Type: String[]</>
	 */
	public static final String[] PROJECTION = new String[]{IBILL_ID,IBILL_NUM,FINISHED,RESULT,CREATE_DATE,OPER_NAME};
}
