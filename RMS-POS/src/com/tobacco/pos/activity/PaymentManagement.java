package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.List;

import com.tobacco.pos.contentProvider.GoodsCPer;
import com.tobacco.pos.contentProvider.GoodsKindCPer;
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
	private GoodsKindCPer gKindCPer = null;
	
	private TextView paymentWelcome;
	
	private EditText barcodeEText;
	private EditText sGoodsNumEText;
	private TableLayout salesBillTable;
	
	private String userName = "";
	private int vipId = -1;
	private String vipName = "";
	private double vipDiscountRate = 1;
	
	private int newSBillId = -1;//新增的销售单Id
	private double totalMoney = 0;	
	
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
									
									String goodsName = gCPer.getGoodsNameByGoodsId(goodsId) + "(" + unitCPer.getUnitNameById(unitId) + ")";
									
									
									salesBillTable = (TableLayout) PaymentManagement.this
											.findViewById(R.id.salesBillTable);
									
									for(int i=1;i<salesBillTable.getChildCount()-1;i++){//同样单位同样的商品已经在购物单中
										TableRow compareRow = (TableRow) salesBillTable.getChildAt(i);
										TextView compareNameTView = (TextView) compareRow.getChildAt(0);
										
										if(compareNameTView.getText().toString().equals(goodsName)){
											TextView cTView = (TextView)compareRow.getChildAt(1);
										 
											TextView tMoneyTView = (TextView)compareRow.getChildAt(3);
											 
											TextView oPTView = (TextView)compareRow.getChildAt(2);
											 
											
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
																
																
																String VIPNum = VIPNumEText.getText().toString();
																if(VIPNum.length() != 0){
//																	Intent getVIPInfoIntent = new Intent();
//																	getVIPInfoIntent.setAction("com.tobacco.custrel.activity.bm.VipDiscountManager.GET_VIP_DISCOUNT");
//																	getVIPInfoIntent.putExtra("vipNum", VIPNumEText.getText().toString());
//																	PaymentManagement.this.startActivityForResult(getVIPInfoIntent, 0);//此处调用回去VIP信息的方法。
																	
																	vipId = vipInfoCPer.getVIPIdByVIPNum(VIPNumEText.getText().toString());
																	vipDiscountRate = 0.8;
																	if(vipId == -1)
																		Toast.makeText(PaymentManagement.this, "没有该会员", Toast.LENGTH_SHORT).show();
																	else{
																		gKindCPer = new GoodsKindCPer();
																		final List<Integer> allCigaretteKindId = gKindCPer.getCigaretteKindId();
																		//处理销售单的增加以及打印
																	
																		final ArrayList<String> goodsNameList = new ArrayList<String>();//存储商品名字的List
																		final ArrayList<String> countList = new ArrayList<String>();//存储销量的List
																		final ArrayList<String> outPriceList = new ArrayList<String>();//存储单价的List
																		final ArrayList<String> tMoneyList = new ArrayList<String>();//单项总额的List
																		final ArrayList<String> flagList = new ArrayList<String>();//有否打折的标志，普通客户都为""
																	
																		for(int i=1;i<salesBillTable.getChildCount()-1;i++){//存储最后的销售项
																			String theBarcode = barcodeList.get(i-1);
																			
																			int kindId = gCPer.getGoodsKindIdByGoodsId(gPriceCPer.getGoodsIdByBarcode(theBarcode));//根据条形码找出商品的Id，再根据商品的Id查找其类别的Id
																			int goodsCount = Integer.parseInt(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(1)).getText().toString());
																
																		
																			double outPrice = gPriceCPer.getOutPriceByBarcode(theBarcode);
																			if(allCigaretteKindId.contains(kindId)){
																				totalMoney += goodsCount*outPrice*vipDiscountRate;
																			 
																				flagList.add("*");//如果是烟类有打折，用*标志
																				tMoneyList.add(goodsCount*outPrice*vipDiscountRate+"");
																			}
																			else{
																				totalMoney += goodsCount*outPrice;
																				 
																				flagList.add("");//没打折，不做标志
																				tMoneyList.add(goodsCount*outPrice+"");
																			}
																			
																			goodsNameList.add(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(0)).getText().toString());
																			countList.add(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(1)).getText().toString());
																			outPriceList.add(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(2)).getText().toString());
																		
																		}

																		AlertDialog.Builder payMoneyTip = new AlertDialog.Builder(PaymentManagement.this);
																		payMoneyTip.setTitle("付款");
																		payMoneyTip.setMessage("总额为:" + totalMoney);
																		final EditText payMoneyEText = new EditText(PaymentManagement.this);
																		
																		payMoneyEText.setWidth(120);
																		payMoneyTip.setView(payMoneyEText);
																		
																		payMoneyTip.setPositiveButton("确定", new DialogInterface.OnClickListener(){

																			public void onClick(
																					DialogInterface dialog,
																					int which) {
																				try{
																				int userId = userInfoCPer.getUserIdByUserName(userName);
																				double payMoney =  Double.parseDouble(payMoneyEText.getText().toString());
																				if(payMoney>totalMoney){
																					
																					newSBillId = sBillCPer.addSBill(userId, vipId, Double.parseDouble(payMoneyEText.getText().toString()));//得到最后新加的销售单的ID
																					
																					String sTime = sBillCPer.getSTimeBySBillId(newSBillId);//获得刚加入的销售单的时间
																					String newSBillNum = sBillCPer.getSBillNumBySBillId(newSBillId);//获取刚加入的销售单的编号
																					
																					for(int i=1;i<salesBillTable.getChildCount()-1;i++){//存储最后的销售项
																						String theBarcode = barcodeList.get(i-1);
																						
																						int kindId = gCPer.getGoodsKindIdByGoodsId(gPriceCPer.getGoodsIdByBarcode(theBarcode));//根据条形码找出商品的Id，再根据商品的Id查找其类别的Id
																						int goodsCount = Integer.parseInt(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(1)).getText().toString());
																			
																						double inPrice = gPriceCPer.getInPriceByBarcode(theBarcode);
																						double outPrice = Double.parseDouble(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(2)).getText().toString());
																						
																						if(allCigaretteKindId.contains(kindId)){
																						 
																							sItemCPer.addSalesItem(newSBillId, goodsCount, theBarcode, inPrice, outPrice, vipDiscountRate);//如果是烟类，则打折
																							 
																						}
																						else{
																							 
																							sItemCPer.addSalesItem(newSBillId, goodsCount, theBarcode, inPrice, outPrice, 1);//不是烟类，不打折
																						 
																						}
																					}
																				
																					vipName = vipInfoCPer.getVIPNameByVIPId(vipId);
																				
																					Toast.makeText(PaymentManagement.this, "已为VIP客户:" + vipName + " 创建销售单:" + sBillCPer.getSBillNumBySBillId(newSBillId), Toast.LENGTH_SHORT).show();
																					salesBillTable.removeViews(1, salesBillTable.getChildCount()-1);//删除所有的销售项
																					barcodeList.clear();
																				
																					vipId = -1;//完成一单后要将VIPId重新设置为-1，要不还会是上次那个VIP客户的。
																					vipDiscountRate = 1;//完成一单后腰将VIPDiscountRate设置为1，要不还会用上的那个折扣
																					vipName = "";
																				
																					//开始打印销售单
																					Intent printService = new Intent();
																					printService.setAction("com.tobacco.pos.service.PrintService");
																					printService.putExtra("sTime", sTime);//传入销售时间
																					printService.putExtra("userName", userName);//传入处理者名字
																					printService.putExtra("totalMoney", totalMoney);//传入总额
																					printService.putExtra("payMoney", payMoney);//传入付款																					
																					printService.putExtra("newSBillNum", newSBillNum);//传入销售单的编号
																					printService.putStringArrayListExtra("goodsNameList", goodsNameList);
																					printService.putStringArrayListExtra("countList", countList);
																					printService.putStringArrayListExtra("outPriceList", outPriceList);
																					printService.putStringArrayListExtra("tMoneyList", tMoneyList);
																					printService.putStringArrayListExtra("flagList", flagList);
																				
																					PaymentManagement.this.startService(printService);
																				
																					totalMoney = 0;
																				}
																				else{
																					Toast.makeText(PaymentManagement.this, "付款怎能比总额少呢！", Toast.LENGTH_SHORT).show();
																					totalMoney = 0;
																				}
																				
																			}catch(NumberFormatException e){
																				Toast.makeText(PaymentManagement.this, "数据格式不符合", Toast.LENGTH_SHORT).show();
																				totalMoney = 0;
																			}
																			}	
																		});
																	
																    	payMoneyTip.show();
																	}
																}
																
																
																else
																{
																	
																	for(int i=1;i<salesBillTable.getChildCount()-1;i++){
																		totalMoney += Double.parseDouble(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(3)).getText().toString());
																	}
																	AlertDialog.Builder payMoneyTip = new AlertDialog.Builder(PaymentManagement.this);
																	payMoneyTip.setTitle("付款");
																	payMoneyTip.setMessage("总额为:" + totalMoney);
																	final EditText payMoneyEText = new EditText(PaymentManagement.this);
																	payMoneyEText.setWidth(120);
																	payMoneyTip.setView(payMoneyEText);
																	payMoneyTip.setPositiveButton("确定", new DialogInterface.OnClickListener(){

																		public void onClick(
																				DialogInterface dialog,
																				int which) {
																		
																			try{
																				int userId = userInfoCPer.getUserIdByUserName(userName);
																				
																				double payMoney =  Double.parseDouble(payMoneyEText.getText().toString());
																				if(payMoney>=totalMoney){
																					//增加新销售单
																					newSBillId = sBillCPer.addSBill(userId, -1, Double.parseDouble(payMoneyEText.getText().toString()));//得到最后新加的销售单的ID
																					
																					//处理销售单的增加以及打印
																					String sTime = sBillCPer.getSTimeBySBillId(newSBillId);//获得刚加入的销售单的时间
																					String newSBillNum = sBillCPer.getSBillNumBySBillId(newSBillId);//获取刚加入的销售单的编号
																					ArrayList<String> goodsNameList = new ArrayList<String>();//存储商品名字的List
																					ArrayList<String> countList = new ArrayList<String>();//存储销量的List
																					ArrayList<String> outPriceList = new ArrayList<String>();//存储单价的List
																					ArrayList<String> tMoneyList = new ArrayList<String>();//单项总额的List
																					ArrayList<String> flagList = new ArrayList<String>();//有否打折的标志，普通客户都为""
																					for(int i=1;i<salesBillTable.getChildCount()-1;i++){//存储最后的销售项
																						
																						String theBarcode = barcodeList.get(i-1);
																						int goodsCount = Integer.parseInt(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(1)).getText().toString());
																				
																						double inPrice = gPriceCPer.getInPriceByBarcode(theBarcode);
																						double outPrice = Double.parseDouble(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(2)).getText().toString());
																						sItemCPer.addSalesItem(newSBillId, goodsCount, theBarcode, inPrice, outPrice, 1);
																						
																						goodsNameList.add(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(0)).getText().toString());
																						countList.add(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(1)).getText().toString());
																						outPriceList.add(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(2)).getText().toString());
																						tMoneyList.add(((TextView)((TableRow)salesBillTable.getChildAt(i)).getChildAt(3)).getText().toString());
																						flagList.add("");
																					}
																					Toast.makeText(PaymentManagement.this, "已为该普通客户创建销售单:" + sBillCPer.getSBillNumBySBillId(newSBillId), Toast.LENGTH_SHORT).show();
																					
																					salesBillTable.removeViews(1, salesBillTable.getChildCount()-1);//删除所有的销售项
																					barcodeList.clear();
																				
																					//开始打印销售单
																					Intent printService = new Intent();
																					printService.setAction("com.tobacco.pos.service.PrintService");
																					printService.putExtra("sTime", sTime);//传入销售时间
																					printService.putExtra("userName", userName);//传入处理者名字
																					printService.putExtra("totalMoney", totalMoney);//传入总额
																					printService.putExtra("payMoney", payMoney);//传入付款																					
																					printService.putExtra("newSBillNum", newSBillNum);//传入销售单的编号
																					printService.putStringArrayListExtra("goodsNameList", goodsNameList);
																					printService.putStringArrayListExtra("countList", countList);
																					printService.putStringArrayListExtra("outPriceList", outPriceList);
																					printService.putStringArrayListExtra("tMoneyList", tMoneyList);
																					printService.putStringArrayListExtra("flagList", flagList);
																					
																					PaymentManagement.this.startService(printService);
																					
																					totalMoney = 0;
																				}
																				else{
																					Toast.makeText(PaymentManagement.this, "付款怎能比总额少呢！", Toast.LENGTH_SHORT).show();
																					totalMoney = 0;
																				}

																			}catch(NumberFormatException e){
																				Toast.makeText(PaymentManagement.this, "数据格式不符合", Toast.LENGTH_SHORT).show();
																				totalMoney = 0;
																			}
																		}
																		
																	});
																	payMoneyTip.show();
																	
																
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
										
										Button cancelButton = new Button(PaymentManagement.this);
										cancelButton.setText("取消");
										cancelButton.setTextSize(10);
						
										cancelButton.setOnClickListener(new OnClickListener(){

											public void onClick(View v) {
												salesBillTable.removeViews(1, salesBillTable.getChildCount()-1);//取消之前的所有销售项
												barcodeList.clear();
											}
										
										});
									
										TextView blank3 = new TextView(PaymentManagement.this);
										blank3.setText("");
										TableRow lastRow = new TableRow(PaymentManagement.this);
										lastRow.addView(blank1);
										lastRow.addView(blank2);
										lastRow.addView(saveButton);
										lastRow.addView(cancelButton);
										lastRow.addView(blank3);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//startForResult返回后获取VIP信息
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0 && resultCode == RESULT_OK){//请求VIP信息
			vipId = data.getIntExtra("vipId", -1);
			vipName = data.getStringExtra("vipName");
			vipDiscountRate = data.getDoubleExtra("vipDiscountRate ", 0);
			
		}
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
