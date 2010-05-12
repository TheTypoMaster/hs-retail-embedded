package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.tobacco.main.entities.globalconstant.BCodeConst;
import com.tobacco.pos.activity.ConsumeSelect.Task;
import com.tobacco.pos.entity.InventoryBillFull;
import com.tobacco.pos.entity.InventoryBillModel;
import com.tobacco.pos.entity.AllTables.InventoryBill;
import com.tobacco.pos.entity.AllTables.InventoryItem;
import com.tobacco.pos.handler.InventoryBillHandler;
import com.tobacco.pos.searchStrategy.SearchState;
import com.tobacco.pos.util.DateTool;
import com.tobacco.pos.util.PageModel;
import com.tobacco.pos.util.SearchCondition;

public class InventoryBillSelect extends RMSBaseView{

	private static final String TAG = "InventoryBillSelect";
	private static final int MENU_SHOW_BILL_DETAIL = Menu.FIRST;
	private static final int MENU_SHOW_ALL = Menu.FIRST+1;
	private static final int MENU_SHOW_BY_FACTORS = Menu.FIRST+2;
	
	private final int TASK_COMPLETE = 1;
	private int recordCount = 0;
	
	private InventoryBillHandler handler = new InventoryBillHandler(this);
	ArrayList<InventoryBillModel> billList = new ArrayList<InventoryBillModel>();
	PageModel pageModel;
	SearchCondition search;
	ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.inventory_bill_select);	
		Intent intent = getIntent();
		if(intent.getData()==null)
			intent.setData(InventoryBill.CONTENT_URI);
		
		setActivityPrivList(new int[] { BCodeConst.USER_PRIV_ADMIN,
				BCodeConst.USER_PRIV_OPERATOR, 0 });
		if (!checkActivityPriv()) {
			openPrivViolationDialog();

		}
		
		String timeTable = InventoryBillFull.CREATE_DATE;
		HashMap<Integer,Integer> mappingType = new HashMap<Integer,Integer>();
		HashMap<Integer,String> mappingSel = new HashMap<Integer,String>();
		String[] conditionStr = new String[]{"单号"};
		mappingType.put(0, SearchState.BILLNUM);
    	mappingSel.put(0, InventoryBillFull.IBILL_NUM);
    	search = (SearchCondition)findViewById(R.id.inventoryBillSelectSearch);
    	search.init(timeTable,conditionStr, mappingType, mappingSel);

    	LinearLayout layout = (LinearLayout)findViewById(R.id.inventoryBillSelectLinearLayout);
    	pageModel = new PageModel(InventoryBillSelect.this,6,recordCount);
		layout.addView(pageModel);
	}
	
	protected void showInventoryBill(){
		TableLayout table = (TableLayout)findViewById(R.id.inventoryBillSelectTable);
		table.removeViews(1, table.getChildCount()-1);
		Log.i(TAG, "table.removeViews");
		
		for(final InventoryBillModel bill : billList){
			
			LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);  
			final TableRow row = (TableRow)inflater.inflate(R.layout.table_row_five,null);  
			
			TextView inventoryNumText = (TextView)row.findViewById(R.id.text_five1);		
			TextView inventoryFlagText = (TextView)row.findViewById(R.id.text_five2);
			TextView inventoryResultText = (TextView)row.findViewById(R.id.text_five3);
			TextView inventoryTimeText = (TextView)row.findViewById(R.id.text_five4);
			TextView inventoryOperatorText = (TextView)row.findViewById(R.id.text_five5);

			inventoryNumText.setText(""+bill.getiBillNum());
			inventoryFlagText.setText(bill.isFinished()?"是":"否");
			inventoryResultText.setText(""+bill.getResult());
			String time = bill.getCreateDate();
			inventoryTimeText.setText(time.substring(0, time.length()-3));
			inventoryOperatorText.setText(bill.getOperator());			
								
			table = (TableLayout)findViewById(R.id.inventoryBillSelectTable);		

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
        	billList = handler.getPage(0, 6);	
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
                showInventoryBill();
                break;  
                  
            }  
        }  
    }; 
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler = null;
		billList.clear();
	}

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
