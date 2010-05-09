package com.tobacco.pos.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class DateTool {

	static String DATE_FORMAT = "yyyy-MM-dd:hh.mm.ss";
	static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	
	public static String formatDateToString(Date date){
		Log.i("DateTool", "formatDateToString:"+sdf.format(date));
		return sdf.format(date);
	}
	
	public static Date formatStringToDate(String date){
		try {
			Log.i("DateTool", "formatStringToDate:"+sdf.parse(date));
			return sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getToDate(String time){
		return time.substring(0, time.length()-9);
	}
	
	public static String fillComplete(String time){
		return time+=":00.00.00";
	}
	
	public static int getYear(String time){
		Log.i("addDate", "stringYear:"+time.substring(0, 4));
		Log.i("addDate", "intYear:"+Integer.valueOf(time.substring(0, 4)));
		return Integer.valueOf(time.substring(0, 4));
	}
	
	public static int getMouth(String time){
		Log.i("addDate", "stringMouth:"+time.substring(6, 7));
		Log.i("addDate", "intMouth:"+Integer.valueOf(time.substring(6, 7)));
		return Integer.valueOf(time.substring(6, 7));
	}
	
	public static int getDay(String time){
		Log.i("addDate", "stringDay:"+time.substring(8, 10));
		Log.i("addDate", "intDay:"+Integer.valueOf(time.substring(8, 10)));
		return Integer.valueOf(time.substring(8, 10));
	}
	
	public static String addDay(String dateTime){
		
		int day = getDay(dateTime)+1;
		int month = getMouth(dateTime);
		int year = getYear(dateTime);
		
		if(day==24){
			day = 0;
			month +=1;
		}
		if(month == 12){
			month = 0;
			year +=1;
		}
		String monthString = (String.valueOf(month).length()==1)?"0"+String.valueOf(month):String.valueOf(month);
		String dayString = (String.valueOf(day).length()==1)?"0"+String.valueOf(day):String.valueOf(day);
		
		return year+"-"+monthString+"-"+dayString+":00.00.00";
	}
}
