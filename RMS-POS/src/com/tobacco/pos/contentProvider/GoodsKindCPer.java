package com.tobacco.pos.contentProvider;

import java.util.ArrayList;
import java.util.List;


import android.database.Cursor;
import android.net.Uri; 

public class GoodsKindCPer extends BaseContentProvider {
		
	    private static final String TABLE_NAME   = DatabaseHelper.GOODSKIND;
	 
	    public static final Uri CONTENT_URI = Uri.parse("content://com.tobacco.pos.contentProvider.GoodsKindCPer");

	    public boolean onCreate() {
		 
			ctx = getContext();
			dbHelper = new DatabaseHelper(getContext());
			this.tableName = GoodsKindCPer.TABLE_NAME;
			return true;
		}
	  
		public List<String> getAllGoodsKindName() {
			Cursor c = this.query(CONTENT_URI, null, null, null, null);
			if(c.getCount()>0){
				c.moveToFirst();
				List<String> allGoodsKindName = new ArrayList<String>();
				for(int i=0;i<c.getCount();i++){
					allGoodsKindName.add(c.getString(1));
					c.moveToNext();
				}
				return allGoodsKindName;
			}
			return null;
		}
		
		public int getGoodsKindIdByGoodsKindName(String gKindName){
			Cursor c = this.query(CONTENT_URI, null, " name = ? ", new String[]{gKindName}, null);
			if(c.getCount()>0){
				c.moveToFirst();
				return c.getInt(0);
			}
			else
				return -1;
		}
		public String getGoodsKindNameByGoodsKindId(int kindId){
			Cursor c = this.query(CONTENT_URI, null, " _id = ? ", new String[]{kindId+""}, null);
			if(c.getCount()>0){
				c.moveToFirst();
				return c.getString(1);
			}
			return "";
		}
	
		

		
	
		public List<Integer> getGoodsKindIdListByGoodsKind(String kindName){//根据商品的类别名称，查找所有的类别Id，支持模糊搜索，返回的是List
			 
			Cursor c = this.query(CONTENT_URI, null, null, null, null);
			if(c.getCount()>0){
				c.moveToFirst();
				List<String> allKindName = new ArrayList<String>();//所有类别的名字
				List<Integer> allKindId = new ArrayList<Integer>();
				
				List<String> satisfiedKindName = new ArrayList<String>();//所有满足的类别的名字
				List<Integer> satisfiedKindId = new ArrayList<Integer>();
				for(int i=0;i<c.getCount();i++){
					String temp = c.getString(1);
					
					if(temp.contains("->")){
						temp = temp.substring(temp.lastIndexOf("->")+2);
						allKindName.add(temp);
					}
					else
						allKindName.add(temp);
					
					allKindId.add(c.getInt(0));
			
					
					c.moveToNext();
				}
				for(int i=0;i<allKindName.size();i++){
					if(allKindName.get(i).contains(kindName)){
						satisfiedKindName.add(allKindName.get(i));
						satisfiedKindId.add(allKindId.get(i));
					}
				}
				
				return satisfiedKindId;
			}
			return new ArrayList<Integer>();
		}
		
		public String getAttributeById(String attribute,String id){
			Cursor c = this.query(CONTENT_URI, new String[]{attribute}, "_id = "+"'"+id+"'" , null, null);
			if(c.getCount()>0){
				c.moveToFirst();
				return c.getString(0);
			}else{
				return null;
			}
		}
		
		public List<Integer> getCigaretteKindId(){//获取所有的烟类的Id
			Cursor c = this.query(CONTENT_URI, null, " name like '%烟%' ", null, null);
			if(c.getCount()>0){
				List<Integer> cigaretteKindList = new ArrayList<Integer>();
				c.moveToFirst();
				for(int i=0;i<c.getCount();i++){
					cigaretteKindList.add(c.getInt(0));
					c.moveToFirst();
				}
				for(int i=0;i<cigaretteKindList.size();i++){
					Cursor c1 = this.query(CONTENT_URI, null, " parent = ? ", new String[]{cigaretteKindList.get(i)+""}, null);
					if(c1.getCount()>0){
						c1.moveToFirst();
						for(int j=0;j<c1.getCount();j++){
							cigaretteKindList.add(c1.getInt(0));
							c1.moveToNext();
						}
					}
				}
				return cigaretteKindList;
			}
			return new ArrayList<Integer>();
		}
}
