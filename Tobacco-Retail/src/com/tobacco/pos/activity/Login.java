package com.tobacco.pos.activity;

import com.tobacco.pos.util.Loginer;
import com.tobacco.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {

	private Loginer loginer = null;

	private EditText userNameEText;
	private EditText passwordEText;
	private TextView loginInfo;
	private Button loginOk;
	private Button loginReset;

	private String userName = "";
	private String password = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logindialog);

		final String purpose = this.getIntent().getStringExtra("purpose");

		loginer = new Loginer(this);

		userNameEText = (EditText) this.findViewById(R.id.userName);
		passwordEText = (EditText) this.findViewById(R.id.password);
		loginInfo = (TextView) this.findViewById(R.id.loginInfo);
		loginInfo.setTextColor(Color.RED);
		loginOk = (Button) this.findViewById(R.id.loginOk);
		loginReset = (Button) this.findViewById(R.id.loginReset);

		loginOk.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				userName = userNameEText.getText().toString();
				password = passwordEText.getText().toString();

				boolean flag = loginer.login(userName, password, purpose,
						loginer.getReadableDatabase());
				if (!flag)
					loginInfo.setText("权限不足或密码出错");
				else {
					loginInfo.setText("");
					if (purpose.equals("PurchaseManagement")) {
						Intent intent = new Intent(Login.this,
								PurchaseManagement.class);
						intent.putExtra("userName", userName);
						startActivity(intent);
					} else if (purpose.equals("PaymentManagement")) {
						Intent intent = new Intent(Login.this,
								PaymentManagement.class);
						intent.putExtra("userName", userName);
						startActivity(intent);
					}
				}
			}

		});

		loginReset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}

		});

	}
}
