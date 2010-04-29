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
import android.view.Menu;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.Order;
import com.tobacco.onlinesrv.entities.PreOrder;
import com.tobacco.onlinesrv.util.FieldSupport;

public class QueryOrderActivity extends Activity {
	private String orderType[] = { "预订单", "订单" };
	private String queryType[] = { "单号", "商品名称", "规格" };
	private String from[] = new String[] { "id", "orderId", "brandCode",
			"brandCount", "date", "vip", "format", "amount", "agency", "desc",
			"status" };
	private Spinner sp1;
	private Spinner sp2;
	private ListView listView;
	private EditText queryEdt;
	private Button okBtn;
	private Button backBtn;
	private Button homeBtn;
	private Button lastBtn;
	private Button nextBtn;
	private Button bottomBtn;
	private TextView currentPageTxt;
	private String selection = "";
	private HashMap<Integer, String> queryPreOrderMap = new HashMap<Integer, String>();
	private HashMap<Integer, String> queryOrderMap = new HashMap<Integer, String>();
	private HashMap<Integer, Uri> uriMap = new HashMap<Integer, Uri>();
	private List<HashMap<String, String>> dataMaps;
	private Uri currentOrder;
	private int pageNum = 1;
	private int pageCount = 3;

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
				fillDataMaps(getCurrentOrder(), null);
				setListAdapter(getFillMaps());
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int orderType = sp1.getSelectedItemPosition();
				int queryType = sp2.getSelectedItemPosition();
				if (orderType == 0) {
					selection = queryPreOrderMap.get(queryType) + "=\""
							+ queryEdt.getText().toString() + "\"";
				} else {
					selection = queryOrderMap.get(queryType) + "=\""
							+ queryEdt.getText().toString() + "\"";
				}
				fillDataMaps(getCurrentOrder(), selection);
				setListAdapter(getFillMaps());
			}
		});
		homeBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				pageNum = 1;
				lastBtn.setEnabled(false);
				homeBtn.setEnabled(false);
				nextBtn.setEnabled(true);
				bottomBtn.setEnabled(true);
				setListAdapter(getFillMaps());
			}
		});
		lastBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				pageNum--;
				if (pageNum != 1) {
				} else {
					lastBtn.setEnabled(false);
					homeBtn.setEnabled(false);
				}
				nextBtn.setEnabled(true);
				bottomBtn.setEnabled(true);
				setListAdapter(getFillMaps());
			}
		});
		nextBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				pageNum++;
				if (pageNum != getAllPages()) {
				} else {
					nextBtn.setEnabled(false);
					bottomBtn.setEnabled(false);
				}
				homeBtn.setEnabled(true);
				lastBtn.setEnabled(true);
				setListAdapter(getFillMaps());
			}
		});
		bottomBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				pageNum = getAllPages();
				nextBtn.setEnabled(false);
				bottomBtn.setEnabled(false);
				lastBtn.setEnabled(true);
				homeBtn.setEnabled(true);
				setListAdapter(getFillMaps());
			}
		});
		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private int getAllPages() {
		if (dataMaps != null) {
			int dataLength = dataMaps.size();
			if (dataLength % pageCount == 0)
				return dataLength / pageCount;
			else
				return dataLength / pageCount + 1;
		}
		return 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
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
			public void onItemClick(AdapterView<?> va, View v, int position,
					long id) {
				TextView idText = (TextView) v.findViewById(R.id.item1);
				HashMap<String, String> map = dataMaps.get(Integer.parseInt(idText.getText().toString())-1);
	
				Intent intent = new Intent();
				intent.setClass(QueryOrderActivity.this,
						EditOrderActivity.class);
				intent.putExtra("orderId", map.get("orderId"));
				intent.putExtra("brandCode", map.get("brandCode"));
				intent.putExtra("brandCount", map.get("brandCount"));
				intent.putExtra("date", map.get("date"));
				intent.putExtra("vip", map.get("vip"));
				intent.putExtra("format", map.get("format"));
				intent.putExtra("amount", map.get("amount"));
				intent.putExtra("agency", map.get("agency"));
				intent.putExtra("desc", map.get("desc"));
				intent.putExtra("status", map.get("status"));
				startActivity(intent);
				Toast.makeText(QueryOrderActivity.this, ""+idText.getText().toString(), Toast.LENGTH_LONG).show();
			}
		});
	}

	private void setListAdapter(List<HashMap<String, String>> fillMaps) {
		int[] to = new int[] { R.id.item1, R.id.item2, R.id.item3, R.id.item4,
				R.id.item5 };
		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
				R.layout.grid_item, from, to);
		listView.setAdapter(adapter);
		currentPageTxt.setText(pageNum + "/" + getAllPages());
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
		homeBtn = (Button) this.findViewById(R.id.homePage);
		homeBtn.setEnabled(false);
		lastBtn = (Button) this.findViewById(R.id.lastPage);
		lastBtn.setEnabled(false);
		nextBtn = (Button) this.findViewById(R.id.nextPage);
		backBtn = (Button) this.findViewById(R.id.backBtn);
		bottomBtn = (Button) this.findViewById(R.id.bottomPage);
		currentPageTxt = (TextView) this.findViewById(R.id.currentPage);
		listView = (ListView) this.findViewById(R.id.ListView01);
	}

	private void fillDataMaps(Uri uri, String selection) {
		dataMaps = new ArrayList<HashMap<String, String>>();
		Cursor cursor = this.managedQuery(uri, null, selection, null, null);
		if (cursor.getCount() == 0)
			openfailDialog();
		else {
			Log.i("The size of cursor", cursor.getCount() + "");
			cursor.moveToFirst();
			do {
				HashMap<String, String> firstMap = new HashMap<String, String>();
				putOrderMap(cursor, firstMap);
				dataMaps.add(firstMap);
			} while (cursor.moveToNext());
		}
	}

	private List<HashMap<String, String>> getFillMaps() {
		List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
		for (int i = pageNum * pageCount - pageCount; i < pageCount * pageNum; i++) {
			if (i < dataMaps.size()) {
				HashMap<String, String> map = dataMaps.get(i);
				if (map != null)
					fillMaps.add(map);
			}
		}
		return fillMaps;
	}

	private void putOrderMap(Cursor cursor, HashMap<String, String> map) {
		map.put("id", cursor.getString(FieldSupport.ID_COLUMN));
		map.put("orderId", cursor.getString(FieldSupport.ORDER_ID_COLUMN));
		map.put("brandCode", cursor.getString(FieldSupport.BRANDCODE_COLUMN));
		map.put("brandCount", cursor.getString(FieldSupport.BRANDCOUNT_COLUMN));
		map.put("date", cursor.getString(FieldSupport.DATE_COLUMN));
		map.put("vip", cursor.getString(FieldSupport.VIP_COLUMN));
		map.put("format", cursor.getString(FieldSupport.FORMAT_COLUMN));
		map.put("amount", cursor.getString(FieldSupport.AMOUNT_COLUMN));
		map.put("agency", cursor.getString(FieldSupport.AGENCY_COLUMN));
		map.put("desc", cursor.getString(FieldSupport.DESC_COLUMN));
		map.put("status", cursor.getString(FieldSupport.STATUS_COLUMN));
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