package com.tobacco.pos.activity;

import java.util.Vector;

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
	
	private TextView kindInfoTView;//显示某种类的详细信息
	public int maxLevel;//总共有几层，在显示的时候有用
	
	public TreeNode tree[];
	public TreeNode root[];

	public Vector<Integer> levels = new Vector<Integer>();// ��Ų��
	public Vector<String> names = new Vector<String>();// �������

	public Cursor c = null;

	public String selectedName = "";// 选择种类的名字
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kindmanagement);
		
		kindInfoTView = (TextView)KindManagement.this.findViewById(R.id.kindInfoTView);
		lookup();
	}

	@Override
	protected void onResume() {
		super.onResume();
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

		if (selectedName.equals("")) {
			Toast.makeText(this, "请先选择父类别", Toast.LENGTH_SHORT).show();
		} else {// selectedName有值，代表有选择父类了。
			if (selectedName.equals("TOP")) {//选择了最高类别
				Intent intent = new Intent(this, AddGoodsKind.class);
				intent.putExtra("parentName", "TOP");
				intent.putExtra("parentId", 0);
				intent.putExtra("parentLevel", 0);
				this.startActivity(intent);
			} else {
				int id = 0;
				int level = 0;
				Intent intent = new Intent(this, AddGoodsKind.class);
				intent.putExtra("parentName", selectedName);// ��Ÿ���������
				c.moveToFirst();
				for (int i = 0; i < c.getCount(); i++) {
					if (c.getString(1).equals(selectedName)) {
						id = c.getInt(0);
						level = c.getInt(3);
						break;
					}
					c.moveToNext();
				}
				intent.putExtra("parentId", id);// ��Ÿ�����ID
				intent.putExtra("parentLevel", level);// ��Ÿ����Ĳ��
				this.startActivity(intent);

			}
		}
	}
	private void delete() {

		if (selectedName.equals("")) {
			Toast.makeText(this, "请选择要删除的类别", Toast.LENGTH_SHORT).show();

		} else {
			int deleteKId = 0;//要删除的类别ID
			boolean flag = true;//是否可删除的标志,有子类的类别不能删除
			c.moveToFirst();
			for (int i = 0; i < c.getCount(); i++) {//根据名字查找选择类别的ID
				if (c.getString(1).equals(selectedName)) {
					deleteKId = c.getInt(0);
					break;
				}
				c.moveToNext();
			}
			c.moveToFirst();
			for (int i = 0; i < c.getCount(); i++) {
				if (c.getInt(2) == deleteKId) {
					flag = false;
					break;
				}
				c.moveToNext();
			}
			if (flag) {// 可删除

				//传递给GoodsKindCPer删除
				getContentResolver().delete(AllTables.GoodsKind.CONTENT_URI, " _id = " + deleteKId, null);

				Toast.makeText(this, "成功删除类别:" + selectedName, Toast.LENGTH_SHORT).show();
				selectedName = "";
				this.onResume();//
			} else {
				Toast.makeText(this, "该类别不能删除", Toast.LENGTH_SHORT).show();

			}
		}
	}
	
	private void lookup() {
		int temp = 0;// 取最大层数时要用到的临时变量
		
		final LinearLayout l = (LinearLayout) this
				.findViewById(R.id.wholeLayout);
		final TextView tempView = (TextView) this.findViewById(R.id.temp);
		
		l.removeAllViews();
		l.addView(tempView);

		final TextView top = new TextView(this);
		top.setText("TOP");
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
				selectedName = ((TextView) v).getText().toString();
			
				kindInfoTView.setText("\n\n\n"+moreInfo(selectedName));
			}

		});
		top.setOnLongClickListener(new OnLongClickListener(){

			public boolean onLongClick(View v) {
			    Log.d("lyq",""+((TextView)v).getText());
				return false;
			}
			
		});

		l.addView(top);
		TextView t;

		c = this.managedQuery(AllTables.GoodsKind.CONTENT_URI, null, null, null, null);
		c.moveToFirst();

		for (int i = 0; i < c.getCount(); i++) {

			if (c.getInt(3) > temp) {
				temp = c.getInt(3);

			}
			c.moveToNext();
		}
		maxLevel = temp;
		c.moveToFirst();

		tree = new TreeNode[c.getCount()];

		for (int i = 0; i < c.getCount(); i++) {

			if (c.getInt(3) == maxLevel) {
				tree[i] = new TreeLeafNode(c.getInt(0), c.getString(1), c
						.getInt(2), c.getInt(3) - 1, c.getString(4));
			} else {
				tree[i] = new TreeBranchNode(c.getInt(0), c.getString(1), c
						.getInt(2), c.getInt(3) - 1, c.getString(4));
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
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tree.length; i++) {
			if (tree[i].getLevel() == 0)
				sb.append(tree[i].print());

		}

		ProcessStr pStr = new ProcessStr(sb.toString());
		levels = pStr.getLevels();
		names = pStr.getNames();

		String name = "";// 每个商品种类要显示的名称
		int level;
		for (int i = 0; i < levels.size(); i++) {
			t = new TextView(this);
			name = names.get(i);
			level = levels.get(i);

			t.setText(name);
			t.setTextColor(tempView.getTextColors());
			t.setPadding(20 * (level + 1), 0, 0, 0);

			t.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					((TextView) v).setTextColor(Color.RED);// 点击的文本颜色改为红色
					for (int i = 0; i < l.getChildCount(); i++) {// 其他的文本改成白色
						if (l.getChildAt(i) != v) {
							((TextView) l.getChildAt(i)).setTextColor(tempView
									.getTextColors());
						}
					}

					selectedName = ((TextView) v).getText().toString();
					kindInfoTView.setText("\n\n\n"+moreInfo(selectedName));

				}
			});
			t.setOnLongClickListener(new OnLongClickListener(){

				public boolean onLongClick(View v) {
					
					return false;
				}
				
			});

			l.addView(t);

		}
	}

	private String moreInfo(String searchName) {// 点击某一类别，显示更加详细的信息
		

		if(searchName.equals("TOP")){
			return "";//如果是TOP类别，不显示任何东西
		}
	
		else {// 选择了某一商品类别
			StringBuffer sb = new StringBuffer();
			
			c.moveToFirst();
			int pId = 0;// 父类别的ID
			String pName = "";// 父类别的名称
			for (int i = 0; i < c.getCount(); i++) {
				if (c.getString(1).equals(searchName)) {
					sb.append("ID:");
					sb.append(c.getInt(0));
					sb.append("\n");

					sb.append("名称:");
					sb.append(c.getString(1));
					sb.append("\n");

					sb.append("父类别:");
					sb.append(c.getInt(2));
					pId = c.getInt(2);
					sb.append("\n");

					sb.append("层次:");
					sb.append(c.getInt(3));
					sb.append("\n");

					sb.append("备注:");
					sb.append(c.getString(4));
					sb.append("\n");

					break;
				}
				c.moveToNext();
			}
			c.moveToFirst();
			if (pId != 0) {// 根据ID查找类别名称，将父类别改成名称而不是ID
				for (int i = 0; i < c.getCount(); i++) {
					if (c.getInt(0) == pId) {
						pName = c.getString(1);
						break;
					}
					c.moveToNext();
				}
			} else {
				pName = "TOP";
			}
			sb.replace(sb.indexOf("父类别:") + 4, sb.indexOf("\n层次"), pName);
			
			return sb.toString();
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
			int eId = 0;//要编辑类别的ID，不可改变
			String eName = "";//要编辑类别的名字
			String eComment = "";//要编辑的备注
			int pId = 0;//根据该父类别ID查找父类别名字
			String pName = "";//要编辑类别的父类别名字
			eName = selectedName;
			
			c.moveToFirst();
			
			for(int i=0;i<c.getCount();i++){
				if(c.getString(1).equals(eName)){
					eId = c.getInt(0);
					eComment = c.getString(4);
					pId = c.getInt(2);
					
					break;
				}
				c.moveToNext();
			}
			if(pId == 0)
				pName = "TOP";
			else{
				c.moveToFirst();
				for(int i=0;i<c.getCount();i++){
					if(c.getInt(0) == pId){
						pName = c.getString(1);
						break;
					}
					c.moveToNext();
				}
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