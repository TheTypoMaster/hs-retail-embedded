package com.tobacco.onlinesrv.activity;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.PreOrder;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class QueryPreOrderActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preorder);

		Uri preorder = PreOrder.CONTENT_URI;
		final EditText brandEdt = (EditText) this.findViewById(R.id.EditText01);
		final EditText countEdt = (EditText) this.findViewById(R.id.EditText02);
		final EditText fomartEdt = (EditText) this
				.findViewById(R.id.EditText03);
		final EditText dateEdt = (EditText) this.findViewById(R.id.EditText04);
		final EditText amountEdt = (EditText) this
				.findViewById(R.id.EditText05);
		final EditText agencyEdt = (EditText) this
				.findViewById(R.id.EditText06);
		final EditText vipEdt = (EditText) this.findViewById(R.id.EditText07);
		final EditText descEdt = (EditText) this.findViewById(R.id.EditText08);
		Cursor cursor = this.managedQuery(preorder, null, null, null, null);
		if (cursor.getCount() == 0)
			openfailDialog();
		else {
			Log.i("The size of cursor", cursor.getCount() + "");
			cursor.moveToFirst();
			String s = cursor.getString(1);
			brandEdt.setText(s);
			cursor.moveToNext();
			countEdt.setText(cursor.getString(2));
		}
	}

	private void openfailDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(QueryPreOrderActivity.this).setTitle("")
				.setMessage("fail").show();
	}

}