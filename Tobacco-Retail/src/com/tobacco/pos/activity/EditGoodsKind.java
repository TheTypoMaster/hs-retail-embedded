package com.tobacco.pos.activity;

import static android.provider.BaseColumns._ID;

import com.tobacco.pos.util.TableCreater;
import com.tobacco.R;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditGoodsKind extends Activity {

	private static final String TABLE_NAME = "GoodsKind";// 数据表
	private static String[] FROM = { _ID, "name", "parent", "level", "comment" };

	private TableCreater tableHelper = null;

	private TextView parentText;
	private Button ok;
	private Button reset;
	private EditText nameEText;
	private EditText commentEText;
	private int id;// 这四个变量从前面的Activity获得
	private String previousName;//没改之前的名字
	private String previousComment;//没改之前的备注
	private String parentName;//改类别的父类别名称
	
	private String afterName;//改之后的名字
	private String afterComment;//改之后的备注

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.addeditkinddialog);
		tableHelper = new TableCreater(this);

		parentText = (TextView) this.findViewById(R.id.parentName);
		Intent intent = this.getIntent();
		parentName = intent.getStringExtra("pName");
		parentText.setText(parentName);

		ok = (Button) this.findViewById(R.id.ok);
		reset = (Button) this.findViewById(R.id.reset);
		nameEText = (EditText) this.findViewById(R.id.name);
		previousName = intent.getStringExtra("eName");
		nameEText.setText(previousName);
		commentEText = (EditText) this.findViewById(R.id.comment);
		previousComment = intent.getStringExtra("eComment");
		commentEText.setText(previousComment);
		
		id = intent.getIntExtra("eId", 0);

		ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				afterName = nameEText.getText().toString();
				afterComment = commentEText.getText().toString();
				
				Builder b = new Builder(EditGoodsKind.this);
				b.setTitle("提示");
				b.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if(!afterName.trim().equals(""))
									finish();
							}

						});
				
				

				if (afterName.trim().equals("")) {// 判断名字是否为空
					b.setMessage("请输入名字");
					b.show();
				} else {//在此处修改
					SQLiteDatabase d = tableHelper.getWritableDatabase();

					ContentValues value = new ContentValues();
					value.put("name", afterName);
					value.put("comment", afterComment);

					d.update(TABLE_NAME, value, _ID + " = " + id, null);
					d.close();
					if(!previousName.equals(afterName))
					{
						b.setMessage("成功将类别："+previousName+" 改名为："+afterName);
						b.show();
					}
					else{
						finish();
					}
				}
				
			}

		});
		reset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}

		});

		nameEText.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {

				if (!hasFocus) {// 失去焦点时查找数据库中是否有相同名字的类别
					Builder b = new Builder(EditGoodsKind.this);
					String inputName = ((EditText) v).getText().toString();
					if (!inputName.equals(previousName)) {// 如果有改类别名字
						SQLiteDatabase db = tableHelper.getReadableDatabase();
						Cursor c = db.query(TABLE_NAME, FROM, null, null, null,
								null, null);

						c.moveToFirst();
						for (int i = 0; i < c.getCount(); i++) {
							if (c.getString(1).equals(inputName)) {
								b.setTitle("提示");
								b.setMessage("已有该类别,请重新输入");
								b.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												nameEText.setText("");
											}

										});
								b.show();
								break;
							}
							c.moveToNext();
						}
						db.close();
					}
					if(nameEText.getText().toString().trim().equals("")){
						b.setTitle("提示");
						b.setPositiveButton("确定", new DialogInterface.OnClickListener(){

						 
							public void onClick(DialogInterface dialog,
									int which) {
							 
							}
							
						});
						b.setMessage("请输入名字!");
						b.show();
					}
				}
			}
		});
	}

}
