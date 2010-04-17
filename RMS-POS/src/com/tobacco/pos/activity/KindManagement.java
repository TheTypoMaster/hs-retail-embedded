package com.tobacco.pos.activity;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.tobacco.pos.contentProvider.GoodsKindCPer;
import com.tobacco.pos.extentActivity.KindManagementExtention;
import com.tobacco.pos.util.ProcessStr;
import com.tobacco.pos.util.TreeBranchNode;
import com.tobacco.pos.util.TreeLeafNode;
import com.tobacco.pos.util.TreeNode;
import com.tobacco.R;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

public class KindManagement extends Activity {
	public static final int DELETEFLAG = 1;
	public static final int INFOFLAG = 2;
	public static final int EDITFLAG = 3;
	public static final int ADDFLAG = 4;
//	public static final int ZOOMFLAG = 5;

	private TextView kindInfoTView;//显示某种类的详细信息
	public int maxLevel = 0;//总共有几层，在显示的时候有用
	
	public TreeNode tree[];
	public TreeNode root[];

	public Vector<Integer> levels = new Vector<Integer>();
	public Vector<String> names = new Vector<String>();
	public Vector<Integer> ids = new Vector<Integer>();
	
	public Cursor c = null;

	public String selectedName = "";// 选择种类的名字
	public int selectedId = -1;//选择种类的ID
	
	private Map<Integer, Integer> clickCount = new Hashtable<Integer, Integer>();
//	private ArrayList<Integer> allDescendantId = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kindmanagement);
		
		kindInfoTView = (TextView)KindManagement.this.findViewById(R.id.kindInfoTView);
		
		lookup();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 0, 0, "增加种类");
		menu.add(0, 1, 1, "删除该类");
		menu.add(0, 2, 2, "编辑该类");
	
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 0://增加类别
			add();
			break;
		case 1://删除类别
			delete();
			break;
		case 2://编辑类别
			edit();
			break;
		
		}
		return super.onOptionsItemSelected(item);
	}

	private void add() {

		if(selectedId == -1 && !selectedName.equals("TOP")){
			Toast.makeText(this, "请先选择父类别", Toast.LENGTH_SHORT).show();
		}
		else{
			Intent addIntent = new Intent(this, KindManagementExtention.class);
			addIntent.putExtra("REQUESTCODE", ADDFLAG);
			if(selectedName.equals("TOP")){//选择是最高的级别
				addIntent.putExtra("parentName", "TOP");
				addIntent.putExtra("parentId", 0);
				addIntent.putExtra("parentLevel", 0);
				
			}
			else{
				 Cursor c = getContentResolver().query(GoodsKindCPer.CONTENT_URI, null, _ID + " = ? ", new String[]{selectedId+""}, null);
				 c.moveToFirst();
			
				addIntent.putExtra("parentName", c.getString(1));
				addIntent.putExtra("parentId", selectedId);
				addIntent.putExtra("parentLevel", levels.get(ids.indexOf(selectedId))+1);
			}
			this.startActivityForResult(addIntent, ADDFLAG);
	
		}

	}
	private void delete() {

		if (selectedName.equals("")) {
			Toast.makeText(this, "请选择要删除的类别", Toast.LENGTH_SHORT).show();

		} else {
			if(selectedName.equals("TOP"))
				Toast.makeText(this, "该类别不能删除", Toast.LENGTH_SHORT).show();
			else{
				Intent deleteGoodsKindIntent = new Intent(KindManagement.this, KindManagementExtention.class);
				deleteGoodsKindIntent.putExtra("REQUESTCODE", DELETEFLAG);
				deleteGoodsKindIntent.putExtra("selectedId", selectedId);
				
				this.startActivityForResult(deleteGoodsKindIntent, DELETEFLAG);
			}
		}
	}
	
	private void moreInfo(int selectedId) {// 点击某一类别，显示更加详细的信息
		

		if(selectedId == -1){
			kindInfoTView.setText("");
			//如果是TOP类别，不显示任何东西
		}
	
		else {// 选择了某一商品类别
			
			Intent moreInfoIntent = new Intent(KindManagement.this, KindManagementExtention.class);
			
			moreInfoIntent.putExtra("REQUESTCODE", INFOFLAG);
			moreInfoIntent.putExtra("selectedId", selectedId);
			
			this.startActivityForResult(moreInfoIntent, INFOFLAG);
			
		}
		
	}

	private void edit(){
		if(selectedName.equals("TOP")){
			return;//TOP不可编辑
		}
		if(selectedName.equals("")){
			Toast.makeText(this, "请先选择某一类别", Toast.LENGTH_SHORT).show();
		}
		else{		 
		
			Intent editIntent = new Intent(this,KindManagementExtention.class);
			editIntent.putExtra("selectedId", selectedId);
		
			editIntent.putExtra("REQUESTCODE", EDITFLAG);
			this.startActivityForResult(editIntent, EDITFLAG);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK){//如果成功返回
			if(requestCode == DELETEFLAG){//如果是删除请求
				boolean hasChildren = data.getBooleanExtra("hasChildren", false);
				if(hasChildren){
					Toast.makeText(this, "该类别不允许删除", Toast.LENGTH_SHORT).show();
				}
				else{
					boolean deleteFlag = data.getBooleanExtra("deleteFlag", true);
					if(deleteFlag){
						Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
						lookup();
					}
				}
			}
			else if(requestCode == INFOFLAG){//如果是查看详细信息的请求
				kindInfoTView.setText(data.getStringExtra("moreInfo"));
			}
			else if(requestCode == EDITFLAG){
				boolean hasUpdated = data.getBooleanExtra("hasUpdated", false);
				if(hasUpdated){
					String updateInfo = data.getStringExtra("updateInfo");
					Toast.makeText(this, updateInfo, Toast.LENGTH_SHORT).show();
					lookup();
					kindInfoTView.setText("");
				}
			}
			else if(requestCode == ADDFLAG){
				String addInfo = data.getStringExtra("addInfo");
				if(addInfo.startsWith("成功")){
					Toast.makeText(this, addInfo, Toast.LENGTH_SHORT).show();
					lookup();
				}
				else{
					Toast.makeText(this, addInfo, Toast.LENGTH_SHORT).show();
				}
				
			}
//			else if(requestCode == ZOOMFLAG){
//				allDescendantId = data.getIntegerArrayListExtra("allDescendantIdList");
//				
//			}
		}
	}
	

	private void lookup() {
		
		final LinearLayout l = (LinearLayout) this
				.findViewById(R.id.wholeLayout);
		final TextView tempView = (TextView) this.findViewById(R.id.temp);
		
		l.removeAllViews();
		l.addView(tempView);

		final TextView top = new TextView(this);
		top.setText("TOP");
		top.setId(-1);
		clickCount.put(-1, 0);
	
		top.setTextColor(tempView.getTextColors());
		top.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				((TextView) v).setTextColor(Color.RED);// 点击的文本颜色改为红色
				
				for (int i = 0; i < l.getChildCount(); i++) {//其他的文本改成白色
					if (l.getChildAt(i) != v) {
						((TextView) l.getChildAt(i)).setTextColor(tempView
								.getTextColors());
					}
				}
				selectedId = -1;
				selectedName = ((TextView) v).getText().toString();
			
				kindInfoTView.setText("");
			
			}

		});
		top.setOnLongClickListener(new OnLongClickListener(){

			public boolean onLongClick(View v) {
				
				int tempClickCount = clickCount.get(-1);
				
				if(tempClickCount%2 == 0){
					for(int i=2;i<l.getChildCount();i++){
						TextView temp = (TextView)l.getChildAt(i);
						temp.setHeight(0);
					}
				}
				else{
					for(int i=2;i<l.getChildCount();i++){
						TextView temp = (TextView)l.getChildAt(i);
						temp.setHeight(top.getHeight());
					}
				}
				tempClickCount++;
				
				clickCount.remove(-1);
				clickCount.put(-1, tempClickCount);
				
				return false;
			}
			
		});

		l.addView(top);
		TextView t;

		c = this.managedQuery(GoodsKindCPer.CONTENT_URI, null, null, null, null);
		c.moveToFirst();

		for (int i = 0; i < c.getCount(); i++) {

			if (c.getInt(3) > maxLevel) {
				maxLevel = c.getInt(3);
			}
			c.moveToNext();
		}
		
		c.moveToFirst();
		
		StringBuffer sb = new StringBuffer();
		
		if(maxLevel != 1){
			c.moveToFirst();
			tree = new TreeNode[c.getCount()];
			
			for (int i = 0; i < c.getCount(); i++) {
				if (c.getInt(3) == maxLevel) {
					tree[i] = new TreeLeafNode(c.getInt(0), c.getString(1), c
						.getInt(2), c.getInt(3)-1, c.getString(4));
				} else {
					tree[i] = new TreeBranchNode(c.getInt(0), c.getString(1), c
						.getInt(2), c.getInt(3)-1, c.getString(4));
				}
				c.moveToNext();
			}
		
			
			for (int i = 0; i < tree.length; i++) {
				for (int j = i; j < tree.length; j++) {
					if (tree[j].getPId() == tree[i].getId()) {
						tree[i].addSubNode(tree[j]);
					}
				}
			}
			
			for (int i = 0; i < tree.length; i++) {
		
				if (tree[i].getLevel() == 0)
					sb.append(tree[i].print());
			}
		}

		else{//如果最大只有1层
			c.moveToFirst();
			for(int i=0;i<c.getCount();i++){
				sb.append("[nodeId=");
				sb.append(c.getInt(0));
				sb.append(" nodeName=");
				sb.append(c.getString(1));
				sb.append(" pId=");
				sb.append(c.getInt(2));
				sb.append(" level=");
				sb.append(c.getInt(3));
				sb.append(" comment=");
				sb.append(c.getString(4));
				sb.append(']');
				c.moveToNext();
			}
		}
		
		ProcessStr pStr = new ProcessStr(sb.toString());
		
		levels = pStr.getLevels();
		names = pStr.getNames();
		ids = pStr.getIds();

		String name = "";// 每个商品种类要显示的名称
		int level;
		int id;
		for (int i = 0; i < levels.size(); i++) {
			t = new TextView(this);
			name = names.get(i);
			level = levels.get(i);
			id = ids.get(i);
			
			if(name.contains(">"))
				t.setText(name.substring(name.lastIndexOf(">")+1));
			else
				t.setText(name);
			
			t.setId(id);
			
			clickCount.put(id, 0);
			
			t.setTextColor(tempView.getTextColors());
		
			if(maxLevel == 1)
				t.setPadding(20 * level, 0, 0, 0);
			else
				t.setPadding(20 * (level+1), 0, 0, 0);
			

			t.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					((TextView) v).setTextColor(Color.RED);// 点击的文本颜色改为红色
					selectedId = v.getId();
					for (int i = 0; i < l.getChildCount(); i++) {// 其他的文本改成白色
						if (l.getChildAt(i) != v) {
							((TextView) l.getChildAt(i)).setTextColor(tempView
									.getTextColors());
						}
					}

					selectedName = ((TextView) v).getText().toString();
					moreInfo(selectedId);
				}
			});
		
			t.setOnLongClickListener(new OnLongClickListener(){

				public boolean onLongClick(View v) {
				
//					Intent zoomIntent = new Intent(KindManagement.this, KindManagementExtention.class);
//					zoomIntent.putExtra("REQUESTCODE", ZOOMFLAG);
//					zoomIntent.putExtra("ancestorId", v.getId());
//					
//					KindManagement.this.startActivityForResult(zoomIntent, ZOOMFLAG);
					ArrayList<Integer> allDescendantId = null;//KindManagement.this.getAllDescendantByAncestorId(v.getId(), c);
					Log.d("lyq", "kkkkkkkkkk" + allDescendantId.size());
					int tempClickCount = clickCount.get(v.getId());
					
					if(tempClickCount%2 == 0){
						for(int i=2;i<l.getChildCount();i++){
							TextView temp = (TextView)l.getChildAt(i);
							if(allDescendantId.contains(temp.getId())){
								temp.setHeight(0);
							}
						}
					}
					else{
						for(int i=2;i<l.getChildCount();i++){
							TextView temp = (TextView)l.getChildAt(i);
							if(allDescendantId.contains(temp.getId())){
								temp.setHeight(top.getHeight());
							}
						}
					}
					tempClickCount++;
					clickCount.remove(v.getId());
					clickCount.put(v.getId(), tempClickCount);
				
					return false;
				}
				
			});

			l.addView(t);

		}
	}

//	public List<Integer> getChildrenByParentId(int parentId, Cursor c){
////		Cursor c = getContentResolver().query(GoodsKindCPer.CONTENT_URI, null, " parent = ? ", new String[]{parentId+""}, null);
//		if(c.getCount()>0){
//			List<Integer> temp = new ArrayList<Integer>();
//			c.moveToFirst();
//			for(int i=0;i<c.getCount();i++){
//				temp.add(c.getInt(0));
//				c.moveToNext();
//			}
//			return temp;
//		}
//		return new ArrayList<Integer>();
//		
//	}
//	public ArrayList<Integer> getAllDescendantByAncestorId(int ancestorId, Cursor c){
//		ArrayList<Integer> t = new ArrayList<Integer>();
//		List<Integer> temp = new ArrayList<Integer>();
//		temp = getChildrenByParentId(ancestorId , c);
//		
//		for(int i=0;i<temp.size();i++){
//			t.add(temp.get(i));
//		}
//		for(int i=0;i<t.size();i++){
//			Integer tempId = t.get(i);
//			temp =  getChildrenByParentId(tempId, c);
//			
//			for(int j=0;j<temp.size();j++){
//				t.add(temp.get(j));
//			}
//		}
//		
//		return t;
//	}

}