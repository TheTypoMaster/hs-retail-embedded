package com.tobacco.pos.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.tobacco.pos.entity.ReturnModel;

public class TestSendObjectService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter("com.tobacco.action.net.object");
		this.registerReceiver(new ObjectReceiver(), filter);
	}

	public class ObjectReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("complaint", "receive object");
			// TODO Auto-generated method stub
			ReturnModel model = intent.getParcelableExtra("object");
			Log.i("return", "comment:"+model.getComment());
			Log.i("return", "date:"+model.getCreateDate());
			Log.i("return", "goodsPriceId:"+model.getGoodsPriceId());
			Log.i("return", "customer:"+model.getCustomerId());
			Log.i("return", "number:"+model.getNumber());
		}
		
	}
}
