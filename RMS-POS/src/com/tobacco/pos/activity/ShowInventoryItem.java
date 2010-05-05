package com.tobacco.pos.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.tobacco.pos.entity.InventoryItemModel;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.InventoryItem;
import com.tobacco.pos.handler.InventoryItemHandler;

public class ShowInventoryItem extends Activity{
	
	private static final String TAG = "ShowInventoryItem";
	private static final int MENU_SHOW_GOODS_DETAIL = Menu.FIRST;
	private InventoryItemHandler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.inventory_item_select);
		Intent intent = getIntent();
		String iBillId = intent.getStringExtra(InventoryItem.IBILL_ID);
		
		handler = new InventoryItemHandler(this);
		ArrayList<InventoryItemModel> items = handler.getItemsByBillId(Integer.valueOf(iBillId));
		showItems(items);
	}
	
	protected void showItems(ArrayList<InventoryItemModel> items){
		for(InventoryItemModel item : items)
			addTableRow(item);
	}
	
	protected void addTableRow(final InventoryItemModel model){
		
		TextView goodsNameText = new TextView(ShowInventoryItem.this,null,R.style.TextViewfillWrapSmallStyle);		
		TextView unitNameText = new TextView(ShowInventoryItem.this,null,R.style.TextViewfillWrapSmallStyle);
		final TextView inPriceText = new TextView(ShowInventoryItem.this,null,R.style.TextViewfillWrapSmallStyle);
		final TextView expectNumText = new TextView(ShowInventoryItem.this,null,R.style.TextViewfillWrapSmallStyle);
		final TextView realNumText = new TextView(ShowInventoryItem.this,null,R.style.TextViewfillWrapSmallStyle);
		final TextView itemResultText = new TextView(ShowInventoryItem.this,null,R.style.TextViewfillWrapSmallStyle);

		goodsNameText.setText(model.getGoodsName());
		unitNameText.setText(model.getUnitName());
		inPriceText.setText(""+model.getGoodsInPrice());
		expectNumText.setText(""+model.getExpectNum());
		realNumText.setText(""+model.getRealNum());	
		itemResultText.setText(""+model.getItemResult());
		
		itemResultText.setPadding(10, 0, 0, 0);

		TableLayout table = (TableLayout)findViewById(R.id.inventoryItemSelectTable);	
		TableRow row = new TableRow(ShowInventoryItem.this);
		row.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				menu.setHeaderTitle("菜单项");
				menu.add(0, MENU_SHOW_GOODS_DETAIL, 0, "商品明细");
				menu.findItem(MENU_SHOW_GOODS_DETAIL).setOnMenuItemClickListener(new OnMenuItemClickListener(){

					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						Intent intent = new Intent("com.tobacco.pos.activity.ShowGoodsDetail");
						intent.putExtra(GoodsPrice._ID, String.valueOf(model.getGoodsPriceId()));
						ShowInventoryItem.this.startActivity(intent);
						return true;
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
	}
	
//	protected void addTableRow(String goodsName,String unitName,Double inPrice,String expectNum,String realNum,String result,final String goodsPriceId){
//		
//		TextView goodsNameText = new TextView(ShowInventoryItem.this,null,R.style.TextViewfillWrapSmallStyle);		
//		TextView unitNameText = new TextView(ShowInventoryItem.this,null,R.style.TextViewfillWrapSmallStyle);
//		final TextView inPriceText = new TextView(ShowInventoryItem.this,null,R.style.TextViewfillWrapSmallStyle);
//		final TextView expectNumText = new TextView(ShowInventoryItem.this,null,R.style.TextViewfillWrapSmallStyle);
//		final TextView realNumText = new TextView(ShowInventoryItem.this,null,R.style.TextViewfillWrapSmallStyle);
//		final TextView itemResultText = new TextView(ShowInventoryItem.this,null,R.style.TextViewfillWrapSmallStyle);
//
//		goodsNameText.setText(goodsName);
//		unitNameText.setText(unitName);
//		inPriceText.setText(inPrice.toString());
//		//now we don't have stock table. so use 100 for test.
//		expectNumText.setText((expectNum==null)?"100":expectNum);
//		realNumText.setText((realNum==null)?"0":realNum);	
//		itemResultText.setText((result==null)?"0":result);
//		
//		itemResultText.setPadding(10, 0, 0, 0);
////		realNumText.addTextChangedListener(new TextWatcher(){
////
////			@Override
////			public void afterTextChanged(Editable s) {
////				// TODO Auto-generated method stub
////				if(realNumText.getText().toString().matches("[0-9]+"))
////				{
////					int realNum = Integer.valueOf(realNumText.getText().toString());
////					int expectNum = Integer.valueOf(expectNumText.getText().toString());
////					int count = realNum-expectNum;
////					double result = count*Double.valueOf(inPriceText.getText().toString());			
////					itemResultText.setText(""+result);
////				}			
////			}
////
////			@Override
////			public void beforeTextChanged(CharSequence s, int start, int count,
////					int after) {
////				// TODO Auto-generated method stub
////			}
////
////			@Override
////			public void onTextChanged(CharSequence s, int start, int before,
////					int count) {
////				// TODO Auto-generated method stub
////				
////			}
////			
////		});
//		final TableLayout table = (TableLayout)findViewById(R.id.inventoryItemSelectTable);	
//		final TableRow row = new TableRow(ShowInventoryItem.this);
//		row.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
//
//			public void onCreateContextMenu(ContextMenu menu, View v,
//					ContextMenuInfo menuInfo) {
//				// TODO Auto-generated method stub
//				menu.setHeaderTitle("菜单项");
//				menu.add(0, MENU_SHOW_GOODS_DETAIL, 0, "商品明细");
//				menu.findItem(MENU_SHOW_GOODS_DETAIL).setOnMenuItemClickListener(new OnMenuItemClickListener(){
//
//					public boolean onMenuItemClick(MenuItem item) {
//						// TODO Auto-generated method stub
//						Intent intent = new Intent("com.tobacco.pos.activity.ShowGoodsDetail");
//						intent.putExtra(GoodsPrice._ID, goodsPriceId);
//						ShowInventoryItem.this.startActivity(intent);
//						return true;
//					}
//					
//				});
//			}
//			
//		});
//		
//		row.addView(goodsNameText, 0);
//		row.addView(unitNameText, 1);
//		row.addView(inPriceText, 2);
//		row.addView(expectNumText, 3);
//		row.addView(realNumText, 4);
//		row.addView(itemResultText, 5);
//		
//		table.addView(row);
//	}
}
