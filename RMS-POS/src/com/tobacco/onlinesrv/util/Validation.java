package com.tobacco.onlinesrv.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
	

	public static boolean isInteger(String number) {
		Pattern pattern = Pattern.compile("^-?\\d+$");
		Matcher match = pattern.matcher(number);
		return match.matches();
	}
	
	public static boolean isValidDate(String date) {
		Date dateOfToday = null;
		Date dateOfSelectedDay = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dateOfToday = sdf.parse(getToday());
			dateOfSelectedDay = sdf.parse(date);
			if (dateOfSelectedDay.getTime() - dateOfToday.getTime() >= 0)
				return true;
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return false;
	} 
	
	public static String getToday(){
		return Calendar.getInstance().get(Calendar.YEAR) + "-"
		+ (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-"
		+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}
}
