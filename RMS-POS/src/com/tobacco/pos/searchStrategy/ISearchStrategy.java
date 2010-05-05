package com.tobacco.pos.searchStrategy;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.tobacco.pos.Module.BaseModel;

public abstract class ISearchStrategy {
	
	protected Context ctx;
	protected Uri contentUri;
	protected String[] projection;
	protected String[] selectionList;
	protected String[] selectionArgs;
	protected String selection;
	
//	protected SearchState state;
	
	public ISearchStrategy() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ISearchStrategy(Context ctx, Uri contentUri, String[] projection) {
		super();
		this.ctx = ctx;
		this.contentUri = contentUri;
		this.projection = projection;
		setSelection();
	}

//	public ISearchStrategy(Context ctx, Uri contentUri, String[] projection,SearchState.StrategyObject stateObject) {
//		super();
//		initStrategy(ctx, contentUri, projection, stateObject);
//	}
	
	public final void initStrategy(Context ctx, Uri contentUri, String[] projection,SearchState.StrategyObject stateObject){
		Log.i("TestStrategy", "ISearchStrategy.initStrategy()");
		init(ctx, contentUri, projection,stateObject);
		setSelection();
	}

	public void init(Context ctx, Uri contentUri, String[] projection,SearchState.StrategyObject stateObject){
		this.ctx = ctx;
		this.contentUri = contentUri;
		this.projection = projection;
		this.selectionList = stateObject.getSelections();
		this.selectionArgs = stateObject.getSelectionArgs();
		Log.i("TestStrategy", "ISearchStrategy.init()");
	}
	
	public String[] getSelectionArgs() {
		return selectionArgs;
	}
	
	public String getSelection(){
		return selection;
	}
	
	public Cursor searchc(){
		Log.i("TestStrategy", "Invoke ISearchStrategy.searchc()");
		Cursor cursor = ctx.getContentResolver().query(contentUri, projection, selection, selectionArgs, null);
		return cursor;
	}
	
	abstract void setSelection();
	abstract ArrayList<BaseModel> search();
	
}
