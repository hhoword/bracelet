package com.huayu.bracelet.http;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.huayu.bracelet.activity.IOnDataListener;
import com.huayu.bracelet.vo.ItemEntity;
import com.huayu.bracelet.vo.MessageVo;

import android.os.Handler;

public class HttpUtil {

	public  Handler handler = new Handler();
	public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
	private static final int post = 0;
	private static final int get = 1;

	public HttpUtil() {
		// TODO Auto-generated constructor stub
	}


	public void login(){
		//		fixedThreadPool.execute(new HttpThread(0));
		//		new HttpThread<V>(0);
	}

	public void getFriendCircle(String id,String index,String pagesize,
			IOnDataListener<MessageVo<ItemEntity>> dataListener){
		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
		data.add("id", id);
		HttpThread<MessageVo<ItemEntity>, MultiValueMap<String, String>> httpThread = 
				new HttpThread<MessageVo<ItemEntity>, MultiValueMap<String, String>>(0, data);
		httpThread.setDataListener(dataListener);
		fixedThreadPool.execute(httpThread);
	}

	public void stop(){
		handler.removeCallbacksAndMessages(null);
	}


	class HttpThread<T,V> implements Runnable{

		public int type;
		private  V v;
		private Class<T> t;
		private IOnDataListener<T> dataListener;

		@SuppressWarnings("unchecked")
		public HttpThread(int type,V v) {
			// TODO Auto-generated constructor stub
			this.type = type;
			this.t = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
			this.v = v;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//new MediaType("application","x-www-form-urlencoded");
			SpringProxy<T, V> proxy = new SpringProxy<T, V>(MediaType.MULTIPART_FORM_DATA);
			T result = null;
			switch (type) {
			case post:
				result = proxy.postData(null, t, v);
				break;
			case get:

				break;
			default:
				break;
			}
			excuteResult(result);
		}

		private void excuteResult(final T result){
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					dataListener.onDataResult(result);
				}
			});

		}

		public void setDataListener(IOnDataListener<T> dataListener){
			this.dataListener = dataListener;
		}
	}


}
