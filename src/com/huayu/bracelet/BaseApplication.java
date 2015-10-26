package com.huayu.bracelet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huayu.bracelet.vo.UserInfo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class BaseApplication extends Application{

	private static BaseApplication mApplication;
	public static final String HUAYUPHONE = "huayuphone";
	public static final String USERINFO = "userInfo";
	
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private Gson gson;
	
	public static synchronized BaseApplication getInstance() {
		return mApplication;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mApplication = this;
		gson = new Gson();
		sp = getSharedPreferences(HUAYUPHONE, Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	
	public void setUserInfo(UserInfo info){
		String user = gson.toJson(info);
		editor.putString(USERINFO, user);
		editor.commit();
	}
	
	public UserInfo getUserInfo() {
		UserInfo info = null;
		String user = sp.getString(USERINFO, "");
		info = gson.fromJson(user, new TypeToken<UserInfo>(){}.getType());
		return info;
	}
}
