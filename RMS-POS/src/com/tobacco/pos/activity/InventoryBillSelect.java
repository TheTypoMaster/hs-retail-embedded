package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tobacco.R;
import com.tobacco.pos.Module.InventoryBillFull;
import com.tobacco.pos.Module.InventoryBillModel;
import com.tobacco.pos.Module.AllTables.InventoryBill;
import com.tobacco.pos.Module.AllTables.InventoryItem;
import com.tobacco.pos.handler.InventoryBillHandler;
import com.tobacco.pos.searchStrategy.SearchState;
import com.tobacco.pos.util.DateTool;
import com.tobacco.pos.util.PageModel;
import com.tobacco.pos.util.SearchCondition;

public class InventoryBillSelect extends Activity{

	private static final String TAG = "InventoryBillSelect";
	private static final int MENU_SHOW_BILL_DETAIL = Menu.FIRST;
	private static final int MENU_SHOW_ALL = Menu.FIRST+1;
	private static final int MENU_SHOW_BY_FACTORS = Menu.FIRST+2;
	
	private InventoryBillHandler handler = new InventoryBillHandler(this);
	ArrayList<InventoryBillModel> billList = new ArrayList<InventoryBillModel>();
	PageModel pageModel;
	SearchCondition search;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.inventory_bill_select);	
		Intent intent = getIntent();
		if(intent.getData()==null)
			intent.setData(InventoryBill.CONTENT_URI);
		
		String timeTable = InventoryBillFull.CREATE_DATE;
		HashMap<Integer,Integer> mappingType = new HashMap<Integer,Integer>();
		HashMap<Integer,String> mappingSel = new HashMap<Integer,String>();
		String[] conditionStr = new String[]{"单号"};
		mappingType.put(0, SearchState.BILLNUM);
//    	mappingType.put(1, SearchState.OPERATOR);
    	mappingSel.put(0, InventoryBillFull.IBILL_NUM);
//    	mappingSel.put(1, ComplaintFull.OPER_NAME);	
    	search = (SearchCondition)findViewById(R.id.inventoryBillSelectSearch);
    	search.init(timeTable,conditionStr, mappingType, mappingSel);
		
//		showInventoryBill();
	}
	
	protected void showInventoryBill(){
//		final Cursor cursor = this.managedQuery(InventoryBillFull.CONTENT_URI, InventoryBillFull.PROJECTION, null, null, null);
//		cursor.moveToFirst();
//		for(int i = 0;i<cursor.getCount();i++){
//			int inventoryIdIndex = cursor.getColumnIndex(InventoryBillFull._ID);
//			final String inventoryBillId = cursor.getString(inventoryIdIndex);
//			int inventoryNumIndex = cursor.getColumnIndex(InventoryBillFull.IBILL_NUM);
//			String inventoryNum = cursor.getString(inventoryNumIndex);
//			int inventoryFlagIndex = cursor.getColumnIndex(InventoryBillFull.FINISHED);
//			String inventoryFlag = (cursor.getString(inventoryFlagIndex).equals("1"))?"是":"否";
//			int inventoryResultIndex = cursor.getColumnIndex(InventoryBillFull.RESULT);
//			String inventoryResult = cursor.getString(inventoryResultIndex);
//			int inventoryTimeIndex = cursor.getColumnIndex(InventoryBillFull.TIME);
//			String inventoryTime = cursor.getString(inventoryTimeIndex);
//			int operatorIndex = cursor.getColumnIndex(InventoryBillFull.OPER_NAME);
//			String inventoryOperator = cursor.getString(operatorIndex);
		TableLayout table = (TableLayout)findViewById(R.id.inventoryBillSelectTable);
		table.removeViews(1, table.getChildCount()-1);
		Log.i(TAG, "table.removeViews");
		
		for(final InventoryBillModel bill : billList){
			TextView inventoryNumText = new TextView(InventoryBillSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView inventoryFlagText = new TextView(InventoryBillSelect.this,null,R.style.TextViewfillWrapSmallStyle);		
			TextView inventoryResultText = new TextView(InventoryBillSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView inventoryTimeText = new TextView(InventoryBillSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView inventoryOperatorText = new TextView(InventoryBillSelect.this,null,R.style.TextViewfillWrapSmallStyle);

//			setMarquee(contentText);
		
			inventoryNumText.setText(""+bill.getiBillNum());
			inventoryFlagText.setText(bill.isFinished()?"是":"否");
			inventoryResultText.setText(""+bill.getResult());
			String time = DateTool.formatDateToString(bill.getCreateDate());
			inventoryTimeText.setText(time.substring(0, time.length()-3));
			inventoryOperatorText.setText(bill.getOperName());			
								
			table = (TableLayout)findViewById(R.id.inventoryBillSelectTable);		
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
							intent.putExtra(InventoryItem.IBILL_ID, ""+bill.getiBillId());
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
//			cache.add(row);
//			cursor.moveToNext();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onCreateOptionMenu()");
		menu.add(0, MENU_SHOW_ALL, 0, "全部显示");
		menu.add(0, MENU_SHOW_BY_FACTORS, 0, "查询");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onOptionsItemSelected()");
		SearchState instance = SearchState.getInstance();
		switch(item.getItemId()){
		case MENU_SHOW_ALL:
			Log.i("TestStrategy", "type:"+SearchState.ALL);
			Log.i("TestStrategy", "selection:null");
			instance.setSelectionFactor(SearchState.ALL, null, null);
			break;
		case MENU_SHOW_BY_FACTORS:
			if(instance.getStrategyObjects().size()==0)
				return false;
			break;
		}
		search.reset();
		int recordCount = handler.search(instance);
		LinearLayout layout = (LinearLayout)findViewById(R.id.inventoryBillSelectLinearLayout);
		if(pageModel == null){
			pageModel = new PageModel(this,5,recordCount);
			layout.addView(pageModel);
		}else
			pageModel.init(5, recordCount);
		
		billList = handler.getPage((pageModel.getCurrentIndex()-1)*pageModel.getRowsCount(), pageModel.getRowsCount());
		showInventoryBill();
		return true;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler = null;
		billList.clear();
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		Log.e(TAG, "onActivityResult()");
//		switch(requestCode){
//		case GET_TIME:
//			if(resultCode == RESULT_OK){		
//				Date startDate = DateTool.formatStringToDate(data.getExtras().getString("startDate"));		
//				Date endDate = DateTool.formatStringToDate(data.getExtras().getString("endDate"));
//				TableLayout table = (TableLayout)findViewById(R.id.inventoryBillSelectTable);
//				table.removeViews(1, table.getChildCount()-1);
//				for(TableRow row : cache){
//					TextView time = (TextView)row.getChildAt(row.getChildCount()-2);
//					String test = time.getText().toString();
//					Date date = DateTool.formatStringToDate(test+".00");
//					if(date.after(startDate)&&date.before(endDate)){
//						table.addView(row);
//					}
//				}
//			} 
//			
//			break;
//		}
//	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if((event.getX()>pageModel.getLeft())&&(event.getX()<pageModel.getLeft()+pageModel.getWidth())){
			Log.i("SearchCondition", "ConsumeSelect2.onTouchEvent()");
			billList = handler.getPage((pageModel.getCurrentIndex()-1)*pageModel.getRowsCount(), pageModel.getRowsCount());
			showInventoryBill();
			return true;
		}
		else 
			return false;
	}
}
