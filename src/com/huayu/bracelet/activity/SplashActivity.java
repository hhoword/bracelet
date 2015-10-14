package com.huayu.bracelet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.huayu.bracelet.R;

public class SplashActivity extends PActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.acitvity_splash);


		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
				SplashActivity.this.startActivity(mainIntent);
				SplashActivity.this.finish();
			}
		}, 2000);

	}

}
