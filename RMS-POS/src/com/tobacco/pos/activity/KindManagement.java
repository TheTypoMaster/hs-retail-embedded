package com.tobacco.pos.activity;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.tobacco.pos.contentProvider.GoodsKindCPer;
import com.tobacco.pos.entity.AllTables;
import com.tobacco.pos.util.ProcessStr;
import com.tobacco.pos.util.TreeBranchNode;
import com.tobacco.pos.util.TreeLeafNode;
import com.tobacco.pos.util.TreeNode;
import com.tobacco.R;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
	private GoodsKindCPer kindCPer = null;
	
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kindmanagement);
		
		kindInfoTView = (TextView)KindManagement.this.findViewById(R.id.kindInfoTView);
		
		kindCPer = new GoodsKindCPer();
		
		lookup();
	}

	@Override
	protected void onResume() {
		super.onResume();
		kindInfoTView.setText("");
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
			Intent intent = new Intent(this, AddGoodsKind.class);
			if(selectedName.equals("TOP")){//选择是最高的级别
				intent.putExtra("parentName", "TOP");
				intent.putExtra("parentId", 0);
				intent.putExtra("parentLevel", 0);
				
			}
			else{
			
				intent.putExtra("parentName", selectedName);
				intent.putExtra("parentId", selectedId);
				intent.putExtra("parentLevel", levels.get(ids.indexOf(selectedId))+1);
			}
			this.startActivity(intent);
		}

	}
	private void delete() {

		if (selectedName.equals("")) {
			Toast.makeText(this, "请选择要删除的类别", Toast.LENGTH_SHORT).show();

		} else {
			if(selectedName.equals("TOP"))
				Toast.makeText(this, "该类别不能删除", Toast.LENGTH_SHORT).show();
			else{
				boolean flag = kindCPer.hasChildrenKind(selectedId);
				if(flag)
					Toast.makeText(this, "该类别不能删除", Toast.LENGTH_SHORT).show();
				else{
					int count = getContentResolver().delete(AllTables.GoodsKind.CONTENT_URI, " _id = " + selectedId, null);
					if(count>0){
						Toast.makeText(this, "成功删除类别:" + selectedName, Toast.LENGTH_SHORT).show();
						this.onResume();
					}
					else
						Toast.makeText(this, "删除类别:"+selectedName+"失败", Toast.LENGTH_SHORT).show();
				}

			}
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
			
				kindInfoTView.setText("\n\n\n"+moreInfo(selectedId));
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

		c = this.managedQuery(AllTables.GoodsKind.CONTENT_URI, null, null, null, null);
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
					kindInfoTView.setText("\n\n\n"+moreInfo(selectedId));

				}
			});
		
			t.setOnLongClickListener(new OnLongClickListener(){

				public boolean onLongClick(View v) {
				
					List<Integer> allDescendantId = kindCPer.getAllDescendantByAncestorId(v.getId());
					
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

	private String moreInfo(int selectedId) {// 点击某一类别，显示更加详细的信息
		

		if(selectedId == -1){
			return "";//如果是TOP类别，不显示任何东西
		}
	
		else {// 选择了某一商品类别
			
			return kindCPer.getGoodsKindInfoByGoodsKindId(selectedId);
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
			int eId = selectedId;//要编辑类别的ID，不可改变
			String eName = "";//要编辑类别的名字
			String eComment = "";//要编辑的备注
			String pName = "";//要编辑类别的父类别名字
			eName = selectedName;
			
			c.moveToFirst();
			
			for(int i=0;i<c.getCount();i++){
				if(c.getInt(0) == selectedId){
					eComment = c.getString(4);//获取要更改的备注
					eName = c.getString(1);
					break;
				}
				c.moveToNext();
			}
			if(eName.contains(">")){
				String temp = eName;
				pName = temp.substring(0, temp.lastIndexOf(">")-1);
				eName = temp.substring(temp.lastIndexOf(">")+1);
			}
			else{
				pName = "TOP";
				
			}

			Intent intent = new Intent(this,EditGoodsKind.class);
			intent.putExtra("eId", eId);
			intent.putExtra("eName", eName);
			intent.putExtra("eComment", eComment);
			intent.putExtra("pName", pName);
			this.startActivity(intent);
		}
	}
}