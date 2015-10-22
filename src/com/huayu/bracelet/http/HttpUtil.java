package com.huayu.bracelet.http;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.huayu.bracelet.activity.IOnDataListener;
import com.huayu.bracelet.vo.ItemEntity;
import com.huayu.bracelet.vo.MessageVo;
import com.huayu.bracelet.vo.UserInfo;

import android.os.Handler;

public class HttpUtil {

	public  Handler handler = new Handler();
	public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
	private static final int post = 0;
	private static final int get = 1;
	private String url = "http://192.168.0.109:9999";

	public HttpUtil() {
		// TODO Auto-generated constructor stub
	}


	public void login(String username ,String pwd,
			IOnDataListener<MessageVo> dataListener){
		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
		data.add("username", username);
		data.add("pwd", pwd);
		HttpThread<MessageVo, MultiValueMap<String, String>> httpThread = 
				new HttpThread<MessageVo, MultiValueMap<String, String>>(
						url+"/User/Login",post, data,MessageVo.class);
//		GenericCollectionTypeResolver.getMapValueType((MessageVo<UserInfo>).getClass());
//		GenericTypeResolver.resolveTypeArgument((MessageVo<UserInfo>).getClass());
		httpThread.setDataListener(dataListener);
		fixedThreadPool.execute(httpThread);
	}

	/*public void getFriendCircle(String id,String index,String pagesize,
			IOnDataListener<MessageVo<ItemEntity>> dataListener){
		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
		data.add("id", id);
		HttpThread<MessageVo<ItemEntity>, MultiValueMap<String, String>> httpThread = 
				new HttpThread<MessageVo<ItemEntity>, MultiValueMap<String, String>>("",0, data);
		httpThread.setDataListener(dataListener);
		fixedThreadPool.execute(httpThread);
	}*/

	public void stop(){
		handler.removeCallbacksAndMessages(null);
	}


	class HttpThread<T,V> implements Runnable{

		public int type;
		private  V v;
		private Class<T> clazz;
		private IOnDataListener<T> dataListener;
		private String url;

		@SuppressWarnings("unchecked")
		public HttpThread(String url,int type,V v,Class<T> clazz ) {
			// TODO Auto-generated constructor stub
			this.type = type;
//			this.clazz =  (Class<T>) ((ParameterizedType) super.getClass()  
//	                .getGenericSuperclass()).getActualTypeArguments()[0];  
			this.clazz = clazz;
			this.v = v;
			this.url = url;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//new MediaType("application","x-www-form-urlencoded");
			SpringProxy<T, V> proxy = new SpringProxy<T, V>(MediaType.MULTIPART_FORM_DATA);
			T result = null;
			switch (type) {
			case post:
				result = proxy.postData(url, clazz, v);
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
