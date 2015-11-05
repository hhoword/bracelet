package com.huayu.bracelet.activity;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.vo.UserData;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserInfoActivity extends PActivity implements OnClickListener{

	private TextView userTvName;
	private TextView userTvSex;
	private TextView userTvHeight;
	private TextView userTvWeight;
//	private TextView userTvName;
	private ImageView userIvBack;
	private LinearLayout userLyLogout;
	
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
		userLyLogout = (LinearLayout)findViewById(R.id.userLyLogout);
//		userTvName = (TextView)findViewById(R.id.userTvName);
		userIvBack.setOnClickListener(this);
		userLyLogout.setOnClickListener(this);
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
	
	private void logoutDialog(){
		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage("退出将会清除步数信息，确认退出吗？")
		.setNegativeButton("取消", null)
		.setPositiveButton("确认",  new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				BaseApplication.getInstance().setUserInfo(null);
				BaseApplication.getInstance().setDeviceStepInfo(null);
				BaseApplication.getInstance().setStep(0);
				Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		})
		.create()
		.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.userIvBack:
			finish();
			break;
		case R.id.userLyLogout:
			logoutDialog();
			break;
		default:
			break;
		}
	}
	
}
