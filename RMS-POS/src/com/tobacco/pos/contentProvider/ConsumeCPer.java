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

import com.tobacco.pos.entity.AllTables;
import com.tobacco.pos.entity.ConsumeFull;
import com.tobacco.pos.entity.AllTables.Consume;
import com.tobacco.pos.util.DateTool;

public class ConsumeCPer extends ContentProvider{

	private static final String TAG = "Consume";
	
	private static final String DATABASE_NAME = "AllTables.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "Consume";
	
	private static final int CONSUMES = 1;
	private static final int CONSUME_ID = 2;
	private static final int CONSUMES_FULL = 3;
	private static final int CONSUME_FULL_ID = 4;
	
	private static final UriMatcher cUriMatcher;
	private static HashMap<String,String> consumeProjectionMap;
	private static Context ct = null;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		//add
		private SQLiteDatabase db = null;
		private Context ctx = null;
		//
		DatabaseHelper(Context context){
			super(context,DATABASE_NAME,null,DATABASE_VERSION);
			Log.i("lqz", "initial the databasehelp");
			//add
			ctx = context;
			ct = context;
			
			db = openDatabase(DATABASE_NAME);
			onCreate(db);
			//
		}
		//add
		private SQLiteDatabase openDatabase(String databaseName) {
			db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
			return db;
		}
		//
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			Log.i("lqz", "start to create table");
			db.execSQL("CREATE TABLE if not exists "+TABLE_NAME+" ("
					+Consume._ID+" INTEGER PRIMARY KEY,"
					+Consume.NUMBER+" INTEGER,"
					+Consume.GOODS+" INTEGER,"
					+Consume._COUNT+" INTEGER,"
					+Consume.CREATED_DATE+ " TEXT,"
					+Consume.OPERATOR+" TEXT,"
					+Consume.FLAG+" BOOLEAN,"				
					+Consume.COMMENT+" TEXT"
					+");");
			Log.i("lqz", "finish create table.");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG, "Upgrading database from version "+oldVersion+ "to "+newVersion+",which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS consume");
			onCreate(db);
		}
	}
	
	private DatabaseHelper databaseHelper;
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		int count = 0;
		switch(cUriMatcher.match(uri)){
		case CONSUMES:
			count = db.delete(TABLE_NAME, where, whereArgs);
			break;
			
		case CONSUME_ID:
			String consumeId = uri.getPathSegments().get(1);
			count = db.delete(TABLE_NAME, Consume._ID + "=" + consumeId+
					(!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI"+uri);
		}
		this.getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch(cUriMatcher.match(uri)){
		case CONSUMES:
			return Consume.CONTENT_TYPE;
		case CONSUME_ID:
			return Consume.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown uri"+uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if(cUriMatcher.match(uri)!=CONSUMES){
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
		if(values.containsKey(Consume.NUMBER)==false){
			values.put(Consume.NUMBER, 0);
		}
		if(values.containsKey(Consume.GOODS)==false){
			values.put(Consume.GOODS, 0);
		}
		if(values.containsKey(Consume.CREATED_DATE)==false){
			values.put(Consume.CREATED_DATE, now);
		}
		if(values.containsKey(Consume.FLAG)==false){
			values.put(Consume.FLAG, 0);
		}
		if(values.containsKey(Consume.OPERATOR)==false){
			values.put(Consume.OPERATOR, "");
		}
		if(values.containsKey(Consume.COMMENT)==false){
			values.put(Consume.COMMENT, "");
		}

		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		//!!!!!!!!!!deal with nullColumnHack!!!!!!!!!!
		long rowId = db.insert(TABLE_NAME, null, values);
		if(rowId>0){
			Uri consumeUri = ContentUris.withAppendedId(Consume.CONTENT_URI, rowId);
			this.getContext().getContentResolver().notifyChange(consumeUri, null);
			
			Log.i("lqz", "insert data to "+consumeUri);
			
			return consumeUri;
		}else{
			db.close();
			throw new SQLException("Failed to insert data to "+uri);
		}
		
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		databaseHelper = new DatabaseHelper(this.getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		databaseHelper = new DatabaseHelper(ct);
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		Log.e("lqz", uri+"projection:"+projection+"selection:"+selection+"selectionArgs"+selectionArgs);
		switch(cUriMatcher.match(uri)){
		case CONSUMES:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(consumeProjectionMap);
			break;
			
		case CONSUME_ID:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(consumeProjectionMap);
			qb.appendWhere(Consume._ID +"=" +uri.getPathSegments().get(1));
			break;
		case CONSUMES_FULL:			
			qb.setTables(ConsumeFull.TABLES);
            qb.appendWhere(ConsumeFull.APPEND_WHERE);
            if(selection!=null)
            {
            	qb.appendWhere(selection);
            	selection = null;
            }         	
            break;	
		case CONSUME_FULL_ID:			
			qb.setTables(ConsumeFull.TABLES);
            qb.appendWhere("Consume._ID =" +uri.getPathSegments().get(1));
            qb.appendWhere(" AND "+ConsumeFull.APPEND_WHERE);
            break;	
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

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		int count = 0;
		switch(cUriMatcher.match(uri)){
		case CONSUMES:
			count = db.update(TABLE_NAME, values, where, whereArgs);
			break;
		case CONSUME_ID:
			String consumeId = uri.getPathSegments().get(1);
			count = db.update(TABLE_NAME, values, Consume._ID + "=" + consumeId +
					(!TextUtils.isEmpty(where) ? "AND (" + where + ')' : ""), whereArgs);
		}
		this.getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	static{
		cUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		cUriMatcher.addURI(Consume.AUTHORITY, "consumes", CONSUMES);
		cUriMatcher.addURI(Consume.AUTHORITY, "consumes/#", CONSUME_ID);
		cUriMatcher.addURI(Consume.AUTHORITY, "consumes_full", CONSUMES_FULL);
		cUriMatcher.addURI(Consume.AUTHORITY, "consumes_full/#", CONSUME_FULL_ID);
		
		consumeProjectionMap = new HashMap<String,String>();
		consumeProjectionMap.put(Consume._ID, Consume._ID);
		consumeProjectionMap.put(Consume.NUMBER, Consume.NUMBER);
		consumeProjectionMap.put(Consume.GOODS, Consume.GOODS);
		consumeProjectionMap.put(Consume._COUNT, Consume._COUNT);
		consumeProjectionMap.put(Consume.CREATED_DATE, Consume.CREATED_DATE);
		consumeProjectionMap.put(Consume.FLAG, Consume.FLAG);
		consumeProjectionMap.put(Consume.OPERATOR, Consume.OPERATOR);
		consumeProjectionMap.put(Consume.COMMENT, Consume.COMMENT);
	}

	public String getAttributeById(String attribute,String id){
		Cursor c = this.query(AllTables.Consume.CONTENT_URI, new String[]{attribute}, " _id = "+"'"+id+"'" , null, null);
		if(c.getCount()>0){
			c.moveToFirst();
			return c.getString(0);
		}else{
			return null;
		}
	}
   
    public String getAttributeByAttribute(String attribute,String attribute2,String value){
		Cursor c = this.query(AllTables.Consume.CONTENT_URI, new String[]{attribute}, attribute2+" = "+"'"+value+"'" , null, null);
		if(c.getCount()>0){
			c.moveToFirst();
			return c.getString(0);
		}else{
			return null;
		}
	}
}
