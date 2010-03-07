package com.tobacco.pos.activity;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.tobacco.pos.dao.GoodsDAO;
import com.tobacco.pos.dao.GoodsPriceDAO;
import com.tobacco.pos.dao.KindDAO;
import com.tobacco.pos.dao.PurchaseBillDAO;
import com.tobacco.pos.dao.PurchaseItemDAO;
import com.tobacco.pos.dao.UnitDAO;
import com.tobacco.pos.util.Loginer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PurchaseManagement extends Activity {
	private Loginer loginer = null;
	private GoodsDAO gDAO= null;
	private UnitDAO uDAO = null;
	private KindDAO kDAO = null;
	private GoodsPriceDAO gPriceDAO = null;
	private PurchaseBillDAO pBillDAO = null;
	private PurchaseItemDAO pItemDAO = null;
	
	private Button purchasereturn;
	private TextView purchaseWelcome;

	private RadioButton rButton1;// �½��������İ�ť
	private RadioButton rButton2;// ���������м�����Ʒ�İ�ť

	private LinearLayout lay;
	private LinearLayout lay1;
	private TextView pBillId;// ���������
	private TextView pBillTime;// ��ӽ�������ʱ��
	private EditText pBillComment;// ��ӽ�����ʱ�ı�ע

	private LinearLayout lay2;

	private Button savePBill;

	private String maxPBillNum;// Ҫ����Ľ������Ľ�����
	private Date theDate;// Ҫ����Ľ�������ʱ��
	
	private Spinner wholePBill;//������������ʾ���еĽ��������Ա���������Ʒ������ʱ�����ѡ��
	private Button addGoodsButton;//��ť������ð�ť���Կ�ʼ������Ʒ�Ļ�����Ϣ��������λ���۸�ȡ�
	private Button addGoodsIntoPBillButton;//��ť������ð�ť���Խ���������Ʒ������Ӧ�Ľ����������������ȡ�

	private String pBillCode = "";//�������Ĵ���
	private int selectedPBillId = 0;//��������ID
	private int selectedPriceId = 0;//ѡ��ļ۸��ID
	
	private String userName = "";//��½�û�������
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.purchasemanagement);
		
		userName = this.getIntent().getStringExtra("userName");

		purchaseWelcome = (TextView) this.findViewById(R.id.purchaseWelcome);
		purchaseWelcome.setText("��ã�" + userName);
		rButton1 = (RadioButton) this.findViewById(R.id.radio1);
		rButton2 = (RadioButton) this.findViewById(R.id.radio2);

		lay = (LinearLayout) this.findViewById(R.id.lay);
		lay1 = (LinearLayout) this.findViewById(R.id.lay1);
		pBillId = (TextView) this.findViewById(R.id.pBillId);
		pBillTime = (TextView) this.findViewById(R.id.pBillTime);
		lay2 = (LinearLayout) this.findViewById(R.id.lay2);

		rButton1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					pBillDAO = new PurchaseBillDAO(PurchaseManagement.this);
					maxPBillNum = "P"
							+ (Integer.parseInt(pBillDAO.getMaxPBillNum(
									pBillDAO.getReadableDatabase())
									.substring(1)) + 1);

					pBillId.setText(maxPBillNum);
					theDate = new Date();
					pBillTime.setText(theDate.toLocaleString());
					lay.removeView(lay2);
					lay.addView(lay1);

					pBillComment = (EditText) PurchaseManagement.this
							.findViewById(R.id.pBillComment);
					pBillComment.setText("");

					savePBill = (Button) PurchaseManagement.this
							.findViewById(R.id.savePBill);
					savePBill.setOnClickListener(new OnClickListener() {// "����"��ť�ļ����¼�

								public void onClick(View v) {
									boolean flag = pBillDAO.savePBill(
											maxPBillNum, 1, theDate
													.toLocaleString(),
											pBillComment.getText().toString(),
											pBillDAO.getWritableDatabase());
									AlertDialog.Builder addPBillTip = new AlertDialog.Builder(
											PurchaseManagement.this);
									addPBillTip.setTitle("��ʾ");

									if (flag)
										addPBillTip.setMessage("��Ӷ����ɹ�");

									else
										addPBillTip.setMessage("��Ӷ���ʧ��");

									addPBillTip
											.setPositiveButton(
													"ȷ��",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int which) {
														}
													});
									addPBillTip.show();
								}

							});

				}
			}

		});
		rButton2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					gDAO = new GoodsDAO(PurchaseManagement.this);
					uDAO = new UnitDAO(PurchaseManagement.this);
					kDAO = new KindDAO(PurchaseManagement.this);
					gPriceDAO = new GoodsPriceDAO(PurchaseManagement.this);
					pBillDAO = new PurchaseBillDAO(PurchaseManagement.this);
					pItemDAO = new PurchaseItemDAO(PurchaseManagement.this);
					
					lay.removeView(lay1);
					lay.addView(lay2);
					
					wholePBill = (Spinner)PurchaseManagement.this.findViewById(R.id.wholePBill);
					String allPBill [] = pBillDAO.getAllPBill(pBillDAO.getReadableDatabase());
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(PurchaseManagement.this,android.R.layout.simple_spinner_item,allPBill);
					wholePBill.setAdapter(adapter);
					
					wholePBill.setOnItemSelectedListener(new OnItemSelectedListener(){
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							String temp = ((TextView)arg1).getText()+"";
							pBillCode = temp.substring(0, temp.indexOf(":"));
							selectedPBillId = pBillDAO.getPBillIdByCode(pBillCode, pBillDAO.getReadableDatabase());
						
						}

						public void onNothingSelected(
								AdapterView<?> arg0) {
						}
					});
					
					addGoodsButton = (Button)PurchaseManagement.this.findViewById(R.id.addGoodsButton);
					addGoodsButton.setOnClickListener(new OnClickListener(){

						public void onClick(View v) {
						
							AlertDialog.Builder addGoodsDialog= new AlertDialog.Builder(PurchaseManagement.this);
							addGoodsDialog.setTitle("�����Ʒ");
							
						    TableLayout addGoodsTable = new TableLayout(PurchaseManagement.this);
		
							TableRow row1 = new TableRow(PurchaseManagement.this);
							TextView goodsNameTextView = new TextView(PurchaseManagement.this);
							goodsNameTextView.setText("��Ʒ����:");
							final EditText goodsNameEditText = new EditText(PurchaseManagement.this);
							goodsNameEditText.setWidth(200);
						
							row1.addView(goodsNameTextView,0);
							row1.addView(goodsNameEditText,1);
						 	
							TableRow row2 = new TableRow(PurchaseManagement.this);
							TextView manufacturerName = new TextView(PurchaseManagement.this);
							manufacturerName.setText("��������:");
							final Spinner allManufacturer = new Spinner(PurchaseManagement.this);
							String allManufacturerName [] = gDAO.getAllManufacturer(gDAO.getReadableDatabase());
							ArrayAdapter<String> allManufacturerAdapter = new ArrayAdapter<String>(PurchaseManagement.this,android.R.layout.simple_spinner_item,allManufacturerName);
							allManufacturer.setAdapter(allManufacturerAdapter);
							
							row2.addView(manufacturerName, 0);
							row2.addView(allManufacturer, 1);
							
							TableRow row3 = new TableRow(PurchaseManagement.this);
							TextView kind = new TextView(PurchaseManagement.this);
							kind.setText("����:");
							final Spinner allKind = new Spinner(PurchaseManagement.this);
							String allKindName[] = kDAO.getAllKind(kDAO.getReadableDatabase());
							ArrayAdapter<String> allKindAdapter = new ArrayAdapter<String>(PurchaseManagement.this,android.R.layout.simple_spinner_item,allKindName);
							allKind.setAdapter(allKindAdapter);
							
							row3.addView(kind, 0);
							row3.addView(allKind,1);
							
							TableRow row4 = new TableRow(PurchaseManagement.this);
							TextView addPrice = new TextView(PurchaseManagement.this);
							addPrice.setText("�۸�:");
							Button addPriceButton = new Button(PurchaseManagement.this);//����ð�ť��ʼ�����Ʒ�ļ۸�
							addPriceButton.setText("��Ӽ۸�");
							final Vector allPrice = new Vector();//һ����Ʒ�����м۸�
						
							addPriceButton.setOnClickListener(new OnClickListener(){

								public void onClick(View v) {
									final Vector singlePrice = new Vector();//һ����Ʒ��һ���۸���Щ�۸��еĵ�λ�ǲ�һ���ġ�
									
									AlertDialog.Builder addPriceDialog = new AlertDialog.Builder(PurchaseManagement.this);
									
									TableLayout priceLayout = new TableLayout(PurchaseManagement.this);
									
									TableRow pRow1 = new TableRow(PurchaseManagement.this);
									TextView unitNameTextView = new TextView(PurchaseManagement.this);
									unitNameTextView.setText("��λ:");
									final Spinner unitNameSpinner = new Spinner(PurchaseManagement.this);//���еĵ�λ����
									List<String> allUnitName = uDAO.getAllUnit(uDAO.getReadableDatabase());
									
									for(int i=0;i<allPrice.size();i++)//����allPrice�洢��һ����Ʒ�Ķ��ּ۸��ڳ�ʼ����ʱ����Ҫ��������۸�ĵ�λȥ��
									{
										Vector temp = new Vector();
										temp = (Vector) allPrice.get(i);
										
										if(allUnitName.contains(temp.get(0))){
											allUnitName.remove(temp.get(0));
										}
										
									}
									ArrayAdapter<String> allUnitAdapter = new ArrayAdapter<String>(PurchaseManagement.this,android.R.layout.simple_spinner_item,allUnitName);
									unitNameSpinner.setAdapter(allUnitAdapter);
									
									pRow1.addView(unitNameTextView, 0);
									pRow1.addView(unitNameSpinner, 1);
									priceLayout.addView(pRow1);
									
									TableRow pRow2 = new TableRow(PurchaseManagement.this);
									TextView barcodeTextView = new TextView(PurchaseManagement.this);
									barcodeTextView.setText("������:");
									final EditText barcodeEditText = new EditText(PurchaseManagement.this);
									barcodeEditText.setWidth(200);
									
									pRow2.addView(barcodeTextView, 0);
									pRow2.addView(barcodeEditText, 1);
									priceLayout.addView(pRow2);
									
									TableRow pRow3 = new TableRow(PurchaseManagement.this);
									TextView inPriceTextView = new TextView(PurchaseManagement.this);
									inPriceTextView.setText("������:");
									final EditText inPriceEditText = new EditText(PurchaseManagement.this);
									
									inPriceEditText.setWidth(200);
									
									pRow3.addView(inPriceTextView, 0);
									pRow3.addView(inPriceEditText, 1);
									priceLayout.addView(pRow3);
									
									TableRow pRow4 = new TableRow(PurchaseManagement.this);
									TextView outPriceTextView = new TextView(PurchaseManagement.this);
									outPriceTextView.setText("�ۼ�:");
									final EditText outPriceEditText = new EditText(PurchaseManagement.this);
									outPriceEditText.setWidth(200);
									
									pRow4.addView(outPriceTextView, 0);
									pRow4.addView(outPriceEditText, 1);
									priceLayout.addView(pRow4);
										
									final TextView addPriceInfo = new TextView(PurchaseManagement.this);
									addPriceInfo.setTextColor(Color.RED);
									priceLayout.addView(addPriceInfo);
									
									addPriceDialog.setTitle("�����Ʒ�۸�");
								
									inPriceEditText.setOnFocusChangeListener(new OnFocusChangeListener(){//�жϽ����۵ĸ�ʽ

										public void onFocusChange(View v,
												boolean hasFocus) {
											
											if(!hasFocus){
												String inPrice = ((EditText)v).getText().toString();
												if(inPrice.trim().length()==0 || inPrice.equals(null)){
													addPriceInfo.setText("�����������");
													inPriceEditText.setText("");
												}
												else {
													try{
														Double.valueOf(inPrice);
													}
													catch(Exception e){
														addPriceInfo.setText("�����۸�ʽ����");
														inPriceEditText.setText("");
													}
												}
											}
										}
										
									});
									
									outPriceEditText.setOnFocusChangeListener(new OnFocusChangeListener(){//�ж��ۼ۵ĸ�ʽ

										public void onFocusChange(View v,
												boolean hasFocus) {
											 
											if(!hasFocus){
												String outPrice = ((EditText)v).getText().toString();
												if(outPrice.trim().length()==0 || outPrice.equals(null)){
													addPriceInfo.setText("�������ۼ�");
													outPriceEditText.setText("");
												}
												else {
													try{
														Double.valueOf(outPrice);
													}
													catch(Exception e){
														addPriceInfo.setText("�ۼ۸�ʽ����");
														outPriceEditText.setText("");
													}
												}
											}
										}
										
									});
									addPriceDialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

										public void onClick(
												DialogInterface dialog,
												int which) {
											
											String inP = inPriceEditText.getText().toString();
											String outP = outPriceEditText.getText().toString();
											if(inP.trim().length() == 0 || inP.equals(null) || outP.trim().length() == 0 || outP.equals(null))
												;
											else{
												try{
													double inPrice = Double.valueOf(inP);//��Ʒ��ÿһ���۸���"��λ����","������","������","�ۼ�"���
													double outPrice = Double.valueOf(outP);
													singlePrice.add(unitNameSpinner.getSelectedItem().toString());
													singlePrice.add(barcodeEditText.getText().toString());
													singlePrice.add(inPriceEditText.getText().toString());
													singlePrice.add(outPriceEditText.getText().toString());
																										
													allPrice.add(singlePrice);
												}
												catch(Exception e){
													
												}
											}
							
										}
										
									});
									addPriceDialog.setNegativeButton("ȡ��",  new DialogInterface.OnClickListener(){

										public void onClick(
												DialogInterface dialog,
												int which) {
										}
										
									});
									addPriceDialog.setView(priceLayout);
									addPriceDialog.show();
								}
								
							});
							row4.addView(addPrice, 0);
							row4.addView(addPriceButton, 1);
							
							addGoodsTable.addView(row1);
							addGoodsTable.addView(row2);
							addGoodsTable.addView(row3);
							addGoodsTable.addView(row4);
							
							addGoodsDialog.setView(addGoodsTable);
							
							addGoodsDialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
								 
								public void onClick(DialogInterface dialog,
										int which) {
									String goodsName = goodsNameEditText.getText().toString();//��Ʒ����
									String mName = allManufacturer.getSelectedItem().toString();//��������
									String kindName = allKind.getSelectedItem().toString();//��������

									String addGoodsResult = gDAO.addGoods(goodsName, mName, kindName, allPrice, gDAO.getWritableDatabase());
									AlertDialog.Builder addGoodsTip = new AlertDialog.Builder(PurchaseManagement.this);
									addGoodsTip.setTitle("�����Ʒ��ʾ");
									addGoodsTip.setMessage(addGoodsResult);
									addGoodsTip.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

										public void onClick(
												DialogInterface dialog,
												int which) {
										
										}
										
									});
									addGoodsTip.show();
								}
								
							});
							addGoodsDialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){

								public void onClick(DialogInterface dialog,
										int which) {
									
								}
							 
								
							});
							addGoodsDialog.show();

						}
						
					});
					
					addGoodsIntoPBillButton = (Button)PurchaseManagement.this.findViewById(R.id.addGoodsIntoPBillButton);
					addGoodsIntoPBillButton.setOnClickListener(new OnClickListener(){

						public void onClick(View v) {
						
							AlertDialog.Builder addGoodsIntoPBillDialog = new AlertDialog.Builder(PurchaseManagement.this);
							addGoodsIntoPBillDialog.setTitle("���������м�����Ʒ");
							TableLayout addGoodsIntoPBillTable = new TableLayout(PurchaseManagement.this);
					
							TableRow row1 = new TableRow(PurchaseManagement.this);//ѡ����Ʒ����
							TextView tView1 = new TextView(PurchaseManagement.this);
							tView1.setText("��Ʒ:");
							
							Spinner goodsSpinner = new Spinner(PurchaseManagement.this);
							String [] allGoodsName = gDAO.getAllGoods(gDAO.getReadableDatabase());
							ArrayAdapter<String> allGoodsAdapter = new ArrayAdapter<String>(PurchaseManagement.this,android.R.layout.simple_spinner_item,allGoodsName);
							goodsSpinner.setAdapter(allGoodsAdapter);
							
							
							row1.addView(tView1);
							row1.addView(goodsSpinner);
							addGoodsIntoPBillTable.addView(row1);
							
							TableRow row2 = new TableRow(PurchaseManagement.this);//ѡ��۸����
							TextView tView2 = new TextView(PurchaseManagement.this);
							tView2.setText("�۸�:");
							final Spinner goodsPriceSpinner = new Spinner(PurchaseManagement.this);
							row2.addView(tView2);
							row2.addView(goodsPriceSpinner);
							addGoodsIntoPBillTable.addView(row2);
							
						
							goodsSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									String selectedGoods = ((TextView)arg1).getText()+"";
									String selectedGoodsCode = selectedGoods.substring(0,selectedGoods.indexOf(":"));
									List<String> allPrice = gPriceDAO.getPriceByGoodsCode(selectedGoodsCode, gPriceDAO.getReadableDatabase());
								
									ArrayAdapter<String> allPriceAdapter = new ArrayAdapter<String>(PurchaseManagement.this,android.R.layout.simple_spinner_item,allPrice);
									goodsPriceSpinner.setAdapter(allPriceAdapter);
									
								}

								public void onNothingSelected(
										AdapterView<?> arg0) {
								}
								
							});
							goodsPriceSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
								
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									String selectedPrice = ((TextView)arg1).getText()+"";
									selectedPriceId = Integer.parseInt(selectedPrice.substring(0, selectedPrice.indexOf(":")));
								
								}
								public void onNothingSelected(
										AdapterView<?> arg0) {
								
								}
							});
							
							TableRow row3 = new TableRow(PurchaseManagement.this);//������������
							TextView tView3 = new TextView(PurchaseManagement.this);
							tView3.setText("����:");
							final EditText countEText = new EditText(PurchaseManagement.this);
							row3.addView(tView3);
							row3.addView(countEText);
							addGoodsIntoPBillTable.addView(row3);
							
							addGoodsIntoPBillDialog.setView(addGoodsIntoPBillTable);
				
							addGoodsIntoPBillDialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

								public void onClick(DialogInterface dialog,
										int which) {
									
									try{//�����ĸ�ʽ��ȷ
										int theCount = Integer.parseInt(countEText.getText().toString());
										
//										Log.d("lyq", "priceId:"+selectedPriceId);
//										Log.d("lyq", "purchaseBillId:"+selectedPBillId);
//										Log.d("lyq", "pGoodsNum:"+theCount);
										
										boolean flag = pItemDAO.addPurchaseItem(selectedPBillId, theCount, selectedPriceId, pItemDAO.getWritableDatabase());
									
										if(flag){
											AlertDialog.Builder addPItemTip = new AlertDialog.Builder(PurchaseManagement.this);
											addPItemTip.setTitle("��ʾ");
											addPItemTip.setMessage("�ɹ����ӽ�����Ʒ");
											addPItemTip.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

												public void onClick(
														DialogInterface dialog,
														int which) {
												 
												}
											});
											addPItemTip.show();
										}
									}
									catch(Exception e){//��������ĸ�ʽ����ȷ
										AlertDialog.Builder countConvertTip = new AlertDialog.Builder(PurchaseManagement.this);
										countConvertTip.setTitle("��ʾ");
										countConvertTip.setMessage("������ʽ����ȷ");
										countConvertTip.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

											public void onClick(
													DialogInterface dialog,
													int which) {
											 
											}
										});
										countConvertTip.show();
									}
								}
								
							});
							addGoodsIntoPBillDialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog,
										int which) {
								
								}
							});
							addGoodsIntoPBillDialog.show();
						}
						
					});
				}
			}

		});

		purchasereturn = (Button) this.findViewById(R.id.purchasereturn);
		purchasereturn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				loginer = new Loginer(PurchaseManagement.this);
				if(loginer.logout(userName, loginer.getWritableDatabase()))// �ǳ�
				{
					Intent intent = new Intent(PurchaseManagement.this, Main.class);
					PurchaseManagement.this.startActivity(intent);
				}
			}

		});

		lay.removeView(lay1);
		lay.removeView(lay2);

	}

	@Override
	protected void onPause() {// �����ؼ���ʱ��Ҫ�ǳ�
		super.onPause();

		loginer = new Loginer(PurchaseManagement.this);
		if(loginer.logout(userName, loginer.getWritableDatabase()))// �ǳ�
		{
			Intent intent = new Intent(PurchaseManagement.this, Main.class);
			PurchaseManagement.this.startActivity(intent);
		}
	}

}
