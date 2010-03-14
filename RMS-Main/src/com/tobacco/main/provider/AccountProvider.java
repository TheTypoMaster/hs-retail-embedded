package com.tobacco.main.provider;

import java.util.HashMap;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class AccountProvider extends ContentProvider {

	public static final String CONTENT_URI = "com.tobacco.main.provider.accountprovider";

	private static final String TAG = "AccountProvider";
	
	private static HashMap<String, String> usersProjectionMap;
	
	private static final String DATABASE_NAME = "RMS_MAIN.db";
	private static final int DATABASE_VERSION = 2;
	private static final String USER_TABLE_NAME = "users";

	private DatabaseHelper accountHelper = null;

	@Override
	public boolean onCreate() {

		accountHelper = new DatabaseHelper(getContext());
		return true;

	}

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
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
            qb.setTables(USER_TABLE_NAME);
            qb.setProjectionMap(usersProjectionMap);
           

        // Get the database and run the query
        SQLiteDatabase db = accountHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * nested class Database helper used for create and upgrade SQLite database
	 */
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

		private SQLiteDatabase openDatabase(String databaseName) {
			db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
			return db;
		}

		private void createtable(SQLiteDatabase db) {
			
			//wipe old table
			db.execSQL("DROP TABLE IF EXISTS "+ USER_TABLE_NAME+";");
			
			db.execSQL("CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME
					+ " (_ID VARCHAR, username VARCHAR, password VARCHAR, priv VARCHAR, status VARCHAR)");

			Log.i(TAG, "Table created...");
			db
					.execSQL("INSERT INTO "
							+ USER_TABLE_NAME
							+ " (_ID, username, password, priv, status)"
							+ " VALUES ('1', 'zwd', '123', 'admin' , '0');");

			Log.i(TAG, "Init Data inserted...");
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

		public boolean exist(String dbName) {
			Log.i(TAG, "called fun : exist()");
			boolean flag = false;
			try {
				db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);
				Log.i(TAG, "database/" + dbName + " exist");
				flag = true;
			} finally {
				if (db != null)
					db.close();
				db = null;
			}
			return flag;
		}
	}

}
