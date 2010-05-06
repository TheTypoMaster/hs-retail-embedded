package com.tobacco.pos.contentProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.tobacco.pos.entity.ComplaintFull;
import com.tobacco.pos.entity.AllTables.Complaint;
import com.tobacco.pos.util.DateTool;
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

public class ComplaintCPer extends ContentProvider{

	private static final String TAG = "Complaint";

	private static final String DATABASE_NAME = "AllTables.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "Complaint";
	
	private static final int COMPLAINTS = 1;
	private static final int COMPLAINT_ID = 2;
	private static final int COMPLAINTS_FULL = 3;
	private static final int COMPLAINT_FULL_ID = 4;
	
	private static final UriMatcher uriMatcher;
	private static HashMap<String,String> complaintProjectionMap;
	private static Context ct = null;
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		private SQLiteDatabase db = null;
		private Context ctx = null;
		
		DatabaseHelper(Context context){
			super(context,DATABASE_NAME,null,DATABASE_VERSION);
			Log.i("lqz", "initial the databasehelp complaint");
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
			Log.i("lqz", "start to create table complaint");
			db.execSQL("CREATE TABLE if not exists "+TABLE_NAME+" ("
					+Complaint._ID+" INTEGER PRIMARY KEY,"
					+Complaint.OPERATOR+" TEXT,"
					+Complaint.CREATE_DATE+" TEXT,"
					+Complaint.VIP_ID+" INTEGER,"
					+Complaint.GOODS_ID+ " INTEGER,"
					+Complaint.CONTENT+" TEXT,"
					+Complaint.COMMENT+" TEXT"				
					+");");
			Log.i("lqz", "finish create table complaint.");
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG, "Upgrading database from version "+oldVersion+ "to "+newVersion+",which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS Complaint");
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
		case COMPLAINTS:
			return Complaint.CONTENT_TYPE;
		case COMPLAINT_ID:
			return Complaint.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown uri"+uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if(uriMatcher.match(uri)!=COMPLAINTS){
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
		if(values.containsKey(Complaint.OPERATOR)==false){
			values.put(Complaint.OPERATOR, "");
		}
		if(values.containsKey(Complaint.VIP_ID)==false){
			values.put(Complaint.VIP_ID, 1);
		}
		if(values.containsKey(Complaint.CREATE_DATE)==false){
			values.put(Complaint.CREATE_DATE, now);
		}
		if(values.containsKey(Complaint.GOODS_ID)==false){
			values.put(Complaint.GOODS_ID, 0);
		}
		if(values.containsKey(Complaint.CONTENT)==false){
			values.put(Complaint.CONTENT, "");
		}
		if(values.containsKey(Complaint.COMMENT)==false){
			values.put(Complaint.COMMENT, "");
		}

		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		//!!!!!!!!!!deal with nullColumnHack!!!!!!!!!!
		long rowId = db.insert(TABLE_NAME, null, values);
		if(rowId>0){
			Uri consumeUri = ContentUris.withAppendedId(Complaint.CONTENT_URI, rowId);
			this.getContext().getContentResolver().notifyChange(consumeUri, null);
			
			Log.i("lqz", "insert data to "+consumeUri);
			
			return consumeUri;
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
		case COMPLAINTS:
			count = db.delete(TABLE_NAME, where, whereArgs);
			break;
			
		case COMPLAINT_ID:
			String complaintId = uri.getPathSegments().get(1);
			count = db.delete(TABLE_NAME, Complaint._ID + "=" + complaintId+
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
		case COMPLAINTS:
			count = db.update(TABLE_NAME, values, where, whereArgs);
			break;
		case COMPLAINT_ID:
			String complaintId = uri.getPathSegments().get(1);
			count = db.update(TABLE_NAME, values, Complaint._ID + "=" + complaintId +
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
		case COMPLAINTS:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(complaintProjectionMap);
			break;
			
		case COMPLAINT_ID:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(complaintProjectionMap);
			qb.appendWhere(Complaint._ID +"=" +uri.getPathSegments().get(1));
			break;
		case COMPLAINTS_FULL:			
			qb.setTables(ComplaintFull.TABLES);
            qb.appendWhere(ComplaintFull.APPEND_WHERE);
            if(selection!=null)
            {
            	qb.appendWhere(selection);
            	selection = null;
            } 
            break;	
		case COMPLAINT_FULL_ID:			
			qb.setTables(ComplaintFull.TABLES);
            qb.appendWhere("Complaint._ID =" +uri.getPathSegments().get(1));
            qb.appendWhere(" AND "+ComplaintFull.APPEND_WHERE);
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
		uriMatcher.addURI(Complaint.AUTHORITY, "complaints", COMPLAINTS);
		uriMatcher.addURI(Complaint.AUTHORITY, "complaints/#", COMPLAINT_ID);
		uriMatcher.addURI(Complaint.AUTHORITY, "complaints_full", COMPLAINTS_FULL);
		uriMatcher.addURI(Complaint.AUTHORITY, "complaints_full/#", COMPLAINT_FULL_ID);
		
		complaintProjectionMap = new HashMap<String,String>();
		complaintProjectionMap.put(Complaint._ID, Complaint._ID);
		complaintProjectionMap.put(Complaint.OPERATOR, Complaint.OPERATOR);
		complaintProjectionMap.put(Complaint.CREATE_DATE, Complaint.CREATE_DATE);
		complaintProjectionMap.put(Complaint.VIP_ID, Complaint.VIP_ID);
		complaintProjectionMap.put(Complaint.GOODS_ID, Complaint.GOODS_ID);
		complaintProjectionMap.put(Complaint.CONTENT, Complaint.CONTENT);
		complaintProjectionMap.put(Complaint.COMMENT, Complaint.COMMENT);
	}
	
}
