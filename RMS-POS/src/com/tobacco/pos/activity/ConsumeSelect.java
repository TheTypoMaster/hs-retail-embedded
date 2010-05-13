package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tobacco.R;
import com.tobacco.main.activity.view.RMSBaseView;
import com.tobacco.pos.entity.ConsumeFull;
import com.tobacco.pos.entity.ConsumeModel;
import com.tobacco.pos.entity.AllTables.Consume;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.handler.ConsumeHandler;
import com.tobacco.pos.searchStrategy.SearchState;
import com.tobacco.pos.util.DateTool;
import com.tobacco.pos.util.PageModel;
import com.tobacco.pos.util.SearchCondition;

public class ConsumeSelect extends RMSBaseView{

	private static final String TAG = "ConsumeSelect";
	private static final int MENU_SHOW_GOODS_DETAIL = Menu.FIRST;
	private static final int MENU_SHOW_COMMENT = Menu.FIRST+1;
	private static final int MENU_SHOW_ALL = Menu.FIRST+2;
	private static final int MENU_SHOW_BY_FACTORS = Menu.FIRST+3;
	
	private final int TASK_COMPLETE = 1;
	private int recordCount = 0;
	
	private ConsumeHandler handler = new ConsumeHandler(this);
	ArrayList<ConsumeModel> goodsList = new ArrayList<ConsumeModel>();
	PageModel pageModel;
	SearchCondition search;
	ProgressDialog pd;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub	
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if(intent.getData()==null)
			intent.setData(Consume.CONTENT_URI);
		this.setContentView(R.layout.consume_select);	

		HashMap<Integer,Integer> mappingType = new HashMap<Integer,Integer>();
		HashMap<Integer,String> mappingSel = new HashMap<Integer,String>();

		//"商品种类", 
    	String[] conditionStr = new String[]{"商品名称", "操作人"};
    	String timeTable = ConsumeFull.CREATE_DATE;
    	mappingType.put(0, SearchState.NAME);
    	mappingType.put(1, SearchState.OPERATOR);
    	mappingSel.put(0, ConsumeFull.GOODS_NAME);
    	mappingSel.put(1, ConsumeFull.OPER_NAME);
    	
    	search = (SearchCondition)findViewById(R.id.consumeSelectSearch);
    	search.init(timeTable,conditionStr, mappingType, mappingSel);
    	
    	LinearLayout layout = (LinearLayout)findViewById(R.id.consumeSelectLinearLayout);
    	pageModel = new PageModel(ConsumeSelect.this,6,recordCount);
		layout.addView(pageModel);
	}
	
	protected void showConsumeGoods(){	

		TableLayout table = (TableLayout)findViewById(R.id.consumeSelectTable);
		table.removeViews(1, table.getChildCount()-1);
		Log.i("ConsumeSelect", "table.removeViews");
		int i = 0;
		
		for(final ConsumeModel goods : goodsList){
			
			LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);  
			final TableRow row = (TableRow)inflater.inflate(R.layout.table_row_six,null);  
			
			TextView goodsIndexText = (TextView)row.findViewById(R.id.text_six1);
			TextView goodsNameText = (TextView)row.findViewById(R.id.text_six2);	
//			TextView typeText = (TextView)row.findViewById(R.id.text_six3);	
			TextView unitNameText = (TextView)row.findViewById(R.id.text_six3);
			TextView numberText = (TextView)row.findViewById(R.id.text_six4);
			TextView timeText = (TextView)row.findViewById(R.id.text_six5);
			TextView operatorText = (TextView)row.findViewById(R.id.text_six6);

			goodsIndexText.setText(""+((pageModel.getCurrentIndex()-1)*pageModel.getRowsCount()+1+i++));
			goodsNameText.setText(goods.getGoodsName());
//			typeText.setText(goods.getType());
			unitNameText.setText(goods.getUnitName());					
//			inPriceText.setText(""+goods.getInPrice());
			
			String number = (goods.getType().equals("溢"))?" "+goods.getNumber():""+-goods.getNumber();
			numberText.setText(number);
			String time = goods.getCreateDate();	
			timeText.setText(time.substring(0, time.length()-3));
			operatorText.setText(goods.getOperator());					
								
			table = (TableLayout)findViewById(R.id.consumeSelectTable);	

			row.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					// TODO Auto-generated method stub
					menu.setHeaderTitle("商品可选菜单");
					menu.add(0, MENU_SHOW_GOODS_DETAIL, 0, "商品详细");
					menu.add(0, MENU_SHOW_COMMENT, 1, "溢耗原因");
					
					menu.findItem(MENU_SHOW_GOODS_DETAIL).setOnMenuItemClickListener(new OnMenuItemClickListener(){

						public boolean onMenuItemClick(MenuItem item) {
							// TODO Auto-generated method stub
							Intent intent = new Intent("com.tobacco.pos.activity.ShowCountGoodsDetail");		
							intent.putExtra(GoodsPrice._ID, ""+goods.getGoodsPriceId());
							intent.putExtra("COUNT", ""+goods.getNumber());
							ConsumeSelect.this.startActivity(intent);
							return true;
						}
						
					});
					
					menu.findItem(MENU_SHOW_COMMENT).setOnMenuItemClickListener(new OnMenuItemClickListener(){

						public boolean onMenuItemClick(MenuItem item) {
							// TODO Auto-generated method stub
							new AlertDialog.Builder(ConsumeSelect.this)
							.setTitle("溢耗原因")
							.setMessage(goods.getComment()).show();
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
		Log.e("ConsumeInsert", "onCreateOptionMenu()");
//		menu.add(0, MENU_TIME_PICKER, 0, "选择时间");
		menu.add(0, MENU_SHOW_ALL, 0, "全部显示");
		menu.add(0, MENU_SHOW_BY_FACTORS, 0, "查询");
		return true;
	}

	//change
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
			search.reset();
			break;
		}
		
		startTask();
		return true;
	}	 
	
	public void startTask(){   
		Log.e(TAG, "startTask()");		
		showDialog();            
        Thread task = new Thread(new Task());  
        task.start();  
    } 

	private void showDialog(){
		Log.e(TAG, "showDialog()");
		pd = ProgressDialog.show(this, "请稍候...", "Loading...", true,  
                false);
	}
	
	public class Task implements Runnable {  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
        	Log.e(TAG, "Task.run()");
        	SearchState instance = SearchState.getInstance();
        	recordCount = handler.search(instance);
    		goodsList = handler.getPage(0, 6);	
            messageListener.sendEmptyMessage(TASK_COMPLETE);             
        }  
          
    }  
	
	private Handler messageListener = new Handler(){  
        public void handleMessage(Message msg) {  
        	Log.e(TAG, "messageListener.handleMessage()");
            switch(msg.what){  
            case TASK_COMPLETE:   
            	Log.e(TAG, "Message:TASK_COMPLETE");
                pd.dismiss(); 
                pageModel.init(6, recordCount);
                showConsumeGoods();
                break;  
                  
            }  
        }  
    }; 
    
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
			showConsumeGoods();
			return true;
		}
		else 
			return false;
	}
	
}
