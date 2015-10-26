package com.huayu.bracelet.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.http.HttpUtil;
import com.huayu.bracelet.vo.UserData;
import com.huayu.bracelet.vo.UserInfo;

public class LoginActivity extends PActivity{
	
	private Button loginBtnLogin;
	private EditText loginEdUser;
	private EditText loginEdPW;
	private ImageView logIvBack;
	private TextView loginTvRegister;
	private ProgressDialog progressDialog;
	private BluetoothAdapter mBtAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginBtnLogin = (Button)findViewById(R.id.loginBtnLogin);
		loginEdUser = (EditText)findViewById(R.id.loginEdUser);
		loginEdPW = (EditText)findViewById(R.id.loginEdPW);
		logIvBack = (ImageView)findViewById(R.id.logIvBack);
		loginTvRegister = (TextView)findViewById(R.id.loginTvRegister);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在登录，请稍候...");
		progressDialog.setCanceledOnTouchOutside(false);
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		loginBtnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String user = loginEdUser.getText().toString();
				String pwd = loginEdPW.getText().toString();
				if(TextUtils.isEmpty(user)||TextUtils.isEmpty(pwd)){
					Toast.makeText(LoginActivity.this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
					return;
				}
				progressDialog.show();
				HttpUtil httpUtil = new HttpUtil();
				httpUtil.login(user, pwd, new IOnDataListener<UserData>() {
					
					@Override
					public void onDataResult(UserData t) {
						// TODO Auto-generated method stub
						progressDialog.dismiss();
						if(t!=null){
							Toast.makeText(getApplicationContext(),
									t.getMsg(), Toast.LENGTH_SHORT).show();
							Intent mainIntent;
							if(t.getCode()==200){
								BaseApplication.getInstance().setUserInfo(t);
								if (mBtAdapter == null) {
									Toast.makeText(LoginActivity.this, "蓝牙不可用", Toast.LENGTH_LONG).show();
									mainIntent = new Intent(LoginActivity.this, MainActivity.class);
									LoginActivity.this.startActivity(mainIntent);
								}else{
									if(mBtAdapter.isEnabled()){
										mainIntent = new Intent(LoginActivity.this, BTsearchActivity.class);
										LoginActivity.this.startActivity(mainIntent);
									}else{
										mainIntent = new Intent(LoginActivity.this, MainActivity.class);
										LoginActivity.this.startActivity(mainIntent);
										Toast.makeText(LoginActivity.this, "蓝牙未打开", Toast.LENGTH_LONG).show();
									}
								}
								finish();
							}
						}else{
							Toast.makeText(getApplicationContext(),
									R.string.connect_err, Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
		logIvBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		loginTvRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
	}

}
