package com.tobacco.pos.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tobacco.R;
import com.tobacco.main.activity.view.RMSBaseView;
import com.tobacco.pos.entity.AllTables.Goods;
import com.tobacco.pos.entity.AllTables.GoodsKind;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.Unit;
import com.tobacco.pos.util.tree.CheckTreeView;
import com.tobacco.pos.util.tree.TreeNode;

public class SelectInventoryGoods extends RMSBaseView {
    /** Called when the activity is first created. */
	
	ArrayList<TreeNode> treeNodes;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_tree_view);
        Intent intent = getIntent();
        ArrayList<Integer> exitsGoodsPrice = intent.getIntegerArrayListExtra(GoodsPrice._ID);
        
        treeNodes = new ArrayList<TreeNode>();
		Cursor kindc = managedQuery(GoodsKind.CONTENT_URI, new String[]{GoodsKind._ID,GoodsKind.name,GoodsKind.parent,GoodsKind.level}, null, null, null);
		if(kindc.getCount()>0){
			kindc.moveToFirst();
			for(int i = 0; i < kindc.getCount(); i++){
				
				int idIndex = kindc.getColumnIndex(GoodsKind._ID);
				int id = kindc.getInt(idIndex);
				int nameIndex = kindc.getColumnIndex(GoodsKind.name);
				String name = kindc.getString(nameIndex);
				int parentIndex = kindc.getColumnIndex(GoodsKind.parent);
				int parentId = kindc.getInt(parentIndex);
				int levelIndex = kindc.getColumnIndex(GoodsKind.level);
				int level = kindc.getInt(levelIndex);
				
				Log.i("GoodsKind", "id:"+id+" name:"+name+" parendId:"+parentId);
				TreeNode kindNode = new TreeNode(name, id,parentId,level,"kind");
				treeNodes.add(kindNode);
				kindc.moveToNext();
			}
		}
		
		TreeNode root = new TreeNode(true,"Root");
		
		for(TreeNode node: treeNodes){
			
			for(TreeNode node2 : treeNodes){
				if (node2.getpId() == node.getId()) {
					node.addNode(node2);
					Log.i("GoodsKind", "id:"+node.getId()+" name:"+node.getText()+" add:"+node2.getId());
				}
			}
			if(node.getpId()==0){
				root.addNode(node);
				Log.i("GoodsKind", "firstLayer:"+node.getIdentifier());
			}
				
		}
		
		Cursor goodsPricec = managedQuery(GoodsPrice.CONTENT_URI, new String[]{GoodsPrice._ID,GoodsPrice.goodsId,GoodsPrice.unitId}, null, null, null);
		if(goodsPricec.getCount()>0){
			goodsPricec.moveToFirst();
			for(int i = 0; i<goodsPricec.getCount(); i++){
				
				int gpIdIndex = goodsPricec.getColumnIndex(GoodsPrice._ID);
				int goodsPriceId = goodsPricec.getInt(gpIdIndex);
				
				if(exitsGoodsPrice==null||!exitsGoodsPrice.contains(Integer.valueOf(goodsPriceId))){
					int gdIdIndex = goodsPricec.getColumnIndex(GoodsPrice.goodsId);
					String goodsId = goodsPricec.getString(gdIdIndex);
					int unitIdIndex = goodsPricec.getColumnIndex(GoodsPrice.unitId);
					String unitId = goodsPricec.getString(unitIdIndex);
					
					Cursor unitc = managedQuery(Unit.CONTENT_URI, new String[]{Unit.name}, Unit._ID+" = ? ", new String[]{unitId}, null);
					String unitName = "";
					if(unitc.getCount()>0){
						unitc.moveToFirst();
						int unitNameIndex = unitc.getColumnIndex(Unit.name);
						unitName = unitc.getString(unitNameIndex);
					}
					
					Cursor goodsc = managedQuery(Goods.CONTENT_URI, new String[]{Goods.goodsName,Goods.kindId}, Goods._ID+" = ? ", new String[]{goodsId}, null);
					if(goodsc.getCount()>0){
						goodsc.moveToFirst();
						int goodsNameIndex = goodsc.getColumnIndex(Goods.goodsName);
						String goodsName = goodsc.getString(goodsNameIndex);
						int kindIdIndex = goodsc.getColumnIndex(Goods.kindId);
						int kindId = goodsc.getInt(kindIdIndex);
						
						TreeNode goodsNode = new TreeNode(goodsName+" "+unitName,goodsPriceId,"goods");
						
						for(TreeNode kind : treeNodes){
							if(kind.getId()==kindId){
								kind.addNode(goodsNode);						
								goodsNode.setLevel(kind.getLevel()+1);
							}						
						}
						
						treeNodes.add(goodsNode);
					}					
					goodsPricec.moveToNext();
				}		
			}
		}
        
        CheckTreeView tree = (CheckTreeView)findViewById(R.id.tree);
        tree.init(root);
        
        Button confirm = (Button)findViewById(R.id.selectInventoryGoodsConfirm);
        Button cancel = (Button)findViewById(R.id.selectInventoryGoodsCancel);
        confirm.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
        
    }
    
    private OnClickListener listener = new OnClickListener() {
		
	 
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.selectInventoryGoodsConfirm:
				ArrayList<Integer> goodsPriceIdList = new ArrayList<Integer>();
				for(TreeNode node: treeNodes){
					if(node.getIdentifier().equals("goods")&&node.isChecked()){
						goodsPriceIdList.add(node.getId());
					}
				}
				Intent intent = new Intent();
				intent.putExtra(GoodsPrice._ID, goodsPriceIdList);
				setResult(RESULT_OK,intent);
				finish();
				break;
			case R.id.selectInventoryGoodsCancel:
				finish();
				break;
			}
		}
	};
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.e("ShowTreeView:onTouchEvent", "true");
		onResume();
		return true;
	}
}