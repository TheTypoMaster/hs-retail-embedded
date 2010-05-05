package com.tobacco.pos.util;

import com.tobacco.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class PageModel extends LinearLayout {
	
	private int currentIndex;
	private int rowsCount;
	private int pageCount;
	private int recordCount;
	private TextView first;
	private ImageView pre;
	private ImageView next;
	private TextView last;
	private TextView showPageNumber;

	public PageModel(Context context,int rowsCount,int recordCount) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		setOrientation(HORIZONTAL);
		first = new TextView(context);
		first.setText("首页");
		first.setPadding(100, 20, 0, 0);
		
		pre = new ImageView(context);
		pre.setImageResource(R.drawable.pre);
		pre.setPadding(20, 20, 0, 0);
		pre.setMaxHeight(20);
		pre.setMaxWidth(20);
		
		next = new ImageView(context);
		next.setImageResource(R.drawable.nex);
		next.setPadding(20, 20, 0, 0);
		
		last = new TextView(context);
		last.setText("末页");
		last.setPadding(20, 20, 0, 0);
		
		showPageNumber = new TextView(context);
		showPageNumber.setText("第1页");
		showPageNumber.setPadding(20, 20, 100, 0);
		
		addView(first, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		addView(pre, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		addView(next, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		addView(last, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		addView(showPageNumber, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		
		init(rowsCount,recordCount);
	}

	public PageModel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PageModel(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public int getRowsCount() {
		return rowsCount;
	}
	
	public int getCurrentIndex() {
		return currentIndex;
	}

	public void init(int rowsCount,int recordCount){
		this.rowsCount = rowsCount;
		this.recordCount = recordCount;
		pageCount = (recordCount%rowsCount>0)?recordCount/rowsCount+1:recordCount/rowsCount;
		if(pageCount<2)
			this.setVisibility(View.GONE);
		Log.i("pagetest", "currentIndex:"+this.currentIndex);
		Log.i("pagetest", "rowsCount:"+this.rowsCount);
		Log.i("pagetest", "recordCount:"+this.recordCount);
		Log.i("pagetest", "pageCount:"+this.pageCount);
		init();
	}
	
	private void init(){
		this.currentIndex = 1;
		check();
	}
	
	private void check(){
		if(currentIndex ==1){
			this.currentIndex = 1;
			first.setTextColor(Color.RED);
		}else if(currentIndex == pageCount){
			currentIndex = pageCount;
			last.setTextColor(Color.RED);
		}
		
		if(currentIndex != 1)
			pre.setVisibility(View.VISIBLE);
		else if(currentIndex != pageCount)
			next.setVisibility(View.VISIBLE);
		
		showPageNumber.setText("第"+currentIndex+"页");
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.e("PageModel:onInterceptTouchEvent", "false");
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub	
		Log.e("PageModel:onTouchEvent", "false");
		clearRed();
		if(mouseInFirst(event)){
			this.currentIndex = 1;
		}else if(mouseInPre(event)){
			if(currentIndex>1)
				currentIndex -= 1;
		}else if(mouseInNext(event)){
			if(currentIndex<pageCount)
				currentIndex += 1;
		}else if(mouseInLast(event)){
			currentIndex = pageCount;
		}
		check();
		return false;	
	}
	
	protected void clearRed(){
		first.setTextColor(Color.WHITE);
		last.setTextColor(Color.WHITE);
	}
	
	protected boolean mouseInFirst(MotionEvent event){
		if((event.getX()>first.getLeft())&&(event.getX()<first.getLeft()+first.getWidth()))
			return true;
		else 
			return false;
	}
	
	protected boolean mouseInPre(MotionEvent event){
		if((event.getX()>pre.getLeft())&&(event.getX()<pre.getLeft()+pre.getWidth()))
			return true;
		else 
			return false;
	}
	
	protected boolean mouseInNext(MotionEvent event){
		if((event.getX()>next.getLeft())&&(event.getX()<next.getLeft()+next.getWidth()))
			return true;
		else 
			return false;
	}
	
	protected boolean mouseInLast(MotionEvent event){
		if((event.getX()>last.getLeft())&&(event.getX()<last.getLeft()+last.getWidth()))
			return true;
		else 
			return false;
	}
}
