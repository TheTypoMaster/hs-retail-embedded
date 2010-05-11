package com.tobacco.pos.util;

public class RegexCheck{
  public static boolean checkSeqWithNum(String value){
	  
    return value.matches("[a-zA-Z][a-zA-Z0-9_]{1,15}$");
  }
  
  public static boolean checkInteger(String value){
    return value.matches("[1-9][0-9]{0,9}");
  }
  
  public static boolean checkVIPNum(String value){
	  return value.matches("[0-9]{1,5}$");
  }
  
  public static boolean checkBarcode(String value){
	  return value.matches("[0-9]{1,13}");
  }
}
