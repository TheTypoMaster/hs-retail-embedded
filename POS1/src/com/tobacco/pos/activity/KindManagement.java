package com.tobacco.pos.activity;

import static android.provider.BaseColumns._ID;

import java.util.Vector;

import com.tobacco.pos.util.ProcessStr;
import com.tobacco.pos.util.TableCreater;
import com.tobacco.pos.util.TreeBranchNode;
import com.tobacco.pos.util.TreeLeafNode;
import com.tobacco.pos.util.TreeNode;


import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;

public class KindManagement extends Activity {

	private Button kindreturn ;//返回主页面的按钮
	private TableCreater tableHelper = null;
	private static String[] FROM = { _ID, "name", "parent", "level", "comment" };
	private static final String TABLE_NAME = "GoodsKind";// 数据表

	public int maxLevel;// 最大层，会用于显示树状结构

	public TreeNode tree[];
	public TreeNode root[];

	public Vector<Integer> levels = new Vector<Integer>();// 存放层次
	public Vector<String> names = new Vector<String>();// 存放名字

	public Cursor c = null;

	public String selectedName = "";// 点击文本时将名字存放于此，会用于查看详细信息，增加孩子等。

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kindmanagement);
		
		kindreturn = (Button)this.findViewById(R.id.kindreturn);
		kindreturn.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent(KindManagement.this,Main.class);//返回主页面
				KindManagement.this.startActivity(intent);
			}
			
		});
		
		tableHelper = new TableCreater(this);// 新建数据库帮助类

		SQLiteDatabase db = tableHelper.getReadableDatabase();
		
		try {// 进行查询，如果查找到了就显示树状结构，如果没查找到就构建，完了都显示树状结构
			c = db.query(TABLE_NAME, FROM, null, null, null, null, null);
			
		} catch (Exception e) {
			tableHelper.createTable(tableHelper.getWritableDatabase());
			
		}
		
		db.close();
		lookup();
	}

	@Override
	protected void onResume() {// 添加商品的Activity执行后执行此函数
		super.onResume();

		lookup();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 0, 0, "添加商品分类");
		menu.add(0, 1, 1, "删除该类别");
		menu.add(0, 2, 2, "查看详细信息");
		menu.add(0, 3, 3, "编辑该类别");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 0:// 创建商品分类表
			add();
			break;
		case 1:// 添加商品分类
			delete();
			break;
		case 2:
			moreInfo(selectedName);
			break;
		case 3:
			edit();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// 增加商品类别
	private void add() {

		if (selectedName.equals("")) {
			Builder b = new Builder(this);
			b.setTitle("提示");
			b.setMessage("请先选择父类");
			b.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

				}

			});
			b.show();
		} else {// selectedName为要增加子类的父类，addedName为要要增加子类的名字，addedComment为其备注
			if (selectedName.equals("TOP")) {//如果是顶级类别
				Intent intent = new Intent(this, AddGoodsKind.class);
				intent.putExtra("parentName", "TOP");
				intent.putExtra("parentId", 0);
				intent.putExtra("parentLevel", 0);
				this.startActivity(intent);
			} else {//否则
				int id = 0;
				int level = 0;
				Intent intent = new Intent(this, AddGoodsKind.class);
				intent.putExtra("parentName", selectedName);// 存放父类别的名字
				c.moveToFirst();
				for (int i = 0; i < c.getCount(); i++) {
					if (c.getString(1).equals(selectedName)) {
						id = c.getInt(0);
						level = c.getInt(3);
						break;
					}
					c.moveToNext();
				}
				intent.putExtra("parentId", id);// 存放父类别的ID
				intent.putExtra("parentLevel", level);// 存放父类别的层次
				this.startActivity(intent);

			}
		}
	}

	// 删除所选类别
	private void delete() {
		Builder deleteInfo = new Builder(this);
		deleteInfo.setTitle("删除提示");
		deleteInfo.setIcon(android.R.drawable.ic_dialog_info);
		deleteInfo.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}

				});

		if (selectedName.equals("")) {
			deleteInfo.setMessage("请选择某一类别");
			deleteInfo.show();
		} else {
			int deletePId = 0;// 要删除的类别ID
			boolean flag = true;// 是否可删除的标志
			c.moveToFirst();
			for (int i = 0; i < c.getCount(); i++) {// 根据名字查找选择类别的ID
				if (c.getString(1).equals(selectedName)) {
					deletePId = c.getInt(0);
					break;
				}
				c.moveToNext();
			}
			c.moveToFirst();
			for (int i = 0; i < c.getCount(); i++) {
				if (c.getInt(2) == deletePId) {
					flag = false;
					break;
				}
				c.moveToNext();
			}
			if (flag) {// 可删除
				SQLiteDatabase db = tableHelper.getWritableDatabase();
				db.execSQL("delete from " + TABLE_NAME + " where " + _ID
						+ " = " + deletePId);
				deleteInfo.setMessage("成功删除该类别");
				deleteInfo.show();
				selectedName = "";
				this.onResume();// 删除完成后更新树状结构
			} else {// 不可删除
				deleteInfo.setMessage("该类别不可删除");
				deleteInfo.show();
			}
		}
	}

	// 显示类别的树状结构
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

				for (int i = 0; i < l.getChildCount(); i++) {// 其他的文本改成白色
					if (l.getChildAt(i) != v) {
						((TextView) l.getChildAt(i)).setTextColor(tempView
								.getTextColors());
					}
				}
				selectedName = ((TextView) v).getText().toString();
			}

		});
		l.addView(top);
		TextView t;

		SQLiteDatabase db = tableHelper.getReadableDatabase();
		c = db.query(TABLE_NAME, FROM, null, null, null, null, null);
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

				}// 点击TextView时的事件

			});

			l.addView(t);

		}
	}

	// 查看详细信息
	private void moreInfo(String searchName) {// 点击某一类别，显示更加详细的信息

		if(searchName.equals("TOP")){
			return;//如果是TOP类别，不显示任何东西
		}
		Builder info = new Builder(this);
		if (searchName.equals("")) {// 如果没有选择类别
			info.setTitle("提示");
			info.setIcon(android.R.drawable.ic_dialog_info);
			info.setMessage("请先选择某一商品类别");
		} else {// 选择了某一商品类别
			StringBuffer sb = new StringBuffer();
			info.setTitle("详细信息");
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
			info.setIcon(android.R.drawable.ic_dialog_info);
			info.setMessage(sb.toString());
		}
		info.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}

		});
		info.show();
	}

	private void edit(){
		Builder editB = new Builder(this);
		editB.setTitle("编辑提示");
		editB.setIcon(android.R.drawable.ic_dialog_info);
		editB.setPositiveButton("确定", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				
			}
			
		});
		if(selectedName.equals("TOP")){
			return;//TOP不可编辑
		}
		if(selectedName.equals("")){
			editB.setMessage("请先选择某一类别");
			editB.show();
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