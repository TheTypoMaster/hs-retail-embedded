package com.tobacco.pos.contentProvider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class BaseContentProvider extends ContentProvider {

	private static final String TAG = "lyq";

	protected static DatabaseHelper dbHelper = null;
	protected String tableName = null;
	protected HashMap<String, String> projectMap = new HashMap<String, String>();

	public static Context ctx = null;
	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		 SQLiteDatabase db = dbHelper.getWritableDatabase();
         int count;
         
         count = db.delete(tableName, selection, selectionArgs);
         getContext().getContentResolver().notifyChange(uri, null);
         return count;
	 
	}

	@Override
	public String getType(Uri uri) {

		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG, "insert()" + " " + uri.toString());

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		long colNum = db.insert(tableName, null, values);
		db.close();

		return ContentUris.withAppendedId(uri, colNum);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
	 
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(tableName);

		SQLiteDatabase db = dbHelper.getReadableDatabase();

		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, sortOrder);
	 
		c.setNotificationUri(this.getContext().getContentResolver(), uri);
	 
		return c;
	}



	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		Log.d(TAG, "update()" + " " + uri.toString());

		// Get the database and run the query
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.update(tableName, values, selection, selectionArgs);
		db.close();

		return 0;
	}

	
}
