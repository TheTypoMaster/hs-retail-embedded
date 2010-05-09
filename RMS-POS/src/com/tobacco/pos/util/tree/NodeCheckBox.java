package com.tobacco.pos.util.tree;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.CheckBox;

public class NodeCheckBox extends CheckBox {

	public NodeCheckBox(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NodeCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NodeCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.e("NodeCheckBox:onTouchEvent", "false");
		return false;
	}
}
