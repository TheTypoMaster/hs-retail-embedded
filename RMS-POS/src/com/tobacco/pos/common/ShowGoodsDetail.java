package com.tobacco.pos.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tobacco.R;
import com.tobacco.pos.Module.AllTables.Goods;
import com.tobacco.pos.Module.AllTables.GoodsKind;
import com.tobacco.pos.Module.AllTables.GoodsPrice;
import com.tobacco.pos.Module.AllTables.Manufacturer;
import com.tobacco.pos.Module.AllTables.Unit;
import com.tobacco.pos.contentProvider.GoodsCPer;
import com.tobacco.pos.contentProvider.GoodsKindCPer;
import com.tobacco.pos.contentProvider.GoodsPriceCPer;
import com.tobacco.pos.contentProvider.ManufacturerCPer;
import com.tobacco.pos.contentProvider.UnitCPer;

public class ShowGoodsDetail extends Activity{

//	private static final int GET_DATA = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("商品详细信息");
		Intent intent = getIntent();
		setContentView(R.layout.show_goods_detail);
		String goodsPriceId = intent.getStringExtra(GoodsPrice._ID);
//		String count = intent.getStringExtra("COUNT");
		
		
		Button okButton = (Button)findViewById(R.id.okButtonShowGoodsDetail);
		okButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
//		getData(String.valueOf(consumeId));
		showConsumeGoods(goodsPriceId);
	}
	
	protected void showConsumeGoods(String goodsPriceId){

		GoodsPriceCPer goodsPriceCPer = new GoodsPriceCPer();
		GoodsCPer goodsCPer = new GoodsCPer();
		UnitCPer unitCPer = new UnitCPer();
		GoodsKindCPer goodsKindCPer =new GoodsKindCPer();
		ManufacturerCPer manufacturerCPer = new ManufacturerCPer();
		
		String goodsId = goodsPriceCPer.getAttributeById(GoodsPrice.goodsId, goodsPriceId);
		String goodsName = goodsCPer.getAttributeById(Goods.goodsName, goodsId);
		
		String unitId = goodsPriceCPer.getAttributeById(GoodsPrice.unitId, goodsPriceId);
		String unitName = unitCPer.getAttributeById(Unit.name, unitId);
		
		String inPrice = goodsPriceCPer.getAttributeById(GoodsPrice.inPrice, goodsPriceId);

		String kindsId = goodsCPer.getAttributeById(Goods.kindId, goodsId);
		String kindsName = goodsKindCPer.getAttributeById(GoodsKind.name, kindsId);
		
		String manufacturerId = goodsCPer.getAttributeById(Goods.manufacturerId, goodsId);
		String manufacturerName = manufacturerCPer.getAttributeById(Manufacturer.mName, manufacturerId);
				
//		TextView kindsNameText = (TextView)findViewById(R.id.goodsKindShowGoodsDetail);
		EditText kindsNameText = (EditText)findViewById(R.id.goodsKindShowGoodsDetail);
		EditText manfaNameText = (EditText)findViewById(R.id.manuNameShowGoodsDetail);
		EditText goodsNameText = (EditText)findViewById(R.id.goodsNameShowGoodsDetail);	
		EditText unitNameText = (EditText)findViewById(R.id.unitNameShowGoodsDetail);
		EditText inPriceText = (EditText)findViewById(R.id.goodsPriceShowGoodsDetail);
//		TextView numberText = (TextView)findViewById(R.id.goodsCountShowGoodsDetail);
//		TextView totalText = (TextView)findViewById(R.id.totalPriceShowGoodsDetail);
		
		kindsNameText.setText(kindsName);
		manfaNameText.setText(manufacturerName);
		goodsNameText.setText(goodsName);
		unitNameText.setText(unitName);
		inPriceText.setText(inPrice);
//		numberText.setText(count);
//		totalText.setText(new String().valueOf(Double.valueOf(inPrice)*Integer.valueOf(count)));
			
//		final TableLayout table = (TableLayout)findViewById(R.id.consumeInsertTable);		
//		final TableRow row = new TableRow(ConsumeSelect.this);
//		row.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
//			@Override
//			public void onCreateContextMenu(ContextMenu menu, View v,
//					ContextMenuInfo menuInfo) {
//				// TODO Auto-generated method stub
//				//onCreateContextMenu(menu, v, menuInfo);
//				menu.setHeaderTitle("Choice for item");
//				menu.add(0, MENU_SHOW_DETAIL, 0, "Goods_Detail");
//				menu.findItem(MENU_SHOW_DETAIL).setOnMenuItemClickListener(new OnMenuItemClickListener(){
//					@Override
//					public boolean onMenuItemClick(MenuItem item) {
//						// TODO Auto-generated method stub
//						switch(item.getItemId()){
//						case MENU_SHOW_DETAIL:		
//							Intent intent = new Intent("com.tobacco.pos.activity.ShowGoodsDetail");
//							intent.putExtra(GoodsPrice._ID, goodsPriceId);
//							intent.putExtra("COUNT",count);
//							ConsumeSelect.this.startActivity(intent);
//							return true;
//						}
//						return false;
//						//return onContextItemSelected(item);
//					}
//						
//				});
//			}
//				
//		});
//			
//		row.addView(name, 0);
//		row.addView(unit, 1);
////		row.addView(number, 2);
////		row.addView(price, 3);
////		row.addView(total, 4);
//		row.addView(time, 2);
//		row.addView(operator, 3);
//		row.addView(comment,4);
//		table.addView(row);
//		cache.add(row);
//		HashMap<String,Object> map = new HashMap<String, Object>();
//		map.put(Consume.GOODS, goodsPriceCPer.getGoodsPriceIdByBarcode(barcode));
//		map.put(Consume.NUMBER, count);
//		map.put(Consume.COMMENT, comment); 显示在详细说明中。
//		cache.add(map);
	}
//	protected void getData(String sourceId){
//		
//		String goodsName = QueryDataFromTables.THREE_TABLE+";"+"ConsumeCPer/consumes"+";"+Consume._ID+";"+sourceId+";"+Consume.GOODS+";"+GoodsPrice._ID+";"+"GoodsPriceCPer"+";"+GoodsPrice.goodsId+";"+Goods._ID+";"+"GoodsCPer"+";"+Goods.goodsName;
//		String unitName = QueryDataFromTables.THREE_TABLE+";"+"ConsumeCPer/consumes"+";"+Consume._ID+";"+sourceId+";"+Consume.GOODS+";"+GoodsPrice._ID+";"+"GoodsPriceCPer"+";"+GoodsPrice.unitId+";"+Unit._ID+";"+"UnitCPer"+";"+Unit.name;
//		String inPrice = QueryDataFromTables.TWO_TABLE+";"+"ConsumeCPer/consumes"+";"+Consume._ID+";"+sourceId+";"+Consume.GOODS+";"+GoodsPrice._ID+";"+"GoodsPriceCPer"+";"+GoodsPrice.inPrice;
//		String outPrice = QueryDataFromTables.TWO_TABLE+";"+"ConsumeCPer/consumes"+";"+Consume._ID+";"+sourceId+";"+Consume.GOODS+";"+GoodsPrice._ID+";"+"GoodsPriceCPer"+";"+GoodsPrice.outPrice;
////		String arg6 = QueryDataFromTables.THREE_TABLE+";"+"ConsumeCPer/consumes"+";"+Consume._ID+";"+sourceId+";"+Consume.GOODS+";"+GoodsPrice._ID+";"+"GoodsPriceCPer"+";"+GoodsPrice.goodsId+";"+Goods._ID+";"+"GoodsCPer"+";"+Goods.goodsName;
////		String arg7 = QueryDataFromTables.TWO_TABLE+";"+"ConsumeCPer/consumes"+";"+Consume._ID+";"+sourceId+";"+Consume.OPERATOR+";"+UserInfo._ID+";"+"UserInfoCPer"+";"+UserInfo.userName;		
//
//		Intent intent = new Intent(ShowGoodsDetail.this,QueryDataFromTables.class);
//		String[] args = new String[]{goodsName,unitName,inPrice,outPrice};
//		//String[] args = new String[]{arg1,arg2};
//		intent.putExtra("args", args);
//		//startActivity(intent);
//		startActivityForResult(intent, GET_DATA);
//	}
//	
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		switch(requestCode){
//		case GET_DATA:
//			if(resultCode == RESULT_OK){
//				ArrayList<String> list = data.getExtras().getStringArrayList("data");
//				String[] targetValue = new String[list.size()];
//				targetValue = list.toArray(targetValue);
//				
//				EditText goodsNameText = (EditText)findViewById(R.id.goodsNameShowGoodsDetail);
//				EditText unitNameText = (EditText)findViewById(R.id.unitNameShowGoodsDetail);
//				EditText priceText = (EditText)findViewById(R.id.goodsPriceShowGoodsDetail);
//				
//				goodsNameText.setText(targetValue[0]);
//				unitNameText.setText(targetValue[1]);
//				priceText.setText("入价:"+targetValue[2]+"出价:"+targetValue[3]);
//
//			}
//			
//		}
//	}
}
