package com.tobacco.pos.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tobacco.R;
import com.tobacco.main.activity.view.RMSBaseView;
import com.tobacco.main.entities.globalconstant.BCodeConst;
import com.tobacco.pos.contentProvider.ConsumeCPer;
import com.tobacco.pos.contentProvider.GoodsCPer;
import com.tobacco.pos.contentProvider.GoodsPriceCPer;
import com.tobacco.pos.contentProvider.ManufacturerCPer;
import com.tobacco.pos.contentProvider.PurchaseBillCPer;
import com.tobacco.pos.contentProvider.PurchaseItemCPer;
import com.tobacco.pos.contentProvider.ReturnCPer;
import com.tobacco.pos.contentProvider.SalesItemCPer;
import com.tobacco.pos.contentProvider.UnitCPer;
import com.tobacco.pos.contentProvider.VIPInfoCPer;
import com.tobacco.pos.entity.InventoryItemObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ReportManagement extends RMSBaseView {

	private PurchaseBillCPer pBillCPer = null;
	private PurchaseItemCPer pItemCPer = null;
	private GoodsPriceCPer gPriceCPer = null;
	private GoodsCPer gCPer = null;
	private SalesItemCPer sItemCPer = null;
	private VIPInfoCPer vipInfoCPer = null;
	
	 private int firstItemIndex = 1;
	 private int totalItemCount = 0;
	 
	 private int currentPage = 1;
	 private int pageCount = 0;
	 private TableLayout inventoryReportTable = null;//库存表格
	 private ProgressDialog pD = null;
	 private int surplusCount = 0;//最后一页的数量
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
			this.setActivityPrivList(new int[]{BCodeConst.USER_PRIV_ADMIN});
			this.checkActivityPriv();
		
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
	        	startTimeButton.setOnLongClickListener(new OnLongClickListener(){

					public boolean onLongClick(View v) {
						((Button)v).setText("开始时间");
						return false;
					}
	        		
	        	});
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
	        	endTimeButton.setOnLongClickListener(new OnLongClickListener(){

					public boolean onLongClick(View v) {
						((Button)v).setText("结束时间");
						return false;
					}
	        		
	        	});
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
	        	
	        	final EditText contentEText = (EditText)this.findViewById(R.id.contentEText);
	        	Button pSearchButton = (Button) this.findViewById(R.id.pSearchButton);
	        
	        	pSearchButton.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
					
						
						final String content = contentEText.getText().toString();

						if(content!=null && content.length()>0 ){
							
							final Handler pSearchHandler = new Handler(){
								 public void handleMessage(Message msg) {
				        			   switch(msg.what) {
				        			   case 3:
				        					pD.dismiss();
							if(conditionSpinner.getSelectedItemId() == 0){//以进货单号的条件进行查询
								
								int searchPBillId = pBillCPer.getPBillIdByTimeAndPBillNum(startTimeButton.getText().toString(), endTimeButton.getText().toString(), content);
													
								List<Integer> allPriceId = new ArrayList<Integer>();//根据进货单Id查找该单中所有的priceId
								List<Integer> countList = new ArrayList<Integer>();//根据进货单Id查找该单中所有的数量
								allPriceId = pItemCPer.getAllPriceIdByPBillId(searchPBillId);
								countList = pItemCPer.getAllPCountByPBillId(searchPBillId);
								
								if(allPriceId == null || allPriceId.size() == 0)
								{
									purchaseReportTable.removeViews(1, purchaseReportTable.getChildCount()-1);
									Toast.makeText(ReportManagement.this, "根据条件没查找到进货项。", Toast.LENGTH_SHORT).show();
								}
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
								{
									purchaseReportTable.removeViews(1, purchaseReportTable.getChildCount()-1);
									Toast.makeText(ReportManagement.this, "根据条件没查找到进货项。", Toast.LENGTH_SHORT).show();
								}
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
								{
									purchaseReportTable.removeViews(1, purchaseReportTable.getChildCount()-1);
									Toast.makeText(ReportManagement.this, "根据条件没查找到进货项。", Toast.LENGTH_SHORT).show();
								}
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
				        	 }}
							};
							pD = ProgressDialog.show(ReportManagement.this, "请稍候...", "Loading...", true,  
				                    false);
							new Thread(){
								public void run() {
									try {
										sleep(1500);
									
									} catch (InterruptedException e) {
									 
									}finally{
										 
										pSearchHandler.sendEmptyMessage(3);
										 
									}
								}
							}.start();
						
						}
						
						 
					 
					}
	        		
	        	});
	        }
	        	
	        else if(reportKind == 1){//选择的是销售报表
	        	setContentView(R.layout.salesreport);
	        	
	        	final Button salesStartTimeButton = (Button)this.findViewById(R.id.salesBillStartTimeButton);
	        	final TableLayout salesReportTable = (TableLayout)this.findViewById(R.id.salesReportTable);
	        	salesStartTimeButton.setOnLongClickListener(new OnLongClickListener(){

					 public boolean onLongClick(View v) {
						((Button)v).setText("开始时间");
						return false;
					}
	        		
	        	});
	        	
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
	        	salesEndTimeButton.setOnLongClickListener(new OnLongClickListener(){

					public boolean onLongClick(View v) {
						((Button)v).setText("结束时间");
						return false;
					}
	        		
	        	});
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
	        	Button sSearchButton = (Button)this.findViewById(R.id.sSearchButton);
	        	
	        	sSearchButton.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						final String content = salesContentTView.getText().toString();
					
						if(content.length()>0){
							
							final Handler sSearchHandler = new Handler(){
								   public void handleMessage(Message msg) {
				        			   switch(msg.what) {
				        			   case 1:
				        pD.dismiss();
							if(salesConditionSpinner.getSelectedItemId() == 0){//根据销售单号查找，准确查找
								salesReportTable.removeViews(1, salesReportTable.getChildCount()-1);//清除上次的记录
								Map<String,ArrayList<ArrayList<String>>> sItemResultMap = sItemCPer.getSalesInfoBySalesBillNum(salesStartTimeButton.getText().toString(), salesEndTimeButton.getText().toString(), content);
								
								if(sItemResultMap.size() == 0)
								{
									salesReportTable.removeViews(1, salesReportTable.getChildCount()-1);
									Toast.makeText(ReportManagement.this, "根据条件没查找到销售信息。", Toast.LENGTH_SHORT).show();
								}
								else{
									String VIPName = sItemResultMap.keySet().iterator().next();
									Toast.makeText(ReportManagement.this, "该单客户为:" + VIPName, Toast.LENGTH_SHORT).show();
									ArrayList<ArrayList<String>> sItemResult = sItemResultMap.get(VIPName);
									for(int i=0;i<sItemResult.size();i++){
										TableRow resultRow = new TableRow(ReportManagement.this);
										ArrayList<String> tempList = sItemResult.get(i);
										for(int j=0;j<tempList.size();j++){
											TextView t = new TextView(ReportManagement.this);
											t.setText(tempList.get(j));
											resultRow.addView(t);
										}
										salesReportTable.addView(resultRow);
										
									}
								}
								
							}
						
							else if(salesConditionSpinner.getSelectedItemId() == 1){//根据销售商品的名称查询，模糊搜索
								salesReportTable.removeViews(1, salesReportTable.getChildCount()-1);//清除上次的记录
								ArrayList<ArrayList<String>> sItemResult= sItemCPer.getSalesInfoByGoodsName(salesStartTimeButton.getText().toString(), salesEndTimeButton.getText().toString(), content);
								
								if(sItemResult.size() == 0){
									Toast.makeText(ReportManagement.this, "根据条件没查找到销售信息。", Toast.LENGTH_SHORT).show();
								}
								else{
									for(int i=0;i<sItemResult.size();i++){
										TableRow resultRow = new TableRow(ReportManagement.this);
										ArrayList<String> tempList = sItemResult.get(i);
										for(int j=0;j<tempList.size();j++){
											TextView t = new TextView(ReportManagement.this);
											t.setText(tempList.get(j));
											resultRow.addView(t);
										}
										salesReportTable.addView(resultRow);
										
									}
								}
									
							}
							else if(salesConditionSpinner.getSelectedItemId() == 2){//根据销售商品的种类查询
								salesReportTable.removeViews(1, salesReportTable.getChildCount()-1);//清除上次的记录
								ArrayList<ArrayList<String>> sItemResult= sItemCPer.getSalesInfoByKindName(salesStartTimeButton.getText().toString(), salesEndTimeButton.getText().toString(), content);
								
								if(sItemResult.size() == 0){
									Toast.makeText(ReportManagement.this, "根据条件没查找到销售信息。", Toast.LENGTH_SHORT).show();
								}
								else{
									for(int i=0;i<sItemResult.size();i++){
										TableRow resultRow = new TableRow(ReportManagement.this);
										ArrayList<String> tempList = sItemResult.get(i);
										for(int j=0;j<tempList.size();j++){
											TextView t = new TextView(ReportManagement.this);
											t.setText(tempList.get(j));
											resultRow.addView(t);
										}
										salesReportTable.addView(resultRow);
										
									}
								}
							}
							else if(salesConditionSpinner.getSelectedItemId() == 3){//根据客户编号，VIPNum
								vipInfoCPer = new VIPInfoCPer();
								int VIPId = vipInfoCPer.getVIPIdByVIPNum(content);
								if(VIPId == -1){
									salesReportTable.removeViews(1, salesReportTable.getChildCount()-1);
									Toast.makeText(ReportManagement.this, "抱歉，没有该VIP客户", Toast.LENGTH_SHORT).show();
								}
								else{
									salesReportTable.removeViews(1, salesReportTable.getChildCount()-1);//清除上次的记录
									ArrayList<ArrayList<ArrayList<String>>> result  = sItemCPer.getSalesInfoByVIPNum(salesStartTimeButton.getText().toString(), salesEndTimeButton.getText().toString(), VIPId);
									if(result.size() == 0)
										Toast.makeText(ReportManagement.this, "根据条件没查找到销售信息。", Toast.LENGTH_SHORT).show();
									else{
										for(int i=0;i<result.size();i++){
											ArrayList<ArrayList<String>> temp = result.get(i);
										
											for(int j=0;j<temp.size();j++){
												ArrayList<String> t = temp.get(j);
												TableRow theResultRow = new TableRow(ReportManagement.this);
												for(int z=0;z<t.size();z++){
													TextView t1 = new TextView(ReportManagement.this);
													t1.setText(t.get(z));
													theResultRow.addView(t1);
												}
												salesReportTable.addView(theResultRow);
											}
										}
									}
								}
							}
				        			   }
								   }
							};
						 	pD = ProgressDialog.show(ReportManagement.this, "请稍候...", "Loading...", true,  
				                    false);
						 	new Thread(){
						 		public void run() {
						 			try {
										sleep(1500);
									} catch (InterruptedException e) {
									 
									}finally{
										sSearchHandler.sendEmptyMessage(1);
									}
						 			
						 		}
						 	}.start();
							
							
						}
					 
					}
					}
	        		
	        	);
	        	
	        }
	        else if(reportKind == 2){
	        	final int pageSize = 5;//每页的个数
	        
	        	setContentView(R.layout.inventoryreport);	        	
	        	
	        	final ConsumeCPer cCPer = new ConsumeCPer();
	        	final ReturnCPer rCPer = new ReturnCPer();
	        	final UnitCPer unitCPer = new UnitCPer();
	        	final ManufacturerCPer mCPer = new ManufacturerCPer();
	        	
	        	inventoryReportTable = (TableLayout)this.findViewById(R.id.inventoryReportTable);
	        	
	        	final List<Integer> allPriceId = gPriceCPer.getAllPriceId();
	        	
	        	final List<InventoryItemObject> allInventoryItem = new ArrayList<InventoryItemObject>();
	        	
	        	final Button previous = (Button)this.findViewById(R.id.previous);
	        	final Button next = (Button)this.findViewById(R.id.next);
	        	final TextView currentPageTView = (TextView)this.findViewById(R.id.currentPage);
	        	final TextView pageCountTView = (TextView)this.findViewById(R.id.pageCount);
	        	
	        	
	        	final Handler messageListener = new Handler(){
	        		   public void handleMessage(Message msg) {
	        			   switch(msg.what) {
	        			   case 1:
	        				   for(int i=0;i<allInventoryItem.size();i++){
	    	        			if(inventoryReportTable.getChildCount()-1<pageSize){
	    	        					InventoryItemObject iItemObject = allInventoryItem.get(i);
	    		        			    TableRow t = new TableRow(ReportManagement.this);
	    	    	        		
	    	    	        			TextView gNameTView = new TextView(ReportManagement.this);
	    	    	        			gNameTView.setText(iItemObject.getGoodsName());
	    	    	        			TextView mTView = new TextView(ReportManagement.this);
	    	    	        			mTView.setText(iItemObject.getMName());
	    	    	        			TextView kindTView = new TextView(ReportManagement.this);
	    	    	        			kindTView.setText(iItemObject.getKind());
	    	    	        			TextView surplusNumTView = new TextView(ReportManagement.this);
	    	    	        			surplusNumTView.setText(iItemObject.getSurplusNum());
	    	    	        			TextView unitTView = new TextView(ReportManagement.this);
	    	    	        			unitTView.setText(iItemObject.getUnitName());
	    	    	        			TextView inTView = new TextView(ReportManagement.this);
	    	    	        			inTView.setText(iItemObject.getInPrice());
	    	    	        			TextView outTView = new TextView(ReportManagement.this);
	    	    	        			outTView.setText(iItemObject.getOutPrice());
	    	    	        		
	    	    	        			t.addView(gNameTView);
	    	    	        			t.addView(mTView);
	    	    	        			t.addView(kindTView);
	    	    	        			t.addView(surplusNumTView);
	    	    	        			t.addView(unitTView);
	    	    	        			t.addView(inTView);
	    	    	        			t.addView(outTView);
	    	    	        		
	    	    	        			inventoryReportTable.addView(t);
	    	        			}
	        				   }
	        				   pD.dismiss();
	        				pageCount = allInventoryItem.size()/pageSize;
	        	        	surplusCount = allInventoryItem.size()%pageSize;
	        	        	
	        	        	if(surplusCount > 0)
	        	        		pageCount += 1;
	        	        	
	        	        	if(pageCount == 0)
	        	        		pageCount += 1;
	                		
	                		if(pageCount == 1){
	                			previous.setEnabled(false);
	                			next.setEnabled(false);
	                			
	                			currentPageTView.setText("1");
	                			pageCountTView.setText("1");
	                		}
	                		else{
	                			previous.setEnabled(true);
	                			next.setEnabled(true);
	                			
	                			currentPageTView.setText("1");
	                			pageCountTView.setText("" + pageCount);
	                		}
	                		firstItemIndex = 1;
	                		previous.setEnabled(false);
	        	        	
	        	        	previous.setOnClickListener(new OnClickListener(){

	        					public void onClick(View arg0) {
	        						next.setEnabled(true);
	        						currentPage--;
	        						currentPageTView.setText(currentPage+"");
	        						if(currentPage == 1){
	        							((Button)arg0).setEnabled(false);
	        						}
	        						inventoryReportTable.removeViews(1, inventoryReportTable.getChildCount()-1);
	        						
	        						firstItemIndex -= pageSize;
	        						for(int i=0;i<pageSize;i++){
	        							int index = firstItemIndex+i;
	        							if(index<=allInventoryItem.size()){//如果没有越界
	        								InventoryItemObject iItemObject = allInventoryItem.get(firstItemIndex+i-1);
	        								
	        								TableRow t = new TableRow(ReportManagement.this);
	        				    	        		
	        							    TextView gNameTView = new TextView(ReportManagement.this);
	        			    	        	gNameTView.setText(iItemObject.getGoodsName());
	        			    	        	TextView mTView = new TextView(ReportManagement.this);
	        			    	        	mTView.setText(iItemObject.getMName());
	        			    	        	TextView kindTView = new TextView(ReportManagement.this);
	        			    	        	kindTView.setText(iItemObject.getKind());
	        			    	        	TextView surplusNumTView = new TextView(ReportManagement.this);
	        			    	        	surplusNumTView.setText(iItemObject.getSurplusNum());
	        			    	        	TextView unitTView = new TextView(ReportManagement.this);
	        			    	        	unitTView.setText(iItemObject.getUnitName());
	        			    	        	TextView inTView = new TextView(ReportManagement.this);
	        			    	        	inTView.setText(iItemObject.getInPrice());
	        			    	        	TextView outTView = new TextView(ReportManagement.this);
	        			    	        	outTView.setText(iItemObject.getOutPrice());
	        			    	        		
	        			    	        	t.addView(gNameTView);
	        			    	        	t.addView(mTView);
	        			    	        	t.addView(kindTView);
	        			    	        	t.addView(surplusNumTView);
	        			    	        	t.addView(unitTView);
	        			    	        	t.addView(inTView);
	        			    	        	t.addView(outTView);
	        			    	        		
	        			    	        	inventoryReportTable.addView(t);
	        							}
	        								
	        							
	        						}
	        							
	        					}
	        	        		
	        	        	});
	        	        	next.setOnClickListener(new OnClickListener(){

	        					public void onClick(View arg0) {
	        						previous.setEnabled(true);
	        						currentPage++;
	        						currentPageTView.setText(currentPage+"");
	        						if(currentPage == pageCount){
	        							((Button)arg0).setEnabled(false);
	        						}

	        						inventoryReportTable.removeViews(1, inventoryReportTable.getChildCount()-1);
	        							
	        						firstItemIndex += pageSize;
	        						for(int i=0;i<pageSize;i++){
	        							int index = firstItemIndex+i;
	        							if(index<=allInventoryItem.size()){//如果没有越界
	        								InventoryItemObject iItemObject = allInventoryItem.get(firstItemIndex+i-1);
	        								
	        								TableRow t = new TableRow(ReportManagement.this);
	        				    	        		
	        							    TextView gNameTView = new TextView(ReportManagement.this);
	        			    	        	gNameTView.setText(iItemObject.getGoodsName());
	        			    	        	TextView mTView = new TextView(ReportManagement.this);
	        			    	        	mTView.setText(iItemObject.getMName());
	        			    	        	TextView kindTView = new TextView(ReportManagement.this);
	        			    	        	kindTView.setText(iItemObject.getKind());
	        			    	        	TextView surplusNumTView = new TextView(ReportManagement.this);
	        			    	        	surplusNumTView.setText(iItemObject.getSurplusNum());
	        			    	        	TextView unitTView = new TextView(ReportManagement.this);
	        			    	        	unitTView.setText(iItemObject.getUnitName());
	        			    	        	TextView inTView = new TextView(ReportManagement.this);
	        			    	        	inTView.setText(iItemObject.getInPrice());
	        			    	        	TextView outTView = new TextView(ReportManagement.this);
	        			    	        	outTView.setText(iItemObject.getOutPrice());
	        			    	        		
	        			    	        	t.addView(gNameTView);
	        			    	        	t.addView(mTView);
	        			    	        	t.addView(kindTView);
	        			    	        	t.addView(surplusNumTView);
	        			    	        	t.addView(unitTView);
	        			    	        	t.addView(inTView);
	        			    	        	t.addView(outTView);
	        			    	        		
	        			    	        	inventoryReportTable.addView(t);
	        							}
	        								
	        							
	        						}

	        					}
	        	        		
	        	        	});
	        	        	break;
	        		   }
	        		   }
	        	};
	        	pD = ProgressDialog.show(this, "请稍候...", "Loading...", true,  
	                    false);
	        	new Thread(){
	        		public void run() {
	        			for(int i=0;i<allPriceId.size();i++){
	    	        		int thePriceId = allPriceId.get(i);
	    	        		String theBarcode = gPriceCPer.getBarcodeIdByGoodsPriceId(thePriceId);
	    	        		
	    	        		int pNum = pItemCPer.getAllPNumByPriceId(thePriceId);
	    	        		int sNum = sItemCPer.getSNumByBarcode(theBarcode);
	    	        		int cNum = cCPer.getTotalConsumeByPriceId(thePriceId);	        	 
	    	        		int rNum = rCPer.getTotalReturnByPriceId(thePriceId);
	    	        	
	    	        		int surplusNum = pNum - sNum + cNum - rNum;//剩余量
	    	        		
	    	        		if(surplusNum>0){
	    	        			int gId = gPriceCPer.getGoodsIdByGoodsPriceId(thePriceId);
	    		        		String gName = gCPer.getGoodsNameByGoodsId(gId);//根据价格ID查找到商品ID，再查找商品名字
	    		        		int mId = Integer.parseInt(gCPer.getAttributeById("manufacturerId", gId+""));//查找到厂家的ID
	    		        		String mName = mCPer.getMNameByMId(mId);
	    		        		String kindName = gCPer.getKindNameByGoodsId(gId);//根据商品的ID查找种类名称
	    		        		if(kindName.contains("->"))
	    		        			kindName = kindName.substring(kindName.indexOf("->") + 2);
	    		        		
	    	        			String unitName = unitCPer.getUnitNameById(gPriceCPer.getUnitIdByGoodsPriceId(thePriceId));//通过价格ID查找单位ID
	    	        		
	    	        			double inPrice = gPriceCPer.getInPriceByGoodsPriceId(thePriceId);
	    	        			double outPrice = gPriceCPer.getOutPriceByGoodsPriceId(thePriceId);
	    	        			
	    	        			InventoryItemObject iItemObject = new InventoryItemObject();
	    	        			iItemObject.setIndex(allInventoryItem.size()+1);
	    	        			iItemObject.setGoodsName(gName);
	    	        			iItemObject.setMName(mName);
	    	        			iItemObject.setKind(kindName);
	    	        			iItemObject.setSurplusNum(surplusNum+"");
	    	        			iItemObject.setUnitName(unitName);
	    	        			iItemObject.setInPrice(inPrice+"");
	    	        			iItemObject.setOutPrice(outPrice+"");
	    	        			
	    	        			allInventoryItem.add(iItemObject);
	    	        			     		
	    	        			totalItemCount += 1;
	    	        		}
	    	       
	    	        	}
	        			
	        			 messageListener.sendEmptyMessage(1);  
	        		}
	        	}.start();
	        
	         

	        }
	        

	    }
}
