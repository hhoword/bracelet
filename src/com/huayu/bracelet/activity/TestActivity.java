package com.huayu.bracelet.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.huayu.bracelet.R;
import com.huayu.bracelet.http.HttpUtil;

public class TestActivity extends PActivity{
	
	private Button button1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_test);
		button1 = (Button)findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				HttpUtil httpUtil = new HttpUtil();
//				httpUtil.test("18126280550", "123456", new IOnDataListener<UserInfo>() {
//					
//					@Override
//					public void onDataResult(UserInfo t) {
//						// TODO Auto-generated method stub
//						Toast.makeText(getApplicationContext(), t.getResponse().getHeader().getRm(), Toast.LENGTH_LONG).show();
//					}
//				});
			}
		});
	}
	
}
