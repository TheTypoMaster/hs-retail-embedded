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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class GoodsPriceCPer extends ContentProvider {

		private SQLiteDatabase     sqlDB;
	    private DatabaseHelper    dbHelper;
	    private UnitCPer unitCPer = null;
	    private GoodsCPer gCPer = null;
	    
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
						+ "outPrice double not null,"
						+ "isCigarette integer not null )");
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
				value.put("barcode", "0000000000001");
				value.put("inPrice", 6.0);
				value.put("outPrice", 7.0);
				value.put("isCigarette", 1);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 2);
				value.put("unitId", 2);
				value.put("barcode", "0000000000002");
				value.put("inPrice", 50);
				value.put("outPrice", 60);
				value.put("isCigarette", 1);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 3);
				value.put("unitId", 2);
				value.put("barcode", "0000000000003");
				value.put("inPrice", 70);
				value.put("outPrice", 80);
				value.put("isCigarette", 1);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 4);
				value.put("unitId", 3);
				value.put("barcode", "0000000000004");
				value.put("inPrice", 55);
				value.put("outPrice", 64);
				value.put("isCigarette", 1);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 5);
				value.put("unitId", 3);
				value.put("barcode", "0000000000005");
				value.put("inPrice", 90);
				value.put("outPrice", 100);
				value.put("isCigarette", 1);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 6);
				value.put("unitId", 3);
				value.put("barcode", "0000000000006");
				value.put("inPrice", 100);
				value.put("outPrice", 110);
				value.put("isCigarette", 1);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 7);
				value.put("unitId", 1);
				value.put("barcode", "0000000000007");
				value.put("inPrice", 10);
				value.put("outPrice", 12);
				value.put("isCigarette", 1);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 7);
				value.put("unitId", 2);
				value.put("barcode", "0000000000008");
				value.put("inPrice", 100);
				value.put("outPrice", 120);
				value.put("isCigarette", 1);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 8);
				value.put("unitId", 5);
				value.put("barcode", "0000000000009");
				value.put("inPrice", 233);
				value.put("outPrice", 250);
				value.put("isCigarette", 0);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 9);
				value.put("unitId", 1);
				value.put("barcode", "00000000000010");
				value.put("inPrice", 1.4);
				value.put("outPrice", 2);
				value.put("isCigarette", 0);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 10);
				value.put("unitId", 6);
				value.put("barcode", "0000000000011");
				value.put("inPrice", 4);
				value.put("outPrice", 6);
				value.put("isCigarette", 0);
				db.insertOrThrow(TABLE_NAME, null, value);
				
				value.clear();
				value.put("goodsId", 11);
				value.put("unitId", 7);
				value.put("barcode", "0000000000012");
				value.put("inPrice", 1);
				value.put("outPrice", 1.5);
				value.put("isCigarette", 0);
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
	            Uri rowUri = ContentUris.appendId(AllTables.GoodsPrice.CONTENT_URI.buildUpon(), rowId).build();
	            ct.getContentResolver().notifyChange(rowUri, null);
	            return rowUri;
	        }
	        else
	        {
	        	sqlDB.close();
	        	return null;
	        }
	   //     throw new SQLException("Failed to insert row into " + uri);
	    
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
	    public int getIsCigaretteByBarcode(String barcode){//根据条形码判断是否是香烟
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " barcode = ? " , new String[]{barcode}, null);
	    	
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getInt(6);	    		
	    	}
	    	else
	    		return 0;
	    }
	    public double getInPriceByBarcode(String barcode){
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " barcode = ? " , new String[]{barcode}, null);
	    	
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getDouble(4);
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
	    public List<Integer> getGoodsPriceIdByGoodsId(int goodsId){
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " goodsId = ? ", new String[]{goodsId+""}, null);
	    	if(c.getCount()>0){
	    		List<Integer> priceIdList = new ArrayList<Integer>();
	    		c.moveToFirst();
	    		for(int i=0;i<c.getCount();i++){
	    			priceIdList.add(c.getInt(0));
	    			c.moveToNext();
	    		}
	    		return priceIdList;
	    	}
	    	return new ArrayList<Integer>();
	    }
	    public boolean addGoodsPrice(int goodsId, int unitId, String barcode, double inPrice, double outPrice, Integer isCigarette){
			ContentValues value = new ContentValues();
			value.clear();
			value.put("goodsId", goodsId);
			value.put("unitId", unitId);
			value.put("barcode", barcode);
			value.put("inPrice", inPrice);
			value.put("outPrice", outPrice);
			value.put("isCigarette", isCigarette);
			
			Uri uri = this.insert(AllTables.GoodsPrice.CONTENT_URI, value);
			if(uri!=null)
				return true;
			else
				return false;
		}
	    
	    public List<String> getUnitNameByGoodsId(int goodsId){//根据商品的ID查找单位的名字（一件商品可能有多种单位，比如红塔山的烟有”包“，”条“
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " goodsId = ? ", new String[]{goodsId+""}, null);
	    	List<String> allUnitByGoodsId = new ArrayList<String>();
	  
	     	unitCPer = new UnitCPer();
	   
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		for(int i=0;i<c.getCount();i++){
	    			allUnitByGoodsId.add(unitCPer.getUnitNameById(c.getInt(2)));
	    			c.moveToNext();
	    		}
	    		
	    		return allUnitByGoodsId;
	    	}
	    	return new ArrayList<String>();
	    }
	    //根据商品的Id和单位Id查找进货价
	    public double getInPriceByGoodsIdAndUnitId(int goodsId, int unitId){
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " goodsId = ? and unitId = ? ", new String[]{goodsId+"", unitId+""}, null);
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getDouble(4);
	    	}
	    	return 0;
	    }
	    //根据商品的Id和单位Id查找售价
	    public double getOutPriceByGoodsIdAndUnitId(int goodsId, int unitId){
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " goodsId = ? and unitId = ? ", new String[]{goodsId+"", unitId+""}, null);
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getDouble(5);
	    	}
	    	return 0;
	    }
	    //根据商品的Id和单位Id查找价格项Id
	    public int getPriceIdByGoodsIdAndUnitId(int goodsId, int unitId){
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI,  null, " goodsId = ? and unitId = ? ", new String[]{goodsId+"", unitId+""}, null);
	    	if(c.getCount()>0)
	    	{
	    		c.moveToFirst();
	    		return c.getInt(0);
	    	}
	    	return -1;
	    }

	    public List<String> getInfoByGPriceId(int goodsPriceId){
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " _id = ? ", new String[]{goodsPriceId+""}, null);
	    	if(c.getCount()>0){
	    		gCPer = new GoodsCPer();
	    		unitCPer = new UnitCPer();
	    		c.moveToFirst();
	    		List<String> info = new ArrayList<String>();
	    		
	    		info.add(gCPer.getGoodsNameByGoodsId(c.getInt(1)));
	    		info.add(gCPer.getKindNameByGoodsId(c.getInt(1)));
	    		info.add(unitCPer.getUnitNameById(c.getInt(2)));
	    		info.add(""+c.getDouble(4));
	    		info.add(""+c.getDouble(5));
	    		
	    		return info;
	    	}
	    	return null;
		}
	    public String getBarcodeIdByGoodsPriceId(int priceId){
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " _id = ? " , new String[]{priceId+""}, null);
	    	
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getString(3);
	    	}
	    	else
	    		return null;
	    }
	    public int getGoodsIdByGoodsPriceId(int priceId){//根据商品价格的Id查找商品Id
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " _id = ? ", new String[]{priceId+""}, null);
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getInt(1);
	    	}
	    	return -1;
	    }
	    public int getUnitIdByGoodsPriceId(int priceId){
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " _id = ? ", new String[]{priceId+""}, null);
	    	if(c.getCount()>0)
	    	{
	    		c.moveToFirst();
	    		return c.getInt(2);
	    	}
	    	return -1;
	    }
	    public double getInPriceByGoodsPriceId(int priceId){
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " _id = ? ", new String[]{priceId+""}, null);
	    	if(c.getCount()>0)
	    	{
	    		c.moveToFirst();
	    		return c.getDouble(4);
	    	}
	    	return 0;
	    }
	    public double getOutPriceByGoodsPriceId(int priceId){
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, " _id = ? ", new String[]{priceId+""}, null);
	    	if(c.getCount()>0)
	    	{
	    		c.moveToFirst();
	    		return c.getDouble(5);
	    	}
	    	return 0;
	    }
	    public String getAttributeById(String attribute,String id){
			Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, new String[]{attribute}, "_id = "+"'"+id+"'" , null, null);
			if(c.getCount()>0){
				c.moveToFirst();
				return c.getString(0);
			}else{
				return null;
			}
		}
	    
	    public String getAttributeByAttribute(String attribute,String attribute2,String value){
			Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, new String[]{attribute}, attribute2+" = "+"'"+value+"'" , null, null);
			if(c.getCount()>0){
				c.moveToFirst();
				return c.getString(0);
			}else{
				return null;
			}
		}
	    
	    public List<Integer> getAllPriceId(){
	    	Cursor c = this.query(AllTables.GoodsPrice.CONTENT_URI, null, null, null, null);
	    	
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		List<Integer> allPriceId = new ArrayList<Integer>();
	    		for(int i=0;i<c.getCount();i++){
	    			allPriceId.add(c.getInt(0));
	    			c.moveToNext();
	    		}
	    		return allPriceId;
	    	}
	    	else
	    		return new ArrayList<Integer>();
	    }
	

}
