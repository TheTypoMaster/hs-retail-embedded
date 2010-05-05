package com.tobacco.pos.searchStrategy;

import java.util.ArrayList;

import android.util.Log;

import com.tobacco.pos.entity.BaseModel;

public class RangeSearchStrategy extends ISearchStrategy {

	public RangeSearchStrategy() {
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
		selection =" and "+selectionList[0]+" > ? and "+selectionList[1]+" < ? ";
		
		Log.i("TestStrategy", "RangeSearchStrategy.selection:"+selection + 
				"RangeSearchStrategy.selectionArg1:"+selectionArgs[0]+ 
				"RangeSearchStrategy.selectionArg2:"+selectionArgs[1]);
	}

}
