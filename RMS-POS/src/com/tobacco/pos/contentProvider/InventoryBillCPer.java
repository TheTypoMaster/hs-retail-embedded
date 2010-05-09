package com.tobacco.pos.contentProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.tobacco.pos.entity.InventoryBillFull;
import com.tobacco.pos.entity.AllTables.InventoryBill;
import com.tobacco.pos.util.DateTool;

public class InventoryBillCPer extends ContentProvider{
	
	private static final String TAG = "InventoryBill";

	private static final String DATABASE_NAME = "AllTables.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "InventoryBill";
	
	private static final int INVENTORY_BILLS = 1;
	private static final int INVENTORY_BILL_ID = 2;
	private static final int INVENTORY_BILLS_FULL = 3;
	private static final int INVENTORY_BILL_FULL_ID = 4;
	
	private static final UriMatcher uriMatcher;
	private static HashMap<String,String> inventoryBillProjectionMap;
	private static Context ct = null;
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		private SQLiteDatabase db = null;
		private Context ctx = null;
		
		DatabaseHelper(Context context){
			super(context,DATABASE_NAME,null,DATABASE_VERSION);
			Log.i("lqz", "initial the databasehelp InventoryBill");
			//add
			ctx = context;
			ct = context;
			
			db = openDatabase(DATABASE_NAME);
			onCreate(db);
		}
		private SQLiteDatabase openDatabase(String databaseName) {
			db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
			return db;
		}
		@Override	
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			Log.i("lqz", "start to create table InventoryBill");
			db.execSQL("CREATE TABLE if not exists "+TABLE_NAME+" ("
					+InventoryBill._ID+" INTEGER PRIMARY KEY,"
					+InventoryBill.IBILL_NUM+" TEXT,"
					+InventoryBill.OPERATOR+" TEXT,"
					+InventoryBill.CREATE_DATE+" TEXT,"		
					+InventoryBill.FINISHED+ " BOOLEAN,"
					+InventoryBill.RESULT+ " DOUBLE,"
					+InventoryBill.COMMENT+" TEXT"				
					+");");
			Log.i("lqz", "finish create table InventoryBill.");
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG, "Upgrading database from version "+oldVersion+ "to "+newVersion+",which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS InventoryBill");
			onCreate(db);
		}
		
	}

	private DatabaseHelper databaseHelper;

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		databaseHelper = new DatabaseHelper(this.getContext());
		Log.e(TAG,"new DatabaseHelper"+this.getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch(uriMatcher.match(uri)){
		case INVENTORY_BILLS:
			return InventoryBill.CONTENT_TYPE;
		case INVENTORY_BILL_ID:
			return InventoryBill.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown uri"+uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if(uriMatcher.match(uri)!=INVENTORY_BILLS){
			throw new IllegalArgumentException("Unknown URI"+uri);
		}
		
		ContentValues values;
		if(initialValues!=null){
			values = new ContentValues(initialValues);
		}else {
			values = initialValues;
		}
		
		Date today = Calendar.getInstance().getTime();
		String now = DateTool.formatDateToString(today);
		if(values.containsKey(InventoryBill.IBILL_NUM)==false){
			values.put(InventoryBill.IBILL_NUM, "");
		}
		if(values.containsKey(InventoryBill.OPERATOR)==false){
			values.put(InventoryBill.OPERATOR, "");
		}
		if(values.containsKey(InventoryBill.CREATE_DATE)==false){
			values.put(InventoryBill.CREATE_DATE, now);
		}
		if(values.containsKey(InventoryBill.FINISHED)==false){
			values.put(InventoryBill.FINISHED, 1);
		}
		if(values.containsKey(InventoryBill.RESULT)==false){
			values.put(InventoryBill.RESULT, 0.0);
		}
		if(values.containsKey(InventoryBill.COMMENT)==false){
			values.put(InventoryBill.COMMENT, "");
		}

		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		//!!!!!!!!!!deal with nullColumnHack!!!!!!!!!!
		long rowId = db.insert(TABLE_NAME, null, values);
		if(rowId>0){
			Uri inventoryBillUri = ContentUris.withAppendedId(InventoryBill.CONTENT_URI, rowId);
			this.getContext().getContentResolver().notifyChange(inventoryBillUri, null);
			
			Log.i("lqz", "insert data to "+inventoryBillUri);
			
			return inventoryBillUri;
		}else{
			db.close();
			throw new SQLException("Failed to insert data to "+uri);
		}
	}
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		int count = 0;
		switch(uriMatcher.match(uri)){
		case INVENTORY_BILLS:
			count = db.delete(TABLE_NAME, where, whereArgs);
			break;
			
		case INVENTORY_BILL_ID:
			String inventoryBillId = uri.getPathSegments().get(1);
			count = db.delete(TABLE_NAME, InventoryBill._ID + "=" + inventoryBillId+
					(!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI"+uri);
		}
		this.getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		int count = 0;
		switch(uriMatcher.match(uri)){
		case INVENTORY_BILLS:
			count = db.update(TABLE_NAME, values, where, whereArgs);
			break;
		case INVENTORY_BILL_ID:
			String inventoryBillId = uri.getPathSegments().get(1);
			count = db.update(TABLE_NAME, values, InventoryBill._ID + "=" + inventoryBillId +
					(!TextUtils.isEmpty(where) ? "AND (" + where + ')' : ""), whereArgs);
		}
		this.getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		databaseHelper = new DatabaseHelper(ct);
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		Log.e("lqz", uri+"projection:"+projection+"selection:"+selection+"selectionArgs"+selectionArgs);
		switch(uriMatcher.match(uri)){
		case INVENTORY_BILLS:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(inventoryBillProjectionMap);
			break;
			
		case INVENTORY_BILL_ID:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(inventoryBillProjectionMap);
			qb.appendWhere(InventoryBill._ID +"=" +uri.getPathSegments().get(1));
			break;
		case INVENTORY_BILLS_FULL:			
			qb.setTables(InventoryBillFull.TABLES);
            qb.appendWhere(InventoryBillFull.APPEND_WHERE);
            if(selection!=null)
            {
            	qb.appendWhere(selection);
            	selection = null;
            }
            break;	
//		case INVENTORY_BILL_FULL_ID:			
//			qb.setTables(InventoryBillFull.TABLES);
//            qb.appendWhere("InventoryBill._ID =" +uri.getPathSegments().get(1));
//            qb.appendWhere(" AND "+InventoryBillFull.APPEND_WHERE);
//            break;	
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		//!!!!!!Problem about order!!!!!!!!
		String orderBy = null;
//		if(TextUtils.isEmpty(sortOrder)){
//			orderBy = Consume.DEFAULT_SORT_ORDER;
//		} else{
//			orderBy = sortOrder;
//		}
		
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(ct.getContentResolver(), uri);
		return c;
	}
	
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(InventoryBill.AUTHORITY, "inventoryBills", INVENTORY_BILLS);
		uriMatcher.addURI(InventoryBill.AUTHORITY, "inventoryBills/#", INVENTORY_BILL_ID);
		uriMatcher.addURI(InventoryBill.AUTHORITY, "inventoryBills_full", INVENTORY_BILLS_FULL);
		uriMatcher.addURI(InventoryBill.AUTHORITY, "inventoryBills_full/#", INVENTORY_BILL_FULL_ID);
		
		inventoryBillProjectionMap = new HashMap<String,String>();
		inventoryBillProjectionMap.put(InventoryBill._ID, InventoryBill._ID);
		inventoryBillProjectionMap.put(InventoryBill.OPERATOR, InventoryBill.OPERATOR);
		inventoryBillProjectionMap.put(InventoryBill.CREATE_DATE, InventoryBill.CREATE_DATE);
		inventoryBillProjectionMap.put(InventoryBill.FINISHED, InventoryBill.FINISHED);
		inventoryBillProjectionMap.put(InventoryBill.RESULT, InventoryBill.RESULT);
		inventoryBillProjectionMap.put(InventoryBill.IBILL_NUM, InventoryBill.IBILL_NUM);
		inventoryBillProjectionMap.put(InventoryBill.COMMENT, InventoryBill.COMMENT);
	}
}
