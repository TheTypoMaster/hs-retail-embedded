package com.tobacco.pos.activity;

import com.tobacco.R;
import com.tobacco.pos.entity.AllTables;

import android.app.Activity; 
import android.content.ContentValues; 
import android.content.Intent;
import android.database.Cursor; 
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddGoodsKind extends Activity {

	private TextView parentText;
	private Button ok;
	private Button reset;
	private EditText nameEText;
	private EditText commentEText;
	private String name;
	private String comment;
	
	private int parentId;
	private String parentName;
	private int parentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addeditkinddialog);
	
		parentText = (TextView)this.findViewById(R.id.parentName);
		Intent intent = this.getIntent();
		parentId = intent.getIntExtra("parentId",0);
		parentName = intent.getStringExtra("parentName");
		parentLevel = intent.getIntExtra("parentLevel", 0);
		parentText.setText(parentName);
		
		ok = (Button)this.findViewById(R.id.ok);
		reset = (Button)this.findViewById(R.id.reset);
		nameEText = (EditText)this.findViewById(R.id.name);
		commentEText = (EditText)this.findViewById(R.id.comment);
	
		ok.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				name = nameEText.getText().toString();
				comment = commentEText.getText().toString();
				
				if(name.equals("")){//判断名字是否为空
					Toast.makeText(AddGoodsKind.this, "请输入名字", Toast.LENGTH_SHORT).show();
				}
				else{

					ContentValues value = new ContentValues();
					value.put("name", name);
					value.put("parent", parentId);
					value.put("level", parentLevel+1);
					value.put("comment", comment);
					
					Uri result = getContentResolver().insert(AllTables.GoodsKind.CONTENT_URI, value);
					if(result != null )
					{
						Toast.makeText(AddGoodsKind.this, "成功添加类别:" + name, Toast.LENGTH_SHORT).show();
						finish();//成功添加类别，关闭对话框
					}
					else
						Toast.makeText(AddGoodsKind.this, "添加类别:" + name + "失败", Toast.LENGTH_SHORT).show();			
				}
			}
			
		});
		reset.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				finish();
			}
			
		});

		nameEText.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View v, boolean hasFocus) {
			
				if(!hasFocus){//失去焦点时查找数据库中是否有相同名字的类别
					Cursor c = getContentResolver().query(AllTables.GoodsKind.CONTENT_URI, null, null, null, null);
					String inputName = ((EditText)v).getText().toString();
					c.moveToFirst();
					for(int i=0;i<c.getCount();i++){
						if(c.getString(1).equals(inputName)){
							Toast.makeText(AddGoodsKind.this, "已有该类别，请重新输入", Toast.LENGTH_SHORT).show();
							nameEText.setText("");
							break;
						}
						c.moveToNext();
					}

				}
			}
		});
	}

}
