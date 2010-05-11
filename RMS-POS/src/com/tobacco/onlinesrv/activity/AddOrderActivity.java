package com.tobacco.onlinesrv.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import com.tobacco.R;
import com.tobacco.main.activity.view.RMSBaseView;
import com.tobacco.onlinesrv.entities.Agency;
import com.tobacco.onlinesrv.entities.Order;
import com.tobacco.onlinesrv.entities.OrderDetail;
import com.tobacco.onlinesrv.entities.PreOrder;
import com.tobacco.onlinesrv.entities.Tobacco;
import com.tobacco.onlinesrv.util.FieldSupport;
import com.tobacco.onlinesrv.util.NumberGenerate;

public class AddOrderActivity extends RMSBaseView {
	private int mYear;
	private int mMonth;
	private int mDay;
	private String today;
	private Spinner brandNameSp;
	private Spinner formatSp;
	private Spinner typeSp;
	private EditText countEdt;
	private EditText dateEdt;
	private EditText amountEdt;
	private TextView agencyTxt;
	private EditText descEdt;
	private TextView priceTxt;
	private LinearLayout linearScroll;
	private EditText totalAmountTxt;
	private Button okBtn;
	private Button cancelBtn;
	private TextView addTxt;
	private Uri preorderUri = PreOrder.CONTENT_URI;
	private Uri orderUri = Order.CONTENT_URI;
	private String formatStr;
	private String format[] = { "条", "包" };
	private String type[] = { "预订单", "订单" };
	private String agencyid = "";
	private String agencyName;
	private JSONObject obj;
	private float totalAmount = 0;
	private String[] fieldString = { FieldSupport.KEY_ORDER_ID,
			FieldSupport.KEY_BRANDCODE, FieldSupport.KEY_BRANDCOUNT,
			FieldSupport.KEY_DATE, FieldSupport.KEY_USERNAME,
			FieldSupport.KEY_FORMAT, FieldSupport.KEY_AMOUNT,
			FieldSupport.KEY_AGENTCYID, FieldSupport.KEY_DESCRIPTION,
			FieldSupport.KEY_STATUS };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);
		initView();
		setDateOfToday();
		fillBrandSpinner();
		fillAgencyText();
		setListeners();

	}

	private void initView() {
		brandNameSp = (Spinner) this.findViewById(R.id.brandNameSp);
		formatSp = (Spinner) this.findViewById(R.id.formatSp);
		countEdt = (EditText) this.findViewById(R.id.brandCountText);
		priceTxt = (TextView) this.findViewById(R.id.priceText);
		dateEdt = (EditText) this.findViewById(R.id.dateText);
		amountEdt = (EditText) this.findViewById(R.id.amountText);
		agencyTxt = (TextView) this.findViewById(R.id.unitText);
		descEdt = (EditText) this.findViewById(R.id.descText);
		typeSp = (Spinner) this.findViewById(R.id.typeSp);
		okBtn = (Button) this.findViewById(R.id.orderOkBtn);
		cancelBtn = (Button) this.findViewById(R.id.orderCancelBtn);
		addTxt = (TextView) this.findViewById(R.id.addTxt);
		linearScroll = (LinearLayout) this.findViewById(R.id.LinearForScroll);
		totalAmountTxt = (EditText) this.findViewById(R.id.totalAmountText);
	}

	private void fillBrandSpinner() {

		brandNameSp.setAdapter((new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, FieldSupport.brandType)));

		formatSp.setAdapter((new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, format)));
		typeSp.setAdapter((new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, type)));
	}

	private void fillAgencyText() {
		Cursor cursor = this.managedQuery(Agency.CONTENT_URI, null, null, null,
				null);
		if (cursor.getCount() == 0)
			Toast.makeText(AddOrderActivity.this, "您还未设置您所在单位",
					Toast.LENGTH_SHORT).show();
		else {
			cursor.moveToFirst();
			agencyName = cursor.getString(1);
			agencyTxt.setText(agencyName);
			agencyid = cursor.getString(0);
		}
	}

	private void setListeners() {
		brandNameSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (formatSp.getSelectedItemPosition() == 1) {
					priceTxt.setText(FieldSupport.packetPrice[position]);
				} else
					priceTxt.setText(FieldSupport.itemPrice[position]);
				setAmountText();
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		formatSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (position == 1) {
					priceTxt.setText(FieldSupport.packetPrice[brandNameSp
							.getSelectedItemPosition()]);
				} else {
					priceTxt.setText(FieldSupport.itemPrice[brandNameSp
							.getSelectedItemPosition()]);
				}
				setAmountText();
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		typeSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (position == 0) {
					dateEdt.setEnabled(true);
				} else {
					dateEdt.setText(today);
					dateEdt.setEnabled(false);
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		countEdt.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)
					if (!countEdt.getText().toString().equals("")
							&& isInteger(countEdt.getText().toString()))
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
				if (validation()) {
					Uri uri = null;
					formatStr = format[formatSp.getSelectedItemPosition()];
					if (typeSp.getSelectedItemPosition() == 0) {
						uri = AddOrder(preorderUri, PreOrder.KEY_PREORDER_ID,
								PreOrder.KEY_PREDATE, PreOrder.KEY_AMOUNT,
								PreOrder.KEY_AGENTCYID, PreOrder.KEY_USERNAME,
								PreOrder.KEY_DESCRIPTION, PreOrder.KEY_STATUS);
					} else {
						uri = AddOrder(orderUri, Order.KEY_ORDER_ID,
								Order.KEY_DATE, Order.KEY_AMOUNT,
								Order.KEY_AGENTCYID, Order.KEY_USERNAME,
								Order.KEY_DESCRIPTION, Order.KEY_STATUS);
					}
					if (uri != null) {
						for (int i = 1; i < linearScroll.getChildCount(); i++) {
							String brand = FieldSupport.brandType[((Spinner) linearScroll
									.getChildAt(i).findViewById(
											R.id.brandNameSp))
									.getSelectedItemPosition()];
							String formatStr = format[((Spinner) linearScroll
									.getChildAt(i).findViewById(R.id.formatSp))
									.getSelectedItemPosition()];
							String price = ((TextView) linearScroll.getChildAt(
									i).findViewById(R.id.priceText)).getText()
									.toString();
							String count = ((EditText) linearScroll.getChildAt(
									i).findViewById(R.id.brandCountText))
									.getText().toString();
							String amount = ((EditText) linearScroll
									.getChildAt(i)
									.findViewById(R.id.amountText)).getText()
									.toString();
							Uri detailUri = addOrderDetail(brand, formatStr,
									price, count, amount);
							if (detailUri != null)
								openSuccessDialog();
						}

					} else
						Toast.makeText(AddOrderActivity.this, "添加失败，请检查数据",
								Toast.LENGTH_SHORT).show();

				}
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		addTxt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				final LinearLayout linearContent = (LinearLayout) inflater
						.inflate(R.layout.order_item, null);
				linearScroll.addView(linearContent);
				final Spinner brandSpinner = (Spinner) linearContent
						.findViewById(R.id.brandNameSp);
				brandSpinner.setAdapter((new ArrayAdapter<String>(
						AddOrderActivity.this,
						android.R.layout.simple_spinner_item,
						FieldSupport.brandType)));
				final Spinner formatSpinner = (Spinner) linearContent
						.findViewById(R.id.formatSp);
				formatSpinner.setAdapter((new ArrayAdapter<String>(
						AddOrderActivity.this,
						android.R.layout.simple_spinner_item, format)));
				final TextView priceTxt = (TextView) linearContent
						.findViewById(R.id.priceText);
				priceTxt.setText(FieldSupport.itemPrice[brandSpinner
						.getSelectedItemPosition()]);
				final EditText countEdt = (EditText) linearContent
						.findViewById(R.id.brandCountText);
				final EditText amountEdt = (EditText) linearContent
						.findViewById(R.id.amountText);
				brandSpinner
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int position, long arg3) {

								if (formatSpinner.getSelectedItemPosition() == 1) {
									priceTxt
											.setText(FieldSupport.packetPrice[position]);
								} else
									priceTxt
											.setText(FieldSupport.itemPrice[position]);
								setAmountTextForNew(amountEdt, countEdt
										.getText().toString(), priceTxt
										.getText().toString());
							}

							public void onNothingSelected(AdapterView<?> arg0) {

							}
						});
				formatSpinner
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int position, long arg3) {

								if (position == 1) {
									priceTxt
											.setText(FieldSupport.packetPrice[brandSpinner
													.getSelectedItemPosition()]);
								} else {
									priceTxt
											.setText(FieldSupport.itemPrice[brandSpinner
													.getSelectedItemPosition()]);
								}
								setAmountTextForNew(amountEdt, countEdt
										.getText().toString().trim(), priceTxt
										.getText().toString());
							}

							public void onNothingSelected(AdapterView<?> arg0) {

							}
						});
				countEdt.setOnFocusChangeListener(new OnFocusChangeListener() {

					public void onFocusChange(View v, boolean hasFocus) {

						String count = countEdt.getText().toString();
						if (!hasFocus
								&& !countEdt.getText().toString().equals("")) {
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
		});
	}

	private void setAmountText() {
		if (countEdt.getText() != null
				&& !countEdt.getText().toString().equals(""))
			totalAmount = Float.parseFloat(priceTxt.getText().toString())
					* Float.parseFloat(countEdt.getText().toString().trim());
		amountEdt.setText(totalAmount + "");
		setTotalAmount();

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
		for (int i = 1; i < linearScroll.getChildCount(); i++) {
			String amount = ((EditText) linearScroll.getChildAt(i)
					.findViewById(R.id.amountText)).getText().toString();
			if (!amount.equals(""))
				totalAmount += Float.parseFloat(amount);
		}
		totalAmountTxt.setText(totalAmount + "");
	}

	private Uri addOrderDetail(String brand, String format, String price,
			String count, String amount) {
		ContentValues values = new ContentValues();
		if (typeSp.getSelectedItemPosition() == 0)
			values.put(OrderDetail.KEY_PREORDER_ID,
					getAllCount(PreOrder.CONTENT_URI));
		else
			values
					.put(OrderDetail.KEY_ORDER_ID,
							getAllCount(Order.CONTENT_URI));
		values.put(OrderDetail.KEY_BRANDCODE, brand);
		values.put(OrderDetail.KEY_FORMAT, format);
		values.put(OrderDetail.KEY_PRICE, price);
		values.put(OrderDetail.KEY_BRANDCOUNT, count);
		values.put(OrderDetail.KEY_AMOUNT, amount);
		Uri uri = getContentResolver().insert(OrderDetail.CONTENT_URI, values);
		return uri;
	}

	private Uri AddOrder(Uri uriType, String orderId, String date,
			String amount, String agencyId, String userName, String desc,
			String status) {
		ContentValues values = new ContentValues();
		if (uriType == PreOrder.CONTENT_URI) {
			values.put(orderId, NumberGenerate
					.preOrderIdGeneration(getAllCount(uriType) + 1));
		} else {
			values.put(orderId, NumberGenerate
					.orderIdGeneration(getAllCount(uriType) + 1));
			values.put(Order.KEY_RECIEVE, "0");
		}
		values.put(date, dateEdt.getText().toString());
		values.put(amount, totalAmount);
		values.put(agencyId, agencyid);
		values.put(userName, "cry");
		values.put(desc, descEdt.getText().toString());
		values.put(status, "0");
		Uri uri = getContentResolver().insert(uriType, values);
		// try {
		// putToJson(uriType);
		// // getJsonDataAndUpdate(obj);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		return uri;
	}

	private int getAllCount(Uri uri) {
		Cursor cursor = this.managedQuery(uri, null, null, null, null);
		if (cursor.getCount() != 0) {
			cursor.moveToLast();
			return Integer.parseInt(cursor.getString(0));
		} else
			return 0;
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

	private void openSuccessDialog() {
		new AlertDialog.Builder(AddOrderActivity.this).setTitle("").setMessage(
				"添加成功").setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				}).show();
	}

	private boolean isInteger(String number) {
		Pattern pattern = Pattern.compile("^-?\\d+$");
		Matcher match = pattern.matcher(number);
		return match.matches();
	}

	private void setDateOfToday() {
		today = Calendar.getInstance().get(Calendar.YEAR) + "-"
				+ (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-"
				+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		dateEdt.setText(today);
	}

	private boolean isValidDate() {
		String date = dateEdt.getText().toString();
		Date dateOfToday = null;
		Date dateOfSelectedDay = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dateOfToday = sdf.parse(today);
			dateOfSelectedDay = sdf.parse(date);
			if (dateOfSelectedDay.getTime() - dateOfToday.getTime() >= 0)
				return true;
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return false;
	}

	private boolean validation() {
		String tag = "";
		if (!isInteger(countEdt.getText().toString()))
			tag += "请在商品数量中输入数字\n";
		if (!isValidDate())
			tag += "日期不能小于今天";
		if (tag.equals(""))
			return true;
		else {
			Toast.makeText(AddOrderActivity.this, tag, Toast.LENGTH_LONG)
					.show();
			return false;
		}
	}

	private void putToJson(Uri uriType) throws JSONException {
		obj = new JSONObject();
		if (uriType == preorderUri)
			obj.put(fieldString[0], NumberGenerate
					.preOrderIdGeneration(getAllCount(uriType)));
		else
			obj.put(fieldString[0], NumberGenerate
					.orderIdGeneration(getAllCount(uriType)));
		obj.put(fieldString[1], FieldSupport.brandType[brandNameSp
				.getSelectedItemPosition()]);
		obj
				.put(fieldString[2], Integer.parseInt(countEdt.getText()
						.toString()));
		obj.put(fieldString[3], dateEdt.getText().toString());
		obj.put(fieldString[4], formatStr);
		obj.put(fieldString[5], Float
				.parseFloat(amountEdt.getText().toString()));
		obj.put(fieldString[6], agencyid);
		obj.put(fieldString[7], "cry");
		obj.put(fieldString[9], descEdt.getText().toString());
		obj.put(fieldString[10], "0");
	}

	private void getJsonDataAndUpdate(JSONObject object) throws JSONException {
		object = obj;
		ContentValues values = new ContentValues();
		Uri uriType = preorderUri;
		String orderId = "";
		for (String str : fieldString) {
			if (str.equals(FieldSupport.KEY_ORDER_ID)) {
				orderId = obj.getString(str);
				if (orderId.contains("O"))
					uriType = orderUri;
			}
			System.out.println(obj.getString(str));
			values.put(str, obj.getString(str));
		}
		int number = getContentResolver().update(uriType, values,
				"orderid = \"" + orderId + "\"", null);
		if (number != 0)
			System.out.println("update success" + number);

	}
}
