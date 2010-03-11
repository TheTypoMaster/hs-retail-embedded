package com.tobacco.pos.activity;

import java.util.Date;

import com.tobacco.pos.dao.GoodsDAO;
import com.tobacco.pos.dao.GoodsPriceDAO;
import com.tobacco.pos.dao.SalesBillDAO;
import com.tobacco.pos.dao.SalesItemDAO;
import com.tobacco.pos.dao.UnitDAO;
import com.tobacco.pos.dao.UserInfoDAO;
import com.tobacco.pos.dao.VIPInfoDAO;
import com.tobacco.pos.util.Loginer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PaymentManagement extends Activity {
	private VIPInfoDAO vipInfoDAO = null;
	private GoodsPriceDAO goodsPriceDAO = null;
	private GoodsDAO gDAO = null;
	private UnitDAO unitDAO = null;
	private Loginer loginer = null;
	private UserInfoDAO userInfoDAO  = null;
	private SalesBillDAO sBillDAO = null;
	private SalesItemDAO sItemDAO = null;

	private TextView paymentWelcome;
	private ImageButton paymentreturn;// 返回首页的按钮

	private EditText barcodeEText;//条形码输入框
	private EditText sGoodsNumEText;// 数量的输入框
	private TableLayout salesBillTable;// 显示每一张销售单详细信息的表格

	private String userName = "";// 登陆用户的名字
	private int VIPId = -1;//

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paymentmanagement);

		userName = this.getIntent().getStringExtra("userName");

		paymentWelcome = (TextView) this.findViewById(R.id.paymentWelcome);
		paymentWelcome.setText("你好:" + userName);

		paymentreturn = (ImageButton) this.findViewById(R.id.paymentreturn);
		paymentreturn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				loginer = new Loginer(PaymentManagement.this);
				if (loginer.logout(userName, loginer.getWritableDatabase()))// 登出
				{
					Intent intent = new Intent(PaymentManagement.this,
							Main.class);
					PaymentManagement.this.startActivity(intent);
				}
			}

		});
		barcodeEText = (EditText) this.findViewById(R.id.barcodeEText);
		sGoodsNumEText = (EditText) this.findViewById(R.id.sGoodsNumEText);
		sGoodsNumEText.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {

				if (!hasFocus) {
					int count = 0;
					try {
						count = Integer.parseInt(((EditText) v).getText()
								.toString());
					} catch (Exception e) {
						((EditText) v).setText("");
					}
					String barcode = barcodeEText.getText().toString();
					if (!barcode.trim().equals("") && count != 0) {
						gDAO = new GoodsDAO(PaymentManagement.this);
						goodsPriceDAO = new GoodsPriceDAO(
								PaymentManagement.this);
						unitDAO = new UnitDAO(PaymentManagement.this);

						int goodsId = goodsPriceDAO.getGoodsIdByBarcode(
								barcode, goodsPriceDAO.getReadableDatabase());
						int unitId = goodsPriceDAO.getUnitIdByBarcode(barcode,
								goodsPriceDAO.getReadableDatabase());

						salesBillTable = (TableLayout) PaymentManagement.this
								.findViewById(R.id.salesBillTable);
						final TableRow r = new TableRow(PaymentManagement.this);
						TextView goodsNameTView = new TextView(PaymentManagement.this);

						goodsNameTView.setText(gDAO.getGoodsNameByGoodsId(goodsId, gDAO
								.getReadableDatabase()));
						r.addView(goodsNameTView);//商品名字
						
						TextView countTView = new TextView(PaymentManagement.this);
						countTView.setText(""+count);
						r.addView(countTView);//商品数量
						
						TextView unitTView = new TextView(PaymentManagement.this);
						unitTView.setText(unitDAO.getUnitNameById(unitId, unitDAO.getReadableDatabase()));
						r.addView(unitTView);//商品单价
						
						TextView priceTView = new TextView(PaymentManagement.this);
						priceTView.setText(""+goodsPriceDAO.getOutPriceByBarcode(barcode, goodsPriceDAO.getReadableDatabase()));
						r.addView(priceTView);	
						
						TextView barcodeTView = new TextView(PaymentManagement.this);
						barcodeTView.setText(barcode);
						r.addView(barcodeTView);
						
						ImageView i = new ImageView(PaymentManagement.this);
						i.setImageResource(R.drawable.delete);
						i.setOnClickListener(new OnClickListener(){

							public void onClick(View v) {
								((TableLayout)v.getParent().getParent()).removeView(r);
								if(salesBillTable.getChildCount()==2){
									salesBillTable.removeViewAt(1);
								}
							}
							
						});

						r.addView(i);
						if(salesBillTable.getChildCount()!=1){
							salesBillTable.removeViewAt(salesBillTable.getChildCount()-1);
						}
					
						salesBillTable.addView(r);
						barcodeEText.setText("");
						sGoodsNumEText.setText("");
						
						Button saveButton = new Button(PaymentManagement.this);
						saveButton.setText("确定");
					
						saveButton.setTextSize(10);
						saveButton.setOnClickListener(new OnClickListener(){

							public void onClick(View v) {
								sBillDAO = new SalesBillDAO(PaymentManagement.this);
								sItemDAO = new SalesItemDAO(PaymentManagement.this);
								userInfoDAO = new UserInfoDAO(PaymentManagement.this);
							
								
								int userId = userInfoDAO.getUserIdByUserName(userName, userInfoDAO.getReadableDatabase());//取得销售的人员Id
							
								int newSBillId = sBillDAO.addSBill(userId, (new Date()).toLocaleString(), VIPId, sBillDAO.getWritableDatabase());//新增销售单的ID
//								sItemDAO.addSalesItem(newSBillId, sGoodsNum, sPriceId, db);
								for(int i=1;i<salesBillTable.getChildCount()-1;i++){
									String theBarcode = ((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(4)).getText().toString();//取得每个销售项的条形码
									int goodsCount = Integer.parseInt(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(1)).getText().toString());//取得每个销售项的数量
									int sPriceId = goodsPriceDAO.getGoodsPriceIdByBarcode(theBarcode, goodsPriceDAO.getReadableDatabase());
									sItemDAO.addSalesItem(newSBillId, goodsCount, sPriceId, sItemDAO.getWritableDatabase());
								}
							}
							
						});
						TextView blank1 = new TextView(PaymentManagement.this);
						blank1.setText("");
						TextView blank2 = new TextView(PaymentManagement.this);
						blank2.setText("");
						TextView blank3 = new TextView(PaymentManagement.this);
						blank3.setText("");
						
					
						Button cancelButton = new Button(PaymentManagement.this);
						cancelButton.setText("取消");
						cancelButton.setTextSize(10);
			
						cancelButton.setOnClickListener(new OnClickListener(){

							public void onClick(View v) {
								salesBillTable.removeViews(1, salesBillTable.getChildCount()-1);//取消之前的所有销售项
							}
							
						});
						
						TextView blank4 = new TextView(PaymentManagement.this);
						blank4.setText("");
						TableRow lastRow = new TableRow(PaymentManagement.this);
						lastRow.addView(blank1);
						lastRow.addView(blank2);
						lastRow.addView(blank3);
						lastRow.addView(saveButton);
						lastRow.addView(cancelButton);
						lastRow.addView(blank4);
						salesBillTable.addView(lastRow);
					}
				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 0, 0, "我是会员");

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 0:

			verifyVIPNum();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void verifyVIPNum() {
		AlertDialog.Builder VIPIDDialog = new AlertDialog.Builder(this);
		VIPIDDialog.setTitle("输入会员号");
		final EditText VIPNumEText = new EditText(this);
		VIPNumEText.setWidth(100);
		VIPNumEText.setSingleLine(true);
		VIPIDDialog.setView(VIPNumEText);
		VIPIDDialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						vipInfoDAO = new VIPInfoDAO(PaymentManagement.this);
						
						VIPId = vipInfoDAO.getVIPIdByVIPNum(VIPNumEText.getText()
								.toString(), vipInfoDAO.getReadableDatabase());
						if(VIPId == -1){
							AlertDialog.Builder verifyVIPTip = new AlertDialog.Builder(PaymentManagement.this);
							verifyVIPTip.setTitle("提示");
							verifyVIPTip.setMessage("不存在该VIP客户");
							verifyVIPTip.setPositiveButton("确定", new DialogInterface.OnClickListener(){

								public void onClick(DialogInterface dialog,
										int which) {
									verifyVIPNum();//重新显示输入框
								}
								
							});
							verifyVIPTip.show();
							
						}

					}

				});
		VIPIDDialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}

				});
		VIPIDDialog.show();

	}

	@Override
	protected void onPause() {
		super.onPause();

		loginer = new Loginer(PaymentManagement.this);
		if (loginer.logout(userName, loginer.getWritableDatabase()))// 登出
		{
			Intent intent = new Intent(PaymentManagement.this, Main.class);
			PaymentManagement.this.startActivity(intent);
		}
	}

}
