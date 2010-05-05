package com.tobacco.pos.searchStrategy;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.tobacco.pos.Module.BaseModel;

public class AllSearchStrategy extends ISearchStrategy {

	
	public AllSearchStrategy() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<BaseModel> search() {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	void setSelection() {
		// TODO Auto-generated method stub
		selection = null;
		Log.i("TestStrategy", "AllSearchStrategy.selection:null");
	}

}
