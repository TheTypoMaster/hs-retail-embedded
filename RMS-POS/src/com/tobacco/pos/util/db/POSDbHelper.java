package com.tobacco.pos.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tobacco.pos.entity.AllTables.Complaint;
import com.tobacco.pos.entity.AllTables.Consume;
import com.tobacco.pos.entity.AllTables.InventoryBill;
import com.tobacco.pos.entity.AllTables.InventoryItem;
import com.tobacco.pos.entity.AllTables.Return;

public class POSDbHelper extends SQLiteOpenHelper {

	private static final String TAG = "POSDbHelper";

	private static final String DATABASE_NAME = "AllTables.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String CONSUME_TNAME = "CONSUME";
	public static final String COMPLAINT_TNAME = "COMPLAINT";
	public static final String RETURN_TNAME = "RETURN";
	public static final String INVENTORY_BILL_TNAME = "INVENTORYBILL";
	public static final String INVENTORY_ITEM_TNAME = "INVENTORYITEM";
	
	private SQLiteDatabase db = null;
	private Context ctx = null;
	
	public POSDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		ctx = context;
		if (exist(DATABASE_NAME))
			db = openDatabase(DATABASE_NAME);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	private SQLiteDatabase openDatabase(String databaseName) {
		db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
		return db;
	}
	
	public void initDB() {
		Log.i(TAG, "Init CustRel Database");
		// wipe old table
		db.execSQL("DROP TABLE IF EXISTS " + CONSUME_TNAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + COMPLAINT_TNAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + RETURN_TNAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + INVENTORY_BILL_TNAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + INVENTORY_ITEM_TNAME + ";");

		// create new tables
		db.execSQL("CREATE TABLE IF NOT EXISTS "+CONSUME_TNAME+" ("
				+Consume._ID+" INTEGER PRIMARY KEY,"
				+Consume.NUMBER+" INTEGER,"
				+Consume.GOODS+" INTEGER,"
				+Consume.CREATED_DATE+ " TEXT,"
				+Consume.OPERATOR+" TEXT,"
				+Consume.FLAG+" BOOLEAN,"				
				+Consume.COMMENT+" TEXT"
				+");");
		Log.i(TAG, "Table created: " + CONSUME_TNAME);

		db.execSQL("CREATE TABLE IF NOT EXISTS "+COMPLAINT_TNAME+" ("
				+Complaint._ID+" INTEGER PRIMARY KEY,"
				+Complaint.OPERATOR+" TEXT,"
				+Complaint.CREATE_DATE+" TEXT,"
				+Complaint.VIP_ID+" INTEGER,"
				+Complaint.GOODS_ID+ " INTEGER,"
				+Complaint.COMMENT+" TEXT"				
				+");");
		Log.i(TAG, "Table created: " + COMPLAINT_TNAME);

		db.execSQL("CREATE TABLE IF NOT EXISTS "+RETURN_TNAME+" ("
				+Return._ID+" INTEGER PRIMARY KEY,"
				+Return.OPERATOR+" TEXT,"
				+Return.CREATE_DATE+" TEXT,"
				+Return.VIP_ID+" INTEGER,"
				+Return.GOODS_ID+ " INTEGER,"
				+Return.NUMBER+ " INTEGER,"
				+Return.COMMENT+" TEXT"				
				+");");
		Log.i(TAG, "Table created: " + RETURN_TNAME);

		db.execSQL("CREATE TABLE IF NOT EXISTS "+INVENTORY_BILL_TNAME+" ("
				+InventoryBill._ID+" INTEGER PRIMARY KEY,"
				+InventoryBill.IBILL_NUM+" TEXT,"
				+InventoryBill.OPERATOR+" TEXT,"
				+InventoryBill.CREATE_DATE+" TEXT,"		
				+InventoryBill.FINISHED+ " BOOLEAN,"
				+InventoryBill.RESULT+ " DOUBLE,"
				+InventoryBill.COMMENT+" TEXT"				
				+");");
		Log.i(TAG, "Table created: " + INVENTORY_BILL_TNAME);

		db.execSQL("CREATE TABLE IF NOT EXISTS "+INVENTORY_ITEM_TNAME+" ("
				+InventoryItem._ID+" INTEGER PRIMARY KEY,"
				+InventoryItem.IBILL_ID+" INTEGER,"				
				+InventoryItem.GOODS_PRICE_ID+" INTEGER,"
				+InventoryItem.EXPECT_NUM+" INTEGER,"		
				+InventoryItem.REAL_NUM+ " INTEGER,"
				+InventoryItem.ITEM_RESULT+ " DOUBLE,"
				+InventoryItem.COMMENT+" TEXT"				
				+");");
		Log.i(TAG, "Table created: " + INVENTORY_ITEM_TNAME);

	}

	public boolean exist(String dbName) {
		Log.i(TAG, "exist()");
		boolean flag = false;
		try {
			db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
			flag = true;
		} finally {
			if (db != null)
				db.close();
			db = null;
		}
		return flag;
	}
}
