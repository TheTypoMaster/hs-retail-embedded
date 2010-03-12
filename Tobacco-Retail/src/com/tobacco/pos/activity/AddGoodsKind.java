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

public class AddGoodsKind extends Activity {

	private static final String TABLE_NAME = "GoodsKind";// ���ݱ�
	private static String[] FROM = { _ID, "name", "parent", "level", "comment" };
	
	private TableCreater tableHelper = null;
	
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
	
		tableHelper = new TableCreater(this);
		
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
				
				if(name.equals("")){//�ж������Ƿ�Ϊ��
					Builder b = new Builder(AddGoodsKind.this);
					b.setTitle("��ʾ");
					b.setMessage("����������");
					b.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

						public void onClick(DialogInterface dialog, int which) {
						
						}
						
					});
					b.show();
				}
				else{
				
					Builder b = new Builder(AddGoodsKind.this);
					b.setTitle("��ʾ");
				
					SQLiteDatabase db = tableHelper.getWritableDatabase();
					ContentValues value = new ContentValues();
					value.put("name", name);
					value.put("parent", parentId);
					value.put("level", parentLevel+1);
					value.put("comment", comment);

				    db.insertOrThrow(TABLE_NAME, null, value);
				    db.close();
					b.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
						
					});
					b.setMessage("�ɹ��������:"+name);
					b.show();
			
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
			
				if(!hasFocus){//ʧȥ����ʱ�������ݿ����Ƿ�����ͬ���ֵ����
					Builder b = new Builder(AddGoodsKind.this);
					String inputName = ((EditText)v).getText().toString();
					SQLiteDatabase db = tableHelper.getReadableDatabase();
					Cursor c = db.query(TABLE_NAME, FROM, null, null, null, null, null);
				
					c.moveToFirst();
					for(int i=0;i<c.getCount();i++){
						if(c.getString(1).equals(inputName)){
							b.setTitle("��ʾ");
							b.setMessage("���и����,����������");
							b.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

								public void onClick(DialogInterface dialog, int which) {
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
			}
		});
	}

}
