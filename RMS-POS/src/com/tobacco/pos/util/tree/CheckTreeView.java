package com.tobacco.pos.util.tree;

import java.util.Iterator;

import android.content.Context;
import android.util.AttributeSet;

public class CheckTreeView extends CommonTreeView {

	public CheckTreeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CheckTreeView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CheckTreeView(Context context, TreeNode rootNode) {
		super(context, rootNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void fillSubNodes() {
		// TODO Auto-generated method stub
		Iterator rootIterator = new TreeOutOrder.DepthOrderIterator(root);
		while(rootIterator.hasNext()){
			TreeNode node = (TreeNode)rootIterator.next();
			NodeCheckText nodeText = new NodeCheckText(ctx,node);
			nodeText.setPadding(node.getLevel()*10, 0, 0, 0);
			addView(nodeText);
		}
	}

}
