package com.huayu.bracelet.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.huayu.bracelet.R;
import com.huayu.bracelet.http.HttpUtil;
import com.huayu.bracelet.vo.MessageVo;
import com.huayu.bracelet.vo.UserInfo;

public class LoginActivity extends PActivity{
	
	private Button loginBtnLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginBtnLogin = (Button)findViewById(R.id.loginBtnLogin);
		loginBtnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HttpUtil httpUtil = new HttpUtil();
				httpUtil.login("123", "123444", new IOnDataListener<MessageVo>() {
					
					@Override
					public void onDataResult(MessageVo t) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(),
								t.getData().getSex()+t.getMsg(), Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

}
