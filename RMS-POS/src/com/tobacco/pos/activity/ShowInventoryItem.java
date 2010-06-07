package com.tobacco.pos.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
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
import com.tobacco.main.activity.view.RMSBaseView;
import com.tobacco.pos.entity.InventoryItemModel;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.InventoryItem;
import com.tobacco.pos.handler.InventoryItemHandler;

public class ShowInventoryItem extends RMSBaseView{
	
	private static final String TAG = "ShowInventoryItem";
	private static final int MENU_SHOW_GOODS_DETAIL = Menu.FIRST;
	private InventoryItemHandler handler;
	ArrayList<InventoryItemModel> items ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.inventory_item_select);
		Intent intent = getIntent();
		String iBillId = intent.getStringExtra(InventoryItem.IBILL_ID);
		
		handler = new InventoryItemHandler(this);
		
		items = handler.getItemsByBillId(Integer.valueOf(iBillId));
		showItems();
	}
	
	protected void showItems(){
		int i = 0;
		for(final InventoryItemModel model : items){
			LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);  
			final TableRow row = (TableRow)inflater.inflate(R.layout.table_row_seven,null);  
			
			TextView goodsIndexText = (TextView)row.findViewById(R.id.text_seven1);
			TextView goodsNameText = (TextView)row.findViewById(R.id.text_seven2);		
			TextView unitNameText = (TextView)row.findViewById(R.id.text_seven3);
			final TextView inPriceText = (TextView)row.findViewById(R.id.text_seven4);
			final TextView expectNumText = (TextView)row.findViewById(R.id.text_seven5);
			final TextView realNumText = (TextView)row.findViewById(R.id.text_seven6);
			final TextView itemResultText = (TextView)row.findViewById(R.id.text_seven7);

			goodsIndexText.setText(""+(++i));
			goodsNameText.setText(model.getGoodsName());
			unitNameText.setText(model.getUnitName());
			inPriceText.setText(""+model.getGoodsInPrice());
			expectNumText.setText(""+model.getExpectNum());
			realNumText.setText(""+model.getRealNum());	
			itemResultText.setText(""+model.getItemResult());		
			itemResultText.setPadding(10, 0, 0, 0);

			TableLayout table = (TableLayout)findViewById(R.id.inventoryItemSelectTable);	
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
			table.addView(row);
		}
	}
	
}
