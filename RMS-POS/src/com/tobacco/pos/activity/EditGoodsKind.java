package com.tobacco.pos.activity;

import com.tobacco.R;
import com.tobacco.pos.entity.AllTables;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditGoodsKind extends Activity {

	private TextView parentText;
	private Button ok;
	private Button reset;
	private EditText nameEText;
	private EditText commentEText;
	private int id;// 这四个变量从前面的Activity获得
	private String previousName;// 没改之前的名字
	private String previousComment;// 没改之前的备注
	private String parentName;// 改类别的父类别名称

	private String afterName;// 改之后的名字
	private String afterComment;// 改之后的备注

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.addeditkinddialog);

		this.setTitle("编辑类别");

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

				if (afterName.trim().equals("")) {// 判断名字是否为空
					Toast.makeText(EditGoodsKind.this, "名字不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					if (previousName.equals(afterName)) {//如果名字没有更改
						if(previousComment.equals(afterComment));
						else{
							ContentValues value = new ContentValues();
							if(parentName.equals("TOP")){
								value.put("name", afterName);
								value.put("comment", afterComment);
							}
							else{
								value.put("name", parentName + "->" + afterName);
								value.put("comment", afterComment);
							}

							int count = getContentResolver().update(AllTables.GoodsKind.CONTENT_URI, value,	" _id = " + id, null);
							if(count>0)
								Toast.makeText(EditGoodsKind.this, "成功修改了类别:"+previousName+" 的备注", Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(EditGoodsKind.this, "更改类别:"+ previousName + " 的备注失败", Toast.LENGTH_SHORT).show();
							finish();
						}
							
					} else {

						Cursor c = null;
						if (parentName.equals("TOP"))// 如果是第一层的类别
							c = getContentResolver().query(
									AllTables.GoodsKind.CONTENT_URI, null,
									" name = ? ", new String[] { afterName },
									null);
						else
							c = getContentResolver().query(
									AllTables.GoodsKind.CONTENT_URI,
									null,
									" name = ? ",
									new String[] { parentName + "->"
											+ afterName }, null);
						if (c.getCount() > 0) {
							Toast.makeText(EditGoodsKind.this,
									"同类别下已有同样名字的类别，更新失败", Toast.LENGTH_SHORT)
									.show();
							nameEText.setText("");
						} else {
							ContentValues value = new ContentValues();
							value.put("name", parentName + "->" + afterName);
							value.put("comment", afterComment);

							int count = getContentResolver().update(
									AllTables.GoodsKind.CONTENT_URI, value,
									" _id = " + id, null);

							if (!previousName.equals(afterName) && count != 0) {
								Toast
										.makeText(
												EditGoodsKind.this,
												"成功将类别：" + previousName
														+ " 改名为：" + afterName,
												Toast.LENGTH_SHORT).show();
							} else if (!previousComment.equals(afterComment)
									&& count != 0) {
								Toast.makeText(EditGoodsKind.this,
										"成功更新类别：" + previousName + "的备注",
										Toast.LENGTH_SHORT).show();
							}
							finish();
						}
					}
				}

			}

		});
		reset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}

		});

	}

}
