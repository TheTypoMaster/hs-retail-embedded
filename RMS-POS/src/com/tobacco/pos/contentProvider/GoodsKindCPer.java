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

public class GoodsKindCPer extends ContentProvider {
		private SQLiteDatabase     sqlDB;
	    private DatabaseHelper    dbHelper;
	    private static final String  DATABASE_NAME     = "AllTables.db";
	    private static final int        DATABASE_VERSION         = 1;
	    private static final String TABLE_NAME   = "GoodsKind";
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
						+ " name varchar(50) not null, "
						+ " parent integer references " + TABLE_NAME + " ( " + _ID
						+ " )," + " level integer not null, "
						+ " comment varchar(100) )");
				
				initGoodsKind(db);
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
			private boolean initGoodsKind(SQLiteDatabase db) {
				
				ContentValues value = new ContentValues();
				value.put("name", "衣服");
				value.put("parent", 0);
				value.put("level", 1);
				value.put("comment", "衣服分类，所有的衣物都是该类的子类");

				db.insertOrThrow(TABLE_NAME, null, value);

				value.clear();
				value.put("name", "衣服->男装");
				value.put("parent", 1);
				value.put("level", 2);
				value.put("comment", "男装Man");

				db.insertOrThrow(TABLE_NAME, null, value);

				value.clear();
				value.put("name", "衣服->女装");
				value.put("parent", 1);
				value.put("level", 2);
				value.put("comment", "女装Woman");

				db.insertOrThrow(TABLE_NAME, null, value);

				value.clear();
				value.put("name", "衣服->男装->男羽绒服");
				value.put("parent", 2);
				value.put("level", 3);
				value.put("comment", "冬天来了，有温度才有风度");

				db.insertOrThrow(TABLE_NAME, null, value);

				value.clear();
				value.put("name", "衣服->男装->男裤");
				value.put("parent", 2);
				value.put("level", 3);
				value.put("comment", "呵呵，可别忘了穿裤子喔，要不就糗大了");

				db.insertOrThrow(TABLE_NAME, null, value);

				value.clear();
				value.put("name", "衣服->童装");
				value.put("parent", 1);
				value.put("level", 2);
				value.put("comment", "童装世界");

				db.insertOrThrow(TABLE_NAME, null, value);

				value.clear();
				value.put("name", "衣服->女装->女式围巾");
				value.put("parent", 3);
				value.put("level", 3);
				value.put("comment", "冷了，不要冻到脖子了");

				db.insertOrThrow(TABLE_NAME, null, value);

				value.clear();
				value.put("name", "卷烟");
				value.put("parent", 0);
				value.put("level", 1);
				value.put("comment", "抽烟并不是很帅，身体更重要喔");

				db.insertOrThrow(TABLE_NAME, null, value);

				value.clear();
				value.put("name", "水果");
				value.put("parent", 0);
				value.put("level", 1);
				value.put("comment", "多吃水果，对身体有益");

				db.insertOrThrow(TABLE_NAME, null, value);

				value.clear();
				value.put("name", "卷烟->软盒");
				value.put("parent", 8);
				value.put("level", 2);
				value.put("comment", "软盒卷烟");

				db.insertOrThrow(TABLE_NAME, null, value);

				value.clear();
				value.put("name", "文具");
				value.put("parent", 0);
				value.put("level", 1);
				value.put("comment", "学生的必备之物");

				db.insert(TABLE_NAME, null, value);

				value.clear();
				value.put("name", "文具->铅笔");
				value.put("parent", 11);
				value.put("level", 2);
				value.put("comment", "使用时要注意环保");

				db.insertOrThrow(TABLE_NAME, null, value);

				value.clear();
				value.put("name", "文具->钢笔");
				value.put("parent", 11);
				value.put("level", 2);
				value.put("comment", "成功人士的必备之物");

				db.insertOrThrow(TABLE_NAME, null, value);

				return true;
			}

	    } 

	    @Override
	    public int delete(Uri uri, String s, String[] as) {
	    	 SQLiteDatabase db = dbHelper.getWritableDatabase();
	         int count;
	         
	         count = db.delete(TABLE_NAME, s, null);
	         ct.getContentResolver().notifyChange(uri, null);
	         return count;
	       
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
	    	 SQLiteDatabase db = dbHelper.getWritableDatabase();
	         int count;
	       
	         count = db.update(TABLE_NAME, contentvalues, s, as);
	     
	         ct.getContentResolver().notifyChange(uri, null);
	         return count;
	       
	    }

		public List<String> getAllGoodsKindName() {
			Cursor c = this.query(AllTables.GoodsKind.CONTENT_URI, null, null, null, null);
			if(c.getCount()>0){
				c.moveToFirst();
				List<String> allGoodsKindName = new ArrayList<String>();
				for(int i=0;i<c.getCount();i++){
					allGoodsKindName.add(c.getString(1));
					c.moveToNext();
				}
				return allGoodsKindName;
			}
			return null;
		}
		
		public int getGoodsKindIdByGoodsKindName(String gKindName){
			Cursor c = this.query(AllTables.GoodsKind.CONTENT_URI, null, " name = ? ", new String[]{gKindName}, null);
			if(c.getCount()>0){
				c.moveToFirst();
				return c.getInt(0);
			}
			else
				return -1;
		}
		public String getGoodsKindNameByGoodsKindId(int kindId){
			Cursor c = this.query(AllTables.GoodsKind.CONTENT_URI, null, " _id = ? ", new String[]{kindId+""}, null);
			if(c.getCount()>0){
				c.moveToFirst();
				return c.getString(1);
			}
			return "";
		}
		public String getGoodsKindInfoByGoodsKindId(int kindId){
			Cursor c = this.query(AllTables.GoodsKind.CONTENT_URI, null, " _id = ? ", new String[]{kindId+""}, null);
			if(c.getCount()>0){
				c.moveToFirst();
				
				StringBuffer strBuffer = new StringBuffer();
				strBuffer.append("ID:"+kindId+"\n");
				strBuffer.append("名字:"+c.getString(1)+"\n");
				strBuffer.append("备注:"+c.getString(4));
				
				return strBuffer.toString();
			}
			return "";
		}
		public boolean isAExistingKind(String kindName){//判断是否已经有同样名字的类别
			Cursor c = this.query(AllTables.GoodsKind.CONTENT_URI, null, " name = ? ", new String[]{kindName}, null);
			if(c.getCount()>0)
				return true;//存在同样名字的类别
			else
				return false;//不存在同样的类别
		}
		public boolean hasChildrenKind(int kindId){//ID为kindID的类别是否还有子类别
			Cursor c = this.query(AllTables.GoodsKind.CONTENT_URI, null, " parent = ? ", new String[]{kindId+""}, null);

			if(c.getCount()>0)
				return true;
			else
				return false;
		}

		public List<Integer> getChildrenByParentId(int parentId){
			Cursor c = this.query(AllTables.GoodsKind.CONTENT_URI, null, " parent = ? ", new String[]{parentId+""}, null);
			if(c.getCount()>0){
				List<Integer> temp = new ArrayList<Integer>();
				c.moveToFirst();
				for(int i=0;i<c.getCount();i++){
					temp.add(c.getInt(0));
					c.moveToNext();
				}
				return temp;
			}
			return new ArrayList<Integer>();
			
		}
		public List<Integer> getAllDescendantByAncestorId(int ancestorId){
			List<Integer> t = new ArrayList<Integer>();
			List<Integer> temp = new ArrayList<Integer>();
			temp = this.getChildrenByParentId(ancestorId);
			
			for(int i=0;i<temp.size();i++){
				t.add(temp.get(i));
			}
			for(int i=0;i<t.size();i++){
				Integer tempId = t.get(i);
				temp = this.getChildrenByParentId(tempId);
				
				for(int j=0;j<temp.size();j++){
					t.add(temp.get(j));
				}
			}
			
			return t;
		}

}
