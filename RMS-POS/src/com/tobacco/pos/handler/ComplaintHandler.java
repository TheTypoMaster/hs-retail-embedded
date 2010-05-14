package com.tobacco.pos.handler;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.tobacco.pos.contentProvider.GoodsPriceCPer;
import com.tobacco.pos.contentProvider.VIPInfoCPer;
import com.tobacco.pos.entity.ComplaintFull;
import com.tobacco.pos.entity.ComplaintModel;
import com.tobacco.pos.entity.AllTables.Complaint;
import com.tobacco.pos.entity.AllTables.Consume;
import com.tobacco.pos.entity.AllTables.Goods;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.UserInfo;
import com.tobacco.pos.entity.AllTables.VIPInfo;
import com.tobacco.pos.searchStrategy.ISearchStrategy;
import com.tobacco.pos.searchStrategy.SearchState;
import com.tobacco.pos.searchStrategy.SearchStrategyFactory;
import com.tobacco.pos.util.DateTool;

public class ComplaintHandler {

	/**
	 * record the context which invoke this class.
	 */
	private Context ctx;
	
	/**
	 * record all the consume goods. 
	 */
	private ArrayList<ComplaintModel> complaintGoods;
	
	/**
	 * construct method
	 * @param ctx
	 */
	public ComplaintHandler(Context ctx){
		complaintGoods = new ArrayList<ComplaintModel>();
		this.ctx = ctx;
	}
	
	/**
	 * insert this consume goods.
	 * @param goods
	 * @return the record uri in database.
	 */
	public Uri insert(ComplaintModel goods){
		Uri uri = ctx.getContentResolver().insert(Complaint.CONTENT_URI, goods.genContentValues());
		return uri;
	}
	
	public boolean delete(int complaintId){
		return true;
	}

	public boolean update(ComplaintModel goods){
		return true;
	}
	
	public ComplaintModel get(int complaintId){
		return null;
	}
	
	/**
	 * get the consume goods record with selection.
	 * @param selections the select factor
	 * @return the list of the ConsumeEntity objects.
	 */
	public int search(SearchState state){
		complaintGoods.clear();
		SearchStrategyFactory factory = SearchStrategyFactory.getInstance();
		ISearchStrategy strategy = factory.getSearchStrategy(ctx, ComplaintFull.CONTENT_URI, ComplaintFull.PROJECTION, state);
		Cursor cursor = strategy.searchc();
		Log.i("TestStrategy", "record count:"+cursor.getCount());
		
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i = 0;i<cursor.getCount();i++){
				complaintGoods.add(genComplaintEntity(cursor));
				cursor.moveToNext();
			}	
			cursor.close();
		}
		Log.e("ComplaintHandler", ""+complaintGoods.size());
		return complaintGoods.size();
	}
	
	/**
	 * get the data from cursor and form a ConsumeEntity object
	 * @param cursor
	 * @return return the ConsumeEntity obeject.
	 */
	private ComplaintModel genComplaintEntity(Cursor cursor){
		int idIndex = cursor.getColumnIndex(Complaint._ID);
		int id = cursor.getInt(idIndex);
		
		int goodsPriceIdIndex = cursor.getColumnIndex(Complaint.GOODS_ID);
		int goodsPriceId = cursor.getInt(goodsPriceIdIndex);
		
		int goodsNameIndex = cursor.getColumnIndex(Goods.goodsName);
		String goodsName = cursor.getString(goodsNameIndex);
		
		int vipNameIndex = cursor.getColumnIndex(VIPInfo.VIPName);
		String customer = cursor.getString(vipNameIndex);
		
		int operNameIndex = cursor.getColumnIndex(Complaint.OPERATOR);
		String operator = cursor.getString(operNameIndex);
		
		int timeIndex = cursor.getColumnIndex(Complaint.CREATE_DATE);
		String time = cursor.getString(timeIndex);
//		Date createDate = DateTool.formatStringToDate(time);
		
		int contentIndex = cursor.getColumnIndex(Complaint.COMMENT);
		String content = cursor.getString(contentIndex);
		
		ComplaintModel goods = new ComplaintModel(id, operator, customer, goodsPriceId, goodsName, time, content);
		return goods;
	}
	
	/**
	 * return a page of ConsumeEntity objects.
	 * @param start
	 * @param limit
	 * @return objects list.
	 */
	public ArrayList<ComplaintModel> getPage(int start, int limit){
		ArrayList<ComplaintModel> result = new ArrayList<ComplaintModel>();
		if(complaintGoods.size()>start){
			int end = (complaintGoods.size()>start+limit)?start+limit:complaintGoods.size();
			for(int i= start ;i<end;i++){
				result.add(complaintGoods.get(i));
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
	public ComplaintModel fillVacancy(String barcode, String vipNum,String content){
		GoodsPriceCPer goodsPriceCPer = new GoodsPriceCPer();
		VIPInfoCPer vipInfoCPer = new VIPInfoCPer();
		
		if(vipNum.equals("")){
			vipNum = "common";
		}	
		int	customerId = Integer.valueOf(vipInfoCPer.getAttributeByAttribute(VIPInfo._ID, VIPInfo.VIPNum, vipNum)).intValue();
		int goodsPriceId = Integer.valueOf(goodsPriceCPer.getAttributeByAttribute(GoodsPrice._ID, GoodsPrice.barcode, barcode)).intValue();	
		ComplaintModel goods = new ComplaintModel(customerId, goodsPriceId, content);
		
		return goods;
	}
}
