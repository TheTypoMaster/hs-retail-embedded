package com.tobacco.onlinesrv.provider;

import com.tobacco.onlinesrv.entities.Order;
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

public class OrderProvider extends ContentProvider {

	public static final String CONTENT_URI = "com.tobacco.onlinesrv.provider.orderProvider";

	private static final String TAG = "OrderProvider";
	private static final String DATABASE_NAME = "RMS_OnlineSrv.db";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_TABLE_NAME = "orderinfo";

	private static final String DATABASE_CREATE = "create table if not exists "
			+ DATABASE_TABLE_NAME + "(" + Order.KEY_ID
			+ " integer primary key autoincrement, " + Order.KEY_ORDER_ID
			+ " varchar(20), " + Order.KEY_BRANDCODE + " varchar(20), "
			+ Order.KEY_BRANDCOUNT + " integer, " + Order.KEY_DATE + " date, "
			+ Order.KEY_USERNAME + " varchar(20), " + Order.KEY_VIPID
			+ " integer, " + Order.KEY_FORMAT + " varchar(20), "
			+ Order.KEY_AMOUNT + " float, " + Order.KEY_AGENTCYID
			+ " integer, " + Order.KEY_DESCRIPTION + " text, "
			+ Order.KEY_STATUS + " char(1))";
	private DatabaseHelper orderHelper = null;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (orderHelper == null)
			orderHelper = new DatabaseHelper(getContext());
		SQLiteDatabase db = orderHelper.getWritableDatabase();
		return db.delete(DATABASE_TABLE_NAME, selection, selectionArgs);
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
		SQLiteDatabase sqlDB = orderHelper.getWritableDatabase();
		long rowId = sqlDB.insert(DATABASE_TABLE_NAME, "", values);
		Log.i("rowId is", rowId + "");
		if (rowId > 0) {
			Uri rowUri = ContentUris.appendId(Order.CONTENT_URI.buildUpon(),
					rowId).build();
			getContext().getContentResolver().notifyChange(rowUri, null);
			return rowUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		Log.i(TAG, "step into onCreate");
		if (orderHelper == null)
			orderHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		if (orderHelper == null)
			orderHelper = new DatabaseHelper(getContext());
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = orderHelper.getWritableDatabase();
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
		if (orderHelper == null)
			orderHelper = new DatabaseHelper(getContext());
		SQLiteDatabase db = orderHelper.getWritableDatabase();
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
			// TODO Auto-generated constructor stub
		}

		private void createtable(SQLiteDatabase db) {
//			 db.execSQL("drop table orderinfo");
			db.execSQL(DATABASE_CREATE);
//			 initData(db);
			Log.i(TAG, "Table created...");

		}

		private void initData(SQLiteDatabase db) {
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (orderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
							+ " VALUES ('O-1','中华',10,'2010-3-10','包','450','1','中华好烟','0','1')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (orderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
							+ " VALUES ('O-2','小熊猫',10,'2010-3-11','包','450','1','好烟','0','1')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (orderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
							+ " VALUES ('O-3','中华',10,'2010-3-12','条','450','1','好烟','0','1')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (orderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
							+ " VALUES ('O-4','大熊猫',10,'2010-4-10','条','450','1','好烟','0','1')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (orderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
							+ " VALUES ('O-5','玉溪',10,'2010-5-10','包','450','1','好烟','0','1')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (orderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
							+ " VALUES ('O-6','红双喜',10,'2010-6-10','条','450','1','好烟','0','1')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (orderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
							+ " VALUES ('O-7','七匹狼',10,'2010-6-11','包','450','1','好烟','0','1')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (orderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
							+ " VALUES ('O-8','石狮',10,'2010-5-10','条','450','1','好烟','0','1')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (orderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
							+ " VALUES ('O-9','石狮',10,'2010-6-10','包','450','1','好烟','0','1')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (orderid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid)"
							+ " VALUES ('O-10','七匹狼',10,'2010-6-11','条','450','1','好烟','0','1')");
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
