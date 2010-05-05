package com.tobacco.pos.util;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.tobacco.pos.Module.AllTables.GoodsPrice;
import com.tobacco.pos.Module.AllTables.UserInfo;
import com.tobacco.pos.Module.AllTables.VIPInfo;

public class InputCheck{
  
  public static String checkVIP(Context ctx, String value){
	  
	if(value.equals(""))
		return "VIP号不能为空";
	if(!value.equals("")&&!RegexCheck.checkVIPNum(value))
		return "VIP号码输入无效";
    Cursor c = ctx.getContentResolver().query(VIPInfo.CONTENT_URI,null,VIPInfo.VIPNum+" = ? ",new String[]{value},null);
    if(c.getCount()==0)
    	return "此VIP客户不存在";
	return null;
  }

  public static String checkBarcode(Context ctx, String value){
	  
	if(value.equals(""))
		return "条形码不能为空";
	if(!value.equals("")&&!RegexCheck.checkBarcode(value))
		return "条形码输入无效";
    Cursor c = ctx.getContentResolver().query(GoodsPrice.CONTENT_URI,null,GoodsPrice.barcode+" = ? ",new String[]{value},null);
    if(c.getCount()==0)
    	return "此商品不存在";
	return null;
  }

}
