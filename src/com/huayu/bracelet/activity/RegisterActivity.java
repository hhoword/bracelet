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
import android.widget.Toast;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.http.HttpUtil;
import com.huayu.bracelet.vo.UserData;

public class RegisterActivity extends PActivity implements OnClickListener{

	private ImageView regIvBack;
	private EditText regEdUser;
	private EditText regEdPW;
	private EditText regEdRePW;
	private Button regBtnRegister;
	private ProgressDialog progressDialog;
	private BluetoothAdapter mBtAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_register);
		regIvBack = (ImageView)findViewById(R.id.regIvBack);
		regEdUser = (EditText)findViewById(R.id.regEdUser);
		regEdPW = (EditText)findViewById(R.id.regEdPW);
		regEdRePW = (EditText)findViewById(R.id.regEdRePW);
		regBtnRegister = (Button)findViewById(R.id.regBtnRegister);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在注册，请稍候...");
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		progressDialog.setCanceledOnTouchOutside(false);
		regIvBack.setOnClickListener(this);
		regBtnRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.regIvBack:
			finish();
			break;
		case R.id.regBtnRegister:
			String user = regEdUser.getText().toString();
			String pwd = regEdPW.getText().toString();
			String rePwd = regEdRePW.getText().toString();
			if(user.length()<4){
				Toast.makeText(RegisterActivity.this, "用户名太短", 
						Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(user)||TextUtils.isEmpty(pwd)
					||TextUtils.isEmpty(rePwd)){
				Toast.makeText(RegisterActivity.this, "用户名、密码不能为空", 
						Toast.LENGTH_SHORT).show();
				return;
			}
			if(!pwd.equals(rePwd)){
				Toast.makeText(RegisterActivity.this, "两次输入密码不一致，请检查", 
						Toast.LENGTH_SHORT).show();
				return;
			}
			progressDialog.show();
			HttpUtil httpUtil = new HttpUtil();
			httpUtil.register(user, pwd, new IOnDataListener<UserData>() {
				
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
								Toast.makeText(RegisterActivity.this, "蓝牙不可用", Toast.LENGTH_LONG).show();
								mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
								RegisterActivity.this.startActivity(mainIntent);
							}else{
								if(mBtAdapter.isEnabled()){
									mainIntent = new Intent(RegisterActivity.this, BTsearchActivity.class);
									RegisterActivity.this.startActivity(mainIntent);
								}else{
									mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
									RegisterActivity.this.startActivity(mainIntent);
									Toast.makeText(RegisterActivity.this, "蓝牙未打开", Toast.LENGTH_LONG).show();
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
			break;
		default:
			break;
		}
	}
	
}
