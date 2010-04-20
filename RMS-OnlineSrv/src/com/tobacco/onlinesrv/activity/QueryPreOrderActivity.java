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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.PreOrder;

public class QueryPreOrderActivity extends Activity {
	private String queryMonth[] = { "最近一个月", "最近两个月", "最近三个月" };
	private String queryType[] = { "预订单号", "商品名称", "规格" };
	private String from[] = new String[] { PreOrder.KEY_ID, PreOrder.KEY_BRANDCODE, PreOrder.KEY_BRANDCOUNT,PreOrder.KEY_AMOUNT, PreOrder.KEY_PREDATE };
	private Spinner sp1;
	private Spinner sp2;
	private ListView listView;
	private EditText queryEdt; 
	private HashMap<Integer,String> queryMap = new HashMap<Integer,String>();
	private Uri preorder = PreOrder.CONTENT_URI;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query);
		initMap();
		setSpinner();
		setListView();
		setListAdapter(getFillMaps(null));
		queryEdt = (EditText) this.findViewById(R.id.queryText);
		Button okBtn = (Button) this.findViewById(R.id.okButton);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int queryType = sp2.getSelectedItemPosition();
				String selection = queryMap.get(queryType)+"=\""+queryEdt.getText().toString()+"\"";
				setListAdapter(getFillMaps(selection));
			}
		});
		Button backBtn = (Button) this.findViewById(R.id.queryButton05);
		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	private void initMap()
	{
		queryMap.put(0, PreOrder.KEY_PREORDER_ID);
		queryMap.put(1, PreOrder.KEY_BRANDCODE);
		queryMap.put(2, PreOrder.KEY_FORMAT);
	}
	private void setListView()
	{
	
		listView = (ListView) this.findViewById(R.id.ListView01);
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
	}
	private void setListAdapter(List<HashMap<String, String>> fillMaps)
	{
		int[] to = new int[] { R.id.item1, R.id.item2, R.id.item3, R.id.item4,R.id.item5 };
		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,R.layout.grid_item, from, to);
		listView.setAdapter(adapter);
	}
	private void setSpinner()
	{
		sp1 = (Spinner) this.findViewById(R.id.Spinner01);
		sp1.setAdapter((new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, queryMonth)));
		sp2 = (Spinner) this.findViewById(R.id.Spinner02);
		sp2.setAdapter((new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, queryType)));
	}
	
	private List<HashMap<String, String>> getFillMaps(String selection){
		List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
		Cursor cursor = this.managedQuery(preorder, null, selection, null, null);
		if (cursor.getCount() == 0)
			openfailDialog();
		else {
			Log.i("The size of cursor", cursor.getCount() + "");
			cursor.moveToFirst();
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(PreOrder.KEY_ID, cursor.getString(0));
				map.put(PreOrder.KEY_BRANDCODE, cursor.getString(1));
				map.put(PreOrder.KEY_BRANDCOUNT, cursor.getString(2));
				map.put(PreOrder.KEY_AMOUNT, cursor.getString(7));
				map.put(PreOrder.KEY_PREDATE, cursor.getString(3));
				fillMaps.add(map);
			} while (cursor.moveToNext());
		}
		return fillMaps;
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