package com.tobacco.pos.handler;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.tobacco.pos.Module.InventoryItemFull;
import com.tobacco.pos.Module.InventoryItemModel;
import com.tobacco.pos.Module.AllTables.Goods;
import com.tobacco.pos.Module.AllTables.GoodsPrice;
import com.tobacco.pos.Module.AllTables.InventoryItem;
import com.tobacco.pos.Module.AllTables.Unit;
import com.tobacco.pos.contentProvider.GoodsCPer;
import com.tobacco.pos.contentProvider.GoodsPriceCPer;
import com.tobacco.pos.contentProvider.UnitCPer;
import com.tobacco.pos.searchStrategy.ISearchStrategy;
import com.tobacco.pos.searchStrategy.SearchState;
import com.tobacco.pos.searchStrategy.SearchStrategyFactory;

public class InventoryItemHandler {
	/**
	 * record the context which invoke this class.
	 */
	private Context ctx;
	
	/**
	 * record all the consume goods. 
	 */
	private ArrayList<InventoryItemModel> inventoryItemGoods;
	
	/**
	 * construct method
	 * @param ctx
	 */
	public InventoryItemHandler(Context ctx){
		inventoryItemGoods = new ArrayList<InventoryItemModel>();
		this.ctx = ctx;
	}
	
	/**
	 * insert this consume goods.
	 * @param goods
	 * @return the record uri in database.
	 */
	public Uri insert(InventoryItemModel item){
		Uri uri = ctx.getContentResolver().insert(InventoryItem.CONTENT_URI, item.genContentValues());
		return uri;
	}
	
	public void insert(ArrayList<InventoryItemModel> itemList){
		for(InventoryItemModel item : itemList)
			insert(item);
	}
	
	public boolean delete(int inventoryItemId){
		ctx.getContentResolver().delete(InventoryItem.CONTENT_URI, InventoryItem._ID+" = ? ", new String[]{""+inventoryItemId});
		return true;
	}

	public boolean update(InventoryItemModel goods){
		return true;
	}
	
	public InventoryItemModel get(int inventoryItemId){
		return null;
	}
	
	/**
	 * get the consume goods record with selection.
	 * @param selections the select factor
	 * @return the list of the ConsumeEntity objects.
	 */
	public int search(SearchState state){
		inventoryItemGoods.clear();
		SearchStrategyFactory factory = SearchStrategyFactory.getInstance();
		ISearchStrategy strategy = factory.getSearchStrategy(ctx, InventoryItemFull.CONTENT_URI, InventoryItemFull.PROJECTION, state);
		Cursor cursor = strategy.searchc();
		Log.i("TestStrategy", "record count:"+cursor.getCount());
		
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i = 0;i<cursor.getCount();i++){
				inventoryItemGoods.add(genInventoryItemModel(cursor));
				cursor.moveToNext();
			}	
			cursor.close();
		}
		Log.e("InventoryItemHandler", ""+inventoryItemGoods.size());
		return inventoryItemGoods.size();
	}
	
	/**
	 * get the data from cursor and form a ConsumeEntity object
	 * @param cursor
	 * @return return the ConsumeEntity obeject.
	 */
	private InventoryItemModel genInventoryItemModel(Cursor cursor){
		
//		int itemIdIndex = cursor.getColumnIndex(InventoryItemFull.IItem_ID);
//		int itemId = cursor.getInt(itemIdIndex);
		int iBillIdIndex = cursor.getColumnIndex(InventoryItem.IBILL_ID);
		int ibillId = cursor.getInt(iBillIdIndex);
		int goodsNameIndex = cursor.getColumnIndex(Goods.goodsName);
		String goodsName = cursor.getString(goodsNameIndex);
		int unitNameIndex = cursor.getColumnIndex(Unit.name);
		String unitName = cursor.getString(unitNameIndex);
		int goodsPriceIndex = cursor.getColumnIndex(GoodsPrice.inPrice);
		double goodsPrice = cursor.getDouble(goodsPriceIndex);
		int expectNumIndex = cursor.getColumnIndex(InventoryItem.EXPECT_NUM);
		int expectNum = cursor.getInt(expectNumIndex);
		int realNumIndex = cursor.getColumnIndex(InventoryItem.REAL_NUM);
		int realNum = cursor.getInt(realNumIndex);
		int itemResultIndex = cursor.getColumnIndex(InventoryItem.ITEM_RESULT);
		double itemResult = cursor.getDouble(itemResultIndex);
		int goodsPriceIdIndex = cursor.getColumnIndex(GoodsPrice._ID);
		int goodsPriceId = cursor.getInt(goodsPriceIdIndex);
		
		InventoryItemModel goods = new InventoryItemModel(goodsName, goodsPriceId, goodsPrice, unitName, expectNum, realNum, itemResult, ibillId);
		
		return goods;
	}
	
	/**
	 * return a page of ConsumeEntity objects.
	 * @param start
	 * @param limit
	 * @return objects list.
	 */
	public ArrayList<InventoryItemModel> getPage(int start, int limit){
		ArrayList<InventoryItemModel> result = new ArrayList<InventoryItemModel>();
		if(inventoryItemGoods.size()>start){
			int end = (inventoryItemGoods.size()>start+limit)?start+limit:inventoryItemGoods.size();
			for(int i= start ;i<end;i++){
				result.add(inventoryItemGoods.get(i));
			}
		}
		return result;
	}
	
	public ArrayList<InventoryItemModel> getItemsByBillId(int billId){
		ArrayList<InventoryItemModel> itemLists = new ArrayList<InventoryItemModel>();
		Cursor cursor = ctx.getContentResolver().query(InventoryItemFull.CONTENT_URI, InventoryItemFull.PROJECTION,
				InventoryItemFull.IBILL_ID+" =?", new String[]{""+billId}, null);
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i = 0;i<cursor.getCount();i++){
				itemLists.add(genInventoryItemModel(cursor));
				cursor.moveToNext();
			}	
			cursor.close();
		}
		return itemLists;
	}
	
//	public ArrayList<InventoryItemModel> getItemsByUfBillId(int billId){
//		ArrayList<InventoryItemModel> unfinished = getItemsByBillId(billId);
//		ctx.getContentResolver().delete(InventoryItem.CONTENT_URI, InventoryItem.IBILL_ID+" = ? ", new String[]{""+billId});
//		return unfinished;
//	}
	
	public void removeUfItems(int billId){
		ctx.getContentResolver().delete(InventoryItem.CONTENT_URI, InventoryItem.IBILL_ID+" = ? ", new String[]{""+billId});
	}
//	/**
//	 * fill the blank field of this ConsumeEntity object
//	 * @param barcode
//	 * @param count
//	 * @param comment
//	 * @return the consumeEntity filled.
//	 */
//	public InventoryItemModel fillVacancy(String barcode){
//		
//		GoodsPriceCPer goodsPriceCPer = new GoodsPriceCPer();
//		GoodsCPer goodsCPer = new GoodsCPer();
//		UnitCPer unitCPer = new UnitCPer();
//
//		int goodsPriceId = Integer.valueOf(goodsPriceCPer.getAttributeByAttribute(GoodsPrice._ID, GoodsPrice.barcode, barcode)).intValue();
//		
//		String goodsId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice.goodsId, GoodsPrice.barcode, barcode);
//		String goodsName = goodsCPer.getAttributeById(Goods.goodsName, goodsId);
//		
//		String unitId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice.unitId, GoodsPrice.barcode, barcode);
//		String unitName = unitCPer.getAttributeById(Unit.name, unitId);
//		
//		double goodsPrice = Double.valueOf(goodsPriceCPer.getAttributeByAttribute(GoodsPrice.inPrice, GoodsPrice.barcode, barcode));
//		
//		int expectNum = 100;
//		
//		InventoryItemModel consume = new InventoryItemModel(goodsName, goodsPriceId, goodsPrice, unitName, expectNum);
//		
//		return consume;
//	}
	
	public ArrayList<InventoryItemModel> fillVacancy(ArrayList<Integer> goodsPriceIdList){
		
		GoodsPriceCPer goodsPriceCPer = new GoodsPriceCPer();
		GoodsCPer goodsCPer = new GoodsCPer();
		UnitCPer unitCPer = new UnitCPer();

		ArrayList<InventoryItemModel> itemModels = new ArrayList<InventoryItemModel>();
		
		for(Integer priceId: goodsPriceIdList){
			
			int goodsPriceId = priceId.intValue();
			
			String goodsId = goodsPriceCPer.getAttributeById(GoodsPrice.goodsId, String.valueOf(goodsPriceId));
			String goodsName = goodsCPer.getAttributeById(Goods.goodsName, goodsId);
			
			String unitId = goodsPriceCPer.getAttributeById(GoodsPrice.unitId, String.valueOf(goodsPriceId));
			String unitName = unitCPer.getAttributeById(Unit.name, unitId);
			
			double goodsPrice = Double.valueOf(goodsPriceCPer.getAttributeById(GoodsPrice.inPrice, String.valueOf(goodsPriceId)));
			
			int expectNum = 100;
			
			InventoryItemModel item = new InventoryItemModel(goodsName, goodsPriceId, goodsPrice, unitName, expectNum);
			itemModels.add(item);
		}
		
		return itemModels;
	}
}
