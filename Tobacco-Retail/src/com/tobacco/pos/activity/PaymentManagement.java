package com.tobacco.pos.activity;

import java.util.Date;

import com.tobacco.pos.contentProvider.GoodsCPer;
import com.tobacco.pos.contentProvider.GoodsPriceCPer;
import com.tobacco.pos.contentProvider.Loginer;
import com.tobacco.pos.contentProvider.SalesBillCPer;
import com.tobacco.pos.contentProvider.SalesItemCPer;
import com.tobacco.pos.contentProvider.UnitCPer;
import com.tobacco.pos.contentProvider.UserInfoCPer;
import com.tobacco.pos.contentProvider.VIPInfoCPer;
import com.tobacco.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentManagement extends Activity {
	
	private Loginer loginer = null;
	private GoodsPriceCPer gPriceCPer = null;
	private GoodsCPer gCPer = null;
	private UnitCPer unitCPer = null;
	private UserInfoCPer userInfoCPer = null;
	private SalesBillCPer sBillCPer = null;
	private SalesItemCPer sItemCPer = null;
	private VIPInfoCPer vipInfoCPer = null;
	
	private TextView paymentWelcome;
	
	private EditText barcodeEText;
	private EditText sGoodsNumEText;
	private TableLayout salesBillTable;
	
	private String userName = "";
	private int VIPId = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paymentmanagement);
		
		gPriceCPer = new GoodsPriceCPer();
		gCPer = new GoodsCPer();

		userName = this.getIntent().getStringExtra("userName");

		paymentWelcome = (TextView) this.findViewById(R.id.paymentWelcome);
		paymentWelcome.setText("你好:" + userName);

		
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

						int goodsId = gPriceCPer.getGoodsIdByBarcode(barcode);
						int unitId = gPriceCPer.getUnitIdByBarcode(barcode);

						salesBillTable = (TableLayout) PaymentManagement.this
								.findViewById(R.id.salesBillTable);
						final TableRow r = new TableRow(PaymentManagement.this);
						TextView goodsNameTView = new TextView(PaymentManagement.this);

						goodsNameTView.setText(gCPer.getGoodsNameByGoodsId(goodsId));
						r.addView(goodsNameTView);
						
						TextView countTView = new TextView(PaymentManagement.this);
						countTView.setText(""+count);
						r.addView(countTView);
						
						TextView unitTView = new TextView(PaymentManagement.this);
						unitTView.setText(unitCPer.getUnitNameById(unitId));
						r.addView(unitTView);
						
						TextView priceTView = new TextView(PaymentManagement.this);
						priceTView.setText(""+gPriceCPer.getOutPriceByBarcode(barcode));
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
								userInfoCPer = new UserInfoCPer();
								sBillCPer = new SalesBillCPer();
								int userId = userInfoCPer.getUserIdByUserName(userName);
								
								int newSBillId = sBillCPer.addSBill(userId, (new Date()).toLocaleString(), VIPId);//得到最后新加的销售单的ID
								sItemCPer = new SalesItemCPer();
								gPriceCPer = new GoodsPriceCPer();
								for(int i=1;i<salesBillTable.getChildCount()-1;i++){//存储最后的销售项
									String theBarcode = ((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(4)).getText().toString();
									int goodsCount = Integer.parseInt(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(1)).getText().toString());
									
									int sPriceId = gPriceCPer.getGoodsPriceIdByBarcode(theBarcode);
									sItemCPer.addSalesItem(newSBillId, goodsCount, sPriceId);
								}
								
								if(VIPId == -1)
								{
									Toast.makeText(PaymentManagement.this, "已为该普通客户创建销售单:" + sBillCPer.getSBillNumBySBillId(newSBillId), Toast.LENGTH_SHORT).show();
								}
								else{
									vipInfoCPer = new VIPInfoCPer();
									
									String VIPName = vipInfoCPer.getVIPNameByVIPId(VIPId);
									VIPId = -1;//完成一单后要将VIPId重新设置为-1，要不还会是上次那个VIP客户的。
									Toast.makeText(PaymentManagement.this, "已为VIP客户:" + VIPName + " 创建销售单:" + sBillCPer.getSBillNumBySBillId(newSBillId), Toast.LENGTH_SHORT).show();
									
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
						vipInfoCPer = new VIPInfoCPer();
						
						VIPId = vipInfoCPer.getVIPIdByVIPNum(VIPNumEText.getText()
								.toString());
						
						if(VIPId == -1){

							Toast.makeText(PaymentManagement.this, "没有改会员", Toast.LENGTH_SHORT).show();
						
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
		if (loginer.logout(userName, loginer.getWritableDatabase()))
		{
			Intent intent = new Intent(PaymentManagement.this, Main.class);
			PaymentManagement.this.startActivity(intent);
		}
	}

}
