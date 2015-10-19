package com.huayu.bracelet.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.http.MediaType;

import android.os.Handler;

public class HttpUtil<T> {

	public  Handler handler = new Handler();
	public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
	private Class<T> t;
	private OnDataListener<T> dataListener;
	private static final int post = 0;
	private int get = 1;
	
	public HttpUtil(Class<T> t) {
		// TODO Auto-generated constructor stub
		this.t= t;
	}
	
	
	public void login(){
//		fixedThreadPool.execute(new HttpThread(0));
//		new HttpThread<V>(0);
	}
	
	public void stop(){
		handler.removeCallbacksAndMessages(null);
	}
	
	public void setDataListener(OnDataListener<T> dataListener){
		this.dataListener = dataListener;
	}
	
	class HttpThread<V> implements Runnable{

		public int type;
		private  V v;
		
		public HttpThread(int type,V v) {
			// TODO Auto-generated constructor stub
			this.type = type;
			this.v = v;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			SpringProxy<T, V> proxy = new SpringProxy<T, V>(MediaType.APPLICATION_JSON);
			T result = proxy.postData(null, t, v);
			excuteResult(result);
		}
		
		private void excuteResult(final T result){
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					switch (type) {
					case post:
						dataListener.onDataResult(result);
						break;
//						dataListener.onDataResult(t);
					default:
						break;
					}
				}
			});
		
		}
	}
	
	public interface OnDataListener<T>{
		public void onDataResult(T t);
	}

}
