package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.List;

import com.tobacco.R;
import com.tobacco.pos.contentProvider.GoodsCPer;
import com.tobacco.pos.contentProvider.GoodsPriceCPer;
import com.tobacco.pos.contentProvider.PurchaseBillCPer;
import com.tobacco.pos.contentProvider.PurchaseItemCPer;
import com.tobacco.pos.contentProvider.SalesItemCPer;
import com.tobacco.pos.contentProvider.VIPInfoCPer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ReportManagement extends Activity {

	private PurchaseBillCPer pBillCPer = null;
	private PurchaseItemCPer pItemCPer = null;
	private GoodsPriceCPer gPriceCPer = null;
	private GoodsCPer gCPer = null;
	private SalesItemCPer sItemCPer = null;
	private VIPInfoCPer vipInfoCPer = null;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        pBillCPer = new PurchaseBillCPer();
	        pItemCPer = new PurchaseItemCPer();
	        gPriceCPer = new GoodsPriceCPer();
	        gCPer = new GoodsCPer();
	        sItemCPer = new SalesItemCPer();
	        
	        int reportKind = getIntent().getIntExtra("reportKind", 0);
	        if(reportKind == 0){//选择的类型如果是进货报表
	        	setContentView(R.layout.purchasereport);
	        	
	        	final Button startTimeButton = (Button)this.findViewById(R.id.startTimeButton);
	        	final TableLayout purchaseReportTable = (TableLayout)this.findViewById(R.id.purchaseReportTable);
	        
	        	startTimeButton.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						final DatePicker startTimePicker = new DatePicker(ReportManagement.this);
						startTimePicker.setVerticalScrollBarEnabled(true);
						AlertDialog.Builder startTimeDialog = new AlertDialog.Builder(ReportManagement.this);
						startTimeDialog.setTitle("开始时间");
						startTimeDialog.setView(startTimePicker);
						startTimeDialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){

							public void onClick(DialogInterface dialog,
									int which) {
								int day = startTimePicker.getDayOfMonth();
								String dayStr = "";
								if(day<10)
									dayStr = "0" + day;
								else
									dayStr = "" + day;
								int month = startTimePicker.getMonth()+1;
								String monthStr = "";
								if(month<10)
									monthStr = "0" + month;
								else
									monthStr = "" + month;
								int year = startTimePicker.getYear();
								startTimeButton.setText(year+"-"+monthStr+"-"+dayStr);
							}
							
						});
						startTimeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog,
									int which) {
								
							}
						});
						startTimeDialog.show();
					}
	        		
	        	});
	        	
	        	final Button endTimeButton = (Button)this.findViewById(R.id.endTimeButton);
	        	endTimeButton.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						final DatePicker endTimePicker = new DatePicker(ReportManagement.this);
						AlertDialog.Builder endTimeDialog = new AlertDialog.Builder(ReportManagement.this);
						endTimeDialog.setTitle("结束时间");
						endTimeDialog.setView(endTimePicker);
						endTimeDialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){

							public void onClick(DialogInterface dialog,
									int which) {
								int day = endTimePicker.getDayOfMonth();
								String dayStr = "";
								if(day<10)
									dayStr = "0" + day;
								else
									dayStr = "" + day;
								int month = endTimePicker.getMonth()+1;
								String monthStr = "";
								if(month<10)
									monthStr = "0" + month;
								else
									monthStr = "" + month;
								int year = endTimePicker.getYear();
								endTimeButton.setText(year+"-"+monthStr+"-"+dayStr);
							}
							
						});
						endTimeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog,
									int which) {
								
							}
						});
						endTimeDialog.show();
					}
	        		
	        	});
	        	
	        	final Spinner conditionSpinner = (Spinner)this.findViewById(R.id.conditionSpinner);
	        	String[] conditionStr = new String[]{"进货单号", "商品名称", "商品种类"};
	        	ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, conditionStr);
	        	conditionSpinner.setAdapter(conditionAdapter);
	        	
	        	EditText contentEText = (EditText)this.findViewById(R.id.contentEText);
	        	contentEText.setOnKeyListener(new OnKeyListener(){

					public boolean onKey(View v, int keyCode, KeyEvent event) {
						String content = ((TextView)v).getText().toString();
						if(keyCode == 66 && content!=null && content.length()>0){
							if(conditionSpinner.getSelectedItemId() == 0){//以进货单号的条件进行查询
								int searchPBillId = pBillCPer.getPBillIdByTimeAndPBillNum(startTimeButton.getText().toString(), endTimeButton.getText().toString(), content);
													
								List<Integer> allPriceId = new ArrayList<Integer>();//根据进货单Id查找该单中所有的priceId
								List<Integer> countList = new ArrayList<Integer>();//根据进货单Id查找该单中所有的数量
								allPriceId = pItemCPer.getAllPriceIdByPBillId(searchPBillId);
								countList = pItemCPer.getAllPCountByPBillId(searchPBillId);
								
								if(allPriceId == null || allPriceId.size() == 0)
									Toast.makeText(ReportManagement.this, "根据条件没查找到进货项。", Toast.LENGTH_SHORT).show();
								else{
									for(int i=0;i<allPriceId.size();i++){
										TableRow theResultRow = new TableRow(ReportManagement.this);
									
										TextView pBillNumTView = new TextView(ReportManagement.this);
										pBillNumTView.setText("" + content);
										theResultRow.addView(pBillNumTView);
									
										List<String> info = gPriceCPer.getInfoByGPriceId(allPriceId.get(i));
										for(int j=0;j<info.size();j++){
											if(j==2){
												TextView countTView = new TextView(ReportManagement.this);
												countTView.setText(countList.get(i)+"");
												theResultRow.addView(countTView);
											}
											
											TextView infoTView = new TextView(ReportManagement.this);
											infoTView.setText("" + info.get(j));
											theResultRow.addView(infoTView);
										}
									purchaseReportTable.addView(theResultRow);
									}
								}
							}
							else if(conditionSpinner.getSelectedItemId() == 1){//以商品名称进行模糊搜索
								purchaseReportTable.removeViews(1, purchaseReportTable.getChildCount()-1);
								ArrayList<ArrayList<String>> pInfo = gCPer.getPInfoByGoodsName(startTimeButton.getText().toString(), endTimeButton.getText().toString(), content);
								if(pInfo == null || pInfo.size() == 0)
									Toast.makeText(ReportManagement.this, "根据条件没查找到进货项。", Toast.LENGTH_SHORT).show();
								else{
									for(int i=0;i<pInfo.size();i++){
										ArrayList<String> temp = pInfo.get(i);
										TableRow theResultRow = new TableRow(ReportManagement.this);
										for(int j=0;j<temp.size();j++){
											TextView tView = new TextView(ReportManagement.this);
											tView.setText(temp.get(j));
											theResultRow.addView(tView);
										}
										purchaseReportTable.addView(theResultRow);
									}
								}
							}
							else if(conditionSpinner.getSelectedItemId() == 2){//根据商品的种类查找，要支持模糊搜索
								purchaseReportTable.removeViews(1, purchaseReportTable.getChildCount()-1);
								ArrayList<ArrayList<String>> pInfo = gCPer.getPInfoByGoodsKindName(startTimeButton.getText().toString(), endTimeButton.getText().toString(), content);
								
								if(pInfo == null || pInfo.size() == 0)
									Toast.makeText(ReportManagement.this, "根据条件没查找到进货项。", Toast.LENGTH_SHORT).show();
								else{
									for(int i=0;i<pInfo.size();i++){
										ArrayList<String> temp = pInfo.get(i);
										TableRow theResultRow = new TableRow(ReportManagement.this);
										for(int j=0;j<temp.size();j++){
											TextView tView = new TextView(ReportManagement.this);
											tView.setText(temp.get(j));
											theResultRow.addView(tView);
										}
										purchaseReportTable.addView(theResultRow);
									}
								}
							}
							
							((TextView)v).setText("");
						}
						
						return false;
					}
	        		
	        	});
	        }
	        	
	        else if(reportKind == 1){//选择的是销售报表
	        	setContentView(R.layout.salesreport);
	        	
	        	final Button salesStartTimeButton = (Button)this.findViewById(R.id.salesBillStartTimeButton);
	        	salesStartTimeButton.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						final DatePicker startTimePicker = new DatePicker(ReportManagement.this);
						startTimePicker.setVerticalScrollBarEnabled(true);
						AlertDialog.Builder startTimeDialog = new AlertDialog.Builder(ReportManagement.this);
						startTimeDialog.setTitle("开始时间");
						startTimeDialog.setView(startTimePicker);
						startTimeDialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){

							public void onClick(DialogInterface dialog,
									int which) {
								int day = startTimePicker.getDayOfMonth();
								String dayStr = "";
								if(day<10)
									dayStr = "0" + day;
								else
									dayStr = "" + day;
								int month = startTimePicker.getMonth()+1;
								String monthStr = "";
								if(month<10)
									monthStr = "0" + month;
								else
									monthStr = "" + month;
								int year = startTimePicker.getYear();
								salesStartTimeButton.setText(year+"-"+monthStr+"-"+dayStr);
							}
							
						});
						startTimeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog,
									int which) {
								
							}
						});
						startTimeDialog.show();
					}
	        		
					
	        	});
	        	final Button salesEndTimeButton = (Button)this.findViewById(R.id.salesBillEndTimeButton);
	        	salesEndTimeButton.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						final DatePicker endTimePicker = new DatePicker(ReportManagement.this);
						AlertDialog.Builder endTimeDialog = new AlertDialog.Builder(ReportManagement.this);
						endTimeDialog.setTitle("结束时间");
						endTimeDialog.setView(endTimePicker);
						endTimeDialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){

							public void onClick(DialogInterface dialog,
									int which) {
								int day = endTimePicker.getDayOfMonth();
								String dayStr = "";
								if(day<10)
									dayStr = "0" + day;
								else
									dayStr = "" + day;
								int month = endTimePicker.getMonth()+1;
								String monthStr = "";
								if(month<10)
									monthStr = "0" + month;
								else
									monthStr = "" + month;
								int year = endTimePicker.getYear();
								salesEndTimeButton.setText(year+"-"+monthStr+"-"+dayStr);
							}
							
						});
						endTimeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog,
									int which) {
								
							}
						});
						endTimeDialog.show();
					}
					
	        	});
	        	
	        	final Spinner salesConditionSpinner = (Spinner)this.findViewById(R.id.salesConditionSpinner);
	        	String[] conditionStr = new String[]{"销售单号", "商品名称", "商品种类", "会员编号"};
	        	ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, conditionStr);
	        	salesConditionSpinner.setAdapter(conditionAdapter);
	        	
	        	final EditText salesContentTView = (EditText)this.findViewById(R.id.salesContentEText);
	        	
	        	salesContentTView.setOnKeyListener(new OnKeyListener(){

					public boolean onKey(View v, int keyCode, KeyEvent event) {
						String content = salesContentTView.getText().toString();
						if(keyCode == 66 && content!=null && content.length()>0){
							if(salesConditionSpinner.getSelectedItemId() == 0){//根据销售单号查找，准确查找
								
							}
							else if(salesConditionSpinner.getSelectedItemId() == 1){//根据销售商品的名称查询，模糊搜索
								
							}
							else if(salesConditionSpinner.getSelectedItemId() == 2){//根据销售商品的种类查询
								
							}
							else if(salesConditionSpinner.getSelectedItemId() == 3){//根据客户编号，VIPNum
								vipInfoCPer = new VIPInfoCPer();
								int VIPId = vipInfoCPer.getVIPIdByVIPNum(((TextView)v).getText().toString());
								if(VIPId == -1){
									Toast.makeText(ReportManagement.this, "抱歉，没有该VIP客户", Toast.LENGTH_SHORT).show();
								}
								else{
									Toast.makeText(ReportManagement.this, vipInfoCPer.getVIPNameByVIPId(VIPId), Toast.LENGTH_SHORT).show();
									sItemCPer.getSalesInfoByVIPNum(salesStartTimeButton.getText().toString(), salesEndTimeButton.getText().toString(), VIPId);
								}
								
							}
							((TextView)v).setText("");
						}
						return false;
					}
	        		
	        	});
	        	
	        }
	        else if(reportKind == 2){
	        	setContentView(R.layout.inventoryreport);
	        }
	        

	    }
}
