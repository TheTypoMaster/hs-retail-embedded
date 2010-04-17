package com.tobacco.pos.extentActivity;

import static android.provider.BaseColumns._ID;

 

import com.tobacco.R;
import com.tobacco.pos.activity.KindManagement;
import com.tobacco.pos.contentProvider.GoodsKindCPer;

import android.app.Activity; 
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle; 
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class KindManagementExtention extends Activity {
	 
	private String selfName = "";//自身类别的名字
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		Intent intent = this.getIntent();
		int requestCode = intent.getIntExtra("REQUESTCODE", 0);//取得前端请求的标志
	
		if(requestCode == KindManagement.DELETEFLAG){//删除请求
			int selectedId = intent.getIntExtra("selectedId", 0);
			
			Intent deleteIntent = new Intent(KindManagementExtention.this, KindManagement.class);
			if(hasChildrenKind(selectedId)){//如果有孩子类别，则不允许删除
				deleteIntent.putExtra("hasChildren", true);
			}
			else{//没有孩子类别，执行删除操作
				deleteIntent.putExtra("deleteFlag", delete(selectedId));
			}
			this.setResult(RESULT_OK, deleteIntent);
			finish();
		}
		else if(requestCode == KindManagement.INFOFLAG){//读取详细信息请求
			int selectedId = intent.getIntExtra("selectedId", 0);
			
			 
			
			Intent infoIntent = new Intent(KindManagementExtention.this, KindManagement.class);
			infoIntent.putExtra("moreInfo", getGoodsKindInfoByGoodsKindId(selectedId));

			this.setResult(RESULT_OK, infoIntent);
			finish();
		}
		else if(requestCode == KindManagement.EDITFLAG){//编辑类别的请求
			setContentView(R.layout.addeditkinddialog);
			this.setTitle("编辑类别");
			
			final int selectedId = intent.getIntExtra("selectedId", 0);//取得前端传来的Id
			
			Cursor c = this.managedQuery(GoodsKindCPer.CONTENT_URI, null, _ID + " = ? ", new String[]{selectedId+""}, null);
	
			c.moveToFirst();
			String selectedName = c.getString(c.getColumnIndex("name"));
			final String comment = c.getString(c.getColumnIndex("comment"));
			
			String parentName = "";//父类别的名字
			if(!selectedName.contains("->"))//如果是最高级类别
			{
				parentName = "TOP";
				selfName = selectedName;
			}
			else{
				parentName = selectedName.substring(0, selectedName.lastIndexOf("->"));
				selfName = selectedName.substring(selectedName.lastIndexOf("->")+2);
			}
			final EditText nameEText = (EditText)this.findViewById(R.id.name);
			final TextView parentNameTView = (TextView)this.findViewById(R.id.parentName);
			final EditText commentEText = (EditText)this.findViewById(R.id.comment);
			
			nameEText.setText(selfName);
			parentNameTView.setText(parentName);
			commentEText.setText(comment);
			
			Button editOk = (Button)this.findViewById(R.id.ok);
			editOk.setOnClickListener(new OnClickListener(){//确定，有更改

				public void onClick(View v) {
					if(nameEText.getText().toString().equals("")){
						Toast.makeText(KindManagementExtention.this, "请输入名字", Toast.LENGTH_SHORT).show();
					}
					else{
					if(!nameEText.getText().toString().equals(selfName)){//更改了名字
						String wholeName = "";
						if(parentNameTView.getText().toString().equals("TOP"))
							wholeName = nameEText.getText().toString();
						else
							wholeName = parentNameTView.getText() + "->" + nameEText.getText();
						Cursor cursor = KindManagementExtention.this.managedQuery(GoodsKindCPer.CONTENT_URI, null, " name = ? ", new String[]{wholeName}, null);
						if(cursor.getCount()>0)
							Toast.makeText(KindManagementExtention.this, "同名类别已经存在!", Toast.LENGTH_SHORT).show();
						else{
							ContentValues value = new ContentValues();
							if(parentNameTView.equals("TOP"))
								value.put("name", nameEText.getText().toString());
							else
								value.put("name", parentNameTView.getText() + "->" + nameEText.getText().toString());
							value.put("comment", commentEText.getText().toString());
						
							int updateCount = KindManagementExtention.this.getContentResolver().update(GoodsKindCPer.CONTENT_URI, value, _ID + " = ? ", new String[]{selectedId+""});
					
							if(updateCount == 0){//更新成功
								Intent editIntent = new Intent(KindManagementExtention.this, KindManagement.class);
								editIntent.putExtra("hasUpdated", true);
								editIntent.putExtra("updateInfo", "成功将类别 " + selfName + " 更名为 " + nameEText.getText());
							
								KindManagementExtention.this.setResult(RESULT_OK, editIntent);
								finish();
							}
							else {//更新失败
								Intent editIntent = new Intent(KindManagementExtention.this, KindManagement.class);
								editIntent.putExtra("hasUpdated", true);
								editIntent.putExtra("updateInfo", "更新类别 " + selfName + " 失败");
							
								KindManagementExtention.this.setResult(RESULT_OK, editIntent);
								finish();
							}
						}
					}
					else if(nameEText.getText().toString().equals(selfName) && !commentEText.getText().toString().equals(comment)){//更改了备注
						ContentValues value = new ContentValues();
						if(parentNameTView.equals("TOP"))
							value.put("name", nameEText.getText().toString());
						else
							value.put("name", parentNameTView.getText() + "->" + nameEText.getText().toString());
					
						value.put("comment", commentEText.getText().toString());
						
						int updateCount = KindManagementExtention.this.getContentResolver().update(GoodsKindCPer.CONTENT_URI, value, _ID + " = ? ", new String[]{selectedId+""});
						if(updateCount == 0){
							Intent editIntent = new Intent(KindManagementExtention.this, KindManagement.class);
							editIntent.putExtra("hasUpdated", true);
							editIntent.putExtra("updateInfo", "成功更新类别 " + selfName + " 的备注");
							
							KindManagementExtention.this.setResult(RESULT_OK, editIntent);
							finish();
						}
						else{
							Intent editIntent = new Intent(KindManagementExtention.this, KindManagement.class);
							editIntent.putExtra("hasUpdated", true);
							editIntent.putExtra("updateInfo", "更新类别 " + selfName + " 失败");
						
							KindManagementExtention.this.setResult(RESULT_OK, editIntent);
							finish();
						}
					}
					else{//什么都没更改
						Intent editIntent = new Intent(KindManagementExtention.this, KindManagement.class);
						editIntent.putExtra("hasUpdated", false);

						KindManagementExtention.this.setResult(RESULT_OK, editIntent);
						finish();
					}
				}
				}
			});
			Button editReset = (Button)this.findViewById(R.id.reset);
			editReset.setOnClickListener(new OnClickListener(){//取消，没做任何更改

				public void onClick(View v) {
					Intent editIntent = new Intent(KindManagementExtention.this, KindManagement.class);
					editIntent.putExtra("hasUpdated", false);

					KindManagementExtention.this.setResult(RESULT_OK, editIntent);
					finish();
				}
				
			});
			
		}
		else if(requestCode == KindManagement.ADDFLAG){
		 
			setContentView(R.layout.addeditkinddialog);
			this.setTitle("增加类别");
			final String parentName = intent.getStringExtra("parentName");
			final int parentId = intent.getIntExtra("parentId", 0);
			final int parentLevel = intent.getIntExtra("parentLevel", 0);
			
			final TextView parentNameTView = (TextView)this.findViewById(R.id.parentName);
			parentNameTView.setText(parentName);
			Button addOk = (Button)this.findViewById(R.id.ok);
			addOk.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					 EditText goodsNameEText = (EditText) KindManagementExtention.this.findViewById(R.id.name);
					 EditText commentEText = (EditText)KindManagementExtention.this.findViewById(R.id.comment);
					 String newKindName = "";
					 if(goodsNameEText.getText().toString().equals(""))
						 Toast.makeText(KindManagementExtention.this, "请输入种类名称!", Toast.LENGTH_SHORT).show();
					 else{//开始执行插入操作
						
						 if(parentName.equals("TOP"))
						 	newKindName = goodsNameEText.getText().toString();
						 else 
							newKindName = parentNameTView.getText() + "->" + goodsNameEText.getText();
							
					 
						 if(isAExistingKind(newKindName)){
							 Toast.makeText(KindManagementExtention.this, "该类别已经存在", Toast.LENGTH_SHORT).show();
						 }
						 else{
							 ContentValues value = new ContentValues();
							 value.put("parent", parentId);
							 value.put("name", newKindName);
							 value.put("level", parentLevel+1);
							 value.put("comment", commentEText.getText().toString());

							 Uri result = getContentResolver().insert(GoodsKindCPer.CONTENT_URI, value);
							 if(result == null){//插入失败
								Intent addIntent = new Intent(KindManagementExtention.this, KindManagement.class) ;
								addIntent.putExtra("addInfo", "增加类别 " + newKindName + "失败");
								
								KindManagementExtention.this.setResult(RESULT_OK, addIntent);
								finish();
							 }
							 else{//成功插入
								Intent addIntent = new Intent(KindManagementExtention.this, KindManagement.class) ;
								addIntent.putExtra("addInfo", "成功增加类别 " + newKindName);
									
								KindManagementExtention.this.setResult(RESULT_OK, addIntent);
								finish();
							 }
						 }
 			
					 }
				}
				
			});
			Button addReset = (Button)this.findViewById(R.id.reset);
			addReset.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					 finish();
				}
				
			});
			
		}

	}
	
	public boolean isAExistingKind(String kindName){//判断是否已经有同样名字的类别
		Cursor c = this.getContentResolver().query(GoodsKindCPer.CONTENT_URI, null, " name = ? ", new String[]{kindName}, null);
		if(c.getCount()>0)
			return true;//存在同样名字的类别
		else
			return false;//不存在同样的类别
	}

	public boolean hasChildrenKind(int kindId){//ID为kindID的类别是否还有子类别
		Cursor c = this.getContentResolver().query(GoodsKindCPer.CONTENT_URI, null, " parent = ? ", new String[]{kindId+""}, null);
		if(c.getCount()>0)
			return true;
		else
			return false;
	}
	private boolean delete(int deleteId){//执行删除操作
	
		int deleteCount = getContentResolver().delete(GoodsKindCPer.CONTENT_URI, _ID + " = ? ", new String[]{deleteId+""});
		if(deleteCount > 0)
			return true;
		
		return false;
	}
	
	public String getGoodsKindInfoByGoodsKindId(int kindId){
		Cursor c = getContentResolver().query(GoodsKindCPer.CONTENT_URI, null, " _id = ? ", new String[]{kindId+""}, null);
		if(c.getCount()>0){
			c.moveToFirst();
			
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("ID:"+kindId+"\n");
			strBuffer.append("名字:"+c.getString(1)+"\n");
			strBuffer.append("备注:"+c.getString(4));
			
			return strBuffer.toString();
		}
		return "";
	}
	
}