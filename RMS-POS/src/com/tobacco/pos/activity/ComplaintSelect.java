package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tobacco.R;
import com.tobacco.pos.entity.ComplaintFull;
import com.tobacco.pos.entity.ComplaintModel;
import com.tobacco.pos.entity.AllTables.Complaint;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.handler.ComplaintHandler;
import com.tobacco.pos.searchStrategy.SearchState;
import com.tobacco.pos.util.DateTool;
import com.tobacco.pos.util.PageModel;
import com.tobacco.pos.util.SearchCondition;

public class ComplaintSelect extends Activity{
	private static final String TAG = "ComplaintSelect";
	private static final int MENU_SHOW_GOODS_DETAIL = Menu.FIRST;
	private static final int MENU_SHOW_COMMENT = Menu.FIRST+1;
	private static final int MENU_SHOW_ALL = Menu.FIRST+2;
	private static final int MENU_SHOW_BY_FACTORS = Menu.FIRST+3;
	
	private ComplaintHandler handler = new ComplaintHandler(this);
	ArrayList<ComplaintModel> goodsList = new ArrayList<ComplaintModel>();
	PageModel pageModel;
	SearchCondition search;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if(intent.getData()==null)
			intent.setData(Complaint.CONTENT_URI);
		this.setContentView(R.layout.complaint_select);
		
		String timeTable = ComplaintFull.CREATE_DATE;
		HashMap<Integer,Integer> mappingType = new HashMap<Integer,Integer>();
		HashMap<Integer,String> mappingSel = new HashMap<Integer,String>();
		String[] conditionStr = new String[]{"商品名称", "操作人"};
		mappingType.put(0, SearchState.NAME);
    	mappingType.put(1, SearchState.OPERATOR);
    	mappingSel.put(0, ComplaintFull.GOODS_NAME);
    	mappingSel.put(1, ComplaintFull.OPER_NAME);
    	
    	search = (SearchCondition)findViewById(R.id.complaintSelectSearch);
    	search.init(timeTable,conditionStr, mappingType, mappingSel);

	}
	
	protected void showComplaintGoods(){	
		TableLayout table = (TableLayout)findViewById(R.id.complaintSelectTable);
		table.removeViews(1, table.getChildCount()-1);
		Log.i(TAG, "table.removeViews");
		int i = 0;
		for(final ComplaintModel goods : goodsList){

			LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);  
			final TableRow row = (TableRow)inflater.inflate(R.layout.table_row_five,null);  
			
			TextView goodsIndex = (TextView)row.findViewById(R.id.text_five1);		
			TextView goodsName = (TextView)row.findViewById(R.id.text_five2);
			TextView customerName = (TextView)row.findViewById(R.id.text_five3);
			TextView timeText = (TextView)row.findViewById(R.id.text_five4);
			TextView operator = (TextView)row.findViewById(R.id.text_five5);

			goodsIndex.setText(""+((pageModel.getCurrentIndex()-1)*pageModel.getRowsCount()+1+i++));	
			goodsName.setText(goods.getGoodsName());
			customerName.setText(goods.getCustomer());
			String time = DateTool.formatDateToString(goods.getCreateDate());
			timeText.setText(time.substring(0, time.length()-3));
			operator.setText(goods.getOperator());
													
			table = (TableLayout)findViewById(R.id.complaintSelectTable);		
			row.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					// TODO Auto-generated method stub
					menu.setHeaderTitle("商品可选菜单");
					menu.add(0, MENU_SHOW_GOODS_DETAIL, 0, "商品详细");
					menu.add(0, MENU_SHOW_COMMENT, 1, "投诉原因");
					menu.findItem(MENU_SHOW_GOODS_DETAIL).setOnMenuItemClickListener(new OnMenuItemClickListener(){

						public boolean onMenuItemClick(MenuItem item) {
							// TODO Auto-generated method stub
							Intent intent = new Intent("com.tobacco.pos.activity.ShowGoodsDetail");			
							intent.putExtra(GoodsPrice._ID, ""+goods.getGoodsPriceId());
							ComplaintSelect.this.startActivity(intent);
							return true;
						}
						
					});
					menu.findItem(MENU_SHOW_COMMENT).setOnMenuItemClickListener(new OnMenuItemClickListener(){

						public boolean onMenuItemClick(MenuItem item) {
							// TODO Auto-generated method stub
							new AlertDialog.Builder(ComplaintSelect.this)
							.setTitle("投诉原因")
							.setMessage(goods.getContent()).show();
							return true;
						}
						
					});
				}	
				
			});
			table.addView(row);
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
		LinearLayout layout = (LinearLayout)findViewById(R.id.complaintSelectLinearLayout);
		if(pageModel == null){
			pageModel = new PageModel(this,6,recordCount);
			layout.addView(pageModel);
		}else
			pageModel.init(6, recordCount);
		
		goodsList = handler.getPage((pageModel.getCurrentIndex()-1)*pageModel.getRowsCount(), pageModel.getRowsCount());
		showComplaintGoods();
		return true;
	}	 
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler = null;
		goodsList.clear();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if((event.getX()>pageModel.getLeft())&&(event.getX()<pageModel.getLeft()+pageModel.getWidth())){
			Log.i("SearchCondition", "ConsumeSelect2.onTouchEvent()");
			goodsList = handler.getPage((pageModel.getCurrentIndex()-1)*pageModel.getRowsCount(), pageModel.getRowsCount());
			showComplaintGoods();
			return true;
		}
		else 
			return false;
	}
}

