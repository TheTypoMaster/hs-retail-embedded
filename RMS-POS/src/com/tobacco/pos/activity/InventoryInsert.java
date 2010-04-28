package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
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
import com.tobacco.pos.contentProvider.GoodsCPer;
import com.tobacco.pos.contentProvider.GoodsPriceCPer;
import com.tobacco.pos.contentProvider.UnitCPer;
import com.tobacco.pos.entity.InventoryItemFull;
import com.tobacco.pos.entity.AllTables.Goods;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.InventoryBill;
import com.tobacco.pos.entity.AllTables.InventoryItem;
import com.tobacco.pos.entity.AllTables.Unit;

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
	
	private final int FLAG_PAUSE = 0;
	private final int FLAG_SAVE = 1;
	
	private final int STATE_NORMAL = 0;
	private final int STATE_PAUSE = 1; 
	private final int STATE_CONTINUE = 2; 
	private int state ;
	
	private final int GET_GOODS = 0;
	
	private final ArrayList<HashMap<String,Object>> cache = new ArrayList<HashMap<String,Object>>();
	private final ArrayList<String> goodsPriceIdList = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		this.setContentView(R.layout.inventory_insert);
		Cursor c = managedQuery(InventoryBill.CONTENT_URI, new String[]{InventoryBill._ID}, InventoryBill.FINISHED+" =0", null, null);	
		if(c.getCount()>0)
			state = STATE_PAUSE;
		else
			state = STATE_NORMAL;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_ADD_INVENTORY_GOODS, 0, "增加盘点商品");
		menu.add(0, MENU_SAVE, 1, "保存盘点结果");
		menu.add(0, MENU_RE_INVENTORY, 2, "重新盘点");
		menu.add(0, MENU_PAU_INVENTORY, 3, "暂停盘点");
		menu.add(0, MENU_CON_INVENTORY, 4, "继续盘点");
		menu.add(0, MENU_SELECT_INENTORY_GOODS, 5, "选择盘点物品");
		menu.add(0, MENU_EXIT, 6, "退出");
		menu.add(0, MENU_IMPROVE, 7, "改进");
		
		return super.onCreateOptionsMenu(menu);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case MENU_ADD_INVENTORY_GOODS:
			if(state != STATE_PAUSE){
				Intent intent = new Intent("com.tobacco.pos.activity.AddInventoryGoods");
				startActivityForResult(intent, GET_GOODS);
			}else{
				Toast.makeText(this, "存在未结束的盘点", Toast.LENGTH_SHORT).show();
			}
			break;
		case MENU_PAU_INVENTORY:
			if(checkExistsRows())
				pauseInventory();
			break;
		case MENU_CON_INVENTORY:
			if(state == STATE_PAUSE){
				continueInventory();
			}else{
				Toast.makeText(this, "没有未结束的盘点", Toast.LENGTH_SHORT).show();
			}	
			break;
		case MENU_SAVE:
			if(checkExistsRows())
				saveInventory();
			break;
		case MENU_RE_INVENTORY:
			if(checkExistsRows())
				reInventory();
			break;
		case MENU_EXIT:
			finish();
			break;
		case MENU_SELECT_INENTORY_GOODS:
			Toast.makeText(this, "未实现，需要树型控件", Toast.LENGTH_SHORT).show();
			break;
		case MENU_IMPROVE:
			Toast.makeText(this, "改进：一、保存后提示调整。二、确定删除，取消继续。三、加总额(难)。四、盘点未结束冻结进出货。五、库存表。六、增加盘点使用树及模糊搜索索。七、分页。", Toast.LENGTH_LONG).show();
			break;
		}
		
			
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case GET_GOODS:
			if(resultCode==RESULT_OK){
				String barcode = data.getStringExtra(GoodsPrice.barcode);
				addInventoryGoods(barcode);
				break;
			}
		}
	}
	
	//temp method. should be modified.
	protected void addInventoryGoods(String barcode){
		
		GoodsPriceCPer goodsPriceCPer = new GoodsPriceCPer();
		GoodsCPer goodsCPer = new GoodsCPer();
		UnitCPer unitCPer = new UnitCPer();

		final String goodsPriceId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice._ID, GoodsPrice.barcode, barcode);
		String goodsId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice.goodsId, GoodsPrice.barcode, barcode);
		String goodsName = goodsCPer.getAttributeById(Goods.goodsName, goodsId);		
		String unitId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice.unitId, GoodsPrice.barcode, barcode);
		String unitName = unitCPer.getAttributeById(Unit.name, unitId);
		Double inPrice = Double.valueOf(goodsPriceCPer.getAttributeByAttribute(GoodsPrice.inPrice, GoodsPrice.barcode, barcode));
		
		addTableRow(goodsName,unitName,inPrice,null,null,null,goodsPriceId);
	}

	protected void continueInventory(){
		
		Cursor c = managedQuery(InventoryBill.CONTENT_URI, new String[]{InventoryBill._ID}, InventoryBill.FINISHED+" =?", new String[]{"0"}, null);
		if(c.getCount()>0){
			c.moveToFirst();
			String billId = c.getString(0);
			Cursor cursor = managedQuery(InventoryItemFull.CONTENT_URI, 
					new String[]{InventoryItemFull.GOODS_NAME,InventoryItemFull.UNIT_NAME,InventoryItemFull.GOODS_PRICE_ID,
					InventoryItemFull.GOODS_INPRICE, InventoryItemFull.EXPECT_NUM,InventoryItemFull.REAL_NUM,InventoryItemFull.ITEM_RESULT},
					InventoryItemFull.IBILL_ID+" =?", new String[]{billId}, null);
			cursor.moveToFirst();
			for(int i = 0;i<cursor.getCount();i++){
				int goodsNameIndex = cursor.getColumnIndex(InventoryItemFull.GOODS_NAME);
				String goodsName = cursor.getString(goodsNameIndex);
				int unitNameIndex = cursor.getColumnIndex(InventoryItemFull.UNIT_NAME);
				String unitName = cursor.getString(unitNameIndex);
				int goodsPriceIndex = cursor.getColumnIndex(InventoryItemFull.GOODS_INPRICE);
				double goodsPrice = cursor.getDouble(goodsPriceIndex);
				int expectNumIndex = cursor.getColumnIndex(InventoryItemFull.EXPECT_NUM);
				String expectNum = cursor.getString(expectNumIndex);
				int realNumIndex = cursor.getColumnIndex(InventoryItemFull.REAL_NUM);
				String realNum = cursor.getString(realNumIndex);
				int itemResultIndex = cursor.getColumnIndex(InventoryItemFull.ITEM_RESULT);
				String itemResult = cursor.getString(itemResultIndex);
				int goodsPriceIdIndex = cursor.getColumnIndex(InventoryItemFull.GOODS_PRICE_ID);
				String goodsPriceId = cursor.getString(goodsPriceIdIndex);
				
				addTableRow(goodsName,unitName,goodsPrice,expectNum,realNum,itemResult,goodsPriceId);
				cursor.moveToNext();
			}
			
			//delete last time's data. because the data may change.
			this.getContentResolver().delete(InventoryItem.CONTENT_URI, InventoryItem.IBILL_ID+" =?", new String[]{billId});
			state = STATE_CONTINUE;
		}else{
			new AlertDialog.Builder(this).setMessage("没有未完成的盘点单").show();
		}
	}

	protected void reInventory(){
		TableLayout table = (TableLayout)findViewById(R.id.inventoryInsertTable);
		table.removeViews(1, table.getChildCount()-1);
		goodsPriceIdList.clear();
	}
	
	protected void deletePauseInventory(){
		Cursor c = managedQuery(InventoryBill.CONTENT_URI, new String[]{InventoryBill._ID}, null, null, null);	
		c.moveToLast();
		int iBillId = c.getInt(0);
		Uri billUri = ContentUris.withAppendedId(InventoryBill.CONTENT_URI, iBillId);
		
		this.getContentResolver().delete(InventoryItem.CONTENT_URI, InventoryItem.IBILL_ID+" =?", new String[]{String.valueOf(iBillId)});
		this.getContentResolver().delete(billUri, null, null);
	}
	
	protected void pauseInventory(){
		Uri billUri = null;
		switch(state){
		case STATE_NORMAL:
			billUri = newInventoryBill(FLAG_PAUSE);
			break;
		case STATE_CONTINUE:
			billUri = getPauseInventoryBill();	
			break;
		}
		state = STATE_PAUSE;
		save(billUri);	
	}
	
	protected void saveInventory(){
		Uri billUri = null;
		switch(state){
		case STATE_NORMAL:
			billUri = newInventoryBill(FLAG_SAVE);
			break;
		case STATE_CONTINUE:
			billUri = updatePauseInventoryBill();
			state = STATE_NORMAL;
			break;
		}
		save(billUri);
		// note to convert the stock .
	}
	
	protected boolean checkExistsRows(){
		TableLayout table = (TableLayout)findViewById(R.id.inventoryInsertTable);
		if(table.getChildCount()>=2)
			return true;
		else 
			return false;
	}
	
	protected Uri newInventoryBill(int flag){
		Cursor c = InventoryInsert.this.managedQuery(InventoryBill.CONTENT_URI, new String[]{InventoryBill._ID}, null, null, null);	          		
		int count = c.getCount();
		int iBillId ; 
		if(count==0){
			iBillId = 1;
		}else{
			c.moveToLast();
			iBillId = c.getInt(0)+1;
		}
		ContentValues values = new ContentValues();    		
		values.put(InventoryBill.IBILL_NUM, "Num00000"+iBillId);
		values.put(InventoryBill.FINISHED, flag);
		values.put(InventoryBill.RESULT, 0.0);
		Uri billUri = InventoryInsert.this.getContentResolver().insert(InventoryBill.CONTENT_URI, values);
		return billUri;
	}
	
	protected Uri getPauseInventoryBill(){
		Cursor c = managedQuery(InventoryBill.CONTENT_URI, new String[]{InventoryBill._ID}, null, null, null);	
		c.moveToLast();
		int iBillId = c.getInt(0);
		Uri billUri = ContentUris.withAppendedId(InventoryBill.CONTENT_URI, iBillId);
		return billUri;
	}
	
	protected Uri updatePauseInventoryBill(){
		Uri billUri = getPauseInventoryBill();	
		ContentValues upateValues = new ContentValues();
		upateValues.put(InventoryBill.FINISHED, 1);
		this.getContentResolver().update(billUri, upateValues, null, null);
		
		return billUri;
	}
	
	protected void addTableRow(String goodsName,String unitName,Double inPrice,String expectNum,String realNum,String result,final String goodsPriceId){
		
//		final TextView totalText = (TextView)findViewById(R.id.totalInventoryInsert);
		TextView goodsNameText = new TextView(InventoryInsert.this,null,R.style.TextViewfillWrapSmallStyle);		
		TextView unitNameText = new TextView(InventoryInsert.this,null,R.style.TextViewfillWrapSmallStyle);
		final TextView inPriceText = new TextView(InventoryInsert.this,null,R.style.TextViewfillWrapSmallStyle);
		final TextView expectNumText = new TextView(InventoryInsert.this,null,R.style.TextViewfillWrapSmallStyle);
		final EditText realNumText = new EditText(InventoryInsert.this);
		final TextView itemResultText = new TextView(InventoryInsert.this,null,R.style.TextViewfillWrapSmallStyle);
		
//		totalText.setText("0");
		goodsNameText.setText(goodsName);
		unitNameText.setText(unitName);
		inPriceText.setText(inPrice.toString());
		//now we don't have stock table. so use 100 for test.
		expectNumText.setText((expectNum==null)?"100":expectNum);
		realNumText.setText((realNum==null)?"0":realNum);	
		itemResultText.setText((result==null)?"0":result);
		
		itemResultText.setPadding(10, 0, 0, 0);
		realNumText.addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(realNumText.getText().toString().matches("[0-9]+"))
				{
					int realNum = Integer.valueOf(realNumText.getText().toString());
					int expectNum = Integer.valueOf(expectNumText.getText().toString());
//					int count = (realNum>=expectNum)? realNum-expectNum : expectNum-realNum;
//					double result = count*Double.valueOf(inPriceText.getText().toString());
//					String flag = (realNum>=expectNum)? "" : "-";
//					itemResultText.setText(flag+result);
					int count = realNum-expectNum;
					double result = count*Double.valueOf(inPriceText.getText().toString());			
					itemResultText.setText(""+result);
//					totalText.setText(String.valueOf(Double.valueOf(totalText.getText().toString())+result));
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
		final TableRow row = new TableRow(InventoryInsert.this);
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
							table.removeView(row);
							goodsPriceIdList.remove(goodsPriceId);
							//if is continue state?!!!!!!!!!
							return true;
						}
						return false;
					}
					
				});
			}
			
		});
		
		row.addView(goodsNameText, 0);
		row.addView(unitNameText, 1);
		row.addView(inPriceText, 2);
		row.addView(expectNumText, 3);
		row.addView(realNumText, 4);
		row.addView(itemResultText, 5);
		
		table.addView(row);
		goodsPriceIdList.add(goodsPriceId);
		
//		realNumText.setOnFocusChangeListener(new OnFocusChangeListener(){
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				// TODO Auto-generated method stub
//				if(hasFocus==true){
//					realNumText.setText("");
//				}else{
//					//test if the input is number;
//					int realNum = Integer.valueOf(realNumText.getText().toString());
//					int expectNum = Integer.valueOf(expectNumText.getText().toString());
//					int count = (realNum>=expectNum)? realNum-expectNum : expectNum-realNum;
//					double result = count*Double.valueOf(inPriceText.getText().toString());
//					String flag = (realNum>=expectNum)? "" : "-";
//					itemResultText.setText(flag+result);
//					
//					HashMap<String,Object> map = new HashMap<String, Object>();
//					map.put(InventoryItem.GOODS_PRICE_ID, goodsPriceId);
//					map.put(InventoryItem.EXPECT_NUM, expectNumText.getText().toString());
//					map.put(InventoryItem.REAL_NUM, realNumText.getText().toString());
//					map.put(InventoryItem.ITEM_RESULT, itemResultText.getText().toString());
//					cache.add(map);
//				}
//			}
//			
//		});
	}

	protected void save(final Uri billUri){
		String message = null;
		switch(state){
		case STATE_NORMAL:
			message = "确定要结束盘点吗？";
			break;
		case STATE_CONTINUE:
			message = "确定要结束盘点吗？";
			break;
		case STATE_PAUSE:
			message = "确定要暂停盘点吗？结束盘点前无法入货或销售";
			break;
		}
		new AlertDialog.Builder(this).setMessage(message)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
               		
                		//save the inventory goods.
                		double result = 0.0;
                		TableLayout table = (TableLayout)findViewById(R.id.inventoryInsertTable);
                		for(int i = 0 ; i<goodsPriceIdList.size();i++){
                				
                			TableRow row = (TableRow)table.getChildAt(i+1);
                			TextView expectNum = (TextView)row.getChildAt(3);
                			EditText realNum = (EditText)row.getChildAt(4);
                			TextView itemResult = (TextView)row.getChildAt(5);
                			
                			result += Double.valueOf(itemResult.getText().toString());
                			
                			ContentValues itemValues = new ContentValues();
                			itemValues.put(InventoryItem.IBILL_ID, billUri.getPathSegments().get(1));
                			itemValues.put(InventoryItem.GOODS_PRICE_ID, goodsPriceIdList.get(i));
                			itemValues.put(InventoryItem.EXPECT_NUM, expectNum.getText().toString());
                			itemValues.put(InventoryItem.REAL_NUM, realNum.getText().toString());
                			itemValues.put(InventoryItem.ITEM_RESULT, itemResult.getText().toString());
                			InventoryInsert.this.getContentResolver().insert(InventoryItem.CONTENT_URI, itemValues);
                		}
                		//clear the interface.
                		table.removeViews(1, table.getChildCount()-1);
                		goodsPriceIdList.clear();
                		//update the Inventory bill.
                		ContentValues updateValues = new ContentValues();
                		updateValues.put(InventoryBill.RESULT, result);
                		InventoryInsert.this.getContentResolver().update(billUri, updateValues, null, null);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked Cancel so do some stuff */
                    }
                }).show();
	}

	//not finished yet.
	protected boolean checkPauseState(){
		if(state == STATE_PAUSE)
		{
			final TextView choice = new TextView(this);
			choice.setVisibility(View.INVISIBLE);
			new AlertDialog.Builder(this).setMessage("存在未完成的盘点，确定删除未完成盘点并继续操作，取消返回未完成盘点").setView(choice)
			.setPositiveButton("确定", new OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					choice.setText("true");
				}			
			})
			.setNegativeButton("取消", new OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					choice.setText("false");
				}			
			}).show();
			
			if(choice.getText().toString().equals("true"))
			{
				deletePauseInventory();
				return true;
			}
			else
				return false;
		}
			
		return true;
	}
}
