package com.tobacco.pos.contentProvider;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
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
	    private GoodsPriceCPer gPriceCPer = null;
	    private PurchaseItemCPer pItemCPer = null;
	    private PurchaseBillCPer pBillCPer = null;
	    private UnitCPer unitCPer = null;
	    
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
					value.put("kindId", 15);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G2");
					value.put("goodsName", "黄梅");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 15);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G3");
					value.put("goodsName", "哈德门");
					value.put("manufacturerId", 2);
					value.put("goodsFormat", "");
					value.put("kindId", 15);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G4");
					value.put("goodsName", "古田");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 15);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G5");
					value.put("goodsName", "七匹狼");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 15);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G6");
					value.put("goodsName", "一品梅");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 15);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G7");
					value.put("goodsName", "黄山");
					value.put("manufacturerId", 1);
					value.put("goodsFormat", "");
					value.put("kindId", 15);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G8");
					value.put("goodsName", "白衬衫");
					value.put("manufacturerId", 5);
					value.put("goodsFormat", "");
					value.put("kindId", 2);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G9");
					value.put("goodsName", "香芋饼");
					value.put("manufacturerId", 6);
					value.put("goodsFormat", "");
					value.put("kindId", 14);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G10");
					value.put("goodsName", "麦克笔");
					value.put("manufacturerId", 9);
					value.put("goodsFormat", "");
					value.put("kindId", 11);
					db.insertOrThrow(TABLE_NAME, null, value);
					
					value.clear();
					value.put("goodsCode", "G11");
					value.put("goodsName", "宣纸");
					value.put("manufacturerId", 9);
					value.put("goodsFormat", "");
					value.put("kindId", 11);
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
	    public String getKindNameByGoodsId(int goodsId){
	    	Cursor c = this.query(AllTables.Goods.CONTENT_URI, null, " _id = ? ", new String[]{goodsId+""}, null);
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		gKindCPer = new GoodsKindCPer();
	    		String kindName = gKindCPer.getGoodsKindNameByGoodsKindId(c.getInt(5));
	    		if(kindName.contains("->"))
	    			kindName = kindName.substring(kindName.lastIndexOf("->") + 2);
	    		return kindName;
	    	}
	    	return "";
	    }
	    public ArrayList<ArrayList<String>> getPInfoByGoodsKindName(String startTime, String endTime,String kindName){
	    	gKindCPer = new GoodsKindCPer();
	    	gPriceCPer = new GoodsPriceCPer();
	    	pItemCPer = new PurchaseItemCPer();
	    	pBillCPer = new PurchaseBillCPer();
			unitCPer = new UnitCPer();
			
	    	List<Integer> satisfiedKindIdList = gKindCPer.getGoodsKindIdListByGoodsKind(kindName);
	    	
	    	if(satisfiedKindIdList.size()>0){
	    		Cursor c = this.query(AllTables.Goods.CONTENT_URI, null, null, null, null);
	    		if(c.getCount()>0){
	    		
	    			List<Integer> satisfiedGoodsIdList = new ArrayList<Integer>();//种类名称包含kindName的商品的Id。
	    			c.moveToFirst();
	    			for(int i=0;i<c.getCount();i++){
	    				int tempGoodsId = c.getInt(0);
	    				int tempGoodsKindId = c.getInt(5);
	    				if(satisfiedKindIdList.contains(tempGoodsKindId)){
	    					satisfiedGoodsIdList.add(tempGoodsId);
	    				}
	    				c.moveToNext();
	    			}
	    			List<Integer> satisfiedGoodsPriceIdList = new ArrayList<Integer>();//
	    			for(int i=0;i<satisfiedGoodsIdList.size();i++){
	    				List<Integer> tempPriceList = gPriceCPer.getGoodsPriceIdByGoodsId(satisfiedGoodsIdList.get(i));
	    				satisfiedGoodsPriceIdList.addAll(tempPriceList);
	    			}
	    			ArrayList<ArrayList<String>> pInfoList = new ArrayList<ArrayList<String>>();
	    			ArrayList<ArrayList<Integer>> allPItemList = new ArrayList<ArrayList<Integer>>();
	    			for(int i=0;i<satisfiedGoodsPriceIdList.size();i++){
	    				
	    				ArrayList<ArrayList<Integer>> temp = pItemCPer.getPItemByPriceId(satisfiedGoodsPriceIdList.get(i));
	    				allPItemList.addAll(temp);
	    				
	    			}
	    			for(int i=0;i<allPItemList.size();i++){
	    				ArrayList<Integer> t = allPItemList.get(i);
	    				int pBillId = t.get(1);//进货单Id
	    				int pPriceId = t.get(3);
	    				
	    				boolean flag = true;//时间是否符合的标志
	    				
	    				if(startTime.equals("开始时间") && endTime.equals("结束时间"))//没设时间
		    				flag = true;
		    			else if(!startTime.equals("开始时间") && endTime.equals("结束时间")){//有设开始时间，没设结束时间
		    				String pTime = pBillCPer.getTimeByPBillId(pBillId);
		    				startTime += " 00:00:00";
		    				if(pTime.compareTo(startTime) > 0)
		    					flag = true;
		    				else
		    					flag = false;
		    			}
		    			else if(startTime.equals("开始时间") && !endTime.equals("结束时间")){//没设开始时间，有设结束时间
		    				String pTime = pBillCPer.getTimeByPBillId(pBillId);
		    				endTime += " 23:59:59";
		    				if(pTime.compareTo(endTime) < 0)
		    					flag = true;
		    				else
		    					flag = false;
		    			}
		    			else{//有设开始时间，也有设结束时间
		    				String pTime = pBillCPer.getTimeByPBillId(pBillId);
		    				startTime += " 00:00:00";
		    				endTime += " 23:59:59";
		    				if(startTime.compareTo(pTime) < 0 && pTime.compareTo(endTime) < 0)
		    					flag = true;
		    				else
		    					flag = false;
		    			}
		    			
	    				if(flag){
	    					
	    					String pBillNum = pBillCPer.getPBillNumByPBillId(pBillId);//进货单号
		    				String allGoodsName = this.getGoodsNameByGoodsId(gPriceCPer.getGoodsIdByGoodsPriceId(pPriceId));//商品的全名
		    				String goodsKind = this.getGoodsKindNameByGoodsId(gPriceCPer.getGoodsIdByGoodsPriceId(pPriceId));//商品种类
		    				if(goodsKind.contains("->"))
		    					goodsKind = goodsKind.substring(goodsKind.lastIndexOf("->")+2);
		    				int pCount = t.get(2);//进货数量
		    				String unitName = unitCPer.getUnitNameById(gPriceCPer.getUnitIdByGoodsPriceId(pPriceId));
		    				double inPrice = gPriceCPer.getInPriceByGoodsPriceId(pPriceId);
		    				double outPrice = gPriceCPer.getOutPriceByGoodsPriceId(pPriceId);
		    				ArrayList<String> te = new ArrayList<String>();
		    				te.add(pBillNum);
		    				te.add(allGoodsName);
		    				te.add(goodsKind);
		    				te.add(pCount+"");
		    				te.add(unitName);
		    				te.add(inPrice+"");
		    				te.add(outPrice+"");
		    				pInfoList.add(te);
	    				}
	    				
	    			}
	    			return pInfoList;
	    		}
	    		return null;
	    	}
	    	return null;
	    }
	    public ArrayList<ArrayList<String>> getPInfoByGoodsName(String startTime, String endTime, String goodsName){//根据商品的名字查询关于该商品的进货信息，此处需要模糊查找。
	    
	    	gPriceCPer = new GoodsPriceCPer();
	    	pItemCPer = new PurchaseItemCPer();
	    	List<Integer> goodsIdList = new ArrayList<Integer>();
	    	List<Integer> priceIdList = new ArrayList<Integer>();
	    	Cursor c = this.query(AllTables.Goods.CONTENT_URI, null, " goodsName like '%" + goodsName + "%'", null, null);
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		for(int i=0;i<c.getCount();i++){
	    			goodsIdList.add(c.getInt(0));
	    			c.moveToNext();
	    		}
	    		for(int i=0;i<goodsIdList.size();i++){
	    			List<Integer> temp = gPriceCPer.getGoodsPriceIdByGoodsId(goodsIdList.get(i));
	    			for(int j=0;j<temp.size();j++){
	    				priceIdList.add(temp.get(j));
	    			}
	    		}
	    		ArrayList<ArrayList<Integer>> allPItem = new ArrayList<ArrayList<Integer>>();
	    		for(int i=0;i<priceIdList.size();i++){
	    		
	    			ArrayList<ArrayList<Integer>> temp = pItemCPer.getPItemByPriceId(priceIdList.get(i));
	    			allPItem.addAll(temp);
	    		}
	    		pBillCPer = new PurchaseBillCPer();
	    		unitCPer = new UnitCPer();
	    		ArrayList<ArrayList<String>> pInfoList = new ArrayList<ArrayList<String>>();
	    		for(int i=0;i<allPItem.size();i++){
	    			ArrayList<Integer> temp = allPItem.get(i);
	    			
	    			int pBillId = temp.get(1);
	    			int pPriceId = temp.get(3);
	    			
	    			boolean flag = true;//时间是否符合的标志
	    			
	    			if(startTime.equals("开始时间") && endTime.equals("结束时间"))//没设时间
	    				flag = true;
	    			else if(!startTime.equals("开始时间") && endTime.equals("结束时间")){//有设开始时间，没设结束时间
	    				String pTime = pBillCPer.getTimeByPBillId(pBillId);
	    				startTime += " 00:00:00";
	    				if(pTime.compareTo(startTime) > 0)
	    					flag = true;
	    				else
	    					flag = false;
	    			}
	    			else if(startTime.equals("开始时间") && !endTime.equals("结束时间")){//没设开始时间，有设结束时间
	    				String pTime = pBillCPer.getTimeByPBillId(pBillId);
	    				endTime += " 23:59:59";
	    				if(pTime.compareTo(endTime) < 0)
	    					flag = true;
	    				else
	    					flag = false;
	    			}
	    			else{//有设开始时间，也有设结束时间
	    				String pTime = pBillCPer.getTimeByPBillId(pBillId);
	    				startTime += " 00:00:00";
	    				endTime += " 23:59:59";
	    				if(startTime.compareTo(pTime) < 0 && pTime.compareTo(endTime) < 0)
	    					flag = true;
	    				else
	    					flag = false;
	    			}
	    			
	    			if(flag){
	    				String pBillNum = pBillCPer.getPBillNumByPBillId(pBillId);//进货单号
	    				String allGoodsName = this.getGoodsNameByGoodsId(gPriceCPer.getGoodsIdByGoodsPriceId(pPriceId));//商品的全名
	    				String goodsKind = this.getGoodsKindNameByGoodsId(gPriceCPer.getGoodsIdByGoodsPriceId(pPriceId));//商品种类
	    				if(goodsKind.contains("->"))
	    					goodsKind = goodsKind.substring(goodsKind.lastIndexOf("->")+2);
	    				int pCount = temp.get(2);//进货数量
	    				String unitName = unitCPer.getUnitNameById(gPriceCPer.getUnitIdByGoodsPriceId(pPriceId));
	    				double inPrice = gPriceCPer.getInPriceByGoodsPriceId(pPriceId);
	    				double outPrice = gPriceCPer.getOutPriceByGoodsPriceId(pPriceId);
	    				ArrayList<String> t = new ArrayList<String>();
	    				t.add(pBillNum);
	    				t.add(allGoodsName);
	    				t.add(goodsKind);
	    				t.add(pCount+"");
	    				t.add(unitName);
	    				t.add(inPrice+"");
	    				t.add(outPrice+"");
	    				pInfoList.add(t);
	    			}
	    		}
	    		return pInfoList;
	    	}
	    	return null;
	    }
	    public String getGoodsKindNameByGoodsId(int goodsId){
	    	Cursor c = this.query(AllTables.Goods.CONTENT_URI, null, " _id = ? ", new String[]{goodsId+""}, null);
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		gKindCPer = new GoodsKindCPer();
	    		return gKindCPer.getGoodsKindNameByGoodsKindId(c.getInt(5));
	    		
	    	}
	    	return "";
	    }
	    public int getGoodsKindIdByGoodsId(int goodsId){//根据商品的Id查找其类别的Id
	    	Cursor c = this.query(AllTables.Goods.CONTENT_URI, null, " _id = ? ", new String[]{goodsId+""}, null);
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getInt(5);
	    	}
	    	return -1;
	    }
	    public String getAttributeById(String attribute,String id){
			Cursor c = this.query(AllTables.Goods.CONTENT_URI, new String[]{attribute}, " _id = "+"'"+id+"'" , null, null);
			if(c.getCount()>0){
				c.moveToFirst();
				return c.getString(0);
			}else{
				return null;
			}
		}
	   
	    public String getAttributeByAttribute(String attribute,String attribute2,String value){
			Cursor c = this.query(AllTables.Goods.CONTENT_URI, new String[]{attribute}, attribute2+" = "+"'"+value+"'" , null, null);
			if(c.getCount()>0){
				c.moveToFirst();
				return c.getString(0);
			}else{
				return null;
			}
		}

}
;