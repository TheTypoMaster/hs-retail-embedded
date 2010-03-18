package com.tobacco.onlinesrv.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.PreOrder;

public class QueryPreOrderActivity extends Activity {
	private String query[] = { "最近一个月", "最近两个月", "最近三个月" };
	private ArrayList<String> data = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query);
		String[] from = new String[] { "id", "brandcode", "brandcount",
				"amount", "date" };
		int[] to = new int[] { R.id.item1, R.id.item2, R.id.item3, R.id.item4,
				R.id.item5 };
		List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
		Uri preorder = PreOrder.CONTENT_URI;

		Cursor cursor = this.managedQuery(preorder, null, null, null, null);

		if (cursor.getCount() == 0)
			openfailDialog();
		else {
			Log.i("The size of cursor", cursor.getCount() + "");
			cursor.moveToFirst();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", cursor.getString(0));
			map.put("brandcode", cursor.getString(1));
			map.put("brandcount", cursor.getString(2));
			map.put("amount", cursor.getString(7));
			map.put("date", cursor.getString(3));

			fillMaps.add(map);

			while (cursor.moveToNext()) {
				HashMap<String, String> map1 = new HashMap<String, String>();
				map1.put("id", cursor.getString(0));
				map1.put("brandcode", cursor.getString(1));
				map1.put("brandcount", cursor.getString(2));
				map1.put("amount", cursor.getString(7));
				map1.put("date", cursor.getString(3));

				fillMaps.add(map1);
			}
		}

		ListView listView = (ListView) this.findViewById(R.id.ListView01);

		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
				R.layout.grid_item, from, to);
		listView.setAdapter(adapter);
		listView.setTextFilterEnabled(true);
		Spinner sp = (Spinner) this.findViewById(R.id.Spinner01);
		sp.setAdapter((new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, query)));

	}

	private void openfailDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(QueryPreOrderActivity.this).setTitle("")
				.setMessage("fail").show();
	}

}