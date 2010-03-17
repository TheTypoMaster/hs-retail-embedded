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

public class GoodsPriceCPer extends ContentProvider {

		private SQLiteDatabase     sqlDB;
	    private DatabaseHelper    dbHelper;
	    private static final String  DATABASE_NAME     = "AllTables.db";
	    private static final int        DATABASE_VERSION         = 1;
	    private static final String TABLE_NAME   = "GoodsPrice";
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
				db = ctx.openOrCreateDatabase(DATABASE_NAME, 1, null);
				return db;
			}

			private void createtable(SQLiteDatabase db) {
				try {
					db.query(TABLE_NAME, null, null, null, null, null, null);
				} catch (Exception e) {
				db.execSQL("create table if not exists " + TABLE_NAME + " ( " + _ID
						+ " integer primary key autoincrement,"
						+ " goodsId integer references Goods " 
						+ " (" + _ID + " ) ,"
						+ "unitId integer references Unit ( "
						+ _ID + " )," + " barcode varchar(20) unique ,"
						+ "inPrice double not null,"
						+ "outPrice double not null )");
				initGoodsPrice(db);
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
			private boolean initGoodsPrice(SQLiteDatabase db){

				ContentValues value = new ContentValues();
				
				value.clear();
				value.put("goodsId", 1);
				value.put("unitId", 1);
				value.put("barcode", "gb1");
				value.put("inPrice", 6.0);
				value.put("outPrice", 7.0);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 2);
				value.put("unitId", 2);
				value.put("barcode", "gb2");
				value.put("inPrice", 50);
				value.put("outPrice", 60);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 3);
				value.put("unitId", 3);
				value.put("barcode", "gb3");
				value.put("inPrice", 70);
				value.put("outPrice", 80);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 4);
				value.put("unitId", 3);
				value.put("barcode", "gb4");
				value.put("inPrice", 55);
				value.put("outPrice", 64);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 5);
				value.put("unitId", 3);
				value.put("barcode", "gb5");
				value.put("inPrice", 90);
				value.put("outPrice", 100);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 6);
				value.put("unitId", 3);
				value.put("barcode", "gb6");
				value.put("inPrice", 100);
				value.put("outPrice", 110);
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
	    
	    public int getGoodsIdByBarcode(String barcode){
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " barcode = ? " , new String[]{barcode}, null);
			
			if(c.getCount()>0)
			{
				c.moveToFirst();
				return c.getInt(1);
			}
			else
				return -1;
		}
	    
	    public int getUnitIdByBarcode(String barcode){
			Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " barcode = ? " , new String[]{barcode}, null);
			
			if(c.getCount()>0)
			{
				c.moveToFirst();
				return c.getInt(2);
			}
			return -1;
		}

	    public double getOutPriceByBarcode(String barcode){
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " barcode = ? " , new String[]{barcode}, null);
	    	
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getDouble(5);
	    	}
	    	else
	    		return 0;
	    }
	    public int getGoodsPriceIdByBarcode(String barcode){
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " barcode = ? " , new String[]{barcode}, null);
	    	
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getInt(0);
	    	}
	    	else
	    		return 0;
	    }

}
