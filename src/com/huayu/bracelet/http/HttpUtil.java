package com.huayu.bracelet.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.http.MediaType;

import android.os.Handler;

public class HttpUtil {

	public  Handler handler = new Handler();
	public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
	
	public HttpUtil() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void login(){
//		fixedThreadPool.execute(new HttpRunnable(0));
	}
	
	public void stop(){
		handler.removeCallbacksAndMessages(null);
	}
	
	class HttpRunnable<T,V> implements Runnable{

		public int type;
		public Class<T> t;
		
		public HttpRunnable(int type,Class<T> t) {
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			SpringProxy<T, V> proxy = new SpringProxy<T, V>(MediaType.APPLICATION_JSON);
			switch (type) {
			case 0:
				proxy.postData(null, t, null);
				break;

			default:
				break;
			}
		}
		
	}
	
	public interface OnDataListener<T>{
		public void onDataResult(T t);
	}

}
