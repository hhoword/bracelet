package com.huayu.bracelet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.activity.BindActivity;
import com.huayu.bracelet.activity.UserInfoActivity;
import com.huayu.bracelet.vo.UserData;

public class MeFragment extends Fragment implements OnClickListener{

	private TextView meTvPower;
	private TextView meTvVersion;
	private TextView meTvName;
	private TextView meTvPosition;
	private TextView meTvLevel;
	private TextView meTvHNum;
	private ImageView meIvUser;
	private Button meBtnInfo;
	private LinearLayout meLayoutChange;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_me, null);
		meTvPower = (TextView)view.findViewById(R.id.meTvPower);
		meTvVersion= (TextView)view.findViewById(R.id.meTvVersion);
		meTvName= (TextView)view.findViewById(R.id.meTvName);
		meIvUser = (ImageView)view.findViewById(R.id.meIvUser);
		meTvLevel= (TextView)view.findViewById(R.id.meTvLevel);
		meTvPosition = (TextView)view.findViewById(R.id.meTvPosition);
		meTvHNum = (TextView)view.findViewById(R.id.meTvHNum);
		meLayoutChange = (LinearLayout)view.findViewById(R.id.meLayoutChange);
		meBtnInfo = (Button)view.findViewById(R.id.meBtnInfo);
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden){
			meTvVersion.setText(BaseApplication.getVersionName(getActivity()));
			meTvPower.setText(BaseApplication.getInstance().getPower());
			meLayoutChange.setOnClickListener(this);
			meBtnInfo.setOnClickListener(this);
			UserData info = BaseApplication.getInstance().getUserData();
			if(info!=null){
				meTvName.setText(info.getData().getUserinfo().getName());
				meTvLevel.setText(info.getData().getUserinfo().getLevel()+"");
				meTvPosition.setText(info.getData().getUserinfo().getCity());
				BaseApplication.getImageByloader(getActivity(), info.getData().getUserinfo()
						.getProfile_img_url(), meIvUser, R.drawable.ico_sex_male_100);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.meLayoutChange:
			intent = new Intent(getActivity(), BindActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.meBtnInfo:
			intent = new Intent(getActivity(), UserInfoActivity.class);
			getActivity().startActivity(intent);
			break;
		default:
			break;
		}
	}
	
}
