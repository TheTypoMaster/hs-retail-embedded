package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tobacco.R;
import com.tobacco.pos.entity.InventoryBillFull;
import com.tobacco.pos.entity.AllTables.InventoryBill;
import com.tobacco.pos.entity.AllTables.InventoryItem;
import com.tobacco.pos.util.DateTool;

public class InventoryBillSelect extends Activity{

	private static final String TAG = "InventoryBillSelect";
	private static final int MENU_SHOW_BILL_DETAIL = Menu.FIRST;
	private static final int MENU_TIME_PICKER = Menu.FIRST+1;
	private static final int GET_TIME=1;
	
	private List<TableRow> cache = new ArrayList<TableRow>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.inventory_bill_select);
		
		Intent intent = getIntent();
		if(intent.getData()==null)
			intent.setData(InventoryBill.CONTENT_URI);
		showInventoryBill();
	}
	
	protected void showInventoryBill(){
		final Cursor cursor = this.managedQuery(InventoryBillFull.CONTENT_URI, InventoryBillFull.PROJECTION, null, null, null);
		cursor.moveToFirst();
		for(int i = 0;i<cursor.getCount();i++){
			int inventoryIdIndex = cursor.getColumnIndex(InventoryBillFull._ID);
			final String inventoryBillId = cursor.getString(inventoryIdIndex);
			int inventoryNumIndex = cursor.getColumnIndex(InventoryBillFull.IBILL_NUM);
			String inventoryNum = cursor.getString(inventoryNumIndex);
			int inventoryFlagIndex = cursor.getColumnIndex(InventoryBillFull.FINISHED);
			String inventoryFlag = (cursor.getString(inventoryFlagIndex).equals("1"))?"是":"否";
			int inventoryResultIndex = cursor.getColumnIndex(InventoryBillFull.RESULT);
			String inventoryResult = cursor.getString(inventoryResultIndex);
			int inventoryTimeIndex = cursor.getColumnIndex(InventoryBillFull.TIME);
			String inventoryTime = cursor.getString(inventoryTimeIndex);
			int operatorIndex = cursor.getColumnIndex(InventoryBillFull.OPER_NAME);
			String inventoryOperator = cursor.getString(operatorIndex);

			TextView inventoryNumText = new TextView(InventoryBillSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView inventoryFlagText = new TextView(InventoryBillSelect.this,null,R.style.TextViewfillWrapSmallStyle);		
			TextView inventoryResultText = new TextView(InventoryBillSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView inventoryTimeText = new TextView(InventoryBillSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView inventoryOperatorText = new TextView(InventoryBillSelect.this,null,R.style.TextViewfillWrapSmallStyle);

//			setMarquee(contentText);
		
			inventoryNumText.setText(inventoryNum);
			inventoryFlagText.setText(inventoryFlag);
			inventoryResultText.setText(inventoryResult);
			inventoryTimeText.setText(inventoryTime.substring(0, inventoryTime.length()-3));
			inventoryOperatorText.setText(inventoryOperator);			
								
			final TableLayout table = (TableLayout)findViewById(R.id.inventoryBillSelectTable);		
			final TableRow row = new TableRow(InventoryBillSelect.this);
			row.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					// TODO Auto-generated method stub
					//onCreateContextMenu(menu, v, menuInfo);
					menu.setHeaderTitle("可选菜单");
					menu.add(0, MENU_SHOW_BILL_DETAIL, 0, "盘点详细");
					menu.findItem(MENU_SHOW_BILL_DETAIL).setOnMenuItemClickListener(new OnMenuItemClickListener(){

						public boolean onMenuItemClick(MenuItem item) {
							// TODO Auto-generated method stub
							Intent intent = new Intent("com.tobacco.pos.activity.ShowInventoryItem");
							intent.putExtra(InventoryItem.IBILL_ID, inventoryBillId);
							InventoryBillSelect.this.startActivity(intent);
							return true;
						}
						
					});
				}	
				
			});
		
			row.addView(inventoryNumText, 0);
			row.addView(inventoryFlagText, 1);
			row.addView(inventoryResultText, 2);
			row.addView(inventoryTimeText,3);
			row.addView(inventoryOperatorText, 4);
			
			table.addView(row);
			cache.add(row);
			cursor.moveToNext();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onCreateOptionMenu()");
		menu.add(0, MENU_TIME_PICKER, 0, "选择时间");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onOptionsItemSelected()");
		switch(item.getItemId()){
		case MENU_TIME_PICKER:
			Intent intent = new Intent("com.tobacco.pos.activity.SelectTimeRange");
			startActivityForResult(intent, GET_TIME);
			break;
		}	
		return true;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		cache.clear();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onActivityResult()");
		switch(requestCode){
		case GET_TIME:
			if(resultCode == RESULT_OK){		
				Date startDate = DateTool.formatStringToDate(data.getExtras().getString("startDate"));		
				Date endDate = DateTool.formatStringToDate(data.getExtras().getString("endDate"));
				TableLayout table = (TableLayout)findViewById(R.id.inventoryBillSelectTable);
				table.removeViews(1, table.getChildCount()-1);
				for(TableRow row : cache){
					TextView time = (TextView)row.getChildAt(row.getChildCount()-2);
					String test = time.getText().toString();
					Date date = DateTool.formatStringToDate(test+".00");
					if(date.after(startDate)&&date.before(endDate)){
						table.addView(row);
					}
				}
			} 
			
			break;
		}
	}
}
