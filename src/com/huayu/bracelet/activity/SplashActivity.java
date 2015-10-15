package com.huayu.bracelet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.huayu.bracelet.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class SplashActivity extends PActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.acitvity_splash);


		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
		.showImageForEmptyUri(R.drawable.ic_launcher) //
		.showImageOnFail(R.drawable.ic_launcher) //
		.cacheInMemory(true) //
		.cacheOnDisk(true) //
		.build();//
		ImageLoaderConfiguration config = new ImageLoaderConfiguration//
				.Builder(getApplicationContext())//
		.defaultDisplayImageOptions(defaultOptions)//
		.discCacheSize(50 * 1024 * 1024)//
		.discCacheFileCount(100)// 缓存一百张图片
		.writeDebugLogs()//
		.build();//
		ImageLoader.getInstance().init(config);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
				SplashActivity.this.startActivity(mainIntent);
				SplashActivity.this.finish();
			}
		}, 2000);

	}

}
