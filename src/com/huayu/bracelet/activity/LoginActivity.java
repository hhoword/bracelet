package com.huayu.bracelet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.http.HttpUtil;
import com.huayu.bracelet.vo.UserInfo;

public class LoginActivity extends PActivity{
	
	private Button loginBtnLogin;
	private EditText loginEdPhone;
	private EditText loginEdPW;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginBtnLogin = (Button)findViewById(R.id.loginBtnLogin);
		loginEdPhone = (EditText)findViewById(R.id.loginEdPhone);
		loginEdPW = (EditText)findViewById(R.id.loginEdPW);
		loginBtnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String phone = loginEdPhone.getText().toString();
				String pwd = loginEdPW.getText().toString();
				if(TextUtils.isEmpty(phone)&&TextUtils.isEmpty(pwd)){
					Toast.makeText(LoginActivity.this, "手机号或密码为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(phone.length()!= 11){
					Toast.makeText(LoginActivity.this, "手机号码不合法", Toast.LENGTH_SHORT).show();
					return;
				}
				HttpUtil httpUtil = new HttpUtil();
				httpUtil.login(phone, pwd, new IOnDataListener<UserInfo>() {
					
					@Override
					public void onDataResult(UserInfo t) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(),
								t.getData().getSex()+t.getMsg(), Toast.LENGTH_SHORT).show();
						if(t.getCode()==200){
							BaseApplication.getInstance().setUserInfo(t.getData());
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							LoginActivity.this.startActivity(intent);
						}
					}
				});
			}
		});
	}

}
