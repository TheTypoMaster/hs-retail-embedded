package com.tobacco.onlinesrv.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.Order;
import com.tobacco.onlinesrv.entities.OrderDetail;
import com.tobacco.onlinesrv.entities.PreOrder;
import com.tobacco.onlinesrv.util.FieldSupport;

public class QueryOrderActivity extends Activity {
	private String orderType[] = { "预订单", "订单" };
	private String queryType[] = { "单号" };
	private String from[] = new String[] { "count", FieldSupport.KEY_ORDER_ID,
			FieldSupport.KEY_USERNAME, FieldSupport.KEY_DATE,
			FieldSupport.KEY_VIPID, "statusName", FieldSupport.KEY_AGENTCYID,
			FieldSupport.KEY_AMOUNT, FieldSupport.KEY_DESCRIPTION,
			"recieveName" };

	private String fromForOrderDetail[] = new String[] {
			OrderDetail.KEY_BRANDCODE, OrderDetail.KEY_BRANDCOUNT,
			OrderDetail.KEY_FORMAT, OrderDetail.KEY_PRICE,
			OrderDetail.KEY_AMOUNT };
	private String[] KEY_FIELDS = { OrderDetail.KEY_ID,
			OrderDetail.KEY_PREORDER_ID, OrderDetail.KEY_ORDER_ID,
			OrderDetail.KEY_BRANDCODE, OrderDetail.KEY_BRANDCOUNT,
			OrderDetail.KEY_FORMAT, OrderDetail.KEY_PRICE,
			OrderDetail.KEY_AMOUNT };

	private Spinner orderTypeSp;
	private Spinner queryTypeSp;
	private ListView listView;
	private ListView detailList;
	private EditText queryEdt;
	private EditText startDateEdt;
	private EditText endDateEdt;
	private Button okBtn;
	private Button backBtn;
	private Button homeBtn;
	private Button lastBtn;
	private Button nextBtn;
	private Button bottomBtn;
	private TextView currentPageTxt;
	private TextView actionTxt;
	private String selection = "";
	private HashMap<Integer, String> queryOrderMap = new HashMap<Integer, String>();
	private HashMap<Integer, Uri> uriMap = new HashMap<Integer, Uri>();
	private List<HashMap<String, String>> dataMaps = null;
	private List<HashMap<String, String>> dataDetailMaps = null;
	private Uri currentOrder;
	private int pageNum = 1;
	private int pageCount = 3;
	private Boolean isEmpty = false;
	private int mYear;
	private int mMonth;
	private int mDay;
	private Boolean isStartEmpty = false;
	private Boolean isEndEmpty = false;
	private int listCount;
	private int listPosition = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query);
		initMaps();
		initView();
		setListView();
		setListeners();
	}

	private void initMaps() {
		queryOrderMap.put(0, Order.KEY_ORDER_ID);
		uriMap.put(0, PreOrder.CONTENT_URI);
		uriMap.put(1, Order.CONTENT_URI);
	}

	private void initView() {
		orderTypeSp = (Spinner) this.findViewById(R.id.Spinner01);
		orderTypeSp.setAdapter((new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, orderType)));
		queryTypeSp = (Spinner) this.findViewById(R.id.Spinner02);
		queryTypeSp.setAdapter((new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, queryType)));
		queryEdt = (EditText) this.findViewById(R.id.queryText);
		startDateEdt = (EditText) this.findViewById(R.id.startDateText);
		endDateEdt = (EditText) this.findViewById(R.id.endDateText);
		okBtn = (Button) this.findViewById(R.id.okButton);
		homeBtn = (Button) this.findViewById(R.id.homePage);
		lastBtn = (Button) this.findViewById(R.id.lastPage);
		nextBtn = (Button) this.findViewById(R.id.nextPage);
		backBtn = (Button) this.findViewById(R.id.backBtn);
		bottomBtn = (Button) this.findViewById(R.id.bottomPage);
		currentPageTxt = (TextView) this.findViewById(R.id.currentPage);
		listView = (ListView) this.findViewById(R.id.ListView01);
		detailList = (ListView) this.findViewById(R.id.detailList);
		actionTxt = (TextView) this.findViewById(R.id.actionItem);
	}

	private void setListView() {
		listView.setTextFilterEnabled(true);
		detailList.setTextFilterEnabled(true);
	}

	private void setListeners() {
		orderTypeSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == 0)
					actionTxt.setVisibility(View.INVISIBLE);
				else
					actionTxt.setVisibility(View.VISIBLE);
				pageNum = 1;
				setCurrentOrder(uriMap.get(position));
				fillDataMaps(getCurrentOrder(), null);
				setListAdapter(getFillMaps());
				setDetailListAdapter();
				lastBtn.setEnabled(false);
				homeBtn.setEnabled(false);
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		startDateEdt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				isStartEmpty = true;
				showDialog(0);
			}
		});
		endDateEdt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				isEndEmpty = true;
				showDialog(0);
			}
		});
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int queryType = queryTypeSp.getSelectedItemPosition();
				selection = queryOrderMap.get(queryType) + "=\""
						+ queryEdt.getText().toString() + "\"";
				if (!startDateEdt.getText().toString().equals("开始时间")
						&& !endDateEdt.getText().toString().equals("结束时间"))
					selection += " and date between " + "\""
							+ startDateEdt.getText().toString() + "\""
							+ " and " + "\"" + endDateEdt.getText().toString()
							+ "\"";
				fillDataMaps(getCurrentOrder(), selection);
				setListAdapter(getFillMaps());
			}
		});
		homeBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

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

				finish();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				listPosition = arg2;
				setDetailListAdapter();
			}
		});
		listView.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				listPosition = arg2;
				setDetailListAdapter();
			}

			public void onNothingSelected(AdapterView<?> arg0) {

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

		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		menu.findItem(R.id.menuEdit).setIcon(android.R.drawable.ic_menu_edit);
		menu.findItem(R.id.menuDel).setIcon(android.R.drawable.ic_menu_delete);
		menu.findItem(R.id.menuRecieve).setIcon(
				android.R.drawable.ic_menu_agenda);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		int tempPosition = listView.getSelectedItemPosition();
		if (tempPosition >= 0)
			listPosition = tempPosition;
		int location = (pageNum - 1) * pageCount + listPosition;
		switch (item.getItemId()) {
		case R.id.menuEdit:
			actionForEditMenuItem(location);
			break;
		case R.id.menuRecieve:
			TextView recieveText = (TextView) listView.getChildAt(listPosition)
					.findViewById(R.id.actionItem);
			TextView statusText = (TextView) listView.getChildAt(listPosition)
					.findViewById(R.id.statusItem);
			actionForRecieveMenuItem(recieveText.getText().toString(),
					location, statusText.getText().toString());
			break;
		case R.id.menuDel:
			actionForDelMenuItem(location);
			break;
		}
		return true;
	}

	private void setListAdapter(List<HashMap<String, String>> fillMaps) {
		int[] to = new int[] { R.id.item1, R.id.orderItem, R.id.dateItem,
				R.id.userItem, R.id.vipItem, R.id.statusItem, R.id.agencyItem,
				R.id.amountItem, R.id.descItem, R.id.actionItem };
		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
				R.layout.grid_item, from, to);
		listView.setAdapter(adapter);

		if (!isEmpty) {
			int allPages = getAllPages();
			if (allPages == 1) {
				homeBtn.setEnabled(false);
				lastBtn.setEnabled(false);
				nextBtn.setEnabled(false);
				bottomBtn.setEnabled(false);
			}
			currentPageTxt.setText(pageNum + "/" + allPages);
		} else {
			currentPageTxt.setText(0 + "/" + getAllPages());
			nextBtn.setEnabled(false);
			bottomBtn.setEnabled(false);
		}
	}

	private void setDetailListAdapter() {
		int location = (pageNum - 1) * pageCount + listPosition;
		if (dataMaps != null && dataMaps.size() != 0) {
			HashMap<String, String> map = dataMaps.get(location);
			fillDataDetailMap(map.get(FieldSupport.KEY_ID).toString(),
					getCurrentOrder());
			int[] toForOrderDetail = new int[] { R.id.nameItem, R.id.countItem,
					R.id.formatItem, R.id.priceItem, R.id.amount2Item };
			SimpleAdapter adapterForOrderDetail = new SimpleAdapter(
					QueryOrderActivity.this, dataDetailMaps,
					R.layout.orderdetal_item, fromForOrderDetail,
					toForOrderDetail);
			detailList.setAdapter(adapterForOrderDetail);
		} else
			detailList.setAdapter(null);
	}

	private void fillDataMaps(Uri uri, String selection) {
		listCount = 1;
		dataMaps = new ArrayList<HashMap<String, String>>();
		Cursor cursor = this.managedQuery(uri, null, selection, null, null);
		if (cursor.getCount() == 0) {
			isEmpty = true;
			Toast.makeText(QueryOrderActivity.this, "对不起，没有相关数据",
					Toast.LENGTH_SHORT).show();
		} else {
			isEmpty = false;
			nextBtn.setEnabled(true);
			bottomBtn.setEnabled(true);
			lastBtn.setEnabled(true);
			homeBtn.setEnabled(true);
			Log.i("The size of cursor", cursor.getCount() + "");
			cursor.moveToFirst();
			do {
				HashMap<String, String> firstMap = new HashMap<String, String>();
				putOrderMap(cursor, firstMap);
				if (uri == Order.CONTENT_URI) {
					int RECIEVE_COLUMN = cursor
							.getColumnIndex(FieldSupport.KEY_RECIEVE);
					String recieve = cursor.getString(RECIEVE_COLUMN);
					firstMap.put("recieve", recieve);
					if (recieve.equals("0")) {
						firstMap.put("recieveName", "否");
					} else
						firstMap.put("recieveName", "是");
				}

				dataMaps.add(firstMap);
				listCount++;
			} while (cursor.moveToNext());
		}
	}

	private List<HashMap<String, String>> getFillMaps() {
		List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
		if (dataMaps != null) {
			for (int i = pageNum * pageCount - pageCount; i < pageCount
					* pageNum; i++) {
				if (i < dataMaps.size()) {
					HashMap<String, String> map = dataMaps.get(i);
					if (map != null)
						fillMaps.add(map);
				}
			}
			return fillMaps;
		}
		return null;
	}

	private void putOrderMap(Cursor cursor, HashMap<String, String> map) {
		int ID_COLUMN = cursor.getColumnIndex(FieldSupport.KEY_ID);
		int ORDER_ID_COLUMN = cursor.getColumnIndex(FieldSupport.KEY_ORDER_ID);
		int DATE_COLUMN = cursor.getColumnIndex(FieldSupport.KEY_DATE);
		int USERNAME_COLUMN = cursor.getColumnIndex(FieldSupport.KEY_USERNAME);
		int VIP_COLUMN = cursor.getColumnIndex(FieldSupport.KEY_VIPID);
		int AMOUNT_COLUMN = cursor.getColumnIndex(FieldSupport.KEY_AMOUNT);
		int AGENCY_COLUMN = cursor.getColumnIndex(FieldSupport.KEY_AGENTCYID);
		int DESC_COLUMN = cursor.getColumnIndex(FieldSupport.KEY_DESCRIPTION);
		int STATUS_COLUMN = cursor.getColumnIndex(FieldSupport.KEY_STATUS);
		map.put("count", listCount + "");
		map.put(FieldSupport.KEY_ID, cursor.getString(ID_COLUMN));
		String orderId = cursor.getString(ORDER_ID_COLUMN);
		map.put(FieldSupport.KEY_ORDER_ID, orderId);
		map.put(FieldSupport.KEY_USERNAME, cursor.getString(USERNAME_COLUMN));
		map.put(FieldSupport.KEY_VIPID, cursor.getString(VIP_COLUMN));
		map.put(FieldSupport.KEY_DATE, cursor.getString(DATE_COLUMN));
		map.put(FieldSupport.KEY_AMOUNT, cursor.getString(AMOUNT_COLUMN));
		map.put(FieldSupport.KEY_AGENTCYID, cursor.getString(AGENCY_COLUMN));
		map.put(FieldSupport.KEY_DESCRIPTION, cursor.getString(DESC_COLUMN));
		String statusId = cursor.getString(STATUS_COLUMN);
		map.put(FieldSupport.KEY_STATUS, statusId);
		if (orderId.contains("P")) {
			if (Integer.parseInt(statusId) == 0)
				map.put("statusName", "未审核");
			else
				map.put("statusName", "已审核");

		} else if (Integer.parseInt(statusId) == 0)
			map.put("statusName", "未提交");
		else
			map.put("statusName", "已提交");

	}

	private void fillDataDetailMap(String orderId, Uri detailUri) {
		Cursor cursor = null;
		dataDetailMaps = new ArrayList<HashMap<String, String>>();
		if (detailUri == PreOrder.CONTENT_URI)
			cursor = this.managedQuery(OrderDetail.CONTENT_URI, null,
					" preorderid = " + Integer.parseInt(orderId), null, null);
		else
			cursor = this.managedQuery(OrderDetail.CONTENT_URI, null,
					" orderid = " + Integer.parseInt(orderId), null, null);
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < KEY_FIELDS.length; i++) {
					String value = cursor.getString(cursor
							.getColumnIndex(KEY_FIELDS[i]));

					map.put(KEY_FIELDS[i], value);
				}

				dataDetailMaps.add(map);
			} while (cursor.moveToNext());
		}
	}

	private void actionForEditMenuItem(int location) {
		HashMap<String, String> map = dataMaps.get(location);
		Intent intent = new Intent();
		intent.setAction("android.intent.action.EditOrder");
		intent.putExtra("dataMap", map);
		HashMap<Integer, HashMap<String, String>> detailMap = new HashMap<Integer, HashMap<String, String>>();
		for (int i = 0; i < dataDetailMaps.size(); i++)
			detailMap.put(i, dataDetailMaps.get(i));
		intent.putExtra("detailMap", detailMap);
		startActivity(intent);
	}

	private void actionForRecieveMenuItem(String recieveStr, int location,
			String statusStr) {
		HashMap<String, String> map = dataMaps.get(location);
		if (getCurrentOrder() == Order.CONTENT_URI) {
			if (statusStr.equals("已提交")) {
				if (recieveStr.equals("否")) {
					ContentValues values = new ContentValues();
					values.put(Order.KEY_RECIEVE, "1");
					int num = getContentResolver().update(Order.CONTENT_URI,
							values, "id = " + map.get(FieldSupport.KEY_ID),
							null);
					if (num != 0) {
						Toast.makeText(QueryOrderActivity.this, "该订单确认收货",
								Toast.LENGTH_SHORT).show();
						fillDataMaps(getCurrentOrder(), null);
						setListAdapter(getFillMaps());
					}
				} else
					Toast.makeText(QueryOrderActivity.this, "该订单已经收货",
							Toast.LENGTH_SHORT).show();
			} else
				Toast.makeText(QueryOrderActivity.this, "该订单还未提交",
						Toast.LENGTH_SHORT).show();
		} else
			Toast.makeText(QueryOrderActivity.this, "此功能不支持预订单",
					Toast.LENGTH_SHORT).show();
	}

	private void actionForDelMenuItem(int location) {
		HashMap<String, String> map = dataMaps.get(location);
		String where = "preorderid";
		if (getCurrentOrder() == Order.CONTENT_URI)
			where = "orderid";
		int number1 = getContentResolver().delete(OrderDetail.CONTENT_URI,
				where + "=" + map.get(FieldSupport.KEY_ID), null);
		int number2 = getContentResolver().delete(getCurrentOrder(),
				"id = " + map.get(FieldSupport.KEY_ID), null);
		if (number1 != 0 && number2 != 0) {
			Toast.makeText(QueryOrderActivity.this, "删除成功", Toast.LENGTH_SHORT)
					.show();
			fillDataMaps(getCurrentOrder(), null);
			setListAdapter(getFillMaps());
			setDetailListAdapter();
		} else
			Toast.makeText(QueryOrderActivity.this, "删除失败", Toast.LENGTH_SHORT)
					.show();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			mYear = Calendar.getInstance().get(Calendar.YEAR);
			mMonth = Calendar.getInstance().get(Calendar.MONTH);
			mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}

		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			setDateFiled();
		}

		private void setDateFiled() {
			if (isStartEmpty) {
				startDateEdt.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
				isStartEmpty = false;
			}
			if (isEndEmpty) {
				endDateEdt.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
				isEndEmpty = false;
			}
		}
	};

	public Uri getCurrentOrder() {
		return currentOrder;
	}

	public void setCurrentOrder(Uri currentOrder) {
		this.currentOrder = currentOrder;
	}
}