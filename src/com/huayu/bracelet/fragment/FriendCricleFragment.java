package com.huayu.bracelet.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huayu.bracelet.BaseApplication;
import com.huayu.bracelet.R;
import com.huayu.bracelet.activity.IOnDataListener;
import com.huayu.bracelet.adapter.ListItemAdapter;
import com.huayu.bracelet.http.HttpUtil;
import com.huayu.bracelet.view.NoScrollListView;
import com.huayu.bracelet.vo.ListUserZoonInfo;
import com.huayu.bracelet.vo.UserData;

public class FriendCricleFragment extends Fragment{

	private NoScrollListView friendLv;
	private PullToRefreshScrollView friendPullScrollView;
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
		friendLv  = (NoScrollListView)view.findViewById(R.id.friendLv);
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
				friendPullScrollView.onRefreshComplete();
			}
		});
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		listUserZoonInfo = new ArrayList<ListUserZoonInfo>();
		getCircleData();
	}
	
	private void getCircleData(){
		HttpUtil httpUtil = new HttpUtil();
		UserData userData = BaseApplication.getInstance().getUserData();
		httpUtil.getFriendCircle(userData.getData().getUserinfo().getId()+"", 
				index+"", pageSize+"", new IOnDataListener<ListUserZoonInfo>() {

					@Override
					public void onDataResult(ListUserZoonInfo t) {
						// TODO Auto-generated method stub
						if(t!=null){
							if(t.getData()!=null){
								listUserZoonInfo.addAll(t.getData());
								adapter = new ListItemAdapter(getActivity(), listUserZoonInfo);
								friendLv.setAdapter(adapter);
							}
						}
					}
				});
	}
}
