package com.tobacco.pos.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ScanInputService extends Service {

	BarcodeReaderHelper brh = null;
	
	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {

		brh = new BarcodeReaderHelper();

		ScanThread thread = new ScanThread("scanthread");
		Log.e("lqz", "thread start.");
		thread.start();
		
		

	}

	class ScanThread extends Thread {
		private int sleepTime;

		public ScanThread(String name) {
			super(name);
			sleepTime = 100;
		}

		@Override
		public void run() {

			while (true) {
//				Log.e("lqz", "thread sleep.");
//				try {
//					Thread.sleep(sleepTime);
//				} catch (InterruptedException e) {
//
//					e.printStackTrace();
//				}

				String tex = brh.getBarcode();
				if (tex != null) {// 如果有接收到扫描枪的数据
					Intent t = new Intent("com.tobacco.action.scan");
					t.putExtra("BARCODE", tex);
					sendBroadcast(t);

				} else {
					// t.putExtra("BARCODE","gb2");
					// sendBroadcast(t);
				}

			}

		}
	}

	
}
