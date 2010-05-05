package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.ContextMenu;
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
	private static final int MENU_SHOW_ALL = Menu.FIRST+1;
	private static final int MENU_SHOW_BY_FACTORS = Menu.FIRST+2;
	
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
		
//		showConsumeGoods();
	}
	
	protected void showComplaintGoods(){	
		TableLayout table = (TableLayout)findViewById(R.id.complaintSelectTable);
		table.removeViews(1, table.getChildCount()-1);
		Log.i(TAG, "table.removeViews");
		
		for(final ComplaintModel goods : goodsList){

			TextView goodsName = new TextView(ComplaintSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView customerName = new TextView(ComplaintSelect.this,null,R.style.TextViewfillWrapSmallStyle);		
			TextView timeText = new TextView(ComplaintSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView operator = new TextView(ComplaintSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView contentText = new TextView(ComplaintSelect.this,null,R.style.TextViewfillWrapSmallStyle);

			setMarquee(contentText);
		
			goodsName.setText(goods.getGoodsName());
			customerName.setText(goods.getCustomer());
			String time = DateTool.formatDateToString(goods.getCreateDate());
			timeText.setText(time.substring(0, time.length()-3));
			operator.setText(goods.getOperator());
			contentText.setText(goods.getContent());			
								
			table = (TableLayout)findViewById(R.id.complaintSelectTable);		
			TableRow row = new TableRow(ComplaintSelect.this);
			row.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					// TODO Auto-generated method stub
					//onCreateContextMenu(menu, v, menuInfo);
					menu.setHeaderTitle("商品可选菜单");
					menu.add(0, MENU_SHOW_GOODS_DETAIL, 0, "商品详细");
					menu.findItem(MENU_SHOW_GOODS_DETAIL).setOnMenuItemClickListener(new OnMenuItemClickListener(){

						public boolean onMenuItemClick(MenuItem item) {
							// TODO Auto-generated method stub
							Intent intent = new Intent("com.tobacco.pos.activity.ShowGoodsDetail");			
							intent.putExtra(GoodsPrice._ID, ""+goods.getGoodsPriceId());
							ComplaintSelect.this.startActivity(intent);
							return true;
						}
						
					});
				}	
				
			});
		
			row.addView(goodsName, 0);
			row.addView(customerName, 1);
			row.addView(timeText, 2);
			row.addView(contentText,3);
			row.addView(operator, 4);
			
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
			pageModel = new PageModel(this,5,recordCount);
			layout.addView(pageModel);
		}else
			pageModel.init(5, recordCount);
		
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
	
	protected void setMarquee(TextView view){
		view.setEllipsize(TruncateAt.MARQUEE);
		view.setMarqueeRepeatLimit(-1);
		view.setSingleLine(true);
		view.setFocusable(true);
		view.setOnFocusChangeListener(lisener);
//		view.setGravity(Gravity.CENTER);
	}
	
	
	private OnFocusChangeListener lisener = new OnFocusChangeListener(){
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if(hasFocus == true){
				v.setBackgroundColor(Color.RED);
			}else{
				v.setBackgroundColor(Color.BLACK);
			}
		}
	};
	
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

