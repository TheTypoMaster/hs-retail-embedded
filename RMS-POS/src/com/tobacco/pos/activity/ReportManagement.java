package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.List;

import com.tobacco.R;
import com.tobacco.pos.contentProvider.GoodsPriceCPer;
import com.tobacco.pos.contentProvider.PurchaseBillCPer;
import com.tobacco.pos.contentProvider.PurchaseItemCPer;

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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ReportManagement extends Activity {

	private PurchaseBillCPer pBillCPer = null;
	private PurchaseItemCPer pItemCPer = null;
	private GoodsPriceCPer gPriceCPer = null;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        pBillCPer = new PurchaseBillCPer();
	        pItemCPer = new PurchaseItemCPer();
	        gPriceCPer = new GoodsPriceCPer();
	        
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
	        	String[] conditionStr = new String[]{"进货单号", "Text"};
	        	ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, conditionStr);
	        	conditionSpinner.setAdapter(conditionAdapter);
	        	
	        	TextView contentTView = (TextView)this.findViewById(R.id.contentTView);
	        	contentTView.setOnKeyListener(new OnKeyListener(){

					public boolean onKey(View v, int keyCode, KeyEvent event) {
						String content = ((TextView)v).getText().toString();
						if(keyCode == 66 && content!=null && content.length()>0){
							if(conditionSpinner.getSelectedItemId() == 0){//以进货单号的条件进行查询
								int searchPBillId = pBillCPer.getPBillIdByTimeAndPBillNum(startTimeButton.getText().toString(), endTimeButton.getText().toString(), content);
													
								List<Integer> allPriceId = new ArrayList<Integer>();//根据进货单Id查找该单中所有的priceId
								List<Integer> countList = new ArrayList<Integer>();//根据进货单Id查找该单中所有的数量
								allPriceId = pItemCPer.getAllPriceIdByPBillId(searchPBillId);
								countList = pItemCPer.getAllPCountByPBillId(searchPBillId);
								
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
							else if(conditionSpinner.getSelectedItemId() == 1){
								Log.d("lyq", "Select the second");
							}
							
							((TextView)v).setText("");
						}
						
						return false;
					}
	        		
	        	});
	        }
	        	
	        else if(reportKind == 1){
	        	setContentView(R.layout.salesreport);
	        }
	        else if(reportKind == 2){
	        	setContentView(R.layout.inventoryreport);
	        }
	        

	    }
}
