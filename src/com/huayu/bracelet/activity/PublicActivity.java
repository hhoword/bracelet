package com.huayu.bracelet.activity;

import com.huayu.bracelet.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PublicActivity extends PActivity implements OnClickListener{

	private ImageView pubIvPhoto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_publish);
		pubIvPhoto = (ImageView)findViewById(R.id.pubIvPhoto);
		pubIvPhoto.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pubIvPhoto:
			Intent intent = new Intent(PublicActivity.this, ImageBrowseActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
}
