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

	private Button kindreturn ;//������ҳ��İ�ť
	private TableCreater tableHelper = null;
	private static String[] FROM = { _ID, "name", "parent", "level", "comment" };
	private static final String TABLE_NAME = "GoodsKind";// ���ݱ�

	public int maxLevel;// ���㣬��������ʾ��״�ṹ

	public TreeNode tree[];
	public TreeNode root[];

	public Vector<Integer> levels = new Vector<Integer>();// ��Ų��
	public Vector<String> names = new Vector<String>();// �������

	public Cursor c = null;

	public String selectedName = "";// ����ı�ʱ�����ִ���ڴˣ������ڲ鿴��ϸ��Ϣ�����Ӻ��ӵȡ�

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kindmanagement);
		
		kindreturn = (Button)this.findViewById(R.id.kindreturn);
		kindreturn.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent(KindManagement.this,Main.class);//������ҳ��
				KindManagement.this.startActivity(intent);
			}
			
		});
		
		tableHelper = new TableCreater(this);// �½����ݿ������

		SQLiteDatabase db = tableHelper.getReadableDatabase();
		
		try {// ���в�ѯ��������ҵ��˾���ʾ��״�ṹ�����û���ҵ��͹��������˶���ʾ��״�ṹ
			c = db.query(TABLE_NAME, FROM, null, null, null, null, null);
			
		} catch (Exception e) {
			tableHelper.createTable(tableHelper.getWritableDatabase());
			
		}
		
		db.close();
		lookup();
	}

	@Override
	protected void onResume() {// �����Ʒ��Activityִ�к�ִ�д˺���
		super.onResume();

		lookup();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 0, 0, "�����Ʒ����");
		menu.add(0, 1, 1, "ɾ�������");
		menu.add(0, 2, 2, "�鿴��ϸ��Ϣ");
		menu.add(0, 3, 3, "�༭�����");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 0:// ������Ʒ�����
			add();
			break;
		case 1:// �����Ʒ����
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

	// ������Ʒ���
	private void add() {

		if (selectedName.equals("")) {
			Builder b = new Builder(this);
			b.setTitle("��ʾ");
			b.setMessage("����ѡ����");
			b.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

				}

			});
			b.show();
		} else {// selectedNameΪҪ��������ĸ��࣬addedNameΪҪҪ������������֣�addedCommentΪ�䱸ע
			if (selectedName.equals("TOP")) {//����Ƕ������
				Intent intent = new Intent(this, AddGoodsKind.class);
				intent.putExtra("parentName", "TOP");
				intent.putExtra("parentId", 0);
				intent.putExtra("parentLevel", 0);
				this.startActivity(intent);
			} else {//����
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

	// ɾ����ѡ���
	private void delete() {
		Builder deleteInfo = new Builder(this);
		deleteInfo.setTitle("ɾ����ʾ");
		deleteInfo.setIcon(android.R.drawable.ic_dialog_info);
		deleteInfo.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}

				});

		if (selectedName.equals("")) {
			deleteInfo.setMessage("��ѡ��ĳһ���");
			deleteInfo.show();
		} else {
			int deletePId = 0;// Ҫɾ�������ID
			boolean flag = true;// �Ƿ��ɾ���ı�־
			c.moveToFirst();
			for (int i = 0; i < c.getCount(); i++) {// �������ֲ���ѡ������ID
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
			if (flag) {// ��ɾ��
				SQLiteDatabase db = tableHelper.getWritableDatabase();
				db.execSQL("delete from " + TABLE_NAME + " where " + _ID
						+ " = " + deletePId);
				deleteInfo.setMessage("�ɹ�ɾ�������");
				deleteInfo.show();
				selectedName = "";
				this.onResume();// ɾ����ɺ������״�ṹ
			} else {// ����ɾ��
				deleteInfo.setMessage("����𲻿�ɾ��");
				deleteInfo.show();
			}
		}
	}

	// ��ʾ������״�ṹ
	private void lookup() {
		int temp = 0;// ȡ������ʱҪ�õ�����ʱ����

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

				((TextView) v).setTextColor(Color.RED);// ������ı���ɫ��Ϊ��ɫ

				for (int i = 0; i < l.getChildCount(); i++) {// �������ı��ĳɰ�ɫ
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

		String name = "";// ÿ����Ʒ����Ҫ��ʾ������
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

					((TextView) v).setTextColor(Color.RED);// ������ı���ɫ��Ϊ��ɫ

					for (int i = 0; i < l.getChildCount(); i++) {// �������ı��ĳɰ�ɫ
						if (l.getChildAt(i) != v) {
							((TextView) l.getChildAt(i)).setTextColor(tempView
									.getTextColors());
						}
					}

					selectedName = ((TextView) v).getText().toString();

				}// ���TextViewʱ���¼�

			});

			l.addView(t);

		}
	}

	// �鿴��ϸ��Ϣ
	private void moreInfo(String searchName) {// ���ĳһ�����ʾ������ϸ����Ϣ

		if(searchName.equals("TOP")){
			return;//�����TOP��𣬲���ʾ�κζ���
		}
		Builder info = new Builder(this);
		if (searchName.equals("")) {// ���û��ѡ�����
			info.setTitle("��ʾ");
			info.setIcon(android.R.drawable.ic_dialog_info);
			info.setMessage("����ѡ��ĳһ��Ʒ���");
		} else {// ѡ����ĳһ��Ʒ���
			StringBuffer sb = new StringBuffer();
			info.setTitle("��ϸ��Ϣ");
			c.moveToFirst();
			int pId = 0;// ������ID
			String pName = "";// ����������
			for (int i = 0; i < c.getCount(); i++) {
				if (c.getString(1).equals(searchName)) {
					sb.append("ID:");
					sb.append(c.getInt(0));
					sb.append("\n");

					sb.append("����:");
					sb.append(c.getString(1));
					sb.append("\n");

					sb.append("�����:");
					sb.append(c.getInt(2));
					pId = c.getInt(2);
					sb.append("\n");

					sb.append("���:");
					sb.append(c.getInt(3));
					sb.append("\n");

					sb.append("��ע:");
					sb.append(c.getString(4));
					sb.append("\n");

					break;
				}
				c.moveToNext();
			}
			c.moveToFirst();
			if (pId != 0) {// ����ID����������ƣ��������ĳ����ƶ�����ID
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
			sb.replace(sb.indexOf("�����:") + 4, sb.indexOf("\n���"), pName);
			info.setIcon(android.R.drawable.ic_dialog_info);
			info.setMessage(sb.toString());
		}
		info.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}

		});
		info.show();
	}

	private void edit(){
		Builder editB = new Builder(this);
		editB.setTitle("�༭��ʾ");
		editB.setIcon(android.R.drawable.ic_dialog_info);
		editB.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				
			}
			
		});
		if(selectedName.equals("TOP")){
			return;//TOP���ɱ༭
		}
		if(selectedName.equals("")){
			editB.setMessage("����ѡ��ĳһ���");
			editB.show();
		}
		else{
			int eId = 0;//Ҫ�༭����ID�����ɸı�
			String eName = "";//Ҫ�༭��������
			String eComment = "";//Ҫ�༭�ı�ע
			int pId = 0;//���ݸø����ID���Ҹ��������
			String pName = "";//Ҫ�༭���ĸ��������
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