package com.tobacco.pos.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tobacco.R;
import com.tobacco.main.activity.view.RMSBaseView;
import com.tobacco.pos.activity.ConsumeSelect.Task;
import com.tobacco.pos.entity.AllTables.Goods;
import com.tobacco.pos.entity.AllTables.GoodsKind;
import com.tobacco.pos.entity.AllTables.GoodsPrice;
import com.tobacco.pos.entity.AllTables.Unit;
import com.tobacco.pos.searchStrategy.SearchState;
import com.tobacco.pos.util.db.POSDbHelper;
import com.tobacco.pos.util.tree.CheckTreeView;
import com.tobacco.pos.util.tree.TreeNode;

public class SelectInventoryGoods extends RMSBaseView {
    /** Called when the activity is first created. */
	
	private static String TAG = "SelectInventoryGoods";
	
	ArrayList<TreeNode> treeNodes;
	TreeNode root;
	ArrayList<Integer> exitsGoodsPrice;
	ProgressDialog pd;
	final int TASK_COMPLETE = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_tree_view);
        Log.i(TAG, "onCreate");
        
        Intent intent = getIntent();
        exitsGoodsPrice = intent.getIntegerArrayListExtra(GoodsPrice._ID);
        treeNodes = new ArrayList<TreeNode>();
        root = new TreeNode(true,"Root");
        
        this.setVisible(false);
        startTask();
 
        Button confirm = (Button)findViewById(R.id.selectInventoryGoodsConfirm);
        Button cancel = (Button)findViewById(R.id.selectInventoryGoodsCancel);
        confirm.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
        Log.i(TAG, "show all nodes");      
    }
    
    private void getKindNodes(){
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
		Log.i(TAG, "get all kind nodes");
    }
    
    private void setKindNodes(){
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
		Log.i(TAG, "set all kind nodes");
    }
    
    private void getAndSetGoodsNodes(){
    	POSDbHelper databaseHelper = new POSDbHelper(this);
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		String[] projection = new String[]{"GoodsPrice."+GoodsPrice._ID,"Goods."+Goods.goodsName,"Unit."+Unit.name,"Goods."+Goods.kindId};
		String selection = "GoodsPrice."+GoodsPrice.goodsId+" =Goods."+Goods._ID+" AND "+"GoodsPrice."+GoodsPrice.unitId+" =Unit."+Unit._ID;
		
 		Cursor c = db.query("GoodsPrice,Goods,Unit", projection, selection, null, null, null, null);
		if(c.getCount()>0){
			c.moveToFirst();
			for(int i = 0; i<c.getCount();i++){
				int goodsPriceId = c.getInt(0);
				if(exitsGoodsPrice==null||!exitsGoodsPrice.contains(Integer.valueOf(goodsPriceId))){
					for(TreeNode kind : treeNodes){
						if(kind.getId()==c.getInt(3)){
							TreeNode goodsNode = new TreeNode(c.getString(1)+" "+c.getString(2),goodsPriceId,"goods");						
							kind.addNode(goodsNode);						
							goodsNode.setLevel(kind.getLevel()+1);
						}						
					}
				}
			}
		}
 
		Log.i(TAG, "get all goods node and add to kind nodes");
    }
    
    private void showInventoryGoods(){
    	CheckTreeView tree = (CheckTreeView)findViewById(R.id.tree);
        tree.init(root);
        this.setVisible(true);
    }
    
    private void startTask(){   
		Log.e(TAG, "startTask()");		
		showDialog();            
        Thread task = new Thread(new Task());  
        task.start();  
    } 

	private void showDialog(){
		Log.e(TAG, "showDialog()");
		pd = ProgressDialog.show(this, "请稍候...", "Loading...", true,  
                false);
	}
	
	public class Task implements Runnable {  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
        	Log.e(TAG, "Task.run()");
        	getKindNodes();      		
    		setKindNodes();		
    		getAndSetGoodsNodes();
            messageListener.sendEmptyMessage(TASK_COMPLETE);             
        }  
          
    }  
	
	private Handler messageListener = new Handler(){  
        public void handleMessage(Message msg) {  
        	Log.e(TAG, "messageListener.handleMessage()");
            switch(msg.what){  
            case TASK_COMPLETE:   
            	Log.e(TAG, "Message:TASK_COMPLETE");
                pd.dismiss(); 
                showInventoryGoods();
                break;  
                  
            }  
        }  
    }; 
    
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