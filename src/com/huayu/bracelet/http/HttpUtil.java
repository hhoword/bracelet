package com.huayu.bracelet.http;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import android.os.Handler;

import com.huayu.bracelet.activity.IOnDataListener;
import com.huayu.bracelet.vo.DeviceStepInfo;
import com.huayu.bracelet.vo.ListUserZoonInfo;
import com.huayu.bracelet.vo.UpdateInfo;
import com.huayu.bracelet.vo.UserData;
import com.huayu.bracelet.vo.WeeKAvgStep;
import com.huayu.bracelet.vo.ZoonInfo;

public class HttpUtil {

	public  Handler handler = new Handler();
	public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
	private static final int post = 0;
	private static final int get = 1;
//	public static final String url = "http://192.168.0.109:54242";
	public static final String url = "http://203.195.137.93:54242";
//	private String url2 = "http://14.23.85.254:9000/my1";

	public HttpUtil() {
		// TODO Auto-generated constructor stub
	}


	public void login(String username ,String pwd,
			IOnDataListener<UserData> iOnDataListener){
		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
		data.add("username", username);
		data.add("pwd", pwd);
		HttpThread<UserData, MultiValueMap<String, String>> httpThread = 
				new HttpThread<UserData, MultiValueMap<String, String>>(
						url+"/User/Login",post, data,UserData.class,MediaType.MULTIPART_FORM_DATA);
		httpThread.setDataListener(iOnDataListener);
		fixedThreadPool.execute(httpThread);
	}

	public void register(String username ,String pwd,
			IOnDataListener<UserData> iOnDataListener){
		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
		data.add("username", username);
		data.add("pwd", pwd);
		HttpThread<UserData, MultiValueMap<String, String>> httpThread = 
				new HttpThread<UserData, MultiValueMap<String, String>>(
						url+"User/Register",post, data,UserData.class,MediaType.MULTIPART_FORM_DATA);
		httpThread.setDataListener(iOnDataListener);
		fixedThreadPool.execute(httpThread);
	}
	
	public void postStepInfo(List<DeviceStepInfo> deviceStepInfos,IOnDataListener<WeeKAvgStep> dataListener){
		HttpThread<WeeKAvgStep, List<DeviceStepInfo>> httpThread = new HttpThread<WeeKAvgStep,
				List<DeviceStepInfo>>(url+"/Step/UpdateStep", post, deviceStepInfos, WeeKAvgStep.class,
						MediaType.APPLICATION_JSON);
		httpThread.setDataListener(dataListener);
		fixedThreadPool.execute(httpThread);
	}
	
	public void getFriendCircle(String id,String index,String pagesize,
			IOnDataListener<ListUserZoonInfo> dataListener){
		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
		data.add("userid", id);
		data.add("page", index);
		data.add("pagesize", pagesize);
		HttpThread<ListUserZoonInfo, MultiValueMap<String, String>> httpThread = 
				new HttpThread<ListUserZoonInfo, MultiValueMap<String, String>>(
						url+"/UserZoon/GetZoonByPage",post, data,ListUserZoonInfo.class,MediaType.MULTIPART_FORM_DATA);
		httpThread.setDataListener(dataListener);
		fixedThreadPool.execute(httpThread);
	}
	
	
	public void postFriendTalk(String id,String text,List<String> files,
			IOnDataListener<ZoonInfo> dataListener){
		MultiValueMap<String, Object> data = new LinkedMultiValueMap<String, Object>();
		data.add("uid", id);
		data.add("text", text);
		int size = files.size();
		for(int i = 0 ; i<size ; i++){
			Resource resource = new FileSystemResource(files.get(i));
			data.add("pic"+i, resource);
		}
		HttpThread<ZoonInfo, MultiValueMap<String, Object>> httpThread = 
				new HttpThread<ZoonInfo, MultiValueMap<String, Object>>(
						url+"/UserZoon/Add",post, data,ZoonInfo.class,MediaType.MULTIPART_FORM_DATA);
		httpThread.setDataListener(dataListener);
		fixedThreadPool.execute(httpThread);
	}
	
	
	public void getCheckUpdate(IOnDataListener<UpdateInfo> dataListener){
		HttpThread<UpdateInfo, String> httpThread = new HttpThread<UpdateInfo, String>(
						url+"/CheckUpdate",get, null,UpdateInfo.class,MediaType.TEXT_PLAIN);
		httpThread.setDataListener(dataListener);
		fixedThreadPool.execute(httpThread);
	}
	
	/*public void test(String username ,String pwd,
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
		private MediaType mediaType;

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
				result = proxy.getData(url, clazz, v);
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
