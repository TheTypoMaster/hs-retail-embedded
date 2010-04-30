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
		preOrderHelper = new DatabaseHelper(getContext());
		SQLiteDatabase db = preOrderHelper.getWritableDatabase();
		return db.delete(DATABASE_TABLE_NAME, selection, selectionArgs);
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.i(TAG, "step into insert");
		SQLiteDatabase sqlDB = preOrderHelper.getWritableDatabase();
		long rowId = sqlDB.insert(DATABASE_TABLE_NAME, "", values);
		Log.i("rowId is", rowId + "");
		if (rowId > 0) {
			Uri rowUri = ContentUris.appendId(PreOrder.CONTENT_URI.buildUpon(),
					rowId).build();
			getContext().getContentResolver().notifyChange(rowUri, null);
			return rowUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		Log.i(TAG, "step into onCreate");
		preOrderHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
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
		preOrderHelper = new DatabaseHelper(getContext());
		SQLiteDatabase db = preOrderHelper.getWritableDatabase();
		return db.update(DATABASE_TABLE_NAME, values, selection, selectionArgs);
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
		}

		private void createtable(SQLiteDatabase db) {
//			db.execSQL("drop table preorderinfo");
			db.execSQL(DATABASE_CREATE);
//			initData(db);
			Log.i(TAG, "Table created...");

		}
		private void initData(SQLiteDatabase db)
		{
			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
					+ " (preorderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
					+ " VALUES ('P-1','中华',10,'2010-3-10','包','450','1','中华好烟','0','1')");
			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
					+ " (preorderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
					+ " VALUES ('P-2','小熊猫',10,'2010-3-11','包','450','1','好烟','0','1')");
			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
					+ " (preorderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
					+ " VALUES ('P-3','中华',10,'2010-3-12','条','450','1','好烟','0','1')");
			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
					+ " (preorderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
					+ " VALUES ('P-4','大熊猫',10,'2010-4-10','条','450','1','好烟','0','1')");
			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
					+ " (preorderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
					+ " VALUES ('P-5','玉溪',10,'2010-5-10','包','450','1','好烟','0','1')");
			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
					+ " (preorderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
					+ " VALUES ('P-6','红双喜',10,'2010-6-10','条','450','1','好烟','0','1')");
			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
					+ " (preorderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
					+ " VALUES ('P-7','七匹狼',10,'2010-6-11','包','450','1','好烟','0','1')");
			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
					+ " (preorderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
					+ " VALUES ('P-8','石狮',10,'2010-5-10','条','450','1','好烟','0','1')");
			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
					+ " (preorderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
					+ " VALUES ('P-9','石狮',10,'2010-6-10','包','450','1','好烟','0','1')");
			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
					+ " (preorderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
					+ " VALUES ('P-10','七匹狼',10,'2010-6-11','条','450','1','好烟','0','1')");
		}

		private SQLiteDatabase openDatabase(String databaseName) {
			db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
			return db;
		}

		private boolean exist(String databaseName) {
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
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
	}

}
