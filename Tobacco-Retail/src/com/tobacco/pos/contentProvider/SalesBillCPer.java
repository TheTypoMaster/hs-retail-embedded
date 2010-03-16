package com.tobacco.pos.contentProvider;

import static android.provider.BaseColumns._ID;
 
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

public class SalesBillCPer extends ContentProvider {

	  private SQLiteDatabase     sqlDB;
	    private DatabaseHelper    dbHelper;
	    private static final String  DATABASE_NAME     = "AllTables.db";
	    private static final int        DATABASE_VERSION         = 1;
	    private static final String TABLE_NAME   = "SalesBill";

	    private static class DatabaseHelper extends SQLiteOpenHelper {
	    	
	    	private SQLiteDatabase db = null;
	    

			private Context ctx = null;

			public DatabaseHelper(Context context) {
					super(context, DATABASE_NAME, null, DATABASE_VERSION);
				ctx = context;
		
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
				db.execSQL("create table  if not exists " + TABLE_NAME + " ( " + _ID
						+ " integer primary key autoincrement,"
						+ " sBillNum varchar(20) unique not null, "
						+ "operId integer references UserInfo ( " + _ID + " ),"
						+ "time date not null,"
						+ "VIPId integer references VIPInfo ( " + _ID + " ))");
				initSalesBill(db);
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
			private boolean initSalesBill(SQLiteDatabase db) {
				 
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
	
	    	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	        SQLiteDatabase db = dbHelper.getReadableDatabase();
	        qb.setTables(TABLE_NAME);
	        Cursor c = qb.query(db, projection, selection, null, null, null, sortOrder);
	        c.setNotificationUri(getContext().getContentResolver(), uri);
	        return c;
	    } 

	    @Override
	    public int update(Uri uri, ContentValues contentvalues, String s, String[] as) {
	        return 0;
	    }
	    
	    public int addSBill(int operId, String time, int VIPId){
	    	Cursor c =this.query(AllTables.SalesBill.CONTENT_URI, null, null, null, " order by sBillNum ");
	    	String sBillNum;
	    	if(c.getCount()>0)
	    	{
	    		c.moveToLast();
	    		String lastSBillNum = c.getString(1);
	    		sBillNum = "" + (Integer.parseInt(lastSBillNum) + 1 );
	    	}
	    	else
	    		sBillNum = "1";
	    	
	    	ContentValues value = new ContentValues();
			
			value.clear();
			value.put("sBillNum", sBillNum);
			value.put("operId", operId);
			value.put("time", time);
			value.put("VIPId", VIPId);
			
			this.insert(AllTables.SalesBill.CONTENT_URI, value);
			
			c = this.query(AllTables.SalesBill.CONTENT_URI, null, null, null, null);	
			c.moveToLast();
			return c.getInt(0);//获取最后添加的销售单的ID
		}
	    public int getSBillNumBySBillId(int newSBillId){
	    	Cursor c = this.query(AllTables.SalesBill.CONTENT_URI, null, " _id = " + newSBillId, null, null);
	    	
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getInt(0);
	    	}
	    	return -1;
	    }

}
