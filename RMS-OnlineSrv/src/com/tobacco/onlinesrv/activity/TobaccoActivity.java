package com.tobacco.onlinesrv.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.Tobacco;

public class TobaccoActivity extends Activity {
	private ListView tobaccoList;
	private int listCount = 1;
	private ArrayList<HashMap<String, String>> dataMaps;
	private String[] from = { "count", "name", "orderprice1", "orderprice2",
			"saleprice1", "saleprice2","manufactory"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tobacco);
		tobaccoList = (ListView) this.findViewById(R.id.ListView02);
		fillDataMaps();
		setListAdapter(dataMaps);

	}

	private void fillDataMaps() {
		listCount = 1;
		dataMaps = new ArrayList<HashMap<String, String>>();
		Cursor cursor = this.managedQuery(Tobacco.CONTENT_URI, null, null,
				null, null);
		if (cursor.getCount() == 0) {
			Toast.makeText(TobaccoActivity.this, "对不起，没有相关数据",
					Toast.LENGTH_SHORT).show();
		} else {
			Log.i("The size of cursor", cursor.getCount() + "");
			cursor.moveToFirst();
			do {
				HashMap<String, String> firstMap = new HashMap<String, String>();
				putOrderMap(cursor, firstMap);
				dataMaps.add(firstMap);
				listCount++;
			} while (cursor.moveToNext());
		}
	}

	private void putOrderMap(Cursor cursor, HashMap<String, String> map) {
		// TODO Auto-generated method stub
		map.put("count", listCount + "");
		map.put("name", cursor.getString(1));
		map.put("orderprice1", cursor.getString(2));
		map.put("orderprice2", cursor.getString(3));
		map.put("saleprice1", cursor.getString(4));
		map.put("saleprice2", cursor.getString(5));
		map.put("manufactory", cursor.getString(6));
	}

	private void setListAdapter(List<HashMap<String, String>> fillMaps) {
		int[] to = new int[] { R.id.tobaccoitem1, R.id.tobaccoNameItem,
				R.id.orderPrice1Item, R.id.orderPrice2Item,
				R.id.salePrice1Item, R.id.salePrice2Item,R.id.manufactoryItem};
		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
				R.layout.tobacco_item, from, to);
		tobaccoList.setAdapter(adapter);
	}

}
