package com.tobacco.onlinesrv.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.Order;
import com.tobacco.onlinesrv.entities.PreOrder;
import com.tobacco.onlinesrv.util.FieldSupport;

public class QueryOrderActivity extends Activity {
	private String orderType[] = { "预订单", "订单" };
	private String queryType[] = { "单号", "商品名称", "规格" };
	private String from[] = new String[] { "count", "orderId", "brandCode",
			"brandCount", "amount", "format", "vip", "statusName", "date",
			"agency", "recieveName", "desc" };
	private Spinner orderTypeSp;
	private Spinner queryTypeSp;
	private ListView listView;
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
	private int listPosition;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query);
		initMaps();
		initView();
		setListView();
		setListeners();
	}

	private void initMaps() {
		queryOrderMap.put(0, Order.KEY_ORDER_ID);
		queryOrderMap.put(1, Order.KEY_BRANDCODE);
		queryOrderMap.put(2, Order.KEY_FORMAT);

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
		actionTxt = (TextView) this.findViewById(R.id.actionItem);
	}

	private void setListView() {
		listView.setTextFilterEnabled(true);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> va, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView idText = (TextView) v.findViewById(R.id.item1);
				HashMap<String, String> map = dataMaps.get(Integer
						.parseInt(idText.getText().toString()) - 1);

				Intent intent = new Intent();
				intent.setAction("android.intent.action.OrderDetail");
				putIntentData(intent, map);
				return true;
			}
		});

	}

	private void setListeners() {
		orderTypeSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (position == 0)
					actionTxt.setVisibility(View.INVISIBLE);
				else
					actionTxt.setVisibility(View.VISIBLE);
				pageNum = 1;
				setCurrentOrder(uriMap.get(position));
				fillDataMaps(getCurrentOrder(), null);
				setListAdapter(getFillMaps());
				lastBtn.setEnabled(false);
				homeBtn.setEnabled(false);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

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
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				listPosition = arg2;
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
		// TODO Auto-generated method stub
		int tempPosition = listView.getSelectedItemPosition();
		if (tempPosition >= 0)
			listPosition = tempPosition;
		TextView idText = (TextView) listView.getChildAt(listPosition)
				.findViewById(R.id.item1);
		String idStr = idText.getText().toString();
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
			actionForRecieveMenuItem(recieveText.getText().toString(), idStr,
					statusText.getText().toString());
			break;
		case R.id.menuDel:
			actionForDelMenuItem(idStr);
			break;
		}
		return true;
	}

	private void setListAdapter(List<HashMap<String, String>> fillMaps) {
		int[] to = new int[] { R.id.item1, R.id.orderItem, R.id.nameItem,
				R.id.countItem, R.id.amountItem, R.id.formatItem, R.id.vipItem,
				R.id.statusItem, R.id.dateItem, R.id.agencyItem,
				R.id.actionItem };
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
					String recieve = cursor
							.getString(FieldSupport.RECIEVE_COLUMN);
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

	private void putIntentData(Intent intent, HashMap<String, String> map) {
		intent.putExtra("id", map.get("id"));
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
		intent.putExtra("statusName", map.get("statusName"));
		startActivity(intent);
	}

	private void putOrderMap(Cursor cursor, HashMap<String, String> map) {
		map.put("count", listCount + "");
		map.put("id", cursor.getString(FieldSupport.ID_COLUMN));
		String orderId = cursor.getString(FieldSupport.ORDER_ID_COLUMN);
		map.put("orderId", orderId);
		map.put("brandCode", cursor.getString(FieldSupport.BRANDCODE_COLUMN));
		map.put("brandCount", cursor.getString(FieldSupport.BRANDCOUNT_COLUMN));
		map.put("date", cursor.getString(FieldSupport.DATE_COLUMN));
		map.put("vip", cursor.getString(FieldSupport.VIP_COLUMN));
		map.put("format", cursor.getString(FieldSupport.FORMAT_COLUMN));
		map.put("amount", cursor.getString(FieldSupport.AMOUNT_COLUMN));
		map.put("agency", cursor.getString(FieldSupport.AGENCY_COLUMN));
		map.put("desc", cursor.getString(FieldSupport.DESC_COLUMN));
		String statusId = cursor.getString(FieldSupport.STATUS_COLUMN);
		map.put("status", statusId);
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

	private void actionForEditMenuItem(int location) {
		HashMap<String, String> map = dataMaps.get(location);
		Intent intent = new Intent();
		intent.setAction("android.intent.action.EditOrder");
		intent
				.putExtra("orderType", orderTypeSp.getSelectedItemPosition()
						+ "");
		putIntentData(intent, map);
	}

	private void actionForRecieveMenuItem(String recieveStr, String idStr,
			String statusStr) {
		if (getCurrentOrder() == Order.CONTENT_URI) {
			if (statusStr.equals("已提交")) {
				if (recieveStr.equals("否")) {
					ContentValues values = new ContentValues();
					values.put(Order.KEY_RECIEVE, "1");
					int num = getContentResolver().update(Order.CONTENT_URI,
							values, "id = " + idStr, null);
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

	private void actionForDelMenuItem(String idStr) {
		int number = getContentResolver().delete(getCurrentOrder(),
				"id = " + idStr, null);
		if (number != 0) {
			Toast.makeText(QueryOrderActivity.this, "删除成功", Toast.LENGTH_SHORT)
					.show();
			fillDataMaps(getCurrentOrder(), null);
			setListAdapter(getFillMaps());
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