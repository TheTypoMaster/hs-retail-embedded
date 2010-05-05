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
import com.tobacco.pos.Module.ReturnFull;
import com.tobacco.pos.Module.ReturnModel;
import com.tobacco.pos.Module.AllTables.GoodsPrice;
import com.tobacco.pos.Module.AllTables.Return;
import com.tobacco.pos.handler.ReturnHandler;
import com.tobacco.pos.searchStrategy.SearchState;
import com.tobacco.pos.util.DateTool;
import com.tobacco.pos.util.PageModel;
import com.tobacco.pos.util.SearchCondition;

public class ReturnSelect extends Activity{
	private static final String TAG = "ReturnSelect";
	private static final int MENU_SHOW_GOODS_DETAIL = Menu.FIRST;
	private static final int MENU_SHOW_ALL = Menu.FIRST+1;
	private static final int MENU_SHOW_BY_FACTORS = Menu.FIRST+2;
	
	private ReturnHandler handler = new ReturnHandler(this);
	ArrayList<ReturnModel> goodsList = new ArrayList<ReturnModel>();
	PageModel pageModel;
	SearchCondition search;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if(intent.getData()==null)
			intent.setData(Return.CONTENT_URI);
		this.setContentView(R.layout.return_select);
		
		String timeTable = ReturnFull.CREATE_DATE;
		HashMap<Integer,Integer> mappingType = new HashMap<Integer,Integer>();
		HashMap<Integer,String> mappingSel = new HashMap<Integer,String>();
		String[] conditionStr = new String[]{"商品名称", "操作人"};
		mappingType.put(0, SearchState.NAME);
    	mappingType.put(1, SearchState.OPERATOR);
    	mappingSel.put(0, ReturnFull.GOODS_NAME);
    	mappingSel.put(1, ReturnFull.OPER_NAME);
    	
    	search = (SearchCondition)findViewById(R.id.returnSelectSearch);
    	search.init(timeTable,conditionStr, mappingType, mappingSel);
//		showReturnGoods();
	}
	
	protected void showReturnGoods(){	

		TableLayout table = (TableLayout)findViewById(R.id.returnSelectTable);
		table.removeViews(1, table.getChildCount()-1);
		Log.i(TAG, "table.removeViews");
		
		for(final ReturnModel goods : goodsList){
			
			TextView goodsNameText = new TextView(ReturnSelect.this,null,R.style.TextViewfillWrapSmallStyle);
//			TextView unitNameText = new TextView(ReturnSelect.this,null,R.style.TextViewfillWrapSmallStyle);		
			TextView vipNameText = new TextView(ReturnSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView timeText = new TextView(ReturnSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView contentText = new TextView(ReturnSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView operatorText = new TextView(ReturnSelect.this,null,R.style.TextViewfillWrapSmallStyle);

			setMarquee(contentText);
		
			goodsNameText.setText(goods.getGoodsName());
//			unitNameText.setText(unitName);
			vipNameText.setText(goods.getCustomer());
			String time = DateTool.formatDateToString(goods.getCreateDate());
			timeText.setText(time.substring(0, time.length()-3));
			contentText.setText(goods.getContent());
			operatorText.setText(goods.getOperator());
						
								
			table = (TableLayout)findViewById(R.id.returnSelectTable);		
			TableRow row = new TableRow(ReturnSelect.this);
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
							Intent intent = new Intent("com.tobacco.pos.activity.ShowCountGoodsDetail");		
							intent.putExtra(GoodsPrice._ID, ""+goods.getGoodsPriceId());
							intent.putExtra("COUNT", ""+goods.getNumber());
							ReturnSelect.this.startActivity(intent);
							return true;
						}
						
					});
				}	
				
			});
	
			row.addView(goodsNameText, 0);
//			row.addView(unitNameText, 1);
			row.addView(vipNameText, 1);
			row.addView(timeText, 2);
			row.addView(contentText,3);
			row.addView(operatorText, 4);
			
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
		LinearLayout layout = (LinearLayout)findViewById(R.id.returnSelectLinearLayout);
		if(pageModel == null){
			pageModel = new PageModel(this,5,recordCount);
			layout.addView(pageModel);
		}else
			pageModel.init(5, recordCount);
		
		goodsList = handler.getPage((pageModel.getCurrentIndex()-1)*pageModel.getRowsCount(), pageModel.getRowsCount());
		showReturnGoods();
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
			showReturnGoods();
			return true;
		}
		else 
			return false;
	}
}
