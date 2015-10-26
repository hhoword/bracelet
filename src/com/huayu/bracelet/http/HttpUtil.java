package com.huayu.bracelet.http;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import android.os.Handler;

import com.huayu.bracelet.activity.IOnDataListener;
import com.huayu.bracelet.view.UserInfo;
import com.huayu.bracelet.vo.ItemEntity;

public class HttpUtil {

	public  Handler handler = new Handler();
	public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
	private static final int post = 0;
	private static final int get = 1;
	private String url = "http://192.168.0.109:9999";
	private String url2 = "http://14.23.85.254:9000/my1";

	public HttpUtil() {
		// TODO Auto-generated constructor stub
	}


	public void login(String username ,String pwd,
			IOnDataListener<com.huayu.bracelet.vo.UserInfo> iOnDataListener){
		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
		data.add("username", username);
		data.add("pwd", pwd);
		HttpThread<com.huayu.bracelet.vo.UserInfo, MultiValueMap<String, String>> httpThread = 
				new HttpThread<com.huayu.bracelet.vo.UserInfo, MultiValueMap<String, String>>(
						url+"/User/Login",post, data,com.huayu.bracelet.vo.UserInfo.class,MediaType.MULTIPART_FORM_DATA);
		httpThread.setDataListener(iOnDataListener);
		fixedThreadPool.execute(httpThread);
	}

	public void getFriendCircle(String id,String index,String pagesize,
			IOnDataListener<ItemEntity> dataListener){
		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
		data.add("id", id);
		HttpThread<ItemEntity, MultiValueMap<String, String>> httpThread = 
				new HttpThread<ItemEntity, MultiValueMap<String, String>>(
						"",0, data,ItemEntity.class,MediaType.MULTIPART_FORM_DATA);
		httpThread.setDataListener(dataListener);
		fixedThreadPool.execute(httpThread);
	}
	
	public void test(String username ,String pwd,
			IOnDataListener<UserInfo> dataListener){
		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
		data.add("mobile", username);
		data.add("password", pwd);
		//?mobile="+username+"&password="+pwd
		HttpThread<UserInfo, MultiValueMap<String, String>> httpThread = 
				new HttpThread<UserInfo, MultiValueMap<String, String>>(
						url2+"/mobile/userLogin.do",post, data,UserInfo.class,
						MediaType.MULTIPART_FORM_DATA);
		httpThread.setDataListener(dataListener);
		fixedThreadPool.execute(httpThread);
	}

	public void stop(){
		handler.removeCallbacksAndMessages(null);
	}


	class HttpThread<T,V> implements Runnable{

		public int type;
		private  V v;
		private Class<T> clazz;
		private IOnDataListener<T> dataListener;
		private String url;
		private MediaType mediaType;

		@SuppressWarnings("unchecked")
		public HttpThread(String url,int type,V v,Class<T> clazz, MediaType mediaType) {
			// TODO Auto-generated constructor stub
			this.type = type;
//			this.clazz =  (Class<T>) ((ParameterizedType) super.getClass()  
//	                .getGenericSuperclass()).getActualTypeArguments()[0];  
			this.clazz = clazz;
			this.v = v;
			this.url = url;
			this.mediaType = mediaType;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//new MediaType("application","x-www-form-urlencoded");
			SpringProxy<T, V> proxy = new SpringProxy<T, V>(mediaType);
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

	abstract class Foo<X>
	{
	    public Class<X> getTClass()
	    {
	    	  ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();  
	    	  Class <X>  tClass=(Class<X>) pt.getActualTypeArguments()[0];
	        return tClass;
	    }
	}
	
}
