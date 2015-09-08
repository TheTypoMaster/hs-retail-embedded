package com.tobacco.onlinesrv.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashMapUtil {

	public static HashMap<String, String> orderMap;
	public static List<HashMap<String, String>> detailMapList;

	public static void init() {
		orderMap = new HashMap<String, String>();
		detailMapList = new ArrayList<HashMap<String, String>>();
		
	}

	public static void setOrderMap(String date,
			String amount, String agency, String userName, String description,
			String status,String receive) {
		orderMap.put(FieldSupport.KEY_DATE, date);
		orderMap.put(FieldSupport.KEY_AMOUNT, amount);
		orderMap.put(FieldSupport.KEY_AGENTCYID, agency);
		orderMap.put(FieldSupport.KEY_USERNAME, userName);
		orderMap.put(FieldSupport.KEY_DESCRIPTION, description);
		orderMap.put(FieldSupport.KEY_STATUS, status);
		orderMap.put(FieldSupport.KEY_RECIEVE, receive);
	}
	
	public static void setDetailMapList(String brand, String format, String price,
			String count, String amount){
		HashMap<String, String> detailMap = new HashMap<String, String>();
		detailMap.put(FieldSupport.KEY_BRANDCODE, brand);
		detailMap.put(FieldSupport.KEY_FORMAT, format);
		detailMap.put(FieldSupport.KEY_PRICE, price);
		detailMap.put(FieldSupport.KEY_BRANDCOUNT, count);
		detailMap.put(FieldSupport.KEY_AMOUNT, amount);
		detailMapList.add(detailMap);
	}
	
	public static void printData(){
		for(String key:orderMap.keySet())
			System.out.println(orderMap.get(key));
		for(int i = 0;i<detailMapList.size();i++){
			HashMap<String, String> detailMap = detailMapList.get(i);
			for(String key:detailMap.keySet())
				System.out.println(detailMap.get(key));
		}
			
	}
	

}
