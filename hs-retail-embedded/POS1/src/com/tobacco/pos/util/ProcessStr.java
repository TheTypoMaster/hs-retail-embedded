package com.tobacco.pos.util;

import java.util.StringTokenizer;
import java.util.Vector;

public class ProcessStr {

	public String str;
	public Vector<String> strV = new Vector<String>();
	public Vector<Integer> levels = new Vector<Integer>();
	public Vector<String> names = new Vector<String>();
	
	public ProcessStr(String str) {
		this.str = str;
		
		StringTokenizer st = new StringTokenizer(str, "]");

		while (st.hasMoreElements()) {
			strV.add(st.nextElement().toString());
		}

		for(int i=0;i<strV.size();i++){//去除长度为0的字符串
			if(strV.get(i).toString().trim().equals("")){
				strV.remove(i);
			}
		}
	}

	public Vector<String> getStrV(){
		return strV;
	}
	public Vector<String> getNames(){
		String temp = "";
		for(int i=0;i<strV.size();i++){
			temp = strV.get(i);
			names.add(temp.substring(temp.indexOf("nodeName=")+9, temp.indexOf(" pId")));
		}
		return names;
	}
	
	public Vector<Integer> getLevels(){
		String temp = "";
		for(int i=0;i<strV.size();i++){
			temp = strV.get(i);
			levels.add(Integer.parseInt(temp.substring(temp.indexOf("level=")+6, temp.indexOf(" comment"))));
		}
		return levels;
	}
}
