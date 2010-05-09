package com.tobacco.onlinesrv.provider;

import com.tobacco.onlinesrv.entities.Order;
import com.tobacco.onlinesrv.entities.OrderDetail;
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

public class OrderDetailProvider extends ContentProvider {

	public static final String CONTENT_URI = "com.tobacco.onlinesrv.provider.orderDetailProvider";

	private static final String TAG = "OrderDetailProvider";
	private static final String DATABASE_NAME = "AllTables.db";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_TABLE_NAME = "orderDetail";

	private static final String DATABASE_CREATE = "create table if not exists "
			+ DATABASE_TABLE_NAME + "(" + OrderDetail.KEY_ID
			+ " integer primary key autoincrement, "
			+ OrderDetail.KEY_PREORDER_ID + " integer, "
			+ OrderDetail.KEY_ORDER_ID + " integer, "
			+ OrderDetail.KEY_BRANDCODE + " varchar(20), "
			+ OrderDetail.KEY_BRANDCOUNT + " integer, "
			+ OrderDetail.KEY_FORMAT + " varchar(20), "
			+ OrderDetail.KEY_PRICE + " float, "
			+ OrderDetail.KEY_AMOUNT + " float, FOREIGN KEY ("
			+ OrderDetail.KEY_PREORDER_ID + ") REFERENCES preorderinfo("
			+ PreOrder.KEY_PREORDER_ID + ")," + "FOREIGN KEY ("
			+ OrderDetail.KEY_ORDER_ID + ") REFERENCES orderinfo("
			+ Order.KEY_ORDER_ID + "))";
	private DatabaseHelper orderDetailHelper = null;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.i(TAG, "delete...");
		if (orderDetailHelper == null)
			orderDetailHelper = new DatabaseHelper(getContext());
		SQLiteDatabase db = orderDetailHelper.getWritableDatabase();
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
		SQLiteDatabase sqlDB = orderDetailHelper.getWritableDatabase();
		long rowId = sqlDB.insert(DATABASE_TABLE_NAME, "", values);
		Log.i("rowId is", rowId + "");
		if (rowId > 0) {
			Uri rowUri = ContentUris.appendId(
					OrderDetail.CONTENT_URI.buildUpon(), rowId).build();
			getContext().getContentResolver().notifyChange(rowUri, null);
			return rowUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		Log.i(TAG, "step into onCreate");
		if (orderDetailHelper == null)
			orderDetailHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrderDetail) {
		if (orderDetailHelper == null)
			orderDetailHelper = new DatabaseHelper(getContext());
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = orderDetailHelper.getWritableDatabase();
		qb.setTables(DATABASE_TABLE_NAME);
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, sortOrderDetail);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		if (orderDetailHelper == null)
			orderDetailHelper = new DatabaseHelper(getContext());
		SQLiteDatabase db = orderDetailHelper.getWritableDatabase();
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
//			 db.execSQL("drop table orderDetail");
			db.execSQL(DATABASE_CREATE);
			// if (getAllCount(db) == 0)
			// initData(db);
			Log.i(TAG, "Table created...");

		}

		private void initData(SQLiteDatabase db) {
			Log.i(TAG, "initData...");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (OrderDetailid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid,recieve)"
							+ " VALUES ('O-1','中华',10,'2010-3-10','包','450','1','中华好烟','1','1','0')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (OrderDetailid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid,recieve)"
							+ " VALUES ('O-2','小熊猫',10,'2010-3-11','包','450','1','好烟','0','1','0')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (OrderDetailid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid,recieve)"
							+ " VALUES ('O-3','中华',10,'2010-3-12','条','450','1','好烟','1','1','0')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (OrderDetailid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid,recieve)"
							+ " VALUES ('O-4','大熊猫',10,'2010-4-10','条','450','1','好烟','0','1','0')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (OrderDetailid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid,recieve)"
							+ " VALUES ('O-5','玉溪',10,'2010-5-10','包','450','1','好烟','0','1','0')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (OrderDetailid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid,recieve)"
							+ " VALUES ('O-6','红双喜',10,'2010-6-10','条','450','1','好烟','0','1','0')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (OrderDetailid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid,recieve)"
							+ " VALUES ('O-7','七匹狼',10,'2010-6-11','包','450','1','好烟','0','1','0')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (OrderDetailid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid,recieve)"
							+ " VALUES ('O-8','石狮',10,'2010-5-10','条','450','1','好烟','0','1','0')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (OrderDetailid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid,recieve)"
							+ " VALUES ('O-9','石狮',10,'2010-6-10','包','450','1','好烟','0','1','0')");
			db
					.execSQL("INSERT INTO "
							+ DATABASE_TABLE_NAME
							+ " (OrderDetailid,brandcode,brandcount,date,format,amount,agencyid,description,status,vipid,recieve)"
							+ " VALUES ('O-10','七匹狼',10,'2010-6-11','条','450','1','好烟','0','1','0')");
		}

		private int getAllCount(SQLiteDatabase db) {

			Cursor cursor = db.query(DATABASE_TABLE_NAME, null, null, null,
					null, null, null);
			int count = cursor.getCount();
			cursor.close();
			return count;
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
