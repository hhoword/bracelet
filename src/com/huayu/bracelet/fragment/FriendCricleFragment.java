package com.huayu.bracelet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huayu.bracelet.R;
import com.huayu.bracelet.adapter.ListItemAdapter;
import com.huayu.bracelet.http.HttpUtil;

public class FriendCricleFragment extends Fragment{

	private ListView friendLv;
	private PullToRefreshScrollView friendPullScrollView;
	private ListItemAdapter adapter;
	private int pageSize=20;
	private int index=0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_friend, null);
		friendPullScrollView = (PullToRefreshScrollView)view.findViewById(R.id.friendPullScrollView);
		friendLv  = (ListView)view.findViewById(R.id.friendLv);
		friendPullScrollView.setMode(Mode.BOTH);  
		adapter = new ListItemAdapter(getActivity(), null);
		friendLv.setAdapter(adapter);
		HttpUtil httpUtil = new HttpUtil();
//		httpUtil.getFriendCircle(id, index, pagesize, dataListener);
		friendPullScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				if (friendPullScrollView.getReadyForPullStart()) {// 拉到最上面
				}

				if (friendPullScrollView.getReadyForPullEnd()) {//上拉加载更多
				}
//				friendPullScrollView.onRefreshComplete();
			}
		});
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
}
