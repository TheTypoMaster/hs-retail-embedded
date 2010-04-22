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
import com.tobacco.onlinesrv.entities.Order;
import com.tobacco.onlinesrv.entities.PreOrder;

public class QueryOrderActivity extends Activity {
	private String orderType[] = { "预订单", "订单" };
	private String queryType[] = { "单号", "商品名称", "规格" };
	private String from[] = new String[] { "id", "brandCode", "brandCount",
			"amount", "date" };
	private Spinner sp1;
	private Spinner sp2;
	private ListView listView;
	private EditText queryEdt;
	private Button okBtn;
	private Button backBtn;
	private HashMap<Integer, String> queryPreOrderMap = new HashMap<Integer, String>();
	private HashMap<Integer, String> queryOrderMap = new HashMap<Integer, String>();
	private Uri preorder = PreOrder.CONTENT_URI;
	private Uri order = Order.CONTENT_URI;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query);
		initPreOrderMap();
		initOrderMap();
		initView();
		setListView();
		setListAdapter(getFillMaps(preorder, null));

		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int orderType = sp1.getSelectedItemPosition();
				int queryType = sp2.getSelectedItemPosition();
				String selection = "";
				if (orderType == 0) {
					selection = queryPreOrderMap.get(queryType) + "=\""
							+ queryEdt.getText().toString() + "\"";
					setListAdapter(getFillMaps(preorder, selection));
				} else {
					selection = queryOrderMap.get(queryType) + "=\""
							+ queryEdt.getText().toString() + "\"";
					setListAdapter(getFillMaps(order, selection));
				}

			}
		});
		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void initPreOrderMap() {
		queryPreOrderMap.put(0, PreOrder.KEY_PREORDER_ID);
		queryPreOrderMap.put(1, PreOrder.KEY_BRANDCODE);
		queryPreOrderMap.put(2, PreOrder.KEY_FORMAT);
	}

	private void initOrderMap() {
		queryOrderMap.put(0, Order.KEY_ORDER_ID);
		queryOrderMap.put(1, Order.KEY_BRANDCODE);
		queryOrderMap.put(2, Order.KEY_FORMAT);
	}

	private void setListView() {
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(QueryOrderActivity.this,
						EditOrderActivity.class);
				startActivity(intent);
			}
		});
	}

	private void setListAdapter(List<HashMap<String, String>> fillMaps) {
		int[] to = new int[] { R.id.item1, R.id.item2, R.id.item3, R.id.item4,
				R.id.item5 };
		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
				R.layout.grid_item, from, to);
		listView.setAdapter(adapter);
	}

	private void initView() {
		sp1 = (Spinner) this.findViewById(R.id.Spinner01);
		sp1.setAdapter((new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, orderType)));
		sp2 = (Spinner) this.findViewById(R.id.Spinner02);
		sp2.setAdapter((new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, queryType)));
		queryEdt = (EditText) this.findViewById(R.id.queryText);
		okBtn = (Button) this.findViewById(R.id.okButton);
		backBtn = (Button) this.findViewById(R.id.queryButton05);
		listView = (ListView) this.findViewById(R.id.ListView01);
	}

	private List<HashMap<String, String>> getFillMaps(Uri uri, String selection) {
		List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
		Cursor cursor = this.managedQuery(uri, null, selection, null, null);
		if (cursor.getCount() == 0)
			openfailDialog();
		else {
			Log.i("The size of cursor", cursor.getCount() + "");
			cursor.moveToFirst();
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", cursor.getString(0));
				map.put("brandCode", cursor.getString(2));
				map.put("brandCount", cursor.getString(3));
				map.put("amount", cursor.getString(8));
				map.put("date", cursor.getString(4));
				fillMaps.add(map);
			} while (cursor.moveToNext());
		}
		return fillMaps;
	}

	private void openfailDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(QueryOrderActivity.this).setTitle("")
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