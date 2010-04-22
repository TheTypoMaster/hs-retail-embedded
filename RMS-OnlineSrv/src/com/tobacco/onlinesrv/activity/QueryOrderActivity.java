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
import android.widget.AdapterView.OnItemSelectedListener;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.Order;
import com.tobacco.onlinesrv.entities.PreOrder;
import com.tobacco.onlinesrv.util.FieldSupport;

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
	private HashMap<Integer, Uri> uriMap = new HashMap<Integer, Uri>();
	private Uri currentOrder;

	public Uri getCurrentOrder() {
		return currentOrder;
	}

	public void setCurrentOrder(Uri currentOrder) {
		this.currentOrder = currentOrder;
	}

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query);
		initMaps();
		initView();
		setListView();

		sp1.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				setCurrentOrder(uriMap.get(position));
				setListAdapter(getFillMaps(getCurrentOrder(), null));
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int orderType = sp1.getSelectedItemPosition();
				int queryType = sp2.getSelectedItemPosition();
				String selection = "";
				if (orderType == 0) {
					selection = queryPreOrderMap.get(queryType) + "=\""
							+ queryEdt.getText().toString() + "\"";
				} else {
					selection = queryOrderMap.get(queryType) + "=\""
							+ queryEdt.getText().toString() + "\"";
				}
				setListAdapter(getFillMaps(getCurrentOrder(), selection));
			}
		});
		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void initMaps() {
		queryPreOrderMap.put(0, PreOrder.KEY_PREORDER_ID);
		queryPreOrderMap.put(1, PreOrder.KEY_BRANDCODE);
		queryPreOrderMap.put(2, PreOrder.KEY_FORMAT);

		queryOrderMap.put(0, Order.KEY_ORDER_ID);
		queryOrderMap.put(1, Order.KEY_BRANDCODE);
		queryOrderMap.put(2, Order.KEY_FORMAT);

		uriMap.put(0, PreOrder.CONTENT_URI);
		uriMap.put(1, Order.CONTENT_URI);
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
				map.put("id", cursor.getString(FieldSupport.ID_NUM));
				map.put("brandCode", cursor.getString(FieldSupport.BRANDCODE_NUM));
				map.put("brandCount", cursor.getString(FieldSupport.BRANDCOUNT_NUM));
				map.put("amount", cursor.getString(FieldSupport.AMOUNT_NUM));
				map.put("date", cursor.getString(FieldSupport.DATE_NUM));
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