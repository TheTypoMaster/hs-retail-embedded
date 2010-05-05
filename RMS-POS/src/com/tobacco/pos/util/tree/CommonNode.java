package com.tobacco.pos.util.tree;

import com.tobacco.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommonNode extends LinearLayout {

	protected ImageView imageView;
	protected TextView textView;
	protected TreeNode treeNode;
	
	public CommonNode(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CommonNode(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CommonNode(Context context,TreeNode node){
		super(context);
		
		setOrientation(HORIZONTAL);		
		if(node.hasChild()){
			imageView = new ImageView(context);
			if(node.isExpand()){
				imageView.setImageResource(R.drawable.expand);
			}else{
				imageView.setImageResource(R.drawable.collapse);
			}
			addView(imageView, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			imageView.setPadding(0, 5, 0, 0);
		}else{
			TextView blank = new TextView(context);
			blank.setText("  ");
			addView(blank, new LayoutParams(30,LayoutParams.WRAP_CONTENT));
		}
		
		textView = new TextView(context, null, R.style.TextViewfillWrapLargeStyle);
//		textView = new TextView(context);
		textView.setText(node.getText());
		
		treeNode = node;
		node.setNode(this);
		setVisible(node.isVisible());
	}
	
	public TextView getTextView() {
		return textView;
	}
	
	public void changeImage(){
		if(treeNode.isExpand()){		
			imageView.setImageResource(R.drawable.collapse);
			treeNode.setExpand(false);
		}else{
			imageView.setImageResource(R.drawable.expand);
			treeNode.setExpand(true);
		}	
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.e("CommonNode:onInterceptTouchEvent", "false");
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub	
		Log.e("CommonNode:onTouchEvent", "false");
		return false;	
	}
	
	public boolean mouseInImage(MotionEvent event){
		if((imageView!=null)&&(event.getX()>imageView.getLeft())&&(event.getX()<imageView.getLeft()+imageView.getWidth()))
			return true;
		else 
			return false;
	}
	
	public boolean mouseInText(MotionEvent event){
		if((event.getX()>textView.getLeft())&&(event.getX()<textView.getLeft()+textView.getWidth()))
			return true;
		else 
			return false;
	}
	
	public void setVisible(Boolean visible){
//		Log.e("testExpand", "changeVisible:"+visible);
		if(visible==true)
			setVisibility(View.VISIBLE);
		else 
			setVisibility(View.GONE);
	}
	
	public TreeNode getTreeNode(){
		return treeNode;
	}
	
	public void setChecked(boolean checked){
		
	}
}
