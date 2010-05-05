package com.tobacco.pos.contentProvider;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import com.tobacco.pos.Module.AllTables;

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

public class SalesItemCPer extends ContentProvider {

		private SQLiteDatabase     sqlDB;
	    private DatabaseHelper    dbHelper;
	    private static final String  DATABASE_NAME     = "AllTables.db";
	    private static final int        DATABASE_VERSION         = 1;
	    private static final String TABLE_NAME   = "SalesItem";
	    private static Context ct = null;
	    private SalesBillCPer sBillCPer = null;
	    private GoodsPriceCPer gPriceCPer = null;
	    private UnitCPer unitCPer = null;
	    private GoodsCPer gCPer = null;
	    private VIPInfoCPer vipInfoCPer = null;
	    
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
						+ " salesBillId integer references SalesBill ( " + _ID + " ), "
						+ " sGoodsNum integer not null ,"
					    + " barcode varchar(20) references GoodsPrice ( barcode ),"
					    + " inPrice double not null, " 
					    + " outPrice double not null )" );
				initSalesItem(db);
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

			private boolean initSalesItem(SQLiteDatabase db) {

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
	    public ArrayList<ArrayList<String>> getSalesItemBySalesBillId(int salesBillId){
	    	Cursor c = this.query(AllTables.SalesItem.CONTENT_URI, null, " salesBillId = ? ", new String[]{salesBillId+""}, null);
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		gPriceCPer = new GoodsPriceCPer();
	    		unitCPer = new UnitCPer();
	    		gCPer = new GoodsCPer();
	    		ArrayList<ArrayList<String>> salesItemList = new ArrayList<ArrayList<String>>();
	    		//根据salesBill的Id查找该销售单下的所有salesItem，salesItem包含销售项的Id，销售单Id，销售量，条形码，进价，售价
	    		for(int i=0;i<c.getCount();i++){
	    			ArrayList<String> temp = new ArrayList<String>();
	    			
	    			temp.add(sBillCPer.getSBillNumBySBillId(c.getInt(1)));//销售单编号
	    		
	    			int goodsId = gPriceCPer.getGoodsIdByBarcode(c.getString(3));//根据条形码获得商品的Id
	    			temp.add(gCPer.getGoodsNameByGoodsId(goodsId));//根据商品的Id查找名称
	    			temp.add(gCPer.getGoodsKindNameByGoodsId(goodsId));//根据商品的Id查找种类名称
	    			temp.add(c.getInt(2) + "");//销售量
	    			temp.add(unitCPer.getUnitNameById(gPriceCPer.getUnitIdByBarcode(c.getString(3))));//根据条形码获得单位Id，然后再取得名字
	    			temp.add(c.getDouble(4) + "");//获得进价
	    			temp.add(c.getDouble(5) + "");//获得售价
	    			
	    			salesItemList.add(temp);
	    			
	    			c.moveToNext();
	    		}
	    		return salesItemList;
	    	}
	    	return new ArrayList<ArrayList<String>>();
	    }
	    public boolean addSalesItem(int newSBillId, int goodsCount, String barcode, double inPrice, double outPrice, double vipDiscountRate){
	    	
	    	ContentValues value = new ContentValues();
			
			value.clear();
			value.put("salesBillId", newSBillId);
			value.put("sGoodsNum", goodsCount);
			value.put("barcode", barcode);
			value.put("inPrice", inPrice);
			value.put("outPrice", outPrice * vipDiscountRate);
		
			Uri uri = this.insert(AllTables.SalesItem.CONTENT_URI, value);
			if(uri!=null)
				return true;
			else
				return false;
	    }
	    
	    public ArrayList<ArrayList<ArrayList<String>>> getSalesInfoByVIPNum(String startTime, String endTime, int VIPId){
	    	sBillCPer = new SalesBillCPer();
	    	
	    	if(!startTime.equals("开始时间"))
	    		startTime += " 00:00:00";
	    	if(!endTime.equals("结束时间"))
	    		endTime += " 23:59:59";
	    	
	    	List<Integer> sBillIdList = sBillCPer.getSalesBillIdByVIPId(startTime, endTime, VIPId);
	    	if(sBillIdList.size()>0){
	    		ArrayList<ArrayList<ArrayList<String>>> result = new ArrayList<ArrayList<ArrayList<String>>>();
	    		for(int i=0;i<sBillIdList.size();i++){
	    			ArrayList<ArrayList<String>> tempResult = this.getSalesItemBySalesBillId(sBillIdList.get(i));
	    			result.add(tempResult);
	    		}
	    		return result;
	    	}
	    	return new ArrayList<ArrayList<ArrayList<String>>>();
	    }
	    public ArrayList<ArrayList<String>> getSalesInfoByKindName(String startTime, String endTime, String kindName){
	    	sBillCPer = new SalesBillCPer();
	    	
	    	if(!startTime.equals("开始时间"))
	    		startTime += " 00:00:00";
	    	if(!endTime.equals("结束时间"))
	    		endTime += " 23:59:59";
	    	
	    	List<Integer> sBillIdList = sBillCPer.getSBillIdByTime(startTime, endTime);
	    	if(sBillIdList.size() == 0)
	    		return new ArrayList<ArrayList<String>>();
	    	else{
	    		ArrayList<ArrayList<String>> sItem = new ArrayList<ArrayList<String>> ();
	    		for(int i=0;i<sBillIdList.size();i++){
	    			ArrayList<ArrayList<String>> sItemBySBillId = this.getSalesItemBySalesBillId(sBillIdList.get(i));
	    			for(int j=0;j<sItemBySBillId.size();j++){
	    				ArrayList<String> temp = sItemBySBillId.get(j);
	    				String gName = temp.get(2);
	    				if(gName.contains(kindName)){
	    					sItem.add(temp);
	    					sItemBySBillId.remove(temp);
	    				}
	    			}	
	    		}
	    		return sItem;
	    	
	    	}
	    }
	    public ArrayList<ArrayList<String>> getSalesInfoByGoodsName(String startTime, String endTime, String goodsName){//根据商品的名字查找销售信息
	    	sBillCPer = new SalesBillCPer();
	    	
	    	if(!startTime.equals("开始时间"))
	    		startTime += " 00:00:00";
	    	if(!endTime.equals("结束时间"))
	    		endTime += " 23:59:59";
	    	
	    	List<Integer> sBillIdList = sBillCPer.getSBillIdByTime(startTime, endTime);
	    	if(sBillIdList.size() == 0)
	    		return new ArrayList<ArrayList<String>>();
	    	else{
	    		ArrayList<ArrayList<String>> sItem = new ArrayList<ArrayList<String>> ();
	    		for(int i=0;i<sBillIdList.size();i++){
	    			ArrayList<ArrayList<String>> sItemBySBillId = this.getSalesItemBySalesBillId(sBillIdList.get(i));
	    			for(int j=0;j<sItemBySBillId.size();j++){
	    				ArrayList<String> temp = sItemBySBillId.get(j);
	    				String gName = temp.get(1);
	    				if(gName.contains(goodsName)){
	    					sItem.add(temp);
	    					sItemBySBillId.remove(temp);
	    				}
	    			}	
	    		}
	    		return sItem;
	    	}
	    	
	    	   	
	    }
	    public Map<String, ArrayList<ArrayList<String>>> getSalesInfoBySalesBillNum(String startTime, String endTime, String salesBillNum){
	    	sBillCPer = new SalesBillCPer();
	    	
	    	if(!startTime.equals("开始时间"))
	    		startTime += " 00:00:00";
	    	if(!endTime.equals("结束时间"))
	    		endTime += " 23:59:59";
	    	
	    	int salesBillId = sBillCPer.getSBillIdBySBillNum(startTime, endTime, salesBillNum);//查找到相应的相销售单Id
	    	if(salesBillId == -1)//没有编号为salesBillNum的销售单
	    		return new HashMap<String,ArrayList<ArrayList<String>>>();
	    	else{
	    		vipInfoCPer = new VIPInfoCPer();
	    		gPriceCPer = new GoodsPriceCPer();
	    		gCPer = new GoodsCPer();
	    		unitCPer = new UnitCPer();
	    		int VIPId = sBillCPer.getVIPIdBySBillId(salesBillId);
	    		String VIPName = "";
	    		if(VIPId == -1)
	    			VIPName = "普通客户";
	    		else
	    			VIPName = vipInfoCPer.getVIPNameByVIPId(VIPId);
	    		Map<String, ArrayList<ArrayList<String>>> resultMap = new HashMap<String,ArrayList<ArrayList<String>>>();
	    		Cursor c = this.query(AllTables.SalesItem.CONTENT_URI, null, " salesBillId = ? ", new String[]{salesBillId+""}, null);
	    		if(c.getCount()>0){
	    			ArrayList<ArrayList<String>> sItemResult = new ArrayList<ArrayList<String>>();
	    			c.moveToFirst();
	    			for(int i=0;i<c.getCount();i++){
	    				ArrayList<String> temp = new ArrayList<String>();
	    				int goodsId = gPriceCPer.getGoodsIdByBarcode(c.getString(3));//根据条形码获得商品的Id
	    				
	    				temp.add(salesBillNum);//销售单号
	    				temp.add(gCPer.getGoodsNameByGoodsId(goodsId));//获取商品名称
	    				temp.add(gCPer.getGoodsKindNameByGoodsId(goodsId));//获取种类名称
	    				temp.add(c.getInt(2) + "");//获取数量
	    				temp.add(unitCPer.getUnitNameById(gPriceCPer.getUnitIdByBarcode(c.getString(3))));//根据条形码获得单位Id，然后再取得名字
	    				temp.add(c.getDouble(4)+"");//获取进价
	    				temp.add(c.getDouble(5)+"");//获取售价
	    				
	    				sItemResult.add(temp);
	    				c.moveToNext();
	    				
	    			}
	    			resultMap.put(VIPName, sItemResult);
	    			return resultMap;
	    		}
	    		return new HashMap<String,ArrayList<ArrayList<String>>>();
	    		
	    	}
	    }
}
