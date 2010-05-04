package com.tobacco.onlinesrv.activity;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.tobacco.onlinesrv.R;
import com.tobacco.onlinesrv.entities.Agency;
import com.tobacco.onlinesrv.entities.Order;
import com.tobacco.onlinesrv.entities.PreOrder;
import com.tobacco.onlinesrv.entities.Tobacco;
import com.tobacco.onlinesrv.util.FieldSupport;
import com.tobacco.onlinesrv.util.NumberGenerate;

public class AddOrderActivity extends Activity {
	private int mYear;
	private int mMonth;
	private int mDay;
	private Spinner brandNameSp;
	private EditText countEdt;
	private EditText dateEdt;
	private EditText amountEdt;
	private TextView agencyTxt;
	private EditText vipEdt;
	private EditText descEdt;
	private TextView priceTxt;
	private RadioButton pRadio;
	private RadioButton oRadio;
	private RadioButton packetRadio;
	private RadioButton itemRadio;
	private Button okBtn;
	private Button cancelBtn;
	private Uri preorderUri = PreOrder.CONTENT_URI;
	private Uri orderUri = Order.CONTENT_URI;
	private String brandType[] = {};
	private String packetPrice[] = {};
	private String itemPrice[] = {};
	private String formatStr;
	private String agencyid = "";
	private JSONObject obj;
	private String[] fieldString = { FieldSupport.KEY_ORDER_ID,
			FieldSupport.KEY_BRANDCODE, FieldSupport.KEY_BRANDCOUNT,
			FieldSupport.KEY_DATE, FieldSupport.KEY_USERNAME,
			FieldSupport.KEY_VIPID, FieldSupport.KEY_FORMAT,
			FieldSupport.KEY_AMOUNT, FieldSupport.KEY_AGENTCYID,
			FieldSupport.KEY_DESCRIPTION, FieldSupport.KEY_STATUS };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);
		init();
		fillBrandSpinner();
		fillAgencyText();
		setListeners();

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			mYear = Calendar.getInstance().get(Calendar.YEAR);
			mMonth = Calendar.getInstance().get(Calendar.MONTH);
			mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}

		return null;
	}

	private void init() {
		brandNameSp = (Spinner) this.findViewById(R.id.brandNameSp);
		countEdt = (EditText) this.findViewById(R.id.brandCountText);
		packetRadio = (RadioButton) this.findViewById(R.id.packetRadio);
		itemRadio = (RadioButton) this.findViewById(R.id.itemRadio);
		priceTxt = (TextView) this.findViewById(R.id.priceText);
		dateEdt = (EditText) this.findViewById(R.id.dateText);
		amountEdt = (EditText) this.findViewById(R.id.amountText);
		agencyTxt = (TextView) this.findViewById(R.id.unitText);
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

	private void fillAgencyText() {
		Cursor cursor = this.managedQuery(Agency.CONTENT_URI, null, null, null,
				null);
		if (cursor.getCount() == 0)
			Toast.makeText(AddOrderActivity.this, "您还未设置您所在单位",
					Toast.LENGTH_SHORT).show();
		else {
			cursor.moveToFirst();
			String agencyName = cursor.getString(1);
			agencyTxt.setText(agencyName);
			agencyid = cursor.getString(0);
		}
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
		countEdt.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)
					if (isInteger(countEdt.getText().toString()))
						setAmountText();
					else
						Toast.makeText(AddOrderActivity.this, "输入不合法，请重新输入",
								Toast.LENGTH_SHORT).show();
			}
		});

		dateEdt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showDialog(0);
			}
		});

		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Uri uri = null;
				if (packetRadio.isChecked())
					formatStr = packetRadio.getText().toString();
				if (itemRadio.isChecked())
					formatStr = itemRadio.getText().toString();
				if (pRadio.isChecked())
					uri = AddOrder(preorderUri, PreOrder.KEY_PREORDER_ID,
							PreOrder.KEY_BRANDCODE, PreOrder.KEY_BRANDCOUNT,
							PreOrder.KEY_PREDATE, PreOrder.KEY_FORMAT,
							PreOrder.KEY_AMOUNT, PreOrder.KEY_AGENTCYID,
							PreOrder.KEY_USERNAME, PreOrder.KEY_VIPID,
							PreOrder.KEY_DESCRIPTION, PreOrder.KEY_STATUS);
				if (oRadio.isChecked())
					uri = AddOrder(orderUri, Order.KEY_ORDER_ID,
							Order.KEY_BRANDCODE, Order.KEY_BRANDCOUNT,
							Order.KEY_DATE, Order.KEY_FORMAT, Order.KEY_AMOUNT,
							Order.KEY_AGENTCYID, Order.KEY_USERNAME,
							Order.KEY_VIPID, Order.KEY_DESCRIPTION,
							Order.KEY_STATUS);

				if (uri != null) {
					openSuccessDialog();
				} else
					Toast.makeText(AddOrderActivity.this, "添加失败，请检查数据",
							Toast.LENGTH_SHORT).show();

			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void setAmountText() {
		if (countEdt.getText() != null
				&& !countEdt.getText().toString().equals(""))
			amountEdt.setText(Float.parseFloat(priceTxt.getText().toString())
					* Float.parseFloat(countEdt.getText().toString()) + "");
	}

	private Uri AddOrder(Uri uriType, String orderId, String brandCode,
			String brandCount, String date, String format, String amount,
			String agencyId, String userName, String vipId, String desc,
			String status) {
		ContentValues values = new ContentValues();
		if (uriType == preorderUri)
			values.put(orderId, NumberGenerate
					.preOrderIdGeneration(getAllCount(uriType) + 1));
		else
			values.put(orderId, NumberGenerate
					.orderIdGeneration(getAllCount(uriType) + 1));
		values.put(brandCode, brandType[brandNameSp.getSelectedItemPosition()]);
		values.put(brandCount, Integer.parseInt(countEdt.getText().toString()));
		values.put(date, dateEdt.getText().toString());
		values.put(format, formatStr);
		values.put(amount, Float.parseFloat(amountEdt.getText().toString()));
		values.put(agencyId, agencyid);
		values.put(userName, "cry");
		values.put(vipId, Integer.parseInt(vipEdt.getText().toString()));
		values.put(desc, descEdt.getText().toString());
		values.put(status, "0");
		Uri uri = getContentResolver().insert(uriType, values);
		try {
			putToJson(uriType);
//			getJsonDataAndUpdate(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri;
	}

	private int getAllCount(Uri uri) {
		Cursor cursor = this.managedQuery(uri, null, null, null, null);
		return cursor.getCount();
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			setDateFiled();
		}

		private void setDateFiled() {
			dateEdt.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
		}
	};

	private void openSuccessDialog() {
		new AlertDialog.Builder(AddOrderActivity.this).setTitle("").setMessage(
				"添加成功").setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).show();
	}

	private boolean isInteger(String number) {
		Pattern pattern = Pattern.compile("^-?\\d+$");
		Matcher match = pattern.matcher(number);
		return match.matches();
	}

	private void putToJson(Uri uriType) throws JSONException {
		obj = new JSONObject();
		if (uriType == preorderUri)
			obj.put(fieldString[0], NumberGenerate
					.preOrderIdGeneration(getAllCount(uriType)));
		else
			obj.put(fieldString[0], NumberGenerate
					.orderIdGeneration(getAllCount(uriType)));
		obj.put(fieldString[1], brandType[brandNameSp.getSelectedItemPosition()]);
		obj.put(fieldString[2], Integer.parseInt(countEdt.getText().toString()));
		obj.put(fieldString[3], dateEdt.getText().toString());
		obj.put(fieldString[4], formatStr);
		obj.put(fieldString[5], Float.parseFloat(amountEdt.getText().toString()));
		obj.put(fieldString[6], agencyid);
		obj.put(fieldString[7], "cry");
		obj.put(fieldString[8], Integer.parseInt(vipEdt.getText().toString()));
		obj.put(fieldString[9], descEdt.getText().toString());
		obj.put(fieldString[10], "0");
	}

	private void getJsonDataAndUpdate(JSONObject object) throws JSONException {
		object = obj;
		ContentValues values = new ContentValues();
		Uri uriType = preorderUri; 
		String orderId = "";
		for(String str :fieldString)
		{
			if(str.equals(FieldSupport.KEY_ORDER_ID))
			{
				orderId = obj.getString(str);
				if(orderId.contains("O"))
					uriType=orderUri;
			}
			System.out.println(obj.getString(str));
			values.put(str, obj.getString(str));		
		}		
		int number = getContentResolver().update(uriType, values, "orderid = \""+orderId+"\"", null);
		if(number!=0)
			System.out.println("update success"+number);
	}
}
