package com.tobacco.pos.activity;

import com.tobacco.R;
import com.tobacco.pos.contentProvider.GoodsKindCPer;
import com.tobacco.pos.entity.AllTables;

import android.app.Activity; 
import android.content.ContentValues; 
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddGoodsKind extends Activity {
	private GoodsKindCPer goodsKindCPer = null;

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
		
		this.setTitle("增加类别");
		
		goodsKindCPer = new GoodsKindCPer();
		
		parentText = (TextView)this.findViewById(R.id.parentName);
		Intent intent = this.getIntent();
		parentId = intent.getIntExtra("parentId",0);
		parentName = intent.getStringExtra("parentName");
		if(!parentName.equals("TOP")){//如果不是最高层，根据前面传来的父ID查找完整的名字
			parentName = goodsKindCPer.getGoodsKindNameByGoodsKindId(parentId);
		}
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
					String insertName = "";//将要插入的名字
					if(parentName.equals("TOP"))
						insertName = name;
					else
						insertName = parentName+"->"+name;
					if(!goodsKindCPer.isAExistingKind(insertName)){//如果没有重名的
					
						ContentValues value = new ContentValues();
						value.put("parent", parentId);
						value.put("name", insertName);
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
					else{//有重名的
						Toast.makeText(AddGoodsKind.this, "该类别已在父类别中存在", Toast.LENGTH_SHORT).show();
						nameEText.setText("");
					}
				}
			}
			
		});
		reset.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				finish();
			}
			
		});

	}

}
