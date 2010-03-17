package com.tobacco.onlinesrv.provider;

import com.tobacco.main.util.MD5Hasher;
import com.tobacco.main.util.UUIDGenerator;
import com.tobacco.onlinesrv.entities.PreOrder;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.util.Log;

public class PreOrderProvider extends ContentProvider {

	public static final String CONTENT_URI = "com.tobacco.onlinesrv.provider.preOrderProvider";

	private static final String TAG = "PreOrderProvider";
	private static final String DATABASE_NAME = "RMS_OnlineSrv.db";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_TABLE_NAME = "preorder";
	private static final String KEY_ID = "id";
	private static final String KEY_BRANDCODE = "brandcode";
	private static final String KEY_BRANDCOUNT = "brandcount";
	private static final String KEY_PREDATE = "predate";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_VIPID = "vipid";
	private static final String KEY_FORMAT = "format";
	private static final String KEY_AMOUNT = "amount";
	private static final String KEY_AGENTCYID = "agencyid";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_STATUS = "status";

	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE_NAME + "(" + KEY_ID
			+ " integer primary key autoincrement, " + KEY_BRANDCODE
			+ " varchar(20), " + KEY_BRANDCOUNT + " integer, " + KEY_PREDATE
			+ " date, " + KEY_USERNAME + " varchar(20), " + KEY_VIPID
			+ " integer, " + KEY_FORMAT + " varchar(20), " + KEY_AMOUNT
			+ " float, " + KEY_AGENTCYID + " integer, " + KEY_DESCRIPTION
			+ " text, " + KEY_STATUS + " char(1))";
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
		return null;
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
		Context s = getContext();
		ContentResolver contentResolver = s.getContentResolver();

		Cursor c = contentResolver.query(uri, null, null,
				null, null);
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
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME + ";");
			db.execSQL(DATABASE_CREATE);
			Log.i(TAG, "Table created...");
			db.execSQL("INSERT INTO " + DATABASE_TABLE_NAME
					+ " (id, brandcode,brandcount)"
					+ " VALUES ('1','abc','12')");

			Log.i(TAG, "Init Data inserted...");
			// TODO Auto-generated method stub

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
