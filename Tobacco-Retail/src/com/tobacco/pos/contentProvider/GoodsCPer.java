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

public class GoodsCPer extends ContentProvider {
		private SQLiteDatabase     sqlDB;
	    private DatabaseHelper    dbHelper;
	    private static final String  DATABASE_NAME     = "AllTables.db";
	    private static final int        DATABASE_VERSION         = 1;
	    private static final String TABLE_NAME   = "Goods";
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
						+ " goodsCode varchar(50) not null unique ,"
						+ "goodsName varchar(50) not null,"
						+ " manufacturerId integer references  Manufacturer  ( " + _ID + " ),"
						+ "goodsFormat varchar(50)not null,"
						+ "kindId integer references GoodsKind  (" + _ID
						+ ") )");
				
				initGoods(db);
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
			  private boolean initGoods(SQLiteDatabase db){
					
					ContentValues value = new ContentValues();
					
					value.clear();
					value.put("goodsCode", "G0001");
					value.put("goodsName", "红塔山");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 8);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G0002");
					value.put("goodsName", "黄梅");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 8);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G0003");
					value.put("goodsName", "哈德门");
					value.put("manufacturerId", 2);
					value.put("goodsFormat", "");
					value.put("kindId", 8);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G0004");
					value.put("goodsName", "古田");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 8);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G0005");
					value.put("goodsName", "七匹狼");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 8);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G0006");
					value.put("goodsName", "一品梅");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 8);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G0007");
					value.put("goodsName", "黄山");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 8);
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
	    
	    public String getGoodsNameByGoodsId(int goodsId){
	
	    	Cursor c = this.query(AllTables.Goods.CONTENT_URI, null, " _id = ? ", new String[]{goodsId+""}, null);
	    	c.moveToFirst();
	    
	    	if(c.getCount()>0)
	    		return c.getString(2);
	    	else
	    		return "";
	    }
	    
	  

}
