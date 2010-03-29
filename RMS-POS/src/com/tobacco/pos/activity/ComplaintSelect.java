package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnFocusChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tobacco.R;
import com.tobacco.pos.entity.ComplaintFull;
import com.tobacco.pos.entity.AllTables.Complaint;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.util.DateTool;

public class ComplaintSelect extends Activity{
	private static final String TAG = "ComplaintSelect";
	private static final int MENU_SHOW_GOODS_DETAIL = Menu.FIRST;
	private static final int MENU_TIME_PICKER = Menu.FIRST+1;
//	private static final int MENU_SHOW_COMPLAINT_DETAIL = Menu.FIRST+2;
	private static final int GET_TIME=1;
	private List<TableRow> cache = new ArrayList<TableRow>();
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if(intent.getData()==null)
			intent.setData(Complaint.CONTENT_URI);
		this.setContentView(R.layout.complaint_select);
		showConsumeGoods();
	}
	
	protected void showConsumeGoods(){	
		final Cursor cursor = this.managedQuery(ComplaintFull.CONTENT_URI, ComplaintFull.PROJECTION, null, null, null);
		cursor.moveToFirst();
		for(int i = 0;i<cursor.getCount();i++){
			int goodsNameIndex = cursor.getColumnIndex(ComplaintFull.GOODS_NAME);
			String goodsName = cursor.getString(goodsNameIndex);
			int vipNameIndex = cursor.getColumnIndex(ComplaintFull.VIP_NAME);
			String vipName = cursor.getString(vipNameIndex);
			int operNameIndex = cursor.getColumnIndex(ComplaintFull.OPER_NAME);
			String operName = cursor.getString(operNameIndex);
			int timeIndex = cursor.getColumnIndex(ComplaintFull.CREATE_DATE);
			String time = cursor.getString(timeIndex);
			int contentIndex = cursor.getColumnIndex(ComplaintFull.CONTENT);
			String content = cursor.getString(contentIndex);

			TextView goods = new TextView(ComplaintSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView name = new TextView(ComplaintSelect.this,null,R.style.TextViewfillWrapSmallStyle);		
			TextView timeText = new TextView(ComplaintSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView operator = new TextView(ComplaintSelect.this,null,R.style.TextViewfillWrapSmallStyle);
			TextView contentText = new TextView(ComplaintSelect.this,null,R.style.TextViewfillWrapSmallStyle);

			setMarquee(contentText);
		
			goods.setText(goodsName);
			name.setText(vipName);
			timeText.setText(time.substring(0, time.length()-3));
			operator.setText(operName);
			contentText.setText(content);			
								
			final TableLayout table = (TableLayout)findViewById(R.id.complaintSelectTable);		
			final TableRow row = new TableRow(ComplaintSelect.this);
			row.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

				@Override
				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					// TODO Auto-generated method stub
					//onCreateContextMenu(menu, v, menuInfo);
					menu.setHeaderTitle("商品可选菜单");
					menu.add(0, MENU_SHOW_GOODS_DETAIL, 0, "商品详细");
					menu.findItem(MENU_SHOW_GOODS_DETAIL).setOnMenuItemClickListener(new OnMenuItemClickListener(){

						@Override
						public boolean onMenuItemClick(MenuItem item) {
							// TODO Auto-generated method stub
							Intent intent = new Intent("com.tobacco.pos.activity.ShowGoodsDetail");
							int goodsPriceIdIndex = cursor.getColumnIndex(ComplaintFull.GOODS_PRICE_ID);
							String goodsPriceId = cursor.getString(goodsPriceIdIndex);
							intent.putExtra(GoodsPrice._ID, goodsPriceId);
							ComplaintSelect.this.startActivity(intent);
							return true;
						}
						
					});
				}	
				
			});
		
			row.addView(goods, 0);
			row.addView(name, 1);
			row.addView(timeText, 2);
			row.addView(contentText,3);
			row.addView(operator, 4);
			
			table.addView(row);
			cache.add(row);
			cursor.moveToNext();
		}
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onCreateOptionMenu()");
		menu.add(0, MENU_TIME_PICKER, 0, "选择时间");
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onOptionsItemSelected()");
		Intent intent = new Intent("com.tobacco.pos.activity.SelectTimeRange");
		startActivityForResult(intent, GET_TIME);
		return true;
	}	 
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		cache.clear();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onActivityResult()");
		switch(requestCode){
		case GET_TIME:
			if(resultCode == RESULT_OK){		
				Date startDate = DateTool.formatStringToDate(data.getExtras().getString("startDate"));		
				Date endDate = DateTool.formatStringToDate(data.getExtras().getString("endDate"));
				TableLayout table = (TableLayout)findViewById(R.id.complaintSelectTable);
				table.removeViews(1, table.getChildCount()-1);
				for(TableRow row : cache){
					TextView time = (TextView)row.getChildAt(row.getChildCount()-3);
					String test = time.getText().toString();
					Date date = DateTool.formatStringToDate(test+".00");
					if(date.after(startDate)&&date.before(endDate)){
						table.addView(row);
					}
				}
			} 
			
			break;
		}
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
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if(hasFocus == true){
				v.setBackgroundColor(Color.RED);
			}else{
				v.setBackgroundColor(Color.BLACK);
			}
		}
	};
}

