package com.tobacco.pos.contentProvider;

import static android.provider.BaseColumns._ID;

import java.util.Hashtable;
import java.util.Map;

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
	    private GoodsKindCPer gKindCPer = null;
	    private ManufacturerCPer mCPer = null;
	    
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
					value.put("goodsCode", "G1");
					value.put("goodsName", "红塔山");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 8);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G2");
					value.put("goodsName", "黄梅");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 10);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G3");
					value.put("goodsName", "哈德门");
					value.put("manufacturerId", 2);
					value.put("goodsFormat", "");
					value.put("kindId", 8);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G4");
					value.put("goodsName", "古田");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 8);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G5");
					value.put("goodsName", "七匹狼");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 8);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G6");
					value.put("goodsName", "一品梅");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 8);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G7");
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
	    	dbHelper = new DatabaseHelper(ct);
	        sqlDB = dbHelper.getWritableDatabase();
	        long rowId = sqlDB.insert(TABLE_NAME, "", contentvalues);
	        if (rowId > 0) {
	            Uri rowUri = ContentUris.appendId(AllTables.Unit.CONTENT_URI.buildUpon(), rowId).build();
	           	ct.getContentResolver().notifyChange(rowUri, null);
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
	    
	    public String getGoodsNameByGoodsId(int goodsId){
	
	    	Cursor c = this.query(AllTables.Goods.CONTENT_URI, null, " _id = ? ", new String[]{goodsId+""}, null);
	    	c.moveToFirst();
	    
	    	if(c.getCount()>0)
	    		return c.getString(2);
	    	else
	    		return "";
	    }
	    public int addGoods(String goodsName, int mId, String goodsFormat, int kindId){//增加商品成功后腰返回刚加入的记录的ID，因为在后续的添加价格中需要用到这个ID
	    	String goodsCode = "";//由于商品的代码是系统按照已有的数据生成的，所以不作为参数
	    	ContentValues value = new ContentValues();
	    	value.clear();
	    	Cursor c1 = this.query(AllTables.Goods.CONTENT_URI, null, null, null, " _id ");
	    	String maxGoodsCode = "";
	    	if(c1.getCount()>0){
	    		c1.moveToLast();
	    		maxGoodsCode = c1.getString(1);
	    		goodsCode = "G"+(Integer.parseInt(maxGoodsCode.substring(1)) + 1);
	    	}
			value.put("goodsCode", goodsCode);
			value.put("goodsName", goodsName);
			value.put("manufacturerId", mId);
			value.put("goodsFormat", goodsFormat);
			value.put("kindId", kindId);
			
			if(this.insert(AllTables.Goods.CONTENT_URI, value)!=null){
				Cursor c2 = this.query(AllTables.Goods.CONTENT_URI, null, null, null, " _id ");
				c2.moveToLast();
				return c2.getInt(0);				
			}
			else
				return -1;
	    }
	    public Map<Integer, String> getAllGoodsName(){//以Map的形式返回，格式是商品ID+商品名字+厂家+类别
	    	gKindCPer = new GoodsKindCPer();
	    	mCPer = new ManufacturerCPer();
	    	
	    	Cursor c = this.query(AllTables.Goods.CONTENT_URI, null, null, null, null);
	    	
	    	Map<Integer, String> theMap = new Hashtable<Integer, String>();
	   
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		for(int i=0;i<c.getCount();i++){
	    			String kindName = gKindCPer.getGoodsKindNameByGoodsKindId(c.getInt(5));
	    			if(kindName.contains("->"))
	    				kindName = kindName.substring(kindName.lastIndexOf("->")+2);
	    			theMap.put(c.getInt(0), 
	    					c.getString(2) + "-" + mCPer.getMNameByMId(c.getInt(3)) + "-" + kindName);
	    			c.moveToNext();
	    		}
	    		return theMap;
	    	}
	    	else
	    		return null;
	    }
	    
}
