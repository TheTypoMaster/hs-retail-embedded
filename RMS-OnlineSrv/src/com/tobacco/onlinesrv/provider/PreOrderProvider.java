package com.tobacco.onlinesrv.provider;

import com.tobacco.onlinesrv.entities.PreOrder;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class PreOrderProvider extends ContentProvider {

	public static final String CONTENT_URI = "com.tobacco.onlinesrv.provider.preOrderProvider";

	private static final String TAG = "PreOrderProvider";
	private static final String DATABASE_NAME = "RMS_OnlineSrv.db";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_TABLE_NAME = "preorderinfo";

	private static final String DATABASE_CREATE = "create table if not exists "
			+ DATABASE_TABLE_NAME + "(" + PreOrder.KEY_ID
			+ " integer primary key autoincrement, " + PreOrder.KEY_PREORDER_ID
			+ " varchar(20), " + PreOrder.KEY_BRANDCODE + " varchar(20), "
			+ PreOrder.KEY_BRANDCOUNT + " integer, " + PreOrder.KEY_PREDATE
			+ " date, " + PreOrder.KEY_USERNAME + " varchar(20), "
			+ PreOrder.KEY_VIPID + " integer, " + PreOrder.KEY_FORMAT
			+ " varchar(20), " + PreOrder.KEY_AMOUNT + " float, "
			+ PreOrder.KEY_AGENTCYID + " integer, " + PreOrder.KEY_DESCRIPTION
			+ " text, " + PreOrder.KEY_STATUS + " char(1))";
	private DatabaseHelper preOrderHelper = null;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		Log.i(TAG, "step into insert");
		SQLiteDatabase sqlDB = preOrderHelper.getWritableDatabase();
		long rowId = sqlDB.insert(DATABASE_TABLE_NAME, "", values);
		Log.i("rowId is", rowId + "");
		if (rowId > 0) {
			Uri rowUri = ContentUris.appendId(PreOrder.CONTENT_URI.buildUpon(),
					rowId).build();
			getContext().getContentResolver().notifyChange(rowUri, null);
			// Cursor c = query(PreOrder.CONTENT_URI, null, null, null, null);
			// Log.i("After insert size", c.getCount()+"");
			return rowUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Log.i(TAG, "step into onCreate");
		preOrderHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		preOrderHelper = new DatabaseHelper(getContext());
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = preOrderHelper.getWritableDatabase();
		qb.setTables(DATABASE_TABLE_NAME);
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		private SQLiteDatabase db = null;
		private Context ctx = null;

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			ctx = context;
			exist(DATABASE_NAME);
			db = openDatabase(DATABASE_NAME);
			createtable(db);
			// TODO Auto-generated constructor stub
		}

		private void createtable(SQLiteDatabase db) {
//			db.execSQL("drop table preorderinfo");
			db.execSQL(DATABASE_CREATE);
			Log.i(TAG, "Table created...");
//			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
//					+ " (brandcode,brandcount)"
//					+ " VALUES ('abc','12')");
//
//			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
//					+ " (brandcode,brandcount)"
//					+ " VALUES ('def','1234')");
//			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
//					+ " (brandcode,brandcount)"
//					+ " VALUES ('sdsad','3434')");
//			Log.i(TAG, "Init Data inserted...");
			// TODO Auto-generated method stub

		}
		private void initData(SQLiteDatabase db)
		{
			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
					+ " (preorderid,brandcode,brandcount,predate,format,amount,agencyid,description,status)"
					+ " VALUES ('P-1','中华',10,'2010-3-10','包','450','1','中华好烟','0')");
			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
					+ " (preorderid,brandcode,brandcount,predate,format,amount,agencyid,description,status)"
					+ " VALUES ('P-1','中华',10,'2010-3-10','包','450','1','好烟','0')");
		}

		private SQLiteDatabase openDatabase(String databaseName) {
			db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
			return db;
		}

		private boolean exist(String databaseName) {
			// TODO Auto-generated method stub
			Log.i(TAG, "called fun : exist()");
			boolean flag = false;
			try {
				db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
				Log.i(TAG, "database/" + databaseName + " exist");
				flag = true;
			} finally {
				if (db != null)
					db.close();
				db = null;
			}
			return flag;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
	}

}
