package com.huayu.bracelet.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.http.MediaType;

import android.os.Handler;

public class HttpUtil<T> {

	public  Handler handler = new Handler();
	public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
	private OnDataListener<T> dataListener;
	
	public HttpUtil() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void login(){
//		fixedThreadPool.execute(new HttpThread(0));
	}
	
	public void stop(){
		handler.removeCallbacksAndMessages(null);
	}
	
	public void setDataListener(OnDataListener<T> dataListener){
		this.dataListener = dataListener;
	}
	
	class HttpThread<V> implements Runnable{

		public int type;
		public Class<T> t;
		
		public HttpThread(int type,Class<T> t) {
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			SpringProxy<T, V> proxy = new SpringProxy<T, V>(MediaType.APPLICATION_JSON);
			switch (type) {
			case 0:
				dataListener.onDataResult(proxy.postData(null, t, null));
				break;
//				dataListener.onDataResult(t);
			default:
				break;
			}
		}
		
	}
	
	public interface OnDataListener<T>{
		public void onDataResult(T t);
	}

}
