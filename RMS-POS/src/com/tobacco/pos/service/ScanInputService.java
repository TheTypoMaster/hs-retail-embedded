package com.tobacco.pos.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ScanInputService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
//		while(true){
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
//			Intent t = new Intent("com.tobacco.action.scan");
//			int index = 1;
//			t.putExtra("BARCODE",index++);
//			sendBroadcast(t);
			
//		}
		ScanThread thread = new ScanThread("scanthread");
		Log.e("lqz", "thread start.");
		thread.start();
		
	}
	class ScanThread extends Thread{
		private int sleepTime;
		
		public ScanThread(String name){
			super(name);
			sleepTime = 5000;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int index = 1;
			while(true){
				Log.e("lqz", "thread sleep.");
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.e("lqz", "wakeup");
				Intent t = new Intent("com.tobacco.action.scan");			
				t.putExtra("BARCODE",index++);
				sendBroadcast(t);
				Log.e("lqz", "send broadcast.");
			}
		}
		
		
	}
	
}
