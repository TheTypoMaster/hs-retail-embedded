package com.tobacco.pos.handler;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.tobacco.pos.contentProvider.GoodsCPer;
import com.tobacco.pos.contentProvider.GoodsPriceCPer;
import com.tobacco.pos.contentProvider.UnitCPer;
import com.tobacco.pos.contentProvider.VIPInfoCPer;
import com.tobacco.pos.entity.ReturnFull;
import com.tobacco.pos.entity.ReturnModel;
import com.tobacco.pos.entity.AllTables.Goods;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.Return;
import com.tobacco.pos.entity.AllTables.Unit;
import com.tobacco.pos.entity.AllTables.UserInfo;
import com.tobacco.pos.entity.AllTables.VIPInfo;
import com.tobacco.pos.searchStrategy.ISearchStrategy;
import com.tobacco.pos.searchStrategy.SearchState;
import com.tobacco.pos.searchStrategy.SearchStrategyFactory;
import com.tobacco.pos.util.DateTool;

public class ReturnHandler {
	
	/**
	 * record the context which invoke this class.
	 */
	private Context ctx;
	
	/**
	 * record all the consume goods. 
	 */
	private ArrayList<ReturnModel> returnGoods;
	
	/**
	 * construct method
	 * @param ctx
	 */
	public ReturnHandler(Context ctx){
		returnGoods = new ArrayList<ReturnModel>();
		this.ctx = ctx;
	}
	
	/**
	 * insert this consume goods.
	 * @param goods
	 * @return the record uri in database.
	 */
	public Uri insert(ReturnModel goods){
		Uri uri = ctx.getContentResolver().insert(Return.CONTENT_URI, goods.genContentValues());
		return uri;
	}
	
	public boolean delete(int returnId){
		return true;
	}

	public boolean update(ReturnModel goods){
		return true;
	}
	
	public ReturnModel get(int returnId){
		return null;
	}
	
	/**
	 * get the consume goods record with selection.
	 * @param selections the select factor
	 * @return the list of the ConsumeEntity objects.
	 */
	public int search(SearchState state){
		returnGoods.clear();
		SearchStrategyFactory factory = SearchStrategyFactory.getInstance();
		ISearchStrategy strategy = factory.getSearchStrategy(ctx, ReturnFull.CONTENT_URI, ReturnFull.PROJECTION, state);
		Cursor cursor = strategy.searchc();
		Log.i("TestStrategy", "record count:"+cursor.getCount());
		
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i = 0;i<cursor.getCount();i++){
				returnGoods.add(genReturnEntity(cursor));
				cursor.moveToNext();
			}	
			cursor.close();
		}
		Log.e("ReturnHandler", ""+returnGoods.size());
		return returnGoods.size();
	}
	
	/**
	 * get the data from cursor and form a ConsumeEntity object
	 * @param cursor
	 * @return return the ConsumeEntity obeject.
	 */
	private ReturnModel genReturnEntity(Cursor cursor){
		
		int goodsNameIndex = cursor.getColumnIndex(Goods.goodsName);
		String goodsName = cursor.getString(goodsNameIndex);
//		int unitNameIndex = cursor.getColumnIndex(Unit.name);
//		String unitName = cursor.getString(unitNameIndex);
		int vipNameIndex = cursor.getColumnIndex(VIPInfo.VIPName);
		String customer = cursor.getString(vipNameIndex);
		
		int operNameIndex = cursor.getColumnIndex(Return.OPERATOR);
		String operator = cursor.getString(operNameIndex);
		
		int timeIndex = cursor.getColumnIndex(Return.CREATE_DATE);
		String time = cursor.getString(timeIndex);
//		Date createDate = DateTool.formatStringToDate(time);
		
		int contentIndex = cursor.getColumnIndex(Return.COMMENT);
		String content = cursor.getString(contentIndex);
		
		int numberIndex = cursor.getColumnIndex(Return.NUMBER);
		int number = cursor.getInt(numberIndex);	
		
		int goodsPriceIdIndex = cursor.getColumnIndex(GoodsPrice._ID);
		int goodsPriceId = cursor.getInt(goodsPriceIdIndex);
		
		GoodsPriceCPer goodsPriceCPer = new GoodsPriceCPer();
		Double inPrice = Double.valueOf(goodsPriceCPer.getAttributeById(GoodsPrice.inPrice, String.valueOf(goodsPriceId)));
		
		ReturnModel goods = new ReturnModel(operator, customer, goodsPriceId, goodsName, time, content, number,inPrice);
		return goods;
	}
	
	/**
	 * return a page of ConsumeEntity objects.
	 * @param start
	 * @param limit
	 * @return objects list.
	 */
	public ArrayList<ReturnModel> getPage(int start, int limit){
		ArrayList<ReturnModel> result = new ArrayList<ReturnModel>();
		if(returnGoods.size()>start){
			int end = (returnGoods.size()>start+limit)?start+limit:returnGoods.size();
			for(int i= start ;i<end;i++){
				result.add(returnGoods.get(i));
			}
		}
		return result;
	}
	
	/**
	 * fill the blank field of this ConsumeEntity object
	 * @param barcode
	 * @param count
	 * @param comment
	 * @return the consumeEntity filled.
	 */
//	public ReturnModel fillVacancy(String vipNum, String barcode, int number, String content){
	public ReturnModel fillVacancy(String barcode, int number, String content){
		GoodsPriceCPer goodsPriceCPer = new GoodsPriceCPer();
		GoodsCPer goodsCPer = new GoodsCPer();
		UnitCPer unitCPer = new UnitCPer();
//		VIPInfoCPer vipInfoCPer = new VIPInfoCPer();
//
//		if(vipNum.equals("")){
//			vipNum = "common";
//		}
//
//		String customer = vipInfoCPer.getAttributeByAttribute(VIPInfo.VIPName, VIPInfo.VIPNum, vipNum);
//		int customerId = Integer.valueOf(vipInfoCPer.getAttributeByAttribute(VIPInfo._ID,VIPInfo.VIPNum,vipNum)).intValue();

		int goodsPriceId = Integer.valueOf(goodsPriceCPer.getAttributeByAttribute(GoodsPrice._ID, GoodsPrice.barcode, barcode)).intValue();
		
		String goodsId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice.goodsId, GoodsPrice.barcode, barcode);
		String goodsName = goodsCPer.getAttributeById(Goods.goodsName, goodsId);	
		
		String unitId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice.unitId, GoodsPrice.barcode, barcode);
		String unit = unitCPer.getAttributeById(Unit.name, unitId);
//		Double inPrice = Double.valueOf(goodsPriceCPer.getAttributeByAttribute(GoodsPrice.inPrice, GoodsPrice.barcode, barcode));
//		ReturnModel goods = new ReturnModel(customerId, customer, unit, goodsPriceId, goodsName, content, number);
		ReturnModel goods = new ReturnModel(unit, goodsPriceId, goodsName, content, number);
		
		return goods;
	}
	
	public int getVipId(String vipNum){
		VIPInfoCPer vipInfoCPer = new VIPInfoCPer();
		
		if(vipNum.equals("")){
			vipNum = "common";
		}
		int customerId = Integer.valueOf(vipInfoCPer.getAttributeByAttribute(VIPInfo._ID,VIPInfo.VIPNum,vipNum)).intValue();
		return customerId;
	}
}
