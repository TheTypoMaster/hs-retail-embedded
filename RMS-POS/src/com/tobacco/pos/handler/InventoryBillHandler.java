package com.tobacco.pos.handler;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.tobacco.pos.entity.InventoryBillFull;
import com.tobacco.pos.entity.InventoryBillModel;
import com.tobacco.pos.entity.AllTables.InventoryBill;
import com.tobacco.pos.searchStrategy.ISearchStrategy;
import com.tobacco.pos.searchStrategy.SearchState;
import com.tobacco.pos.searchStrategy.SearchStrategyFactory;
import com.tobacco.pos.util.DateTool;

public class InventoryBillHandler {

	public static final int STATE_NORMAL = 0;
	public static final int STATE_PAUSE = 1; 
	public static final int STATE_CONTINUE = 2; 
	
	private int state;
	
	InventoryItemHandler itemHandler;
	/**
	 * record the context which invoke this class.
	 */
	private Context ctx;
	/**
	 * record all the consume goods. 
	 */
	private ArrayList<InventoryBillModel> inventoryBillGoods;
	
	public InventoryBillHandler(Context ctx) {
		super();
		inventoryBillGoods = new ArrayList<InventoryBillModel>();
		itemHandler = new InventoryItemHandler(ctx);
		this.ctx = ctx;
		state = STATE_NORMAL; 
	}
	
	public int getState() {
		Log.i("state", "handler.getstate:"+state);
		return state;
	}

	public int checkState(){
		Cursor c = ctx.getContentResolver().query(InventoryBill.CONTENT_URI, new String[]{InventoryBill._ID}, InventoryBill.FINISHED+" =0", null, null);	
		if(c.getCount()>0)
			state = STATE_PAUSE;
		else
			state = STATE_NORMAL;	
		return state;
	}
	
	/**
	 * insert this consume goods.
	 * @param goods
	 * @return the record uri in database.
	 */
	public Uri insert(InventoryBillModel bill){
		Uri uri = ctx.getContentResolver().insert(InventoryBill.CONTENT_URI, bill.genContentValues());
		return uri;
	}
	
	public int save(InventoryBillModel bill){
		int id = 0;
		switch(state){
		case STATE_NORMAL:
			setBillNum(bill);
			Uri uri = insert(bill);
			id = Integer.valueOf(uri.getPathSegments().get(1)).intValue();
			break;
		case STATE_CONTINUE:
			ContentValues values = new ContentValues();
			values.put(InventoryBill.FINISHED, bill.isFinished()?1:0);
			values.put(InventoryBill.RESULT, bill.getResult());
			
//			Cursor c = ctx.getContentResolver().query(InventoryBill.CONTENT_URI, new String[]{InventoryBill._ID}, 
//					InventoryBill.FINISHED+" =?",new String[]{"0"}, null);
//			if(c.getCount()>0){
//				c.moveToFirst();
//				id = c.getInt(0);
//				Log.i("billId", ""+id);
//			}
			id = getUnfinishedBillId();
			itemHandler.removeUfItems(id);
			ctx.getContentResolver().update(InventoryBill.CONTENT_URI, values, 
					InventoryBill.FINISHED +" = ? ", new String[]{"0"});
							
			break;
		}
		state = bill.isFinished()?STATE_NORMAL:STATE_PAUSE;
		return id;
	}
	
	private void setBillNum(InventoryBillModel bill){
		int lastId = 0;
		Cursor c = ctx.getContentResolver().query(InventoryBill.CONTENT_URI, new String[]{InventoryBill._ID}, 
				null,null, null);
		if(c.getCount()>0){
			c.moveToLast();
			lastId = c.getInt(0);
		}
		bill.setiBillNum("ib"+lastId+1);
	}
	
	public boolean delete(int consumeId){
		return true;
	}

	public boolean update(InventoryBillModel goods){
		return true;
	}
	
	public InventoryBillModel get(int inventoryBillId){
		return null;
	}
	
	/**
	 * get the consume goods record with selection.
	 * @param selections the select factor
	 * @return the list of the ConsumeEntity objects.
	 */
	public int search(SearchState state){
		inventoryBillGoods.clear();
		SearchStrategyFactory factory = SearchStrategyFactory.getInstance();
//		ISearchStrategy strategy = factory.getSearchStrategy(ctx, InventoryBillFull.CONTENT_URI, InventoryBillFull.PROJECTION, state);
		ISearchStrategy strategy = factory.getSearchStrategy(ctx, InventoryBill.CONTENT_URI, InventoryBillFull.PROJECTION, state);
		Cursor cursor = strategy.searchc();
		Log.i("TestStrategy", "record count:"+cursor.getCount());
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i = 0;i<cursor.getCount();i++){
				inventoryBillGoods.add(genInventoryBillEntity(cursor));
				cursor.moveToNext();
			}	
			cursor.close();
		}
		Log.e("InventoryBillHandler", ""+inventoryBillGoods.size());
		return inventoryBillGoods.size();
	}
	
	/**
	 * get the data from cursor and form a ConsumeEntity object
	 * @param cursor
	 * @return return the ConsumeEntity obeject.
	 */
	private InventoryBillModel genInventoryBillEntity(Cursor cursor){
		
		int inventoryIdIndex = cursor.getColumnIndex(InventoryBillFull._ID);
		int iBillId = cursor.getInt(inventoryIdIndex);
		
		int inventoryNumIndex = cursor.getColumnIndex(InventoryBillFull.IBILL_NUM);
		String iBillNum = cursor.getString(inventoryNumIndex);
		
		int inventoryFlagIndex = cursor.getColumnIndex(InventoryBillFull.FINISHED);
		boolean finished = (cursor.getString(inventoryFlagIndex).equals("1"))?true:false;
		
		int inventoryResultIndex = cursor.getColumnIndex(InventoryBillFull.RESULT);
		double result = cursor.getDouble(inventoryResultIndex);
		
		int inventoryTimeIndex = cursor.getColumnIndex(InventoryBillFull.CREATE_DATE);
		String inventoryTime = cursor.getString(inventoryTimeIndex);
//		Date createDate = DateTool.formatStringToDate(inventoryTime);
		
		int operatorIndex = cursor.getColumnIndex(InventoryBillFull.OPER_NAME);
		String operName = cursor.getString(operatorIndex);
		
		InventoryBillModel goods = new InventoryBillModel(operName, iBillId, iBillNum, finished, result, inventoryTime);
		
		return goods;
	}
	
	/**
	 * return a page of ConsumeEntity objects.
	 * @param start
	 * @param limit
	 * @return objects list.
	 */
	public ArrayList<InventoryBillModel> getPage(int start, int limit){
		ArrayList<InventoryBillModel> result = new ArrayList<InventoryBillModel>();
		if(inventoryBillGoods.size()>start){
			int end = (inventoryBillGoods.size()>start+limit)?start+limit:inventoryBillGoods.size();
			for(int i= start ;i<end;i++){
				result.add(inventoryBillGoods.get(i));
			}
		}
		return result;
	}
	
	public int getUnfinishedBillId(){
		int billId = 0;
		Cursor c = ctx.getContentResolver().query(InventoryBill.CONTENT_URI, new String[]{InventoryBill._ID}, InventoryBill.FINISHED+" =?", 
				new String[]{"0"}, null);
		if(c.getCount()>0){
			c.moveToFirst();
			billId = c.getInt(0);
		}
		state = STATE_CONTINUE;
		Log.i("state", ""+state);
		return billId;
	}
	
	public void clearInventory(){
		Log.e("state", "clearInventory:state:"+state);
//		if(state == STATE_CONTINUE){
//			ctx.getContentResolver().delete(InventoryBill.CONTENT_URI, InventoryBill.FINISHED+" = ?", new String[]{"0"});			
//		}else if(state == STATE_PAUSE){
//			int billId = getUnfinishedBillId();		
//			itemHandler.removeUfItems(billId);
//			ctx.getContentResolver().delete(InventoryBill.CONTENT_URI, InventoryBill.FINISHED+" = ?", new String[]{"0"});
//		}
		
		if(state != STATE_NORMAL){
			int billId = getUnfinishedBillId();		
			itemHandler.removeUfItems(billId);
			ctx.getContentResolver().delete(InventoryBill.CONTENT_URI, InventoryBill.FINISHED+" = ?", new String[]{"0"});
		}
			
		state = STATE_NORMAL;
	}
//	/**
//	 * fill the blank field of this ConsumeEntity object
//	 * @param barcode
//	 * @param count
//	 * @param comment
//	 * @return the consumeEntity filled.
//	 */
//	public InventoryBillModel fillVacancy(String barcode, int count,String comment){
//		
//		GoodsPriceCPer goodsPriceCPer = new GoodsPriceCPer();
//		GoodsCPer goodsCPer = new GoodsCPer();
//		UnitCPer unitCPer = new UnitCPer();
//
//		int goodsPriceId = Integer.valueOf(goodsPriceCPer.getAttributeByAttribute(GoodsPrice._ID, GoodsPrice.barcode, barcode)).intValue();
//		String goodsId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice.goodsId, GoodsPrice.barcode, barcode);
//		String goodsName = goodsCPer.getAttributeById(Goods.goodsName, goodsId);		
//		String unitId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice.unitId, GoodsPrice.barcode, barcode);
//		String unitName = unitCPer.getAttributeById(Unit.name, unitId);
//		Double inPrice = Double.valueOf(goodsPriceCPer.getAttributeByAttribute(GoodsPrice.inPrice, GoodsPrice.barcode, barcode));
//		
//		ConsumeModel consume = new ConsumeModel(count, goodsName,unitName, goodsPriceId, inPrice,comment);
//		return consume;
//	}
	
}
