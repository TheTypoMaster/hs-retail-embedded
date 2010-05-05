package com.tobacco.pos.util.tree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class NodeCheckText extends CommonNode{

	private NodeCheckBox checkBox;
	
	public NodeCheckText(Context context, TreeNode node) {
		super(context,node);
		// TODO Auto-generated constructor stub
		checkBox = new NodeCheckBox(context);
		addView(checkBox, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

		addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub	
		Log.e("NodeText:onTouchEvent", "false");
//		Log.e("imageview getleft", ""+imageView.getLeft());
//		Log.e("imageview getwidth", ""+imageView.getWidth());
//		Log.e("mouse x", ""+event.getX());
		if(mouseInImage(event))
		{
			Log.e("mouseInImage", "true");
			changeImage();
		}		
		else if(mouseInText(event))
		{
			Log.e("mouseInIText", "true");
			((CheckTreeView)getParent()).setSelectedNode(this);		
		}else if(mouseInCheckBox(event))
		{
			Log.e("mouseInCheckBox", "true");
			treeNode.setChecked(!checkBox.isChecked());
		}	
		return false;	
	}
	
	public boolean mouseInCheckBox(MotionEvent event){
		if((event.getX()>checkBox.getLeft())&&(event.getX()<checkBox.getLeft()+checkBox.getWidth()))
			return true;
		else 
			return false;
	}
	
	@Override
	public void setChecked(boolean checked){
		checkBox.setChecked(checked);
	}

}
