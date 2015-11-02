package com.huayu.bracelet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huayu.bracelet.vo.DeviceStepInfo;
import com.huayu.bracelet.vo.DevicesInfo;
import com.huayu.bracelet.vo.UserData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class BaseApplication extends Application{

	private static BaseApplication mApplication;
	public static final String HUAYUPHONE = "huayuphone";
	public static final String USERINFO = "userInfo";
	public static final String DEVICESINFO = "devicesInfo";
	public static final String DEVICESTEPINFO  = "deviceStepInfo";
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
		initImageLoader();
	}

	private void initImageLoader(){
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
//		.showImageForEmptyUri(R.drawable.ico_header_default) //
//		.showImageOnFail(R.drawable.ico_header_default) //
		.cacheInMemory(true) //
		.cacheOnDisk(true) //
		.build();//
		ImageLoaderConfiguration config = new ImageLoaderConfiguration//
				.Builder(getApplicationContext())//
		.defaultDisplayImageOptions(defaultOptions)//
		.discCacheSize(50 * 1024 * 1024)//
		.discCacheFileCount(100)// 缓存一百张图片
		.writeDebugLogs()//
		.build();//
		ImageLoader.getInstance().init(config);
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
		List<DevicesInfo> devicesInfos  = new ArrayList<DevicesInfo>();
		String devicesInfoString = sp.getString(DEVICESINFO, "");
		devicesInfos = gson.fromJson(devicesInfoString,
				new TypeToken<List<DevicesInfo>>(){}.getType());
		return devicesInfos;
	}
	
	public void setDeviceStepInfo(List<DeviceStepInfo> deviceStepInfos){
		String devices = gson.toJson(deviceStepInfos);
		editor.putString(DEVICESTEPINFO, devices);
		editor.commit();
	}

	public List<DeviceStepInfo> getDeviceStepInfo(){
		List<DeviceStepInfo> deviceStepInfos = new ArrayList<DeviceStepInfo>();
		String deviceStepInfoString = sp.getString(DEVICESTEPINFO, "");
		deviceStepInfos = gson.fromJson(deviceStepInfoString,
				new TypeToken<List<DeviceStepInfo>>(){}.getType());
		return deviceStepInfos;
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

	/**
	 * 判断是否与目标日期是同一天
	 * @param now 目标日期
	 * @param date 判定日期
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	public static boolean isSameday(String now , String date){
		boolean istoday  = false;
		Calendar today = Calendar.getInstance();  
		Calendar target = Calendar.getInstance();

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			today.setTime( df.parse(now));
			today.set(Calendar.HOUR, 0);  
			today.set(Calendar.MINUTE, 0);  
			today.set(Calendar.SECOND, 0); 
			target.setTime(df.parse(date));
			target.set(Calendar.HOUR, 0);  
			target.set(Calendar.MINUTE, 0);  
			target.set(Calendar.SECOND, 0); 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return istoday;
		}
		long intervalMilli = target.getTimeInMillis() - today.getTimeInMillis(); 
		int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000)); 
		if(xcts==0){
			istoday = true;
		}
		return istoday;

	}
	
	/**
	 * 获取指定日期的前一天
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {//可以用new Date().toLocalString()传递参数  
        Calendar c = Calendar.getInstance();  
        Date date = null;  
        try {  
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(specifiedDay);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day - 1);  
  
        String dayBefore = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(c  
                .getTime());  
        return dayBefore;  
    }  

	/**
	 * 比较time1与time2时间差是否超过一天
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isOutstripDayLong(String time1,String time2){
		boolean isOutstrip=false;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date1 = df.parse("2004-03-26 13:31:40");
			Date date2=df.parse("2004-01-02 11:30:24");
			long l=date1.getTime()-date2.getTime();
			long day=l/(24*60*60*1000);
			//			long hour=(l/(60*60*1000)-day*24);
			//			long min=((l/(60*1000))-day*24*60-hour*60);
			//			long s=(l/1000-day*24*60*60-hour*60*60-min*60);
			//			System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
			if(day>1){
				isOutstrip = true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isOutstrip;
	}

	/**
	 * 判断时间大小 0：相等 
	 * @param time1
	 * @param time2
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static int compareTimeLine(String time1,String time2){
		int result;
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c1=Calendar.getInstance();
		Calendar c2=Calendar.getInstance();
		try
		{
			c1.setTime(df.parse(time1));
			c2.setTime(df.parse(time2));
		}catch(java.text.ParseException e){
			System.err.println("格式不正确");
		}
		result=c1.compareTo(c2);/*
		if(result==0)
			System.out.println("c1相等c2");
		else if(result<0)
			System.out.println("c1小于c2");
		else
			System.out.println("c1大于c2");*/
		return result;
	}
}
