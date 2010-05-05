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
import com.tobacco.pos.Module.ConsumeFull;
import com.tobacco.pos.Module.ConsumeModel;
import com.tobacco.pos.Module.AllTables.Consume;
import com.tobacco.pos.Module.AllTables.GoodsPrice;
import com.tobacco.pos.handler.ConsumeHandler;
import com.tobacco.pos.searchStrategy.SearchState;
import com.tobacco.pos.util.DateTool;
import com.tobacco.pos.util.PageModel;
import com.tobacco.pos.util.SearchCondition;

public class ConsumeSelect2 extends Activity{

	private static final String TAG = "ConsumeSelect";
	private static final int MENU_SHOW_GOODS_DETAIL = Menu.FIRST;
	private static final int MENU_SHOW_ALL = Menu.FIRST+1;
	private static final int MENU_SHOW_BY_FACTORS = Menu.FIRST+2;
//	private static final int MENU_TIME_PICKER = Menu.FIRST+1;
//	private static final int GET_TIME=1;
	
	private ConsumeHandler handler = new ConsumeHandler(this);
	ArrayList<ConsumeModel> goodsList = new ArrayList<ConsumeModel>();
	PageModel pageModel;
	SearchCondition search;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub	
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if(intent.getData()==null)
			intent.setData(Consume.CONTENT_URI);
		this.setContentView(R.layout.consume_select2);	

		
//		pageModel = new PageModel(this);
		
//		final SearchState instance = SearchState.getInstance();
//		final Spinner conditionSpinner = (Spinner)this.findViewById(R.id.consumeSelectConditionSpinner);
//		final EditText contentText = (EditText)this.findViewById(R.id.consumeSelectContentText);
		HashMap<Integer,Integer> mappingType = new HashMap<Integer,Integer>();
		HashMap<Integer,String> mappingSel = new HashMap<Integer,String>();
		
//		Button timeButton = (Button)findViewById(R.id.consumeSelectTimeButton);
//		timeButton.setOnClickListener(cliskListener);
		
		//"商品种类", 
    	String[] conditionStr = new String[]{"商品名称", "操作人"};
//    	ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, conditionStr);
//    	conditionSpinner.setAdapter(conditionAdapter);
    	String timeTable = ConsumeFull.CREATE_DATE;
//    	mappingType.put(0, instance.KIND);
    	mappingType.put(0, SearchState.NAME);
    	mappingType.put(1, SearchState.OPERATOR);
//    	mappingSel.put(0, "test");
    	mappingSel.put(0, ConsumeFull.GOODS_NAME);
    	mappingSel.put(1, ConsumeFull.OPER_NAME);
    	
    	search = (SearchCondition)findViewById(R.id.consumeSelectSearch);
    	search.init(timeTable,conditionStr, mappingType, mappingSel);
//    	search = new SearchCondition(this, conditionStr, mappingType, mappingSel);
//    	contentText.setOnKeyListener(new OnKeyListener(){
//
//    		@Override
//    		public boolean onKey(View v, int keyCode, KeyEvent event) {
//    			// TODO Auto-generated method stub
//    			String content = ((EditText)v).getText().toString();
//    			if(content!=null && content.length()>0 && event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
//    		
//    				int position = conditionSpinner.getSelectedItemPosition();
//    				Log.i("TestStrategy", "select"+position);
//    				Log.i("TestStrategy", "type:"+mappingType.get(position));
//    				Log.i("TestStrategy", "selection:"+mappingSel.get(position));
//    				instance.setSelectionFactor(mappingType.get(position), new String[]{mappingSel.get(position)}, new String[]{content});
//    				return true;
//    			}
//    			return false;
//    		}
//    		
//    	});
    	
	}
	
	protected void showConsumeGoods(){	

		TableLayout table = (TableLayout)findViewById(R.id.consumeSelectTable);
		table.removeViews(1, table.getChildCount()-1);
//		EditText contentText = (EditText)this.findViewById(R.id.consumeSelectContentText);
//		Button timeButton = (Button)findViewById(R.id.consumeSelectTimeButton);
//		timeButton.setText("选择时间");
//		contentText.setText("");
		Log.i("ConsumeSelect", "table.removeViews");
		for(final ConsumeModel goods : goodsList){
			
			TextView goodsNameText = new TextView(ConsumeSelect2.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView unitNameText = new TextView(ConsumeSelect2.this,null,R.style.TextViewfillWrapSmallStyle);		
			TextView timeText = new TextView(ConsumeSelect2.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView commentText = new TextView(ConsumeSelect2.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView operatorText = new TextView(ConsumeSelect2.this,null,R.style.TextViewfillWrapSmallStyle);

			setMarquee(commentText);
		
			goodsNameText.setText(goods.getGoodsName());
			unitNameText.setText(goods.getUnitName());
			String time = DateTool.formatDateToString(goods.getCreateDate());
			timeText.setText(time.substring(0, time.length()-3));
			commentText.setText(goods.getComment());
			operatorText.setText(goods.getOperator());					
								
			table = (TableLayout)findViewById(R.id.consumeSelectTable);		
			TableRow row = new TableRow(ConsumeSelect2.this);
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
							ConsumeSelect2.this.startActivity(intent);
							return true;
						}
						
					});
				}	
				
			});
	
			row.addView(goodsNameText, 0);
			row.addView(unitNameText, 1);
			row.addView(timeText, 2);
			row.addView(commentText,3);
			row.addView(operatorText, 4);
			
			table.addView(row);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		Log.e("ConsumeInsert", "onCreateOptionMenu()");
//		menu.add(0, MENU_TIME_PICKER, 0, "选择时间");
		menu.add(0, MENU_SHOW_ALL, 0, "全部显示");
		menu.add(0, MENU_SHOW_BY_FACTORS, 0, "查询");
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Log.e("ConsumeInsert", "onOptionsItemSelected()");
		SearchState instance = SearchState.getInstance();
		switch(item.getItemId()){
		case MENU_SHOW_ALL:
			Log.i("TestStrategy", "type:"+SearchState.ALL);
			Log.i("TestStrategy", "selection:null");
//			instance.getStrategyObjects().clear();
			instance.setSelectionFactor(SearchState.ALL, null, null);
			break;
		case MENU_SHOW_BY_FACTORS:
			if(instance.getStrategyObjects().size()==0)
				return false;
			break;
		}
//		goodsList = handler.search(instance);
		search.reset();
		int recordCount = handler.search(instance);
		LinearLayout layout = (LinearLayout)findViewById(R.id.consumeSelectLinearLayout);
		if(pageModel == null){
			pageModel = new PageModel(this,5,recordCount);
			layout.addView(pageModel);
		}else
			pageModel.init(5, recordCount);
		
		goodsList = handler.getPage((pageModel.getCurrentIndex()-1)*pageModel.getRowsCount(), pageModel.getRowsCount());
		showConsumeGoods();
		return true;
	}	 

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler = null;
		goodsList.clear();
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		Log.e("ConsumeInsert", "onActivityResult()");
//		switch(requestCode){
//		case GET_TIME:
//			if(resultCode == RESULT_OK){	
//				Date startDate = DateTool.formatStringToDate(data.getExtras().getString("startDate"));		
//				Date endDate = DateTool.formatStringToDate(data.getExtras().getString("endDate"));
//				
//				Button timeButton = (Button)findViewById(R.id.consumeSelectTimeButton);
//				String start = DateTool.formatDateToString(startDate);
//				String end = DateTool.formatDateToString(endDate);
//				timeButton.setText(start.subSequence(0, start.length()-9)+"到"+end.subSequence(0, end.length()-9));
//				
//				endDate.setDate(endDate.getDate()+1);
//				SearchState instance = SearchState.getInstance();
//				Log.i("TestStrategy", "type:"+instance.TIME);
//				Log.i("TestStrategy", "selection1:start");
//				Log.i("TestStrategy", "selection2:end");
//				instance.setSelectionFactor(instance.TIME, new String[]{ConsumeFull.CREATE_DATE,ConsumeFull.CREATE_DATE}, new String[]{start,end});
//			}
//			break;
//		}
//	}

	protected void setMarquee(TextView view){
		view.setEllipsize(TruncateAt.MARQUEE);
		view.setMarqueeRepeatLimit(-1);
		view.setSingleLine(true);
		view.setFocusable(true);
		view.setOnFocusChangeListener(lisener);
//		view.setGravity(Gravity.CENTER);
	}
	
//	private OnClickListener cliskListener = new OnClickListener(){
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			Intent intent = new Intent("com.tobacco.pos.activity.SelectTimeRange");
//			startActivityForResult(intent, GET_TIME);
//		}
//	};
	
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
			showConsumeGoods();
			return true;
		}
		else 
			return false;
//		Log.i("SearchCondition", "ConsumeSelect2.onTouchEvent()");
//		goodsList = handler.getPage((pageModel.getCurrentIndex()-1)*pageModel.getRowsCount(), pageModel.getRowsCount());
//		showConsumeGoods();
//		return super.onTouchEvent(event);
	}
	
	
}
