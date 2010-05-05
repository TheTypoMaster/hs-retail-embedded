package com.tobacco.pos.util.tree;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public abstract class CommonTreeView extends LinearLayout {

	protected TreeNode root;
	protected CommonNode selectNode;
	protected Context ctx;
	
	public CommonTreeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(VERTICAL);
		ctx = context;
		// TODO Auto-generated constructor stub
	}

	public CommonTreeView(Context context) {
		super(context);
		this.setOrientation(VERTICAL);
		ctx = context;
		// TODO Auto-generated constructor stub
	}
	
	public CommonTreeView(Context context, TreeNode rootNode){
		super(context);
		this.setOrientation(VERTICAL);
		root = rootNode;
		root.setExpand(true);
		ctx = context;
		fillSubNodes();
	}
	
	public void init(TreeNode rootNode){
		root = rootNode;
		root.setExpand(true);
		fillSubNodes();
	}
	
	protected void setSelectedNode(CommonNode node){
		if(selectNode!=null){
			selectNode.getTextView().setTextColor(Color.WHITE);
		}
		selectNode = node;
		selectNode.getTextView().setTextColor(Color.RED);
	}
	
	public TreeNode getSelectNode(){
		return selectNode.getTreeNode();
	}
	
	public abstract void fillSubNodes();
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.e("TreeView:onInterceptTouchEvent", "false");
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub	
		Log.e("TreeView:onTouchEvent", "false");
		return false;	
	}
	
}
