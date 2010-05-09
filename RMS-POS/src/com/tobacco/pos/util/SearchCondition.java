package com.tobacco.pos.util;

import java.util.Date;
import java.util.HashMap;

import com.tobacco.pos.entity.ConsumeFull;
import com.tobacco.pos.searchStrategy.SearchState;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchCondition extends LinearLayout {

	private String TAG = "SearchCondition";
	private final SearchButton startTime;
	private final SearchButton endTime;
	private boolean startSelected;
	private boolean endSelected;
	private Spinner conditionSpinner;
	private EditText contentText;
	private HashMap<Integer,Integer> mappingType = new HashMap<Integer,Integer>();
	private HashMap<Integer,String> mappingSel = new HashMap<Integer,String>();
	private String timeTable;
	private Context context;
	final SearchState instance = SearchState.getInstance();
	
	public SearchCondition(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		Log.i(TAG, "SearchCondition()");
		this.context = context;
		this.setOrientation(HORIZONTAL);
//		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		// TODO Auto-generated constructor stub
		startTime = new SearchButton(context);
		startTime.setText("开始时间");
		startTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		this.addView(startTime, 0, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		
		TextView between = new TextView(context);
		between.setText("到");
		this.addView(between, 1, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		
		endTime = new SearchButton(context);
		endTime.setText("结束时间");
		endTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		this.addView(endTime, 2, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		
		TextView factor = new TextView(context);
		factor.setText(" 条件");
		this.addView(factor, 3, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		
		conditionSpinner = new Spinner(context);
		this.addView(conditionSpinner, 4, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
	
		TextView searchContent = new TextView(context);
		searchContent.setText(" 查询内容");
		this.addView(searchContent, 5, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
	
		contentText = new EditText(context);
		contentText.setSingleLine();
		contentText.setOnKeyListener(keyListener);
		this.addView(contentText, 6, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
	}

	public void init(String timeTable,String[] content,HashMap<Integer,Integer> typeMapping,HashMap<Integer,String> selMapping){
		setTimeTable(timeTable);
		setSpinnerContent(content);
		setMappingType(typeMapping);
		setMappingSel(selMapping);
	}
	
	public void setTimeTable(String timeTable){
		this.timeTable = timeTable;
	}
	
	public void setSpinnerContent(String[] content){
		Log.i(TAG, "setSpinnerContent()");
//		spinnerContent = content;
		ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, content);
    	conditionSpinner.setAdapter(conditionAdapter);
	}

	public void setMappingType(HashMap<Integer,Integer> typeMapping){
		Log.i(TAG, "setMappingType()");
		mappingType = typeMapping;
	}
	
	public void setMappingSel(HashMap<Integer,String> selMapping){
		Log.i(TAG, "setMappingSel()");
		mappingSel = selMapping;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.i("SearchCondition", "onInterceptTouchEvent:false");
		return false;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			Log.i("SearchCondition", "onTouchEvent()");
			if(mouseInFStart(event)){
				Log.i("SearchCondition", "mouse in start");

				final DatePicker startTimePicker = new DatePicker(context);
				startTimePicker.setVerticalScrollBarEnabled(true);
				AlertDialog.Builder startTimeDialog = new AlertDialog.Builder(context);
				startTimeDialog.setTitle("开始时间");
				startTimeDialog.setView(startTimePicker);
				startTimeDialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog,
							int which) {
						Date date = new Date(startTimePicker.getYear()-1900,startTimePicker.getMonth(),startTimePicker.getDayOfMonth());
						String time = DateTool.getToDate(DateTool.formatDateToString(date));
						startTime.setText(time);
						Log.i("time", time);
						startSelected = true;
						Log.i("time", "startTime:"+startTime.getText().toString());
						if(startSelected&&endSelected){
							instance.setSelectionFactor(SearchState.TIME, new String[]{timeTable,timeTable},
									new String[]{DateTool.fillComplete(startTime.getText().toString()),DateTool.addDay(endTime.getText().toString())});
							Log.i("time", "startTimeFill:"+DateTool.fillComplete(startTime.getText().toString()));
							Log.i("time", "endTimeFill:"+DateTool.addDay(endTime.getText().toString()));
							Log.i("SearchCondition", "setSelectionFactor:"+SearchState.TIME);
							startSelected = false;
							endSelected = false;
						}
					}
					
				});
				startTimeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog,
							int which) {
						
					}
				});
				startTimeDialog.show();
				
				
			}else if(mouseInEnd(event)){
				Log.i("SearchCondition", "mouse in end");
//				endTime.setText(year+"-"+monthStr+"-"+dayStr);
				final DatePicker endTimePicker = new DatePicker(context);
				endTimePicker.setVerticalScrollBarEnabled(true);
				AlertDialog.Builder endTimeDialog = new AlertDialog.Builder(context);
				endTimeDialog.setTitle("开始时间");
				endTimeDialog.setView(endTimePicker);
				endTimeDialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog,
							int which) {
						Date date = new Date(endTimePicker.getYear()-1900,endTimePicker.getMonth(),endTimePicker.getDayOfMonth());
						String time = DateTool.getToDate(DateTool.formatDateToString(date));
						endTime.setText(time);
						Log.i("time", time);
						endSelected = true;
						Log.i("time", "endTime:"+endTime.getText().toString());
						if(startSelected&&endSelected){
							instance.setSelectionFactor(SearchState.TIME, new String[]{timeTable,timeTable},
									new String[]{DateTool.fillComplete(startTime.getText().toString()),DateTool.addDay(endTime.getText().toString())});
							Log.i("time", "startTimeFill:"+DateTool.fillComplete(startTime.getText().toString()));
							Log.i("time", "endTimeFill:"+DateTool.addDay(endTime.getText().toString()));
							Log.i("SearchCondition", "setSelectionFactor:"+SearchState.TIME);
							startSelected = false;
							endSelected = false;
						}
					}
					
				});
				endTimeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog,
							int which) {
						
					}
				});
				endTimeDialog.show();
				
			}
		}
		
		return true;
	}
	
	private OnKeyListener keyListener = new OnKeyListener() {
		
	 
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			String content = ((EditText)v).getText().toString();
			if(content!=null && content.length()>0 && event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
		
				int position = conditionSpinner.getSelectedItemPosition();
				Log.i("TestStrategy", "select"+position);
				Log.i("TestStrategy", "type:"+mappingType.get(position));
				Log.i("TestStrategy", "selection:"+mappingSel.get(position));
				instance.setSelectionFactor(mappingType.get(position), new String[]{mappingSel.get(position)}, new String[]{content});
				Log.i("SearchCondition", "setSelectionFactor:"+mappingType.get(position));
				return true;
			}
			return false;
		}
	};

	protected boolean mouseInFStart(MotionEvent event){
		if((event.getX()>startTime.getLeft())&&(event.getX()<startTime.getLeft()+startTime.getWidth()))
			return true;
		else 
			return false;
	}
	
	protected boolean mouseInEnd(MotionEvent event){
		if((event.getX()>endTime.getLeft())&&(event.getX()<endTime.getLeft()+endTime.getWidth()))
			return true;
		else 
			return false;
	}
	
	public void reset(){
		startTime.setText("开始时间");
		endTime.setText("结束时间");
		startSelected = false;
		endSelected = false;
		contentText.setText("");
	}
	
	class SearchButton extends Button{

		public SearchButton(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			Log.i("SearchCondition", this.getText().toString()+".onTouchEvent():false");
			return false;
		}
		
	};
}
