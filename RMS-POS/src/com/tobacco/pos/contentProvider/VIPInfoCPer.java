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

public class VIPInfoCPer extends ContentProvider {

	private SQLiteDatabase sqlDB;
	private DatabaseHelper dbHelper;
	private static final String DATABASE_NAME = "AllTables.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "VIPInfo";
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
				db.execSQL("create table if not exists " + TABLE_NAME + " ( "
						+ _ID + " integer primary key autoincrement,"
						+ " VIPNum varchar(20) unique not null, "
						+ "VIPName varchar(20) not null ,"
						+ "VIPDiscount double not null)");

				initVIPInfo(db);
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

		private boolean initVIPInfo(SQLiteDatabase db) {

			ContentValues value = new ContentValues();

			value.clear();
			value.put("VIPNum", "vip0001");
			value.put("VIPName", "佟湘玉");
			value.put("VIPDiscount", 0.9);
			db.insertOrThrow(TABLE_NAME, null, value);

			value.clear();
			value.put("VIPNum", "vip0002");
			value.put("VIPName", "白展堂");
			value.put("VIPDiscount", 0.85);
			db.insertOrThrow(TABLE_NAME, null, value);

			value.clear();
			value.put("VIPNum", "vip0003");
			value.put("VIPName", "吕轻侯");
			value.put("VIPDiscount", 0.9);
			db.insertOrThrow(TABLE_NAME, null, value);

			value.clear();
			value.put("VIPNum", "vip0004");
			value.put("VIPName", "郭芙蓉");
			value.put("VIPDiscount", 0.9);
			db.insertOrThrow(TABLE_NAME, null, value);

			value.clear();
			value.put("VIPNum", "vip0005");
			value.put("VIPName", "莫小贝");
			value.put("VIPDiscount", 0.8);
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
			Uri rowUri = ContentUris.appendId(
					AllTables.Unit.CONTENT_URI.buildUpon(), rowId).build();
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
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
	  	 dbHelper = new DatabaseHelper(ct);
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		qb.setTables(TABLE_NAME);
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null,
				sortOrder);
		c.setNotificationUri(ct.getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues contentvalues, String s,
			String[] as) {
		return 0;
	}
	
	public String getVIPNameByVIPId(int VIPId){
		Cursor c = this.query(AllTables.VIPInfo.CONTENT_URI, null, " _id = " + VIPId, null, null);
		if(c.getCount()>0){
			c.moveToFirst();
			return c.getString(2);
		}
		return "";
	}
	public int getVIPIdByVIPNum(String VIPNum){
		Cursor c = this.query(AllTables.VIPInfo.CONTENT_URI, null, " VIPNum = ? ", new String[]{VIPNum}, null);
		if(c.getCount()>0){
			c.moveToFirst();
			return c.getInt(0);
		}
		return -1;
	}
}
