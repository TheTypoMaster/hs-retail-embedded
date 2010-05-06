package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
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
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.tobacco.R;
import com.tobacco.main.activity.view.RMSBaseView;
import com.tobacco.pos.entity.ConsumeModel;
import com.tobacco.pos.entity.AllTables.Consume;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.handler.ConsumeHandler;
import com.tobacco.pos.util.RegexCheck;

public class ConsumeInsert extends RMSBaseView{
	
	private static final String TAG = "ConsumeInsert";
	
	private static final int MENU_INPUT_CONSUME = Menu.FIRST;
	private static final int MENU_CONFIRM = Menu.FIRST+1;
	private static final int MENU_CANCEL = Menu.FIRST+2;
	private static final int MENU_REMOVE = Menu.FIRST+3;
	private static final int MENU_MOFIFY_NUM = Menu.FIRST+4;
	
	private static final int SAVE_STATE = 1;
	private static final int UNSAVE_STATE = 0;
	private int state;
	private static final int GET_CON = 0;
	
	private ConsumeHandler handler = new ConsumeHandler(this);
	private final ArrayList<ConsumeModel> consumeGoods = new ArrayList<ConsumeModel>();
	private final HashMap<TableRow,ConsumeModel> mapping = new HashMap<TableRow,ConsumeModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		state = SAVE_STATE;
		Intent intent = getIntent();
		if(intent.getData()==null)
			intent.setData(Consume.CONTENT_URI);
		this.setContentView(R.layout.consume_insert);
		
//		IntentFilter filter = new IntentFilter("com.tobacco.action.scan");
//		this.registerReceiver(new ScanReceiver(), filter);
//		this.startService(new Intent(this,ScanInputService.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_INPUT_CONSUME, 0, "手动输入")
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
		case MENU_INPUT_CONSUME:
			Intent intent = new Intent("com.tobacco.pos.activity.ConsumeInsertDialog");
			this.startActivityForResult(intent, GET_CON);
			return true;
		case MENU_CANCEL:
			if(state == UNSAVE_STATE){
				new AlertDialog.Builder(ConsumeInsert.this)
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
			save();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void save(){
		for(ConsumeModel goods : consumeGoods){
			goods.setOperator(currentUserBO.getUserName());
			handler.insert(goods);
		}
		
		Toast.makeText(ConsumeInsert.this, "保存成功", Toast.LENGTH_SHORT).show();
		state = SAVE_STATE;
		TableLayout table = (TableLayout)findViewById(R.id.consumeInsertTable);
		table.removeViews(1, table.getChildCount()-1);
		onResume();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == GET_CON)
		{
			if(resultCode == RESULT_OK){
				String type = data.getExtras().getString(Consume.FLAG);
				String barcode = data.getExtras().getString(GoodsPrice.barcode);
				int count = Integer.valueOf(data.getExtras().getString(Consume.NUMBER));
				String comment = data.getExtras().getString(Consume.COMMENT);
				
				ConsumeModel goods = handler.fillVacancy(barcode, count, type, comment);
				
				addConsumeGoods(goods);
				state = UNSAVE_STATE;
			}
		}
	}
	
	private void addConsumeGoods(ConsumeModel goods){
		boolean exits = false;
		for(TableRow row : mapping.keySet()){
			ConsumeModel preGoods = mapping.get(row);
			if(preGoods.getGoodsPriceId()==goods.getGoodsPriceId())
			{
				exits = true;
				preGoods.setNumber(preGoods.getNumber()+goods.getNumber());
				((TextView)row.getChildAt(3)).setText(""+preGoods.getNumber());
			}
		}
		if(!exits)
			showConsumeGoods(goods);
	}
	
	protected void showConsumeGoods(ConsumeModel goods){
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);  
		final TableRow row = (TableRow)inflater.inflate(R.layout.table_row_six,null);  
		
		TextView name = (TextView)row.findViewById(R.id.text_six1);	
		TextView type = (TextView)row.findViewById(R.id.text_six2);	
		TextView unit = (TextView)row.findViewById(R.id.text_six3);
		final TextView number = (TextView)row.findViewById(R.id.text_six4);
		TextView price = (TextView)row.findViewById(R.id.text_six5);
		TextView total = (TextView)row.findViewById(R.id.text_six6);

		name.setText(goods.getGoodsName());
		type.setText(goods.getType());
		unit.setText(goods.getUnitName());
		number.setText(""+goods.getNumber());
		price.setText(""+goods.getInPrice());
		total.setText(new String().valueOf(goods.getInPrice()*goods.getNumber()));
		
		final TableLayout table = (TableLayout)findViewById(R.id.consumeInsertTable);
//		final TableRow row = new TableRow(ConsumeInsert.this);
		mapping.put(row, goods);
		row.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				menuInfo = new AdapterContextMenuInfo(row, 0, 0);
				menu.setHeaderTitle("菜单项");
				menu.add(0, MENU_REMOVE, 0, "删除该记录");
				menu.add(0, MENU_MOFIFY_NUM, 1, "修改数量");			
				menu.findItem(MENU_REMOVE).setOnMenuItemClickListener(new OnMenuItemClickListener(){

					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						switch(item.getItemId()){
						case MENU_REMOVE:				
							consumeGoods.remove((ConsumeModel)mapping.get(row));
							mapping.remove(row);	
							table.removeView(row);
							return true;
						}
						return false;
					}				
				});
				menu.findItem(MENU_MOFIFY_NUM).setOnMenuItemClickListener(new OnMenuItemClickListener(){

					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub										
						final EditText numberText = new EditText(ConsumeInsert.this);
						numberText.setWidth(100);
						numberText.setSingleLine(true);
						
						new AlertDialog.Builder(ConsumeInsert.this).setTitle("修改数量").setView(numberText)
						.setPositiveButton("确定", new OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								if(!RegexCheck.checkInteger(numberText.getText().toString())){
									Toast.makeText(ConsumeInsert.this, "数量输入无效", Toast.LENGTH_SHORT).show();
								}else{
									ConsumeModel model = (ConsumeModel)mapping.get(row);
									model.setNumber(Integer.valueOf(numberText.getText().toString()).intValue());
									number.setText(numberText.getText().toString());
								}				
							}			
						}).setNegativeButton("取消", new OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}			
						}).show();
						return true;
					}				
				});
			}
			
		});

		table.addView(row);		
		consumeGoods.add(goods);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler = null;
		consumeGoods.clear();
	}

	public class ScanReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
//			String barcode = intent.getStringExtra("BARCODE");
//			new AlertDialog.Builder(ConsumeInsert.this).setMessage(barcode).show();		
			Intent i = new Intent("com.tobacco.pos.activity.ConsumeInsertDialog");
			i.putExtra(GoodsPrice.barcode, intent.getStringExtra("BARCODE"));
			startActivityForResult(i, GET_CON);
		}
		
	}
}
