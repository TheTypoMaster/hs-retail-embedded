package com.tobacco.onlinesrv.activity;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.Order;
import com.tobacco.onlinesrv.entities.PreOrder;
import com.tobacco.onlinesrv.entities.Tobacco;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class EditOrderActivity extends Activity {
	private Spinner brandNameSp;
	private EditText countEdt;
	private EditText dateEdt;
	private EditText amountEdt;
	private EditText agencyEdt;
	private EditText vipEdt;
	private EditText descEdt;
	private TextView priceTxt;
	private RadioButton packetRadio;
	private RadioButton itemRadio;
	private RadioButton pRadio;
	private RadioButton oRadio;
	private Button okBtn;
	private Button cancelBtn;
	private int orderType;
	private String id;
	private Uri preorderUri = PreOrder.CONTENT_URI;
	private Uri orderUri = Order.CONTENT_URI;
	private String brandType[] = {};
	private String packetPrice[] = {};
	private String itemPrice[] = {};
	private String formatStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);
		init();
		fillBrandSpinner();
		setTextFileds(getIntent());
		setListeners();

	}

	private void setListeners() {
		brandNameSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (packetRadio.isChecked())
					priceTxt.setText(packetPrice[position]);
				else
					priceTxt.setText(itemPrice[position]);
				setAmountText();
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		packetRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					priceTxt.setText(packetPrice[brandNameSp
							.getSelectedItemPosition()]);
				else
					priceTxt.setText(itemPrice[brandNameSp
							.getSelectedItemPosition()]);
				setAmountText();

			}
		});
		itemRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					priceTxt.setText(itemPrice[brandNameSp
							.getSelectedItemPosition()]);
				else
					priceTxt.setText(packetPrice[brandNameSp
							.getSelectedItemPosition()]);
				setAmountText();

			}
		});
		countEdt.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)
					setAmountText();
			}
		});
		okBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int number = 0;
				if (packetRadio.isChecked())
					formatStr = packetRadio.getText().toString();
				if (itemRadio.isChecked())
					formatStr = itemRadio.getText().toString();
				if (orderType == 0) {
					number = updateOrder(preorderUri, PreOrder.KEY_BRANDCODE,
							PreOrder.KEY_BRANDCOUNT, PreOrder.KEY_PREDATE,
							PreOrder.KEY_FORMAT, PreOrder.KEY_AMOUNT,
							PreOrder.KEY_AGENTCYID, PreOrder.KEY_VIPID,
							PreOrder.KEY_DESCRIPTION);
				} else if (orderType == 1) {
					number = updateOrder(orderUri, Order.KEY_BRANDCODE,
							Order.KEY_BRANDCOUNT, Order.KEY_DATE,
							Order.KEY_FORMAT, Order.KEY_AMOUNT,
							Order.KEY_AGENTCYID, Order.KEY_VIPID,
							Order.KEY_DESCRIPTION);
				}
				if (number != 0)
					openSuccessDialog();
				else
					Toast.makeText(EditOrderActivity.this, "修改失败，请检查数据",
							Toast.LENGTH_SHORT).show();
				Log.i("update number", number + "");

			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

	private void setTextFileds(Intent intent) {
		orderType = Integer.parseInt(intent.getExtras().get("orderType")
				.toString());
		id = intent.getExtras().get("id").toString();
		brandNameSp.setSelection(getBrandNamePosition(intent.getExtras().get(
				"brandCode").toString()));
		countEdt.setText(intent.getExtras().get("brandCount").toString());
		setFormatAndPriceValue(intent);
		amountEdt.setText(intent.getExtras().get("amount").toString());
		agencyEdt.setText(intent.getExtras().get("agency").toString());
		vipEdt.setText(intent.getExtras().get("vip").toString());
		dateEdt.setText(intent.getExtras().get("date").toString());
		descEdt.setText(intent.getExtras().get("desc").toString());
		pRadio.setEnabled(false);
		oRadio.setEnabled(false);
	}

	private void setFormatAndPriceValue(Intent intent) {
		String formatStr = intent.getExtras().get("format").toString();
		if (packetRadio.getText().toString().equals(formatStr)) {
			packetRadio.setChecked(true);
			priceTxt
					.setText(packetPrice[brandNameSp.getSelectedItemPosition()]);
		} else if (itemRadio.getText().toString().equals(formatStr)) {
			itemRadio.setChecked(true);
			priceTxt.setText(itemPrice[brandNameSp.getSelectedItemPosition()]);
		}
	}

	private void setAmountText() {
		if (countEdt.getText() != null
				&& !countEdt.getText().toString().equals(""))
			amountEdt.setText(Float.parseFloat(priceTxt.getText().toString())
					* Float.parseFloat(countEdt.getText().toString()) + "");
	}

	private int getBrandNamePosition(String brandName) {
		for (int i = 0; i < brandType.length; i++)
			if (brandType[i].equals(brandName))
				return i;
		return -1;
	}

	private void init() {
		brandNameSp = (Spinner) this.findViewById(R.id.brandNameSp);
		countEdt = (EditText) this.findViewById(R.id.brandCountText);
		dateEdt = (EditText) this.findViewById(R.id.dateText);
		packetRadio = (RadioButton) this.findViewById(R.id.packetRadio);
		itemRadio = (RadioButton) this.findViewById(R.id.itemRadio);
		priceTxt = (TextView) this.findViewById(R.id.priceText);
		amountEdt = (EditText) this.findViewById(R.id.amountText);
		agencyEdt = (EditText) this.findViewById(R.id.unitText);
		vipEdt = (EditText) this.findViewById(R.id.vipText);
		descEdt = (EditText) this.findViewById(R.id.descText);
		pRadio = (RadioButton) this.findViewById(R.id.preOrderRadio);
		oRadio = (RadioButton) this.findViewById(R.id.orderRadio);
		okBtn = (Button) this.findViewById(R.id.orderOkBtn);
		cancelBtn = (Button) this.findViewById(R.id.orderCancelBtn);
	}

	private void fillBrandSpinner() {
		Cursor cursor = this.managedQuery(Tobacco.CONTENT_URI, null, null,
				null, null);
		String tempStr = "";
		String tempStr2 = "";
		String tempStr3 = "";
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			do {
				tempStr += "," + cursor.getString(1);
				tempStr2 += "," + cursor.getString(2);
				tempStr3 += "," + cursor.getString(3);
			} while (cursor.moveToNext());
			brandType = tempStr.substring(1, tempStr.length()).split(",");
			packetPrice = tempStr2.substring(1, tempStr2.length()).split(",");
			itemPrice = tempStr3.substring(1, tempStr3.length()).split(",");
			brandNameSp.setAdapter((new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, brandType)));
		}
	}

	private int updateOrder(Uri uriType, String brandCode, String brandCount,
			String date, String format, String amount, String agencyId,
			String vipId, String desc) {
		ContentValues values = new ContentValues();
		values.put(brandCode, brandType[brandNameSp.getSelectedItemPosition()]);
		values.put(brandCount, Integer.parseInt(countEdt.getText().toString()));
		values.put(date, dateEdt.getText().toString());
		values.put(format, formatStr);
		values.put(amount, Float.parseFloat(amountEdt.getText().toString()));
		values.put(agencyId, agencyEdt.getText().toString());
		values.put(vipId, Integer.parseInt(vipEdt.getText().toString()));
		values.put(desc, descEdt.getText().toString());
		int number = getContentResolver().update(uriType, values, "id = " + id,
				null);
		return number;
	}

	private void openSuccessDialog() {
		new AlertDialog.Builder(EditOrderActivity.this).setTitle("")
				.setMessage("修改成功").setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						}).show();
	}
}
