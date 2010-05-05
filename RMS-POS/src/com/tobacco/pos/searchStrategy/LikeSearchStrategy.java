package com.tobacco.pos.searchStrategy;

import java.util.ArrayList;

import android.util.Log;

import com.tobacco.pos.Module.BaseModel;

public class LikeSearchStrategy extends ISearchStrategy {

	public LikeSearchStrategy() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	ArrayList<BaseModel> search() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void setSelection() {
		// TODO Auto-generated method stub
		selection = " and "+selectionList[0]+" like ? ";
		Log.i("TestStrategy", "LikeSearchStrategy.selection:"+selection + "LikeSearchStrategy.selectionArg:"+selectionArgs[0]);
	}

}
