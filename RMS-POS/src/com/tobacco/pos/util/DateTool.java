package com.tobacco.pos.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTool {

	static String DATE_FORMAT = "yyyy-MM-dd:hh.mm.ss";
	static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	
	public static String formatDateToString(Date date){
		return sdf.format(date);
	}
	
	public static Date formatStringToDate(String date){
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
