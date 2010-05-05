package com.tobacco.pos.searchStrategy;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.tobacco.pos.Module.BaseModel;

public class CompositeSearchStrategy extends ISearchStrategy {

	ArrayList<ISearchStrategy> strategys = new ArrayList<ISearchStrategy>();
	ArrayList<String> argsList = new ArrayList<String>();
	
	public CompositeSearchStrategy(Context ctx, Uri contentUri, String[] projection) {
		super(ctx, contentUri, projection);
		// TODO Auto-generated constructor stub
	}

	public void addStrategy(ISearchStrategy strategy){
		strategys.add(strategy);
		selection += strategy.getSelection();
		setArg(strategy);
	}
	
	@Override
	public ArrayList<BaseModel> search() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void setSelection() {
		// TODO Auto-generated method stub
		selection = "";
	}
	
	private void setArg(ISearchStrategy strategy){
		for(String arg : strategy.getSelectionArgs())
			argsList.add(arg);
	}
	
	public Cursor searchc(){
		Log.i("TestStrategy", "Invoke CompositeSearchStrategy.searchc()");
		selectionArgs = new String[argsList.size()];
		argsList.toArray(selectionArgs);
		Cursor cursor = ctx.getContentResolver().query(contentUri, projection, selection, selectionArgs, null);
		return cursor;
	}

}
