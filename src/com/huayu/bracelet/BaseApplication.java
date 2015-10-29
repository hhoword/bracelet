package com.huayu.bracelet;

import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huayu.bracelet.vo.DevicesInfo;
import com.huayu.bracelet.vo.UserData;

public class BaseApplication extends Application{

	private static BaseApplication mApplication;
	public static final String HUAYUPHONE = "huayuphone";
	public static final String USERINFO = "userInfo";
	public static final String DEVICESINFO = "devicesInfo";
	public static final String DEVICEPOWER = "devicePower";
	public static final String STEP = "step";
	public static final String ASYNCDATE = "asyncDate";

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private Gson gson;
	
	public static String mac;
	public static String devicesId;

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

	public void setUserInfo(UserData info){
		String user = gson.toJson(info);
		editor.putString(USERINFO, user);
		editor.commit();
	}

	public UserData getUserData() {
		UserData info = null;
		String user = sp.getString(USERINFO, "");
		info = gson.fromJson(user, new TypeToken<UserData>(){}.getType());
		return info;
	}

	public void setDevicesInfo(List<DevicesInfo> devicesInfos){
		String devices = gson.toJson(devicesInfos);
		editor.putString(DEVICESINFO, devices);
		editor.commit();
	}

	public List<DevicesInfo> getDevicesInfo(){
		List<DevicesInfo> devicesInfos  = null;
		String devicesInfoString = sp.getString(DEVICESINFO, "");
		devicesInfos = gson.fromJson(devicesInfoString,
				new TypeToken<List<DevicesInfo>>(){}.getType());
		return devicesInfos;
	}
	
	public void  setPower(String power){
		editor.putString(DEVICEPOWER, power);
		editor.commit();
	}
	
	public String getPower(){
		String Power = sp.getString(DEVICEPOWER, "0");
		return Power;
	}
	
	public void  setStep(int step){
		editor.putInt(STEP, step);
		editor.commit();
	}
	
	public int getStep(){
		int step = sp.getInt(STEP, 0);
		return step;
	}
	
	public void  setAsyncDate(String date){
		editor.putString(ASYNCDATE, date);
		editor.commit();
	}
	
	public String getAsyncDate(){
		String date = sp.getString(ASYNCDATE, "");
		return date;
	}
}
