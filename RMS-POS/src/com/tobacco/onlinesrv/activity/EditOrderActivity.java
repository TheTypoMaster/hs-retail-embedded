package com.tobacco.onlinesrv.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tobacco.R;
import com.tobacco.onlinesrv.entities.Agency;
import com.tobacco.onlinesrv.entities.Order;
import com.tobacco.onlinesrv.entities.OrderDetail;
import com.tobacco.onlinesrv.entities.PreOrder;
import com.tobacco.onlinesrv.entities.Tobacco;
import com.tobacco.onlinesrv.util.FieldSupport;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class EditOrderActivity extends Activity {
	private int mYear;
	private int mMonth;
	private int mDay;
	private Spinner brandNameSp;
	private Spinner typeSp;
	private EditText dateEdt;
	private EditText totalAmountTxt;
	private EditText descEdt;
	private Button okBtn;
	private Button cancelBtn;
	private String brandType[] = {};
	private String packetPrice[] = {};
	private String itemPrice[] = {};
	private String format[] = { "条", "包" };
	private String type[] = { "预订单", "订单" };
	private List<String> idList = null;
	private LinearLayout linearScroll;
	private LinearLayout linearContent;
	private float totalAmount;
	private String agencyName;
	private int order_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);
		initView();
		fillBrandSpinner();
		fillAgencyText();
		setTextFileds(getIntent());
		setListeners();

	}

	private void initView() {
		brandNameSp = (Spinner) this.findViewById(R.id.brandNameSp);
		typeSp = (Spinner) this.findViewById(R.id.typeSp);
		dateEdt = (EditText) this.findViewById(R.id.dateText);
		descEdt = (EditText) this.findViewById(R.id.descText);
		okBtn = (Button) this.findViewById(R.id.orderOkBtn);
		cancelBtn = (Button) this.findViewById(R.id.orderCancelBtn);
		totalAmountTxt = (EditText) this.findViewById(R.id.totalAmountText);
		linearScroll = (LinearLayout) this.findViewById(R.id.LinearForScroll);
		linearContent = (LinearLayout) this.findViewById(R.id.LinearForContent);
		linearContent.setVisibility(View.GONE);
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
				tempStr3 += "," + cursor.getString(4);
			} while (cursor.moveToNext());
			brandType = tempStr.substring(1, tempStr.length()).split(",");
			packetPrice = tempStr2.substring(1, tempStr2.length()).split(",");
			itemPrice = tempStr3.substring(1, tempStr3.length()).split(",");
			brandNameSp.setAdapter((new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, brandType)));
		}
		typeSp.setAdapter((new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, type)));
	}

	private void fillAgencyText() {
		Cursor cursor = this.managedQuery(Agency.CONTENT_URI, null, null, null,
				null);
		if (cursor.getCount() == 0)
			Toast.makeText(EditOrderActivity.this, "您还未设置您所在单位",
					Toast.LENGTH_SHORT).show();
		else {
			cursor.moveToFirst();
			agencyName = cursor.getString(1);
		}
	}

	private void setListeners() {

		okBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int number = 0;
				int result = 0;
				if (typeSp.getSelectedItemPosition() == 0) {
					number = updateOrder(PreOrder.CONTENT_URI,
							PreOrder.KEY_PREDATE, PreOrder.KEY_AMOUNT,
							PreOrder.KEY_AGENTCYID, PreOrder.KEY_DESCRIPTION);
				} else {
					number = updateOrder(Order.CONTENT_URI, Order.KEY_DATE,
							Order.KEY_AMOUNT, Order.KEY_AGENTCYID,
							Order.KEY_DESCRIPTION);
				}
				for (int i = 2; i < linearScroll.getChildCount(); i++) {
					String brand = brandType[((Spinner) linearScroll
							.getChildAt(i).findViewById(R.id.brandNameSp))
							.getSelectedItemPosition()];
					String formatStr = format[((Spinner) linearScroll
							.getChildAt(i).findViewById(R.id.formatSp))
							.getSelectedItemPosition()];
					String price = ((TextView) linearScroll.getChildAt(i)
							.findViewById(R.id.priceText)).getText().toString();
					String count = ((EditText) linearScroll.getChildAt(i)
							.findViewById(R.id.brandCountText)).getText()
							.toString();
					String amount = ((EditText) linearScroll.getChildAt(i)
							.findViewById(R.id.amountText)).getText()
							.toString();
					result = updateOrderDetail(brand, formatStr, price, count,
							amount, idList.get(i - 2));
				}
				if (number != 0 && result != 0)
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
		dateEdt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showDialog(0);
			}
		});
	}

	private void setTextFileds(Intent intent) {
		HashMap<String, String> map = (HashMap<String, String>) intent
				.getExtras().get("dataMap");
		HashMap<Integer, HashMap<String, String>> detailMap = (HashMap<Integer, HashMap<String, String>>) intent
				.getExtras().get("detailMap");
		String orderId = map.get(FieldSupport.KEY_ORDER_ID);
		idList = new ArrayList<String>();
		order_id = Integer.parseInt(map.get(FieldSupport.KEY_ID));
		typeSp.setEnabled(false);
		if (orderId.contains("P"))
			typeSp.setSelection(0);
		else
			typeSp.setSelection(1);

		for (int i = 0; i < detailMap.size(); i++) {
			idList.add(detailMap.get(i).get(OrderDetail.KEY_ID));
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			final LinearLayout linearContent = (LinearLayout) inflater.inflate(
					R.layout.order_item, null);
			linearScroll.addView(linearContent);
			final Spinner brandSpinner = (Spinner) linearContent
					.findViewById(R.id.brandNameSp);
			brandSpinner.setAdapter((new ArrayAdapter<String>(
					EditOrderActivity.this,
					android.R.layout.simple_spinner_item, brandType)));
			brandSpinner.setSelection(getBrandNamePosition(detailMap.get(i)
					.get(OrderDetail.KEY_BRANDCODE)));
			final Spinner formatSpinner = (Spinner) linearContent
					.findViewById(R.id.formatSp);
			formatSpinner.setAdapter((new ArrayAdapter<String>(
					EditOrderActivity.this,
					android.R.layout.simple_spinner_item, format)));
			formatSpinner.setSelection(getFormatPosition(detailMap.get(i).get(
					OrderDetail.KEY_FORMAT)));
			final TextView priceTxt = (TextView) linearContent
					.findViewById(R.id.priceText);
			priceTxt.setText(detailMap.get(i).get(OrderDetail.KEY_PRICE));
			final EditText countEdt = (EditText) linearContent
					.findViewById(R.id.brandCountText);
			countEdt.setText(detailMap.get(i).get(OrderDetail.KEY_BRANDCOUNT));
			final EditText amountEdt = (EditText) linearContent
					.findViewById(R.id.amountText);
			amountEdt.setText(detailMap.get(i).get(OrderDetail.KEY_AMOUNT));

			brandSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) {

							if (formatSpinner.getSelectedItemPosition() == 1) {
								priceTxt.setText(packetPrice[position]);
							} else
								priceTxt.setText(itemPrice[position]);
							setAmountTextForNew(amountEdt, countEdt.getText()
									.toString(), priceTxt.getText().toString());
						}

						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});
			formatSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) {

							if (position == 1) {
								priceTxt.setText(packetPrice[brandSpinner
										.getSelectedItemPosition()]);
							} else {
								priceTxt.setText(itemPrice[brandSpinner
										.getSelectedItemPosition()]);
							}
							setAmountTextForNew(amountEdt, countEdt.getText()
									.toString().trim(), priceTxt.getText()
									.toString());
						}

						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});
			countEdt.setOnFocusChangeListener(new OnFocusChangeListener() {

				public void onFocusChange(View v, boolean hasFocus) {

					String count = countEdt.getText().toString();
					if (!hasFocus && !countEdt.getText().toString().equals("")) {
						if (isInteger(count))
							setAmountTextForNew(amountEdt, count, priceTxt
									.getText().toString());
					}
				}
			});
			TextView unitTxt = (TextView) linearContent
					.findViewById(R.id.unitText);
			unitTxt.setText(agencyName);
		}

		totalAmountTxt.setText(map.get(FieldSupport.KEY_AMOUNT));
		dateEdt.setText(map.get(FieldSupport.KEY_DATE));
		descEdt.setText(map.get(FieldSupport.KEY_DESCRIPTION));
	}

	private int updateOrderDetail(String brand, String format, String price,
			String count, String amount, String id) {
		ContentValues values = new ContentValues();
		values.put(OrderDetail.KEY_BRANDCODE, brand);
		values.put(OrderDetail.KEY_FORMAT, format);
		values.put(OrderDetail.KEY_PRICE, price);
		values.put(OrderDetail.KEY_BRANDCOUNT, count);
		values.put(OrderDetail.KEY_AMOUNT, amount);
		int number = getContentResolver().update(OrderDetail.CONTENT_URI,
				values, "id =" + id, null);
		return number;
	}

	private void setAmountTextForNew(EditText amountEdt, String count,
			String price) {
		if (!count.equals("")) {
			float tempAmount = Float.parseFloat(count)
					* Float.parseFloat(price);
			amountEdt.setText(tempAmount + "");
			setTotalAmount();
		}

	}

	private void setTotalAmount() {
		totalAmount = 0;
		for (int i = 2; i < linearScroll.getChildCount(); i++) {
			String amount = ((EditText) linearScroll.getChildAt(i)
					.findViewById(R.id.amountText)).getText().toString();
			if (!amount.equals(""))
				totalAmount += Float.parseFloat(amount);
		}
		totalAmountTxt.setText(totalAmount + "");
	}

	private boolean isInteger(String number) {
		Pattern pattern = Pattern.compile("^-?\\d+$");
		Matcher match = pattern.matcher(number);
		return match.matches();
	}

	private int getBrandNamePosition(String brandName) {
		for (int i = 0; i < brandType.length; i++) {
			if (brandType[i].equals(brandName)) {
				return i;
			}
		}
		return -1;
	}

	private int getFormatPosition(String formatName) {
		for (int i = 0; i < format.length; i++) {
			if (format[i].equals(format)) {
				return i;
			}
		}
		return -1;
	}

	private int updateOrder(Uri uriType, String date, String amount,
			String agencyId, String desc) {
		ContentValues values = new ContentValues();
		values.put(date, dateEdt.getText().toString());
		values.put(amount, totalAmount);
		values.put(desc, descEdt.getText().toString());
		int number = getContentResolver().update(uriType, values,
				"id = " + order_id, null);
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
}
