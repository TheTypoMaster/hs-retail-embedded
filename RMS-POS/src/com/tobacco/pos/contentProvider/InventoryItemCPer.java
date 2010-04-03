package com.tobacco.pos.contentProvider;

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

import com.tobacco.pos.entity.InventoryItemFull;
import com.tobacco.pos.entity.AllTables.InventoryItem;

public class InventoryItemCPer extends ContentProvider{
	
	private static final String TAG = "InventoryItem";

	private static final String DATABASE_NAME = "AllTables.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "InventoryItem";
	
	private static final int INVENTORY_ITEMS = 1;
	private static final int INVENTORY_ITEM_ID = 2;
	private static final int INVENTORY_ITEMS_FULL = 3;
	private static final int INVENTORY_ITEM_FULL_ID = 4;
	
	private static final UriMatcher uriMatcher;
	private static HashMap<String,String> inventoryItemProjectionMap;
	private static Context ct = null;
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		private SQLiteDatabase db = null;
		private Context ctx = null;
		
		DatabaseHelper(Context context){
			super(context,DATABASE_NAME,null,DATABASE_VERSION);
			Log.i("lqz", "initial the databasehelp InventoryItem");
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
			Log.i("lqz", "start to create table InventoryItem");	
			db.execSQL("CREATE TABLE if not exists "+TABLE_NAME+" ("
					+InventoryItem._ID+" INTEGER PRIMARY KEY,"
					+InventoryItem.IBILL_ID+" INTEGER,"				
					+InventoryItem.GOODS_PRICE_ID+" INTEGER,"
					+InventoryItem.EXPECT_NUM+" INTEGER,"		
					+InventoryItem.REAL_NUM+ " INTEGER,"
					+InventoryItem.ITEM_RESULT+ " DOUBLE,"
					+InventoryItem.COMMENT+" TEXT"				
					+");");
			Log.i("lqz", "finish create table InventoryItem.");
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG, "Upgrading database from version "+oldVersion+ "to "+newVersion+",which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS InventoryItem");
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
		case INVENTORY_ITEMS:
			return InventoryItem.CONTENT_TYPE;
		case INVENTORY_ITEM_ID:
			return InventoryItem.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown uri"+uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if(uriMatcher.match(uri)!=INVENTORY_ITEMS){
			throw new IllegalArgumentException("Unknown URI"+uri);
		}
		
		ContentValues values;
		if(initialValues!=null){
			values = new ContentValues(initialValues);
		}else {
			values = initialValues;
		}
		
//		Date today = Calendar.getInstance().getTime();
//		String now = DateTool.formatDateToString(today);
		if(values.containsKey(InventoryItem.IBILL_ID)==false){
			values.put(InventoryItem.IBILL_ID, 0);
		}
		if(values.containsKey(InventoryItem.GOODS_PRICE_ID)==false){
			values.put(InventoryItem.GOODS_PRICE_ID, 0);
		}
		if(values.containsKey(InventoryItem.EXPECT_NUM)==false){
			values.put(InventoryItem.EXPECT_NUM, 0);
		}
		if(values.containsKey(InventoryItem.REAL_NUM)==false){
			values.put(InventoryItem.REAL_NUM, 0);
		}
		if(values.containsKey(InventoryItem.ITEM_RESULT)==false){
			values.put(InventoryItem.ITEM_RESULT, 0.0);
		}
		if(values.containsKey(InventoryItem.COMMENT)==false){
			values.put(InventoryItem.COMMENT, "");
		}

		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		//!!!!!!!!!!deal with nullColumnHack!!!!!!!!!!
		long rowId = db.insert(TABLE_NAME, null, values);
		if(rowId>0){
			Uri inventoryItemUri = ContentUris.withAppendedId(InventoryItem.CONTENT_URI, rowId);
			this.getContext().getContentResolver().notifyChange(inventoryItemUri, null);
			
			Log.i("lqz", "insert data to "+inventoryItemUri);
			
			return inventoryItemUri;
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
		case INVENTORY_ITEMS:
			count = db.delete(TABLE_NAME, where, whereArgs);
			break;
			
		case INVENTORY_ITEM_ID:
			String inventoryItemId = uri.getPathSegments().get(1);
			count = db.delete(TABLE_NAME, InventoryItem._ID + "=" + inventoryItemId+
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
		case INVENTORY_ITEMS:
			count = db.update(TABLE_NAME, values, where, whereArgs);
			break;
		case INVENTORY_ITEM_ID:
			String inventoryItemId = uri.getPathSegments().get(1);
			count = db.update(TABLE_NAME, values, InventoryItem._ID + "=" + inventoryItemId +
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
		case INVENTORY_ITEMS:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(inventoryItemProjectionMap);
			break;
			
		case INVENTORY_ITEM_ID:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(inventoryItemProjectionMap);
			qb.appendWhere(InventoryItem._ID +"=" +uri.getPathSegments().get(1));
			break;
		case INVENTORY_ITEMS_FULL:			
			qb.setTables(InventoryItemFull.TABLES);
            qb.appendWhere(InventoryItemFull.APPEND_WHERE);
            break;	
//		case INVENTORY_ITEM_FULL_ID:			
//			qb.setTables(InventoryItemFull.TABLES);
//            qb.appendWhere("InventoryItem._ID =" +uri.getPathSegments().get(1));
//            qb.appendWhere(" AND "+InventoryItemFull.APPEND_WHERE);
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
		uriMatcher.addURI(InventoryItem.AUTHORITY, "inventoryItems", INVENTORY_ITEMS);
		uriMatcher.addURI(InventoryItem.AUTHORITY, "inventoryItems/#", INVENTORY_ITEM_ID);
		uriMatcher.addURI(InventoryItem.AUTHORITY, "inventoryItems_full", INVENTORY_ITEMS_FULL);
		uriMatcher.addURI(InventoryItem.AUTHORITY, "inventoryItems_full/#", INVENTORY_ITEM_FULL_ID);
		
		inventoryItemProjectionMap = new HashMap<String,String>();
		inventoryItemProjectionMap.put(InventoryItem._ID, InventoryItem._ID);
		inventoryItemProjectionMap.put(InventoryItem.GOODS_PRICE_ID, InventoryItem.GOODS_PRICE_ID);
		inventoryItemProjectionMap.put(InventoryItem.EXPECT_NUM, InventoryItem.EXPECT_NUM);
		inventoryItemProjectionMap.put(InventoryItem.REAL_NUM, InventoryItem.REAL_NUM);
		inventoryItemProjectionMap.put(InventoryItem.ITEM_RESULT, InventoryItem.ITEM_RESULT);
		inventoryItemProjectionMap.put(InventoryItem.IBILL_ID, InventoryItem.IBILL_ID);
		inventoryItemProjectionMap.put(InventoryItem.COMMENT, InventoryItem.COMMENT);
	}
}
