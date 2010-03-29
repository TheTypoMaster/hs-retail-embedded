package com.tobacco.pos.activity;

import com.tobacco.R;

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
import android.widget.TextView;

public class ReportManagement extends Activity {

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        int reportKind = getIntent().getIntExtra("reportKind", 0);
	        if(reportKind == 0){//选择的类型如果是进货报表
	        	setContentView(R.layout.purchasereport);
	        	
	        	final Button startTimeButton = (Button)this.findViewById(R.id.startTimeButton);
	        
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
								int month = startTimePicker.getMonth()+1;
								int year = startTimePicker.getYear();
								startTimeButton.setText(year+"-"+month+"-"+day);
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
								int month = endTimePicker.getMonth()+1;
								int year = endTimePicker.getYear();
								endTimeButton.setText(year+"-"+month+"-"+day);
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
	        	
	        	Spinner conditionSpinner = (Spinner)this.findViewById(R.id.conditionSpinner);
	        	String[] conditionStr = new String[]{"进货单号"};
	        	ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, conditionStr);
	        	conditionSpinner.setAdapter(conditionAdapter);
	        	
	        	TextView contentTView = (TextView)this.findViewById(R.id.contentTView);
	        	contentTView.setOnKeyListener(new OnKeyListener(){

					public boolean onKey(View v, int keyCode, KeyEvent event) {
						String content = ((TextView)v).getText().toString();
						if(keyCode == 66 && content!=null && content.length()>0){
							Log.d("lyq", "........." + ((TextView)v).getText());
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
