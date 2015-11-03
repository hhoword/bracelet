package com.huayu.bracelet.activity;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.vo.UserData;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoActivity extends PActivity implements OnClickListener{

	private TextView userTvName;
	private TextView userTvSex;
	private TextView userTvHeight;
	private TextView userTvWeight;
//	private TextView userTvName;
	private ImageView userIvBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_user_info);
		userTvName = (TextView)findViewById(R.id.userTvName);
		userTvSex = (TextView)findViewById(R.id.userTvSex);
		userTvHeight = (TextView)findViewById(R.id.userTvHeight);
		userTvWeight = (TextView)findViewById(R.id.userTvWeight);
		userIvBack= (ImageView)findViewById(R.id.userIvBack);
//		userTvName = (TextView)findViewById(R.id.userTvName);
		userIvBack.setOnClickListener(this);
		UserData info = BaseApplication.getInstance().getUserData();
		if(info!=null){
			userTvName.setText(info.getData().getUserinfo().getName());
			if(info.getData().getUserinfo().getSex()==0){
				userTvSex.setText("女");
			}else{
				userTvSex.setText("男");
			}
			userTvHeight.setText(info.getData().getUserinfo().getHeight()+"");
			userTvWeight.setText(info.getData().getUserinfo().getWeight()+"");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.userIvBack:
			finish();
			break;

		default:
			break;
		}
	}
	
}
