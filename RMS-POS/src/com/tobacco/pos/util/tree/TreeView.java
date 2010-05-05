package com.tobacco.pos.util.tree;

import java.util.Iterator;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class TreeView extends CommonTreeView{

	public TreeView(Context context, TreeNode rootNode){
		super(context,rootNode);
	}
	
	public void fillSubNodes(){
		Iterator rootIterator = new TreeOutOrder.DepthOrderIterator(root);
		while(rootIterator.hasNext()){
			TreeNode node = (TreeNode)rootIterator.next();
			NodeText nodeText = new NodeText(ctx,node);
			nodeText.setPadding(node.getLevel()*10, 0, 0, 0);
			addView(nodeText);
		}
	}

}
