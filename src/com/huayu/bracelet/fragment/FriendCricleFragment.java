package com.huayu.bracelet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.huayu.bracelet.R;

public class FriendCricleFragment extends Fragment{

	private ListView friendLv;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_friend, null);
		friendLv  = (ListView)view.findViewById(R.id.friendLv);
//		friendLv.setMode(Mode.BOTH);  
		return view;
	}
	
}
