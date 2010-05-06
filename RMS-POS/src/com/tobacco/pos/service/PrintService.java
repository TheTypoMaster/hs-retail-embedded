package com.tobacco.pos.service;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class PrintService extends Service {

	ReceiptPrinterHelper rph = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	@Override
	public void onCreate() {
		 
		super.onCreate();
		
		 rph = new ReceiptPrinterHelper();
	
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
		
	}

	@Override
	public void onStart(Intent intent, int startId) {//在这里接受要打印的数据，然后调用汉林的打印函数
		 
		super.onStart(intent, startId);
		Log.d("lyq", "print start..............");
		String storeName = "亨事达";
		String newSBillNum = intent.getStringExtra("newSBillNum");//销售单号
		String sTime = intent.getStringExtra("sTime");//销售日期
		String userName = intent.getStringExtra("userName");//收银员
		double totalMoney = intent.getDoubleExtra("totalMoney", 0);//应收
		double payMoney = intent.getDoubleExtra("payMoney", 0);//现金
		
		ArrayList<String> goodsNameList = intent.getStringArrayListExtra("goodsNameList");
		ArrayList<String> countList = intent.getStringArrayListExtra("countList");
		ArrayList<String> outPriceList = intent.getStringArrayListExtra("outPriceList");
		ArrayList<String> tMoneyList = intent.getStringArrayListExtra("tMoneyList");
		ArrayList<String> flagList = intent.getStringArrayListExtra("flagList");
		
		Log.d("lyq", ".................."+newSBillNum);
		Log.d("lyq", ".................."+sTime);
		Log.d("lyq", ".................."+userName);
		Log.d("lyq", ".................."+totalMoney);
		Log.d("lyq", ".................."+payMoney);
		
		for(int i=0;i<goodsNameList.size();i++){
			Log.d("lyq", goodsNameList.get(i) + "-" + countList.get(i) + "-" + outPriceList.get(i) + "-" + tMoneyList.get(i) + "-" + flagList.get(i));
		}
		Log.d("lyq", "print end..............");
		
		int fd = rph.open();
		if(fd>0){
			rph.printHead(fd, storeName, newSBillNum, userName, sTime);
			for(int i=0;i<goodsNameList.size();i++){
				rph.printBody(fd, goodsNameList.get(i), outPriceList.get(i), countList.get(i), tMoneyList.get(i)+flagList.get(i));
				Log.d("lyq", goodsNameList.get(i) + "-" + countList.get(i) + "-" + outPriceList.get(i) + "-" + tMoneyList.get(i) + "-" + flagList.get(i));
			}
			rph.printFoot(fd, totalMoney+"", payMoney+"", (payMoney - totalMoney)+"");
			
			
		}
	}
	

}
