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
import com.tobacco.pos.entity.ConsumeFull;
import com.tobacco.pos.entity.ConsumeModel;
import com.tobacco.pos.entity.InventoryItemModel;
import com.tobacco.pos.entity.AllTables.Consume;
import com.tobacco.pos.entity.AllTables.Goods;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.Unit;
import com.tobacco.pos.entity.AllTables.UserInfo;
import com.tobacco.pos.searchStrategy.ISearchStrategy;
import com.tobacco.pos.searchStrategy.SearchState;
import com.tobacco.pos.searchStrategy.SearchStrategyFactory;
import com.tobacco.pos.util.DateTool;

public class ConsumeHandler {

	/**
	 * record the context which invoke this class.
	 */
	private Context ctx;
	
	/**
	 * record all the consume goods. 
	 */
	private ArrayList<ConsumeModel> consumeGoods;
	
	/**
	 * construct method
	 * @param ctx
	 */
	public ConsumeHandler(Context ctx){
		consumeGoods = new ArrayList<ConsumeModel>();
		this.ctx = ctx;
	}
	
	/**
	 * insert this consume goods.
	 * @param goods
	 * @return the record uri in database.
	 */
	public Uri insert(ConsumeModel goods){
		Uri uri = ctx.getContentResolver().insert(Consume.CONTENT_URI, goods.genContentValues());
		return uri;
	}
	
	public Uri insertByInventory(ArrayList<InventoryItemModel> items){
		for(InventoryItemModel item: items){
			int number = (item.getRealNum()>item.getExpectNum())?item.getRealNum()-item.getExpectNum():item.getExpectNum()-item.getRealNum();
			String type = (item.getRealNum()>item.getExpectNum())?"溢":"耗";
			String comment = (item.getRealNum()>item.getExpectNum())?" 盘盈":"盘亏";
			ConsumeModel model = new ConsumeModel(number, item.getGoodsPriceId(), comment, type);
			insert(model);
		}
		return null;
	}
	
	public boolean delete(int consumeId){
		return true;
	}

	public boolean update(ConsumeModel goods){
		return true;
	}
	
	public ConsumeModel get(int consumeId){
		return null;
	}
	
	/**
	 * get the consume goods record with selection.
	 * @param selections the select factor
	 * @return the list of the ConsumeEntity objects.
	 */
	public int search(SearchState state){
		consumeGoods.clear();
		SearchStrategyFactory factory = SearchStrategyFactory.getInstance();
		ISearchStrategy strategy = factory.getSearchStrategy(ctx, ConsumeFull.CONTENT_URI, ConsumeFull.PROJECTION, state);
		Cursor cursor = strategy.searchc();
		Log.i("TestStrategy", "record count:"+cursor.getCount());
// 		return null;
//		Cursor cursor = ctx.getContentResolver().query(QUERY_CONTENT_URI, PROJECTION, null, null, null);
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i = 0;i<cursor.getCount();i++){
				consumeGoods.add(genConsumeEntity(cursor));
				cursor.moveToNext();
			}	
			cursor.close();
		}
		Log.e("ConsumeHandler", ""+consumeGoods.size());
		return consumeGoods.size();
	}
	
	/**
	 * get the data from cursor and form a ConsumeEntity object
	 * @param cursor
	 * @return return the ConsumeEntity obeject.
	 */
	private ConsumeModel genConsumeEntity(Cursor cursor){
		
		int goodsNameIndex = cursor.getColumnIndex(Goods.goodsName);
		String goodsName = cursor.getString(goodsNameIndex);
		int unitNameIndex = cursor.getColumnIndex(Unit.name);
		String unitName = cursor.getString(unitNameIndex);
		int operNameIndex = cursor.getColumnIndex(Consume.OPERATOR);
		String operName = cursor.getString(operNameIndex);
		int timeIndex = cursor.getColumnIndex(Consume.CREATED_DATE);
		String time = cursor.getString(timeIndex);
		Date createDate = DateTool.formatStringToDate(time);
		int commentIndex = cursor.getColumnIndex(Consume.COMMENT);
		String comment = cursor.getString(commentIndex);
		int typeIndex = cursor.getColumnIndex(Consume.FLAG);
		String type = (cursor.getInt(typeIndex)==1)?"溢":"耗";
		
		int numberIndex = cursor.getColumnIndex(Consume.NUMBER);
		int number = cursor.getInt(numberIndex);		
		int goodsPriceIdIndex = cursor.getColumnIndex(GoodsPrice._ID);
		int goodsPriceId = cursor.getInt(goodsPriceIdIndex);
		
		GoodsPriceCPer goodsPriceCPer = new GoodsPriceCPer();
		Double inPrice = Double.valueOf(goodsPriceCPer.getAttributeById(GoodsPrice.inPrice, String.valueOf(goodsPriceId)));
		
		ConsumeModel goods = new ConsumeModel(operName, number, goodsName, unitName, goodsPriceId, inPrice, comment, createDate, type);
		return goods;
	}
	
	/**
	 * return a page of ConsumeEntity objects.
	 * @param start
	 * @param limit
	 * @return objects list.
	 */
	public ArrayList<ConsumeModel> getPage(int start, int limit){
		ArrayList<ConsumeModel> result = new ArrayList<ConsumeModel>();
		if(consumeGoods.size()>start){
			int end = (consumeGoods.size()>start+limit)?start+limit:consumeGoods.size();
			for(int i= start ;i<end;i++){
				result.add(consumeGoods.get(i));
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
	public ConsumeModel fillVacancy(String barcode, int count, String type, String comment){
		
		GoodsPriceCPer goodsPriceCPer = new GoodsPriceCPer();
		GoodsCPer goodsCPer = new GoodsCPer();
		UnitCPer unitCPer = new UnitCPer();

		int goodsPriceId = Integer.valueOf(goodsPriceCPer.getAttributeByAttribute(GoodsPrice._ID, GoodsPrice.barcode, barcode)).intValue();
		String goodsId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice.goodsId, GoodsPrice.barcode, barcode);
		String goodsName = goodsCPer.getAttributeById(Goods.goodsName, goodsId);		
		String unitId = goodsPriceCPer.getAttributeByAttribute(GoodsPrice.unitId, GoodsPrice.barcode, barcode);
		String unitName = unitCPer.getAttributeById(Unit.name, unitId);
		Double inPrice = Double.valueOf(goodsPriceCPer.getAttributeByAttribute(GoodsPrice.inPrice, GoodsPrice.barcode, barcode));
		
		ConsumeModel consume = new ConsumeModel(count, goodsName,unitName, goodsPriceId, inPrice,comment,type);
		return consume;
	}
	
	
//	/**
//	 * send to the contentProvider, and contentProvider use this uri to select the operation.
//	 */
//	private final Uri QUERY_CONTENT_URI = Uri.parse("content://" + Consume.AUTHORITY + "/consumes_full");
//
//	/**
//	 * The id of the operator
//	 * <p>TypE: INTEGER</>
//	 */
//	private final String OPER_NAME = "UserInfo."+UserInfo.userName;
//	/**
//	 * The name of the Unit 
//	 * <p>TypE: TEXT</>
//	 */
//	private final String UNIT_NAME = "Unit."+Unit.name;
//	/**
//	 * The id of the goodsPrice
//	 * <p>TypE: INTEGER</>
//	 */
//	private final String GOODS_PRICE_ID = "GoodsPrice."+GoodsPrice._ID;
//	/**
//	 * The id of the goods
//	 * <p>TypE: INTEGER</>
//	 */
//	private final String GOODS_NAME = "Goods."+Goods.goodsName;
//	/**
//	 * The time complaint create;
//	 * <p>TypE: TEXT</>
//	 */
//	private final String CREATE_DATE = "Consume."+Consume.CREATED_DATE;
//	/**
//	 * The reason for complaint
//	 * <p>TypE: TEXT</>
//	 */
//	private final String COMMENT = "Consume."+Consume.COMMENT;
//	/**
//	 * The count for Consume
//	 * <p>TypE: TEXT</>
//	 */
//	private final String NUMBER = "Consume."+Consume.NUMBER;
//	/**
//	 * The default sort order for this table
//	 */
//	private final String DEFAULT_SORT_ORDER = "modified DESC";
//	
//	/**
//	 * in query operation, need to to join this tables.
//	 */
//	private final String TABLES = "Consume,UserInfo,Unit,Goods,GoodsPrice";
//	
//	/**
//	 * join operation factors
//	 */
//	private final String append1 = "Consume."+Consume.GOODS+" = GoodsPrice." + GoodsPrice._ID;
//	private final String append2 = "GoodsPrice."+GoodsPrice.goodsId+" =Goods."+Goods._ID;
//	private final String append3 = "GoodsPrice."+GoodsPrice.unitId+" =Unit."+Unit._ID;
//	private final String append4 = "Consume."+Consume.OPERATOR+" =UserInfo."+UserInfo._ID;
//	private final String APPEND_WHERE = append1+" AND "+append2+" AND "+append3+" AND "+append4;
//
//	/**
//	 * the projection column in query operation.
//	 */
//	private final String[] PROJECTION = new String[]{GOODS_NAME,UNIT_NAME,CREATE_DATE,OPER_NAME,COMMENT,GOODS_PRICE_ID,NUMBER};

}
