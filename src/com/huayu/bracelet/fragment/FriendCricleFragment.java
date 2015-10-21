package com.huayu.bracelet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huayu.bracelet.R;
import com.huayu.bracelet.adapter.ListItemAdapter;

public class FriendCricleFragment extends Fragment{

	private ListView friendLv;
	private PullToRefreshScrollView friendPullScrollView;
	private ListItemAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_friend, null);
		friendPullScrollView = (PullToRefreshScrollView)view.findViewById(R.id.friendPullScrollView);
		friendLv  = (ListView)view.findViewById(R.id.friendLv);
		friendPullScrollView.setMode(Mode.BOTH);  
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
}
