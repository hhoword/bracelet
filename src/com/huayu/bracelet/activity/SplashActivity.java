package com.huayu.bracelet.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.http.HttpUtil;
import com.huayu.bracelet.vo.UserData;

public class SplashActivity extends PActivity{
	private BluetoothAdapter mBtAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.acitvity_splash);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				mBtAdapter = BluetoothAdapter.getDefaultAdapter();
				Intent mainIntent;
				UserData info = BaseApplication.getInstance().getUserData();
				if(info==null||TextUtils.isEmpty(info.getData().getUserinfo().getName())){
					mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
					SplashActivity.this.startActivity(mainIntent);
				}else{
					HttpUtil httpUtil = new HttpUtil();
					httpUtil.login(info.getData().getUserinfo().getName(),
							info.getData().getUserinfo().getPwd(), new IOnDataListener<UserData>() {
						
						@Override
						public void onDataResult(UserData t) {
							// TODO Auto-generated method stub
							if(t!=null){
								if(t.getCode()==200){
									BaseApplication.getInstance().setUserInfo(t);
								}
							}
						}
					});
					if (mBtAdapter == null) {
						Toast.makeText(SplashActivity.this, "蓝牙不可用", Toast.LENGTH_LONG).show();
						mainIntent = new Intent(SplashActivity.this, MainActivity.class);
						SplashActivity.this.startActivity(mainIntent);
					}else{
						if(mBtAdapter.isEnabled()){
							mainIntent = new Intent(SplashActivity.this, BTsearchActivity.class);
							SplashActivity.this.startActivity(mainIntent);
						}else{
							mainIntent = new Intent(SplashActivity.this, MainActivity.class);
							SplashActivity.this.startActivity(mainIntent);
							Toast.makeText(SplashActivity.this, "蓝牙未打开", Toast.LENGTH_LONG).show();
						}
					}
				}
				SplashActivity.this.finish();
				
			}
		}, 2000);

	}
	
}
