package com.tobacco.pos.util.tree;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

public class NodeText extends CommonNode{
	
	public NodeText(Context context,TreeNode node) {
		super(context,node);
		// TODO Auto-generated constructor stub
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
			((TreeView)getParent()).setSelectedNode(this);		
		}	
		return false;	
	}

}
