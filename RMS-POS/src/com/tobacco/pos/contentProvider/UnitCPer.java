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

public class UnitCPer extends ContentProvider {

	  private SQLiteDatabase     sqlDB;
	    private DatabaseHelper    dbHelper;
	    private static final String  DATABASE_NAME     = "AllTables.db";
	    private static final int        DATABASE_VERSION         = 1;
	    private static final String TABLE_NAME   = "Unit";
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
						+ " name varchar(20) not null unique )");
				initUnit(db);
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
			private boolean initUnit(SQLiteDatabase db){
				ContentValues value = new ContentValues();
				
				value.clear();
				value.put("name", "包");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("name", "条");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("name", "箱");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("name", "斤");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("name", "件");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("name", "支");
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("name", "张");
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
	        sqlDB = dbHelper.getWritableDatabase();
	        long rowId = sqlDB.insert(TABLE_NAME, "", contentvalues);
	        if (rowId > 0) {
	            Uri rowUri = ContentUris.appendId(AllTables.Unit.CONTENT_URI.buildUpon(), rowId).build();
	            getContext().getContentResolver().notifyChange(rowUri, null);
	            return rowUri;
	        }
	        throw new SQLException("Failed to insert row into " + uri);
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
	        SQLiteDatabase db = dbHelper.getReadableDatabase();
	        qb.setTables(TABLE_NAME);
	        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	        c.setNotificationUri(ct.getContentResolver(), uri);
	        return c;
	    } 

	    @Override
	    public int update(Uri uri, ContentValues contentvalues, String s, String[] as) {
	        return 0;
	    }
	    
	    public String getUnitNameById(int unitId){
	    	Cursor c = this.query(AllTables.Unit.CONTENT_URI, null, " _id = " + unitId, null, null);
	    	
	    	if(c.getCount()>0)
	    	{
	    		c.moveToFirst();
	    		return c.getString(1);
	    	}
	    	return "";
	    	
	    }
	    public int getUnitIdByUnitName(String unitName){
	    	Cursor c = this.query(AllTables.Unit.CONTENT_URI, null, " name = ? ", new String[]{unitName}, null);
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getInt(0);
	    	}
	    	return -1;
	    }

		public List<String> getAllUnitName() {
			Cursor c = this.query(AllTables.Unit.CONTENT_URI, null, null, null, null);
			if(c.getCount()>0){
				c.moveToFirst();
				List<String> allUnitName = new ArrayList<String>();
				for(int i=0;i<c.getCount();i++){
					allUnitName.add(c.getString(1));
					c.moveToNext();
				}
				return allUnitName;
			}
			return null;
		}

		public String getAttributeById(String attribute,String id){
			Cursor c = this.query(AllTables.Unit.CONTENT_URI, new String[]{attribute}, "_id = "+"'"+id+"'" , null, null);
			if(c.getCount()>0){
				c.moveToFirst();
				return c.getString(0);
			}else{
				return null;
			}
		}
}
