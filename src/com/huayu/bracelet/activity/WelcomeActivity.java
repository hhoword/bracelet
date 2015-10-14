package com.huayu.bracelet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.huayu.bracelet.R;

public class WelcomeActivity extends PActivity implements OnClickListener{

	private Button welBtnLogin;
	private Button welBtnRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		welBtnLogin = (Button)findViewById(R.id.welBtnLogin);
		welBtnRegister = (Button)findViewById(R.id.welBtnRegister);
		welBtnLogin.setOnClickListener(this);
		welBtnRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.welBtnLogin:
			intent  = new Intent(WelcomeActivity.this, LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.welBtnRegister:
//			intent  = new Intent(WelcomeActivity.this, re.class);
//			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
