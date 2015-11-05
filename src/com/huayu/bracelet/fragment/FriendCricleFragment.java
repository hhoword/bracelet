package com.huayu.bracelet.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.activity.IOnDataListener;
import com.huayu.bracelet.activity.PublicActivity;
import com.huayu.bracelet.adapter.ListItemAdapter;
import com.huayu.bracelet.http.HttpUtil;
import com.huayu.bracelet.view.NoScrollListView;
import com.huayu.bracelet.vo.ListUserZoonInfo;
import com.huayu.bracelet.vo.UserData;

public class FriendCricleFragment extends Fragment implements OnClickListener{

	private NoScrollListView friendLv;
	private PullToRefreshScrollView friendPullScrollView;
	private ImageView friendIvUser;
	private TextView friendTvName;
	private ImageView friendIvWrite;
	private TextView friendTvFans;
	private TextView friendTvConcern;
	private TextView friendTvLevel;
	private TextView friendTvHNum;
	private ListItemAdapter adapter;
	private int pageSize=20;
	private int index=0;
	private List<ListUserZoonInfo> listUserZoonInfo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_friend, null);
		friendPullScrollView = (PullToRefreshScrollView)view.findViewById(R.id.friendPullScrollView);
		friendTvName = (TextView)view.findViewById(R.id.friendTvName);
		friendLv  = (NoScrollListView)view.findViewById(R.id.friendLv);
		friendIvUser = (ImageView)view.findViewById(R.id.friendIvUser);
		friendIvWrite = (ImageView)view.findViewById(R.id.friendIvWrite);
		friendTvFans = (TextView)view.findViewById(R.id.friendTvFans);
		friendTvConcern = (TextView)view.findViewById(R.id.friendTvConcern);
		friendTvLevel = (TextView)view.findViewById(R.id.friendTvLevel);
		friendTvHNum = (TextView)view.findViewById(R.id.friendTvHNum);
		friendIvWrite.setOnClickListener(this);
		friendPullScrollView.setMode(Mode.BOTH);  
		adapter = new ListItemAdapter(getActivity(), null);
		friendLv.setAdapter(adapter);
		
		friendPullScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				if (friendPullScrollView.getReadyForPullStart()) {// 拉到最上面
					index = 0;
					listUserZoonInfo.clear();
					getCircleData();
				}

				if (friendPullScrollView.getReadyForPullEnd()) {//上拉加载更多
					index ++;
					getCircleData();
				}
			}
		});
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		listUserZoonInfo = new ArrayList<ListUserZoonInfo>();
		adapter = new ListItemAdapter(getActivity(), listUserZoonInfo);
		friendLv.setAdapter(adapter);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden){
			getCircleData();
			UserData info = BaseApplication.getInstance().getUserData();
			if(info!=null){
				friendTvName.setText(info.getData().getUserinfo().getName());
				friendTvFans.setText(info.getData().getFollowerCount()+"");
				friendTvConcern.setText(info.getData().getConcernedCount()+"");
				friendTvLevel.setText(info.getData().getUserinfo().getLevel()+"");
//				friendTvHNum.setText(info.getData().getUserinfo().get);
				BaseApplication.getImageByloader(getActivity(), info.getData().getUserinfo().getProfile_img_url(),
						friendIvUser, R.drawable.ico_sex_male_100);
			}
		}
	}
	
	private void getCircleData(){
		HttpUtil httpUtil = new HttpUtil();
		UserData userData = BaseApplication.getInstance().getUserData();
		httpUtil.getFriendCircle(userData.getData().getUserinfo().getId()+"", 
				index+"", pageSize+"", new IOnDataListener<ListUserZoonInfo>() {

					@Override
					public void onDataResult(ListUserZoonInfo t) {
						// TODO Auto-generated method stub
						friendPullScrollView.onRefreshComplete();
						if(t!=null){
							if(t.getData()!=null){
								listUserZoonInfo.addAll(t.getData());
//								adapter = new ListItemAdapter(getActivity(), listUserZoonInfo);
//								friendLv.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							}
						}else{
							Toast.makeText(getActivity(),"连接失败，请重试 ！", Toast.LENGTH_LONG).show();
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.friendIvWrite:
			Intent intent = new Intent(getActivity(), PublicActivity.class);
			getActivity().startActivity(intent);
			break;

		default:
			break;
		}
	}
}
