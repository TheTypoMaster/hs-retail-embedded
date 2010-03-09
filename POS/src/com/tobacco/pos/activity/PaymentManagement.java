package com.tobacco.pos.activity;

import com.tobacco.pos.dao.GoodsDAO;
import com.tobacco.pos.dao.GoodsPriceDAO;
import com.tobacco.pos.dao.VIPInfoDAO;
import com.tobacco.pos.util.Loginer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PaymentManagement extends Activity {
	private VIPInfoDAO vipInfoDAO = null;
	private GoodsPriceDAO goodsPriceDAO = null;
	private GoodsDAO gDAO = null;
	private Loginer loginer = null;

	private TextView paymentWelcome;
	private ImageButton paymentreturn;// ������ҳ�İ�ť
	
	private EditText barcodeEText;//�����������
	private EditText sGoodsNumEText;//�����������
	private TableLayout salesBillTable;//��ʾÿһ�����۵���ϸ��Ϣ�ı��
	
	private String userName = "";// ��½�û�������

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paymentmanagement);

		userName = this.getIntent().getStringExtra("userName");

		paymentWelcome = (TextView) this.findViewById(R.id.paymentWelcome);
		paymentWelcome.setText("���:" + userName);

		paymentreturn = (ImageButton) this.findViewById(R.id.paymentreturn);
		paymentreturn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				loginer = new Loginer(PaymentManagement.this);
				if (loginer.logout(userName, loginer.getWritableDatabase()))// �ǳ�
				{
					Intent intent = new Intent(PaymentManagement.this,
							Main.class);
					PaymentManagement.this.startActivity(intent);
				}
			}

		});
		barcodeEText = (EditText)this.findViewById(R.id.barcodeEText);
		sGoodsNumEText = (EditText)this.findViewById(R.id.sGoodsNumEText);
		sGoodsNumEText.setOnFocusChangeListener(new OnFocusChangeListener(){

			public void onFocusChange(View v, boolean hasFocus) {
				
				if(!hasFocus){
					int count = 0;
					try{
						count = Integer.parseInt(((EditText)v).getText().toString());
					}
					catch(Exception e){
						((EditText)v).setText("");
					}
					String barcode = barcodeEText.getText().toString();
					if(!barcode.trim().equals(null) && count!=0){
						goodsPriceDAO  = new GoodsPriceDAO(PaymentManagement.this);
						
						int goodsId = goodsPriceDAO.getGoodsIdByBarcode(barcode, goodsPriceDAO.getReadableDatabase());
						int unitId = goodsPriceDAO.getUnitIdByBarcode(barcode, goodsPriceDAO.getReadableDatabase());
				
						salesBillTable = (TableLayout)PaymentManagement.this.findViewById(R.id.salesBillTable);
						TableRow r = new TableRow(PaymentManagement.this);
						TextView t = new TextView(PaymentManagement.this);
						t.setText(gDAO.getGoodsInfoByGoodsId(goodsId, gDAO.getReadableDatabase()));
						r.addView(t);
						ImageView i = new ImageView(PaymentManagement.this);
						i.setImageResource(R.drawable.delete);
					
						r.addView(i);
						
						salesBillTable.addView(r);
					}
				}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 0, 0, "���ǻ�Ա");

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 0:

			verifyVIPNum();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void verifyVIPNum() {
		AlertDialog.Builder VIPIDDialog = new AlertDialog.Builder(this);
		VIPIDDialog.setTitle("�����Ա��");
		final EditText VIPNumEText = new EditText(this);
		VIPNumEText.setWidth(100);
		VIPNumEText.setSingleLine(true);
		VIPIDDialog.setView(VIPNumEText);
		VIPIDDialog.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						vipInfoDAO = new VIPInfoDAO(PaymentManagement.this);
						//���ݻ�Ա�Ż�ȡ��Ա��ID
						int flag = vipInfoDAO.verifyIsVIP(VIPNumEText.getText().toString(), vipInfoDAO.getReadableDatabase());
					
					}

				});
		VIPIDDialog.setNegativeButton("ȡ��",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}

				});
		VIPIDDialog.show();

	}

	@Override
	protected void onPause() {
		super.onPause();

		loginer = new Loginer(PaymentManagement.this);
		if (loginer.logout(userName, loginer.getWritableDatabase()))// �ǳ�
		{
			Intent intent = new Intent(PaymentManagement.this, Main.class);
			PaymentManagement.this.startActivity(intent);
		}
	}

}
