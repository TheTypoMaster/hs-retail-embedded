package com.tobacco.pos.common;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.tobacco.R;
import com.tobacco.pos.util.DateTool;

public class SelectTimeRange extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.time_range);
		Button confirmButton = (Button)findViewById(R.id.confirm_date_select);
		Button cancelButton = (Button)findViewById(R.id.cancel_date_select);
		
		confirmButton.setOnClickListener(buttonListener);
		cancelButton.setOnClickListener(buttonListener);
		
	}
	
	private OnClickListener buttonListener = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			
			case R.id.cancel_date_select:
				setResult(RESULT_CANCELED,new Intent());
				finish();
				break;
			case R.id.confirm_date_select:
				DatePicker start = (DatePicker)findViewById(R.id.start_time_datapicker);
				DatePicker end = (DatePicker)findViewById(R.id.end_time_datapicker);
				Date startDate = new Date(start.getYear()-1900,start.getMonth(),start.getDayOfMonth());
				Date endDate = new Date(end.getYear()-1900,end.getMonth(),end.getDayOfMonth()+1);
				if(startDate.after(endDate)||startDate.equals(endDate)){
					Toast.makeText(SelectTimeRange.this, "开始时间不能比结束时间大", Toast.LENGTH_SHORT).show();
				}else{
					Intent timeRangeIntent = new Intent();
					
					timeRangeIntent.putExtra("startDate", DateTool.formatDateToString(startDate));
					timeRangeIntent.putExtra("endDate", DateTool.formatDateToString(endDate));
//					timeRangeIntent.putExtra("startYear", startYear);
//					timeRangeIntent.putExtra("startMonth", startMonth+1);
//					timeRangeIntent.putExtra("startDay", startDay);
//					timeRangeIntent.putExtra("endYear", endYear);
//					timeRangeIntent.putExtra("endMonth", endMonth+1);
//					timeRangeIntent.putExtra("endDay", endDay);
					setResult(RESULT_OK,timeRangeIntent);
					finish();
				}					
			}
		}
		
	};

	
}

/*
 *private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            }; 
 */
