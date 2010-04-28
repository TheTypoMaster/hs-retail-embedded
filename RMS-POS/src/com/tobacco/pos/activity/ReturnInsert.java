//package com.tobacco.pos.activity;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.BroadcastReceiver;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.ContextMenu;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//import android.widget.Toast;
//
//import com.tobacco.R;
//import com.tobacco.pos.entity.AllTables.Consume;
//import com.tobacco.pos.entity.AllTables.Goods;
//import com.tobacco.pos.entity.AllTables.GoodsPrice;
//import com.tobacco.pos.entity.AllTables.Unit;
//
//public class ConsumeInsert extends Activity{
//
//	private static final String TAG = "ConsumeInsert";
//	private static final int MENU_INPUT_CONSUME = Menu.FIRST;
//	private static final int MENU_CONFIRM = Menu.FIRST+1;
//	private static final int MENU_CANCEL = Menu.FIRST+2;
//	private static final int MENU_REMOVE = Menu.FIRST+3;
//	
//	private static final String SAVE_STATE = "save";
//	private static final String UNSAVE_STATE = "unsave";
//	private String state;
//	private static String[] PROJECTION = new String[]{
//		"Goods"+":"+Goods.goodsName,
//		"Unit"+":"+Unit.name,
//		Consume.NUMBER,
//		"GoodsPrice"+":"+GoodsPrice.inPrice,
//		"Total"
//		};
//	private static int[] TARGET = new int []{
//		R.id.cons_insert_item_text1,
//		R.id.cons_insert_item_text2,
//		R.id.cons_insert_item_text3,
//		R.id.cons_insert_item_text4,
//		R.id.cons_insert_item_text5,
//	};
//	
//	private List<HashMap<String,Object>> cache= new ArrayList<HashMap<String,Object>>();
//	private List<HashMap<String,Object>> cache_save= new ArrayList<HashMap<String,Object>>();
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		state = SAVE_STATE;
//		Intent intent = getIntent();
//		if(intent.getData()==null)
//			intent.setData(Consume.CONTENT_URI);
//		this.setContentView(R.layout.consume_insert_list);
//		
//		ListView listView = (ListView)findViewById(R.id.list_view_consume);
//		listView.setOnCreateContextMenuListener(this);
//
////		IntentFilter filter = new IntentFilter("com.tobacco.action.scan");
////		this.registerReceiver(new ScanReceiver(), filter);
////		this.startService(new Intent(this,ScanInputService.class));
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		menu.add(0, MENU_INPUT_CONSUME, 0, "input by hand")
//		.setIcon(android.R.drawable.ic_menu_add);
//		
//		menu.add(0, MENU_CANCEL, 0, "quit")
//		.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
//		
//		menu.add(0, MENU_CONFIRM, 0, "confirm")
//		.setIcon(android.R.drawable.ic_menu_save);
//		
//		return true;
//	}
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		switch(item.getItemId()){
//		case MENU_INPUT_CONSUME:
//			Intent intent = new Intent("com.tobacco.pos.activity.ConsumeInsertDialog");
//			intent.setData(getIntent().getData());
//			intent.putExtra("projection", PROJECTION);
////			startActivity(intent);
//			this.startActivityForResult(intent, GET_CON);
//			return true;
//		case MENU_CANCEL:
//			if(state == UNSAVE_STATE){
//				new AlertDialog.Builder(ConsumeInsert.this)
//	            .setIcon(R.drawable.alert_dialog_icon)
//	            .setTitle("尚未保存,确定要退出吗？")
//	            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//	                public void onClick(DialogInterface dialog, int whichButton) {
//	                	finish();
//	                }
//	            })
//	            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//	                public void onClick(DialogInterface dialog, int whichButton) {
//	                	
//	                }
//	            })
//	            .show();
//			}
//			else{
//				finish();
//			}
//			return true;
//		case MENU_CONFIRM:
//			Iterator<HashMap<String,Object>> cacheIter = cache.iterator();
//			Iterator<HashMap<String,Object>> cacheSaveIter = cache_save.iterator();
//			HashMap<String,Object> map , mapId;
//			while(cacheIter.hasNext()){
//				map = cacheIter.next();
//				mapId = cacheSaveIter.next();
//				ContentValues values = new ContentValues();
//				values.put(Consume.GOODS, mapId.get("GoodsPrice:_ID").toString());
//				values.put(Consume.NUMBER, map.get(Consume.NUMBER).toString());
//			    getContentResolver().insert(getIntent().getData(), values);
//			}
//			Toast.makeText(ConsumeInsert.this, "保存成功", Toast.LENGTH_SHORT).show();
//			cache.clear();
//			state = SAVE_STATE;
//			onResume();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//	
//	private static final int GET_CON = 0;
//	private int index = 1;
//	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		if(requestCode == GET_CON)
//		{
//			if(resultCode == RESULT_OK){
////				new AlertDialog.Builder(ConsumeInsert.this)
////				.setMessage("barcode: "+data.getExtras().getString(Consume.GOODS)
////						+"number: "+data.getExtras().getString(Consume.NUMBER))
////						.show();
//				HashMap<String,Object> map = new HashMap<String,Object>();
//				HashMap<String,Object> mapGoodspriceId = new HashMap<String,Object>();
//				for(int i = 0; i<PROJECTION.length;i++){
//					map.put(PROJECTION[i], data.getExtras().getString(PROJECTION[i]));
//				}		
//				mapGoodspriceId.put("GoodsPrice:_ID", data.getExtras().getString("GoodsPrice:_ID"));
//				cache.add(map);
//				cache_save.add(mapGoodspriceId);
//				state = UNSAVE_STATE;
//				//getData(data.getExtras().getString("BARCODE"));
//				//onResume();
//			}
//		}
//	}
//	
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
////		SimpleAdapter listItemAdapter = new SimpleAdapter(this,cache,R.layout.insert_consume_item,
////				new String[]{Consume._ID,Consume.GOODS,Consume.NUMBER},new int[]{R.id.cons_item_text1,
////				R.id.cons_item_text2,R.id.cons_item_text3});
//		
//		SimpleAdapter listItemAdapter = new SimpleAdapter(this,cache,R.layout.consume_insert_item,
//				PROJECTION,TARGET);
//		ListView listView = (ListView)findViewById(R.id.list_view_consume);
//		listView.setAdapter(listItemAdapter);
//	}
//
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		AdapterView.AdapterContextMenuInfo info = null;
//		try{
//			info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
//		}catch(ClassCastException cce){
//			Log.e(TAG, "wrong cast", cce);
//			return false;
//		}
//		
//		switch(item.getItemId()){
//		case MENU_REMOVE:
//			cache.remove(info.position);
//			onResume();
//			return true;
//		}
//		return false;
//	}
//
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		menu.setHeaderTitle("Menu for item.");
//		menu.add(0, MENU_REMOVE, 0, "Remove the item");
//	}	
//	
//	public class ScanReceiver extends BroadcastReceiver{
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//			int barcode = intent.getIntExtra("BARCODE", 0);
//			new AlertDialog.Builder(ConsumeInsert.this).setMessage("barcode:"+barcode).show();
//		}
//		
//	}
//}


package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.tobacco.R;
import com.tobacco.pos.contentProvider.GoodsCPer;
import com.tobacco.pos.contentProvider.GoodsPriceCPer;
import com.tobacco.pos.contentProvider.UnitCPer;
import com.tobacco.pos.contentProvider.UserInfoCPer;
import com.tobacco.pos.contentProvider.VIPInfoCPer;
import com.tobacco.pos.entity.AllTables.Complaint;
import com.tobacco.pos.entity.AllTables.Consume;
import com.tobacco.pos.entity.AllTables.Goods;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.Return;
import com.tobacco.pos.entity.AllTables.Unit;
import com.tobacco.pos.entity.AllTables.VIPInfo;

public class ReturnInsert extends Activity{

	private static final String TAG = "ReturnInsert";
	
	private static final int MENU_INPUT_RETURN = Menu.FIRST;
	private static final int MENU_CONFIRM = Menu.FIRST+1;
	private static final int MENU_CANCEL = Menu.FIRST+2;
	private static final int MENU_REMOVE = Menu.FIRST+3;
	private static final int GET_CON = 0;
	private static final int SAVE_STATE = 1;
	private static final int UNSAVE_STATE = 0;
	private int state;
	
	private List<HashMap<String,Object>> cache= new ArrayList<HashMap<String,Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		state = SAVE_STATE;
		Intent intent = getIntent();
		if(intent.getData()==null)
			intent.setData(Return.CONTENT_URI);
		this.setContentView(R.layout.return_insert);       
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_INPUT_RETURN, 0, "手动输入")
		.setIcon(android.R.drawable.ic_menu_add);
		
		menu.add(0, MENU_CANCEL, 0, "退出")
		.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		
		menu.add(0, MENU_CONFIRM, 0, "保存")
		.setIcon(android.R.drawable.ic_menu_save);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case MENU_INPUT_RETURN:
			Intent intent = new Intent("com.tobacco.pos.activity.ReturnInsertDialog");
			this.startActivityForResult(intent, GET_CON);
			return true;
		case MENU_CANCEL:
			if(state == UNSAVE_STATE){
				new AlertDialog.Builder(ReturnInsert.this)
	            .setIcon(R.drawable.alert_dialog_icon)
	            .setTitle("尚未保存,确定要退出吗？")
	            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	finish();
	                }
	            })
	            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	
	                }
	            })
	            .show();
			}
			else{
				finish();
			}
			return true;
		case MENU_CONFIRM:
			for(HashMap<String,Object> map : cache){
				ContentValues values = new ContentValues();
				Iterator<String> keys = map.keySet().iterator();
				while(keys.hasNext()){
					String key = keys.next();
					values.put(key, map.get(key).toString());
				}
				getContentResolver().insert(getIntent().getData(), values);
			}
			Toast.makeText(ReturnInsert.this, "保存成功", Toast.LENGTH_SHORT).show();
			cache.clear();
			state = SAVE_STATE;
			TableLayout table = (TableLayout)findViewById(R.id.returnInsertTable);
			table.removeViews(1, table.getChildCount()-1);
			onResume();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == GET_CON)
		{
			if(resultCode == RESULT_OK){
				String vipNum = data.getExtras().getString(Return.VIP_ID);
				String barcode = data.getExtras().getString(GoodsPrice.barcode);
				int count = Integer.valueOf(data.getExtras().getString(Return.NUMBER));
				String content = data.getExtras().getString(Return.CONTENT);
				showReturnGoods(vipNum,barcode,count,content);
				state = UNSAVE_STATE;
			}
		}
	}
	
	protected void showReturnGoods(String vipNum, String barcode, int count, String content){
		GoodsPriceCPer goodsPriceCPer = new GoodsPriceCPer();
		GoodsCPer goodsCPer = new GoodsCPer();
		UnitCPer unitCPer = new UnitCPer();
		VIPInfoCPer vipInfoCPer = new VIPInfoCPer();
//		UserInfoCPer userInfoCPer = new UserInfoCPer();

		String userName;
		if(vipNum.equals("common")){
			userName = "普通客户";
		}else{
//			userName = vipInfoCPer.getVIPNameByVIPId(vipInfoCPer.getVIPIdByVIPNum(vipNum));
			userName = vipInfoCPer.getAttributeByAttribute(VIPInfo.VIPName, VIPInfo.VIPNum, vipNum);
		}

		String goodsPriceId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice._ID, GoodsPrice.barcode, barcode);
		String goodsId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice.goodsId, GoodsPrice.barcode, barcode);
		String goodsName = goodsCPer.getAttributeById(Goods.goodsName, goodsId);		
		String unitId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice.unitId, GoodsPrice.barcode, barcode);
		String unitName = unitCPer.getAttributeById(Unit.name, unitId);
		
		TextView nameText = new TextView(ReturnInsert.this,null,R.style.TextViewfillWrapSmallStyle);		
		TextView unitText = new TextView(ReturnInsert.this,null,R.style.TextViewfillWrapSmallStyle);
		TextView numberText = new TextView(ReturnInsert.this,null,R.style.TextViewfillWrapSmallStyle);
		TextView consumerText = new TextView(ReturnInsert.this,null,R.style.TextViewfillWrapSmallStyle);
		TextView contentText = new TextView(ReturnInsert.this,null,R.style.TextViewfillWrapSmallStyle);
		nameText.setText(goodsName);
		unitText.setText(unitName);
		numberText.setText(new String().valueOf(count));
		consumerText.setText(userName);
		contentText.setText(content);
		final TableLayout table = (TableLayout)findViewById(R.id.returnInsertTable);
		
		final TableRow row = new TableRow(ReturnInsert.this);
		row.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				menuInfo = new AdapterContextMenuInfo(row, 0, 0);
				menu.setHeaderTitle("菜单项");
				menu.add(0, MENU_REMOVE, 0, "删除该记录");
				menu.findItem(MENU_REMOVE).setOnMenuItemClickListener(new OnMenuItemClickListener(){

					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						switch(item.getItemId()){
						case MENU_REMOVE:
							table.removeView(row);
							return true;
						}
						return false;
					}
					
				});
			}
			
		});
		
		row.addView(nameText, 0);
		row.addView(unitText, 1);
		row.addView(numberText, 2);
		row.addView(consumerText, 3);
		row.addView(contentText, 4);
		table.addView(row);
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put(Return.VIP_ID, vipInfoCPer.getAttributeByAttribute(VIPInfo._ID, VIPInfo.VIPNum, vipNum));
		map.put(Return.GOODS_ID, goodsPriceId);
		map.put(Return.NUMBER, count);
		map.put(Return.CONTENT, content);
		cache.add(map);
		onResume();
	}
	
	public class ScanReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int barcode = intent.getIntExtra("BARCODE", 0);
			new AlertDialog.Builder(ReturnInsert.this).setMessage("barcode:"+barcode).show();
		}
		
	}
}

