package com.tobacco.pos.contentProvider;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;
import java.util.List;
 
import com.tobacco.pos.entity.AllTables;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class ManufacturerCPer extends ContentProvider {
	  	private SQLiteDatabase     sqlDB;
	    private DatabaseHelper    dbHelper;
	    private static final String  DATABASE_NAME     = "AllTables.db";
	    private static final int        DATABASE_VERSION         = 1;
	    private static final String TABLE_NAME   = "Manufacturer";
	    private static Context ct = null;

	    private static class DatabaseHelper extends SQLiteOpenHelper {
	    	
	    	private SQLiteDatabase db = null;
	    

			private Context ctx = null;

			public DatabaseHelper(Context context) {
					super(context, DATABASE_NAME, null, DATABASE_VERSION);
				ctx = context;
				ct = context;
				
				db = openDatabase(DATABASE_NAME);
			
				createtable(db);
			
			
			}

			private SQLiteDatabase openDatabase(String databaseName) {
				db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
				return db;
			}

			private void createtable(SQLiteDatabase db) {
				try {
					db.query(TABLE_NAME, null, null, null, null, null, null);
				} catch (Exception e) {
				db.execSQL("create table if not exists " + TABLE_NAME + " ( " + _ID
						+ " integer primary key autoincrement,"
						+ " mName varchar(50) not null unique )");
				initManufacturer(db);
				}
			}


	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	            db.execSQL("drop table if exists " + TABLE_NAME);
	            onCreate(db);
	        }

			@Override
			public void onCreate(SQLiteDatabase db) {
				
			}
			private boolean initManufacturer(SQLiteDatabase db){
				
				ContentValues value = new ContentValues();
				
				value.clear();
				value.put("mName", "龙岩卷烟厂");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("mName", "厦门卷烟厂");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("mName", "泉州卷烟厂");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("mName", "福州卷烟厂");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("mName", "福州衣服厂");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("mName", "漳州饼干厂");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("mName", "莆田拉面馆");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("mName", "泉州安踏");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				
				value.clear();
				value.put("mName", "北京文具厂");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				return true;
			}
	    } 

	    @Override
	    public int delete(Uri uri, String s, String[] as) {
	        return 0;
	    } 

	    @Override
	    public String getType(Uri uri) {
	        return null;
	    } 

	    @Override
	    public Uri insert(Uri uri, ContentValues contentvalues) {
	    	  dbHelper = new DatabaseHelper(ct);
	        sqlDB = dbHelper.getWritableDatabase();
	        long rowId = sqlDB.insert(TABLE_NAME, "", contentvalues);
	        if (rowId > 0) {
	            Uri rowUri = ContentUris.appendId(AllTables.Unit.CONTENT_URI.buildUpon(), rowId).build();
	            ct.getContentResolver().notifyChange(rowUri, null);
	            return rowUri;
	        }
	        else
	        {
	        	sqlDB.close();
	        	throw new SQLException("Failed to insert row into " + uri);
	        }
	    } 

	    @Override
	    public boolean onCreate() {
	        dbHelper = new DatabaseHelper(getContext());
	        return (dbHelper == null) ? false : true;
	    } 

	    @Override
	    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
	    	dbHelper = new DatabaseHelper(ct);
	    	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	        SQLiteDatabase db = dbHelper.getWritableDatabase();
	        qb.setTables(TABLE_NAME);
	        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	        c.setNotificationUri(ct.getContentResolver(), uri);
	        return c;
	    } 

	    @Override
	    public int update(Uri uri, ContentValues contentvalues, String s, String[] as) {
	        return 0;
	    }

		public List<String> getAllManufacturerName() {
			Cursor c = this.query(AllTables.Manufacturer.CONTENT_URI, null, null, null, null);
			if(c.getCount()>0){
				List<String> allManufacturer = new ArrayList<String>();
				c.moveToFirst();
				for(int i=0;i<c.getCount();i++){
					allManufacturer.add(c.getString(1));
					c.moveToNext();
				}
				return allManufacturer;
			}
			return null;
		}
		
		public int getMIdByMName(String mName){//根据厂家的名字查找该厂家的ID
			Cursor c = this.query(AllTables.Manufacturer.CONTENT_URI, null, " mName = ? ", new String[]{mName}, null);
			if(c.getCount()>0){
				c.moveToFirst();
				return c.getInt(0);
			}
			else
				return -1;
		}

		public String getMNameByMId(int mId) {
			Cursor c = this.query(AllTables.Manufacturer.CONTENT_URI, null, " _id = ? ", new String[]{mId + ""}, null);
			if(c.getCount()>0){
				c.moveToFirst();
				return c.getString(1);
			}
			return "";
		}

		public String getAttributeById(String attribute,String id){
			Cursor c = this.query(AllTables.Manufacturer.CONTENT_URI, new String[]{attribute}, "_id = "+"'"+id+"'" , null, null);
			if(c.getCount()>0){
				c.moveToFirst();
				return c.getString(0);
			}else{
				return null;
			}
		}


}
