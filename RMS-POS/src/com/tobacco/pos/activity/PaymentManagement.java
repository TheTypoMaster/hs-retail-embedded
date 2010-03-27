package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
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
	
	private List<String> barcodeList = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paymentmanagement);
		
		gPriceCPer = new GoodsPriceCPer();
		gCPer = new GoodsCPer();
		unitCPer = new UnitCPer();
		userInfoCPer = new UserInfoCPer();
		sBillCPer = new SalesBillCPer();
		sItemCPer = new SalesItemCPer();
		vipInfoCPer = new VIPInfoCPer();
		

		userName = this.getIntent().getStringExtra("userName");

		paymentWelcome = (TextView) this.findViewById(R.id.paymentWelcome);
		paymentWelcome.setText("你好:" + userName);

		
		barcodeEText = (EditText) this.findViewById(R.id.barcodeEText);
		sGoodsNumEText = (EditText) this.findViewById(R.id.sGoodsNumEText);
		
		sGoodsNumEText.setOnKeyListener(new OnKeyListener(){

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == 66){//点击确定按钮
					String barcode = barcodeEText.getText().toString();
					String countStr = sGoodsNumEText.getText().toString();
					
					if(barcode.length()>0){//如果有输入条形码
						if(countStr == null || countStr.equals(""))
							;
						else{//如果有输入数量
							try{
								int count = Integer.parseInt(countStr);
								if(count>0){//一切正常
									boolean isExitingSameGoods = false;//购物单里是否有相同单位的相同商品，如果查找直接改变数量即可
									int goodsId = gPriceCPer.getGoodsIdByBarcode(barcode);
									int unitId = gPriceCPer.getUnitIdByBarcode(barcode);

									if(!barcodeList.contains(barcode))
										barcodeList.add(barcode);
									
									String goodsName = gCPer.getGoodsNameByGoodsId(goodsId);
									String unitName = unitCPer.getUnitNameById(unitId);
									
									
									salesBillTable = (TableLayout) PaymentManagement.this
											.findViewById(R.id.salesBillTable);
									
									for(int i=1;i<salesBillTable.getChildCount()-1;i++){//同样单位同样的商品已经在购物单中
										TableRow compareRow = (TableRow) salesBillTable.getChildAt(i);
										TextView compareNameTView = (TextView) compareRow.getChildAt(0);
										TextView compareUnitTView = (TextView) compareRow.getChildAt(2);
										
										if(compareNameTView.getText().toString().equals(goodsName) 
												&& compareUnitTView.getText().toString().equals(unitName)){
											TextView cTView = (TextView)compareRow.getChildAt(1);
											TextView tMoneyTView = (TextView)compareRow.getChildAt(4);
											TextView oPTView = (TextView)compareRow.getChildAt(3);
											
											int originalCount = Integer.parseInt(cTView.getText().toString());
											cTView.setText(""+(originalCount+count));
											
											tMoneyTView.setText(""+Integer.parseInt(cTView.getText().toString()) * Double.parseDouble(oPTView.getText().toString()));
											
											isExitingSameGoods = true;
											break;
											
										}
										
									}
									if(!isExitingSameGoods){//不存在相同单位的相同商品，添加一行
										final TableRow r = new TableRow(PaymentManagement.this);
									
										TextView goodsNameTView = new TextView(PaymentManagement.this);

										goodsNameTView.setText(goodsName);
										r.addView(goodsNameTView);
									
										TextView countTView = new TextView(PaymentManagement.this);
										countTView.setText(""+count);
										r.addView(countTView);
									
										TextView unitTView = new TextView(PaymentManagement.this);
										unitTView.setText(unitName);
										r.addView(unitTView);
									
										TextView priceTView = new TextView(PaymentManagement.this);
										priceTView.setText(""+gPriceCPer.getOutPriceByBarcode(barcode));
										r.addView(priceTView);	
									
										TextView totalMoneyTView = new TextView(PaymentManagement.this);
										totalMoneyTView.setText(Double.parseDouble(priceTView.getText().toString())*count + "");
										r.addView(totalMoneyTView);
									
										ImageView i = new ImageView(PaymentManagement.this);
										i.setImageResource(R.drawable.delete);
										i.setOnClickListener(new OnClickListener(){

											public void onClick(View v) {
												barcodeList.remove(((TableLayout)v.getParent().getParent()).indexOfChild(r)-1);
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
										
										Button saveButton = new Button(PaymentManagement.this);
										saveButton.setText("确定");
								
										saveButton.setTextSize(10);
										saveButton.setOnClickListener(new OnClickListener(){

											public void onClick(View v) {					
												//////////////////////////////////////////会员信息输入
												AlertDialog.Builder VIPIDDialog = new AlertDialog.Builder(PaymentManagement.this);
												VIPIDDialog.setTitle("是否会员?");
												final EditText VIPNumEText = new EditText(PaymentManagement.this);
												VIPNumEText.setWidth(100);
												VIPNumEText.setSingleLine(true);
												VIPIDDialog.setView(VIPNumEText);
												VIPIDDialog.setPositiveButton("确定",
														new DialogInterface.OnClickListener() {

															public void onClick(DialogInterface dialog, int which) {
																int newSBillId = -1;
																
																String VIPNum = VIPNumEText.getText().toString();
																if(VIPNum.length() != 0){
																	VIPId = vipInfoCPer.getVIPIdByVIPNum(VIPNumEText.getText().toString());
																	if(VIPId == -1)
																		Toast.makeText(PaymentManagement.this, "没有该会员", Toast.LENGTH_SHORT).show();
																	else{
																	
																		int userId = userInfoCPer.getUserIdByUserName(userName);
																
																		newSBillId = sBillCPer.addSBill(userId, (new Date()).toLocaleString(), VIPId);//得到最后新加的销售单的ID
																	
																		for(int i=1;i<salesBillTable.getChildCount()-1;i++){//存储最后的销售项
																			String theBarcode = barcodeList.get(i-1);
																			int goodsCount = Integer.parseInt(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(1)).getText().toString());
																	
																			double inPrice = gPriceCPer.getInPriceByBarcode(theBarcode);
																			double outPrice = gPriceCPer.getOutPriceByBarcode(theBarcode);
																			sItemCPer.addSalesItem(newSBillId, goodsCount, theBarcode, inPrice, outPrice);
																		}
																		
																		
																		String VIPName = vipInfoCPer.getVIPNameByVIPId(VIPId);
																		VIPId = -1;//完成一单后要将VIPId重新设置为-1，要不还会是上次那个VIP客户的。
																		Toast.makeText(PaymentManagement.this, "已为VIP客户:" + VIPName + " 创建销售单:" + sBillCPer.getSBillNumBySBillId(newSBillId), Toast.LENGTH_SHORT).show();
																		salesBillTable.removeViews(1, salesBillTable.getChildCount()-1);//删除所有的销售项
																		barcodeList.clear();
																	}
																}
																
																
																else
																{
																	int userId = userInfoCPer.getUserIdByUserName(userName);
																	
																	newSBillId = sBillCPer.addSBill(userId, (new Date()).toLocaleString(), -1);//得到最后新加的销售单的ID
																
																	for(int i=1;i<salesBillTable.getChildCount()-1;i++){//存储最后的销售项
																		String theBarcode = barcodeList.get(i-1);
																		int goodsCount = Integer.parseInt(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(1)).getText().toString());
																
																		double inPrice = gPriceCPer.getInPriceByBarcode(theBarcode);
																		double outPrice = gPriceCPer.getOutPriceByBarcode(theBarcode);
																		sItemCPer.addSalesItem(newSBillId, goodsCount, theBarcode, inPrice, outPrice);
																	}
																	Toast.makeText(PaymentManagement.this, "已为该普通客户创建销售单:" + sBillCPer.getSBillNumBySBillId(newSBillId), Toast.LENGTH_SHORT).show();
																	
																	salesBillTable.removeViews(1, salesBillTable.getChildCount()-1);//删除所有的销售项
																	barcodeList.clear();
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
												barcodeList.clear();
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
									
								
									barcodeEText.setText("");
									sGoodsNumEText.setText("");
								}else{
									Toast.makeText(PaymentManagement.this, "不合实际的数量", Toast.LENGTH_SHORT).show();
									sGoodsNumEText.setText("");
								}
							}
							catch(Exception e){
								Toast.makeText(PaymentManagement.this, "不正确的数量格式", Toast.LENGTH_SHORT).show();
								sGoodsNumEText.setText("");
							}
						}
					}
				}
				return false;
			}
			
		});
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
