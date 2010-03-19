package com.tobacco.onlinesrv.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.PreOrder;

public class QueryPreOrderActivity extends Activity {
	private String query[] = { "最近一个月", "最近两个月", "最近三个月" };

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
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", cursor.getString(0));
				map.put("brandcode", cursor.getString(1));
				map.put("brandcount", cursor.getString(2));
				map.put("amount", cursor.getString(7));
				map.put("date", cursor.getString(3));
				fillMaps.add(map);
			} while (cursor.moveToNext());
		}

		ListView listView = (ListView) this.findViewById(R.id.ListView01);

		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
				R.layout.grid_item, from, to);
		listView.setAdapter(adapter);
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(QueryPreOrderActivity.this,
						EditPreOrderActivity.class);
				startActivity(intent);
			}
		});
		Spinner sp = (Spinner) this.findViewById(R.id.Spinner01);
		sp.setAdapter((new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, query)));

		Button backBtn = (Button) this.findViewById(R.id.queryButton05);
		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void openfailDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(QueryPreOrderActivity.this).setTitle("")
				.setMessage("没有相关数据").setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								finish();
							}
						}).show();
	}

}