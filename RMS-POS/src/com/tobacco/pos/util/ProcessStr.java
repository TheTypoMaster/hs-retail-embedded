package com.tobacco.pos.util;

import java.util.StringTokenizer;
import java.util.Vector;

import android.util.Log;

public class ProcessStr {

	public String str;
	public Vector<String> strV = new Vector<String>();
	
	public ProcessStr(String str) {
		this.str = str;
		
		StringTokenizer st = new StringTokenizer(str, "]");

		while (st.hasMoreElements()) {
			strV.add(st.nextElement().toString());
		}

		for(int i=0;i<strV.size();i++){
			if(strV.get(i).toString().trim().equals("")){
				strV.remove(i);
			}
		}
		for(int i=0;i<strV.size();i++){
			Log.d("lyq", strV.get(i));
		}
	}

	public Vector<String> getStrV(){
		return strV;
	}
	public Vector<String> getNames(){

		Vector<String> names = new Vector<String>();
		String temp = "";
		for(int i=0;i<strV.size();i++){
			temp = strV.get(i);
			names.add(temp.substring(temp.indexOf("nodeName=")+9, temp.indexOf(" pId")));
		}
		return names;
	}
	
	public Vector<Integer> getLevels(){
		Vector<Integer> levels = new Vector<Integer>();
		String temp = "";
		for(int i=0;i<strV.size();i++){
			temp = strV.get(i);
			levels.add(Integer.parseInt(temp.substring(temp.indexOf("level=")+6, temp.indexOf(" comment"))));
		}
		return levels;
	}
	public Vector<Integer> getIds(){

		Vector<Integer> ids = new Vector<Integer>();
		String temp = "";
		for(int i=0;i<strV.size();i++){
			temp = strV.get(i);
			ids.add(Integer.parseInt(temp.substring(temp.indexOf("nodeId=")+7, temp.indexOf(" nodeName"))));
		}
		return ids;
	}
}
