package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.tobacco.R;
import com.tobacco.pos.entity.InventoryBillModel;
import com.tobacco.pos.entity.InventoryItemModel;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.handler.InventoryBillHandler;
import com.tobacco.pos.handler.InventoryItemHandler;

public class InventoryInsert extends Activity{
	
	private String TAG = "InventoryInsert";
	
	private final int MENU_ADD_INVENTORY_GOODS = Menu.FIRST;
	private final int MENU_SAVE = Menu.FIRST+1;
	private final int MENU_RE_INVENTORY = Menu.FIRST+2;
	private final int MENU_PAU_INVENTORY = Menu.FIRST+3;
	private final int MENU_CON_INVENTORY = Menu.FIRST+4;
	private final int MENU_EXIT = Menu.FIRST+5;
	private final int MENU_REMOVE = Menu.FIRST+6;
	private final int MENU_SELECT_INENTORY_GOODS = Menu.FIRST+7;
	private final int MENU_IMPROVE = Menu.FIRST+8;
	private final int MENU_DROP = Menu.FIRST+9;

	private int state ;
//	private final int GET_GOODS = 0;
	private final int GET_GOODS_LIST = 1;
	
	private InventoryItemHandler itemHandler;
	private InventoryBillHandler billHandler;
	private final HashMap<TableRow,InventoryItemModel> mapping = new HashMap<TableRow,InventoryItemModel>();
	private ArrayList<InventoryItemModel> modelLists = new ArrayList<InventoryItemModel>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		this.setContentView(R.layout.inventory_insert);
		
		itemHandler = new InventoryItemHandler(this);
		billHandler = new InventoryBillHandler(this);
		
		state = billHandler.checkState();
		if(state == InventoryBillHandler.STATE_PAUSE)
			Toast.makeText(this, "存在未完成的盘点", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_SELECT_INENTORY_GOODS, 0, "选择盘点物品");
		menu.add(0, MENU_ADD_INVENTORY_GOODS, 1, "增加盘点商品");
		menu.add(0, MENU_SAVE, 2, "保存盘点结果");
		menu.add(0, MENU_RE_INVENTORY, 3, "重新盘点");
		menu.add(0, MENU_PAU_INVENTORY, 4, "暂停盘点");
		menu.add(1, MENU_CON_INVENTORY, 5, "继续盘点");
		menu.add(0, MENU_EXIT, 6, "退出");	
		menu.add(2, MENU_DROP, 7, "放弃");
		menu.add(0, MENU_IMPROVE, 8, "改进");
		return super.onCreateOptionsMenu(menu);	
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		Log.i("test menu", ""+billHandler.getState());
		
		for(int i = 0;i<9;i++){
			MenuItem item = menu.getItem(i);
			item.setVisible(true);
		}
		
		if(billHandler.getState()==InventoryBillHandler.STATE_PAUSE){
			for(int i = 0;i<9;i++){
				MenuItem item = menu.getItem(i);
				if(item.getGroupId()==0)
					item.setVisible(false);
			}
		}else{
			MenuItem item = menu.findItem(MENU_CON_INVENTORY);
			item.setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent;
		switch(item.getItemId()){
		case MENU_ADD_INVENTORY_GOODS:
			addInventoryGoods();
			break;
		case MENU_PAU_INVENTORY:
			saveInventory(false);
			state = billHandler.checkState();
			break;
		case MENU_CON_INVENTORY:
			continueInventory();
			break;
		case MENU_SAVE:
			saveInventory(true);
			break;
		case MENU_RE_INVENTORY:
			reInventory();
			break;
		case MENU_EXIT:
			finish();
			break;
		case MENU_SELECT_INENTORY_GOODS:
			intent = new Intent("com.tobacco.pos.activity.SelectInventoryGoods");
			startActivityForResult(intent, GET_GOODS_LIST);
			break;
		case MENU_DROP:
			drop();
			break;
		case MENU_IMPROVE:
			Toast.makeText(this, "改进：一、保存后提示调整。二、确定删除，取消继续。三、加总额(难)。四、盘点未结束冻结进出货。五、库存表。六、增加盘点使用树及模糊搜索索。七、分页。", Toast.LENGTH_LONG).show();
			break;
		}		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mapping.clear();
		modelLists.clear();
		super.onDestroy();
	}

	private void drop(){
		Log.i("state", "drop()");
		billHandler.clearInventory();
		finish();
	}
	
	private void addInventoryGoods(){
		ArrayList<Integer> exitsGoodsPriceId = new ArrayList<Integer>();
		for(InventoryItemModel model : modelLists)
			exitsGoodsPriceId.add(model.getGoodsPriceId());
		Intent intent = new Intent("com.tobacco.pos.activity.SelectInventoryGoods");
		intent.putExtra(GoodsPrice._ID, exitsGoodsPriceId);
		startActivityForResult(intent, GET_GOODS_LIST);
	}
	
	private void reInventory(){
		TableLayout table = (TableLayout)findViewById(R.id.inventoryInsertTable);
		table.removeViews(1, table.getChildCount()-1);
		modelLists.clear();
		mapping.clear();
		billHandler.clearInventory();
	}
	
	private void continueInventory(){
		int ufBillId = billHandler.getUnfinishedBillId();
		ArrayList<InventoryItemModel> unfinished = itemHandler.getItemsByBillId(ufBillId);
		for(InventoryItemModel model : unfinished)
			addTableRow(model);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case GET_GOODS_LIST:
			if(resultCode==RESULT_OK){
				ArrayList<Integer> goodsPriceIdList = data.getIntegerArrayListExtra(GoodsPrice._ID);
				Log.i(TAG, "select number:"+goodsPriceIdList.size());
				ArrayList<InventoryItemModel> itemModels = itemHandler.fillVacancy(goodsPriceIdList);
				for(InventoryItemModel model : itemModels)
					addTableRow(model);
			}
		}
	}

	protected boolean checkExistsRows(){	
		TableLayout table = (TableLayout)findViewById(R.id.inventoryInsertTable);
		Log.i("InventoryInsert", "rows:"+table.getChildCount());
		if(table.getChildCount()>=2)
			return true;
		else 
			return false;
	}

	protected void addTableRow(final InventoryItemModel model){
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);  
		final TableRow row = (TableRow)inflater.inflate(R.layout.inventory_item,null);  
		
		TextView goodsNameText = (TextView)row.findViewById(R.id.inventoryItem1);		
		TextView unitNameText = (TextView)row.findViewById(R.id.inventoryItem2);
		final TextView inPriceText = (TextView)row.findViewById(R.id.inventoryItem3);
		final TextView expectNumText = (TextView)row.findViewById(R.id.inventoryItem4);
		final EditText realNumText = (EditText)row.findViewById(R.id.inventoryItem5);
		final TextView itemResultText = (TextView)row.findViewById(R.id.inventoryItem6);

		goodsNameText.setText(model.getGoodsName());
		unitNameText.setText(model.getUnitName());
		inPriceText.setText(""+model.getGoodsInPrice());
		expectNumText.setText(""+model.getExpectNum());
		realNumText.setText(""+model.getRealNum());	
		itemResultText.setText(""+model.getItemResult());
		
		itemResultText.setPadding(10, 0, 0, 0);
		realNumText.addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(realNumText.getText().toString().matches("[0-9]+"))
				{
					int realNum = Integer.valueOf(realNumText.getText().toString());
					int expectNum = Integer.valueOf(expectNumText.getText().toString());
					int count = realNum-expectNum;
					double result = count*Double.valueOf(inPriceText.getText().toString());			
					itemResultText.setText(""+result);
				}			
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});
		final TableLayout table = (TableLayout)findViewById(R.id.inventoryInsertTable);	

		mapping.put(row, model);
		row.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				menu.setHeaderTitle("菜单项");
				menu.add(0, MENU_REMOVE, 0, "删除该商品");
				menu.findItem(MENU_REMOVE).setOnMenuItemClickListener(new OnMenuItemClickListener(){

					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						switch(item.getItemId()){
						case MENU_REMOVE:
							modelLists.remove((InventoryItemModel)mapping.get(row));
							mapping.remove(row);	
							table.removeView(row);
							return true;
							//if is continue state?!!!!!!!!!
						}
						return false;
					}
					
				});
			}
			
		});

		table.addView(row);	
		modelLists.add(model);		
	}

	protected void saveInventory(final boolean finished){
		if(checkExistsRows()){
//			String message = null;
//			switch(billHandler.getState()){
//			case InventoryBillHandler.STATE_NORMAL:
//				message = "确定要结束盘点吗？";
//				break;
//			case InventoryBillHandler.STATE_CONTINUE:
//				message = "确定要结束盘点吗？";
//				break;
//			case InventoryBillHandler.STATE_PAUSE:
//				message = "确定要暂停盘点吗？结束盘点前无法入货或销售";
//				break;
//			}
			new AlertDialog.Builder(this).setMessage("确定要结束盘点吗？")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	double result = 0;
	                		TableLayout table = (TableLayout)findViewById(R.id.inventoryInsertTable);
	                		for(int i = 0 ; i<modelLists.size();i++){
	                				
	                			TableRow row = (TableRow)table.getChildAt(i+1);
	                			EditText realNum = (EditText)row.getChildAt(4);
	                			TextView itemResult = (TextView)row.getChildAt(5);
	                			
	                			result+=Double.valueOf(itemResult.getText().toString());
	                			
	                			modelLists.get(i).setRealNum(Integer.valueOf(realNum.getText().toString()));
	                			Log.i(TAG, "itemRealNum:"+realNum.getText().toString());
	                			modelLists.get(i).setItemResult(Double.valueOf(itemResult.getText().toString()));
	                			Log.i(TAG, "itemResult:"+itemResult.getText().toString());
	                		}
	                		table.removeViews(1, table.getChildCount()-1);

	            			InventoryBillModel billModel = new InventoryBillModel(finished, result);
	            			int billId = billHandler.save(billModel);
	            			
	            			for(InventoryItemModel model : modelLists)
		            			model.setIbillId(billId);       			       			
	            			itemHandler.insert(modelLists);
	            			modelLists.clear();
	            			mapping.clear();
	                    }
	                })
	                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {

	                    }
	                }).show();
		}
		
	}

}
