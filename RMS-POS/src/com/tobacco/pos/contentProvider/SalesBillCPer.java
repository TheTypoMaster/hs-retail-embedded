package com.tobacco.pos.contentProvider;

import static android.provider.BaseColumns._ID;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class SalesBillCPer extends ContentProvider {

	  private SQLiteDatabase     sqlDB;
	    private DatabaseHelper    dbHelper;
	    private static final String  DATABASE_NAME     = "AllTables.db";
	    private static final int        DATABASE_VERSION         = 1;
	    private static final String TABLE_NAME   = "SalesBill";
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
	    
	    public int addSBill(int operId, int VIPId){
	    	SimpleDateFormat dateFormater = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	    	Cursor c =this.query(AllTables.SalesBill.CONTENT_URI, null, null, null, " sBillNum ");
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
			Date d = new Date();
			value.put("time", dateFormater.format(d));
			value.put("VIPId", VIPId);
			
			this.insert(AllTables.SalesBill.CONTENT_URI, value);
			
			c = this.query(AllTables.SalesBill.CONTENT_URI, null, null, null, null);	
			c.moveToLast();
			return c.getInt(0);//获取最后添加的销售单的ID
		}
	    public String getSBillNumBySBillId(int newSBillId){
	    	Cursor c = this.query(AllTables.SalesBill.CONTENT_URI, null, " _id = " + newSBillId, null, null);
	    	
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getString(1);
	    	}
	    	return "";
	    }
	    public int getSBillIdBySBillNum(String startTime, String endTime, String salesBillNum){//根据销售单的编号以及时间查找销售单Id,
	    	Cursor c = null;
	    	
	    	if(startTime.equals("开始时间") && endTime.equals("结束时间"))//没时间约束
	    		c = this.query(AllTables.SalesBill.CONTENT_URI, null, " sBillNum = ? ", new String[]{salesBillNum}, null);
	    	else if(!startTime.equals("开始时间") && endTime.equals("结束时间"))//限定开始时间
	    		c = this.query(AllTables.SalesBill.CONTENT_URI, null, " time >= ? and sBillNum = ? ", new String[]{startTime, salesBillNum}, null);
	    	else if(startTime.equals("开始时间") && !endTime.equals("结束时间"))//有限定结束时间
	    		c = this.query(AllTables.SalesBill.CONTENT_URI, null, " time <= ? and sBillNum = ? ", new String[]{endTime, salesBillNum}, null);
	    	else//有开始时间限制也有结束时间限制
	    		c = this.query(AllTables.SalesBill.CONTENT_URI, null, " time between ? and ? and sBillNum = ? ", new String[]{startTime, endTime, salesBillNum}, null);
	    	
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getInt(0);
	    	}
	    	return -1;
	    }
	    public String getSTimeBySBillId(int salesBillId){//根据销售单的Id查找时间
	    	Cursor c = this.query(AllTables.SalesBill.CONTENT_URI, null, " _id = ? ", new String[]{""+salesBillId}, null);
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getString(3);
	    	}
	    	return "";
	    }
	    public int getVIPIdBySBillId(int salesBillId){//根据销售单的Id查找客户Id
	    	Cursor c = this.query(AllTables.SalesBill.CONTENT_URI, null, " _id = ? ", new String[]{""+salesBillId}, null);
	    	if(c.getCount()>0){
	    		c.moveToFirst();
	    		return c.getInt(4);
	    	}
	    	return -1;
	    }
	   
	    public List<Integer> getSalesBillIdByVIPId(String startTime, String endTime, int VIPId){//根据时间，客户Id查找满足条件的所有SalesBill的Id
	    	Cursor c = null;
	    	if(startTime.equals("开始时间") && endTime.equals("结束时间"))//没有时间限制
	    		c = this.query(AllTables.SalesBill.CONTENT_URI, null, " VIPId = ? ", new String[]{VIPId+""},null);
	    	else if(!startTime.equals("开始时间") && endTime.equals("结束时间"))//有限制开始时间
	    		c = this.query(AllTables.SalesBill.CONTENT_URI, null, " time >= ? and VIPId = ? ", new String[]{startTime, VIPId+""}, null);
	    	else if(startTime.equals("开始时间") && !endTime.equals("结束时间"))//有设置结束时间
	    		c = this.query(AllTables.SalesBill.CONTENT_URI, null, " time <= ? and VIPId = ? ", new String[]{endTime, VIPId+""}, null);
	    	else
	    		c = this.query(AllTables.SalesBill.CONTENT_URI, null, " time between ? and ?  and VIPId = ? ", new String[]{startTime, endTime, VIPId+""}, null);
	    	if(c.getCount()>0){
	    		List<Integer> sBillIdList = new ArrayList<Integer>();
	    		c.moveToFirst();
	    		for(int i=0;i<c.getCount();i++){
	    			sBillIdList.add(c.getInt(0));
	    			c.moveToNext();
	    		}
	    		return sBillIdList;
	    	}
	    	else
	    		return new ArrayList<Integer>();
	    }

}
