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
import com.tobacco.pos.entity.ReturnFull;
import com.tobacco.pos.entity.AllTables.Return;
import com.tobacco.pos.util.DateTool;

public class ReturnCPer extends ContentProvider{

	private static final String TAG = "Return";

	private static final String DATABASE_NAME = "AllTables.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "Return";
	
	private static final int RETURNS = 1;
	private static final int RETURN_ID = 2;
	private static final int RETURNS_FULL = 3;
	private static final int RETURN_FULL_ID = 4;
	
	private static final UriMatcher uriMatcher;
	private static HashMap<String,String> returnProjectionMap;
	private static Context ct = null;
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		private SQLiteDatabase db = null;
		private Context ctx = null;
		
		DatabaseHelper(Context context){
			super(context,DATABASE_NAME,null,DATABASE_VERSION);
			Log.i("lqz", "initial the databasehelp return");
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
			Log.i("lqz", "start to create table return");
			db.execSQL("CREATE TABLE if not exists "+TABLE_NAME+" ("
					+Return._ID+" INTEGER PRIMARY KEY,"
					+Return.OPERATOR+" TEXT,"
					+Return.CREATE_DATE+" TEXT,"
					+Return.VIP_ID+" INTEGER,"
					+Return.GOODS_ID+ " INTEGER,"
					+Return.NUMBER+ " INTEGER,"
					+Return.CONTENT+" TEXT,"
					+Return.COMMENT+" TEXT"				
					+");");
			Log.i("lqz", "finish create table return.");
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG, "Upgrading database from version "+oldVersion+ "to "+newVersion+",which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS Return");
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
		case RETURNS:
			return Return.CONTENT_TYPE;
		case RETURN_ID:
			return Return.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown uri"+uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if(uriMatcher.match(uri)!=RETURNS){
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
		if(values.containsKey(Return.OPERATOR)==false){
			values.put(Return.OPERATOR, "");
		}
		if(values.containsKey(Return.VIP_ID)==false){
			values.put(Return.VIP_ID, 1);
		}
		if(values.containsKey(Return.CREATE_DATE)==false){
			values.put(Return.CREATE_DATE, now);
		}
		if(values.containsKey(Return.GOODS_ID)==false){
			values.put(Return.GOODS_ID, 0);
		}
		if(values.containsKey(Return.NUMBER)==false){
			values.put(Return.NUMBER, 0);
		}
		if(values.containsKey(Return.CONTENT)==false){
			values.put(Return.CONTENT, "");
		}
		if(values.containsKey(Return.COMMENT)==false){
			values.put(Return.COMMENT, "");
		}

		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		//!!!!!!!!!!deal with nullColumnHack!!!!!!!!!!
		long rowId = db.insert(TABLE_NAME, null, values);
		if(rowId>0){
			Uri returnUri = ContentUris.withAppendedId(Return.CONTENT_URI, rowId);
			this.getContext().getContentResolver().notifyChange(returnUri, null);
			
			Log.i("lqz", "insert data to "+returnUri);
			
			return returnUri;
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
		case RETURNS:
			count = db.delete(TABLE_NAME, where, whereArgs);
			break;
			
		case RETURN_ID:
			String returnId = uri.getPathSegments().get(1);
			count = db.delete(TABLE_NAME, Return._ID + "=" + returnId+
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
		case RETURNS:
			count = db.update(TABLE_NAME, values, where, whereArgs);
			break;
		case RETURN_ID:
			String returnId = uri.getPathSegments().get(1);
			count = db.update(TABLE_NAME, values, Return._ID + "=" + returnId +
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
		case RETURNS:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(returnProjectionMap);
			break;
			
		case RETURN_ID:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(returnProjectionMap);
			qb.appendWhere(Return._ID +"=" +uri.getPathSegments().get(1));
			break;
		case RETURNS_FULL:			
			qb.setTables(ReturnFull.TABLES);
            qb.appendWhere(ReturnFull.APPEND_WHERE);
            if(selection!=null)
            {
            	qb.appendWhere(selection);
            	selection = null;
            }
            break;	
		case RETURN_FULL_ID:			
			qb.setTables(ReturnFull.TABLES);
            qb.appendWhere("Return._ID =" +uri.getPathSegments().get(1));
            qb.appendWhere(" AND "+ReturnFull.APPEND_WHERE);
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
	
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Return.AUTHORITY, "returns", RETURNS);
		uriMatcher.addURI(Return.AUTHORITY, "returns/#", RETURN_ID);
		uriMatcher.addURI(Return.AUTHORITY, "returns_full", RETURNS_FULL);
		uriMatcher.addURI(Return.AUTHORITY, "returns_full/#", RETURN_FULL_ID);
		
		returnProjectionMap = new HashMap<String,String>();
		returnProjectionMap.put(Return._ID, Return._ID);
		returnProjectionMap.put(Return.OPERATOR, Return.OPERATOR);
		returnProjectionMap.put(Return.CREATE_DATE, Return.CREATE_DATE);
		returnProjectionMap.put(Return.VIP_ID, Return.VIP_ID);
		returnProjectionMap.put(Return.GOODS_ID, Return.GOODS_ID);
		returnProjectionMap.put(Return.NUMBER, Return.NUMBER);
		returnProjectionMap.put(Return.CONTENT, Return.CONTENT);
		returnProjectionMap.put(Return.COMMENT, Return.COMMENT);
	}
	
	public String getAttributeById(String attribute,String id){
		Cursor c = this.query(AllTables.Return.CONTENT_URI, new String[]{attribute}, " _id = "+"'"+id+"'" , null, null);
		if(c.getCount()>0){
			c.moveToFirst();
			return c.getString(0);
		}else{
			return null;
		}
	}
   
    public String getAttributeByAttribute(String attribute,String attribute2,String value){
		Cursor c = this.query(AllTables.Return.CONTENT_URI, new String[]{attribute}, attribute2+" = "+"'"+value+"'" , null, null);
		if(c.getCount()>0){
			c.moveToFirst();
			return c.getString(0);
		}else{
			return null;
		}
	}
}