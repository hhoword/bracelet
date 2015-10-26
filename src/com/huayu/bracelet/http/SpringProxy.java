package com.huayu.bracelet.http;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
/**
 * 服务器交互工具类，IOC模式
 * @author hho
 *2015-5-14
 * @param <T> 请求的类型
 * @param <V> 请求的参数
 */
public class SpringProxy<T,V> {

	public static RestTemplate restTemplate;
	private MediaType contentType;

	/**
	 * 设置连接的媒体类型
	 * @param contentType
	 */
	public SpringProxy(MediaType contentType) {
		// TODO Auto-generated constructor stub
		this.contentType = contentType;
	}

	public T getData(String url, Class<T> type, V values){
		try {
			HttpHeaders headers = headers(contentType, MediaType.APPLICATION_JSON);
			HttpEntity<V> entity = new HttpEntity<V>(values,headers);

			RestTemplate restTemplate =getRestTemplate();
			ResponseEntity<T> responseEntity = restTemplate.exchange(url,
					HttpMethod.GET, entity, type);
			return responseEntity.getBody();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}


	public T postData(String url, Class<T> type, V values){
		try {
			HttpHeaders headers = headers(contentType, MediaType.APPLICATION_JSON);
//			if (WorkDataCenter.mCsrftoken.length() > 0) {
//				headers.add("X-CSRFToken", WorkDataCenter.mCsrftoken);
//			}
			HttpEntity<V> entity = new HttpEntity<V>(values, headers);
			RestTemplate restTemplate = getRestTemplate();			 
			ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, type);
			
//			HttpHeaders headersBack = responseEntity.getHeaders();
//			List<String> cookies = headersBack.get("Set-Cookie");
//			if(cookies!=null){
//				for(String cookie : cookies){
//					for(String info :cookie.split(";")){
//						if(info.contains("csrftoken")){
//							WorkDataCenter.header+=info+";";
//							WorkDataCenter.mCsrftoken =  info.substring(10,info.length());
//						}else if(info.contains("sessionid")){
//							WorkDataCenter.header+=info+";";
//							WorkDataCenter.sessionid=info.substring(10,info.length());
//						}
//					}
//				}
//			}
			return responseEntity.getBody();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public T putData(String url, Class<T> type, V values){
		try {
			HttpHeaders headers = headers(contentType, MediaType.APPLICATION_JSON);
			HttpEntity<V> entity = new HttpEntity<V>(values,headers);
			RestTemplate restTemplate =getRestTemplate();
			ResponseEntity<T> responseEntity = restTemplate.exchange(url,
					HttpMethod.PUT, entity, type);
			return responseEntity.getBody();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
	
	
	
	/**
	 * 设置httpheader
	 * @param contentType
	 * @param acceptType
	 * @return
	 */
	public HttpHeaders headers(MediaType contentType, MediaType acceptType){
		HttpHeaders requestHeaders = new HttpHeaders();
		//设置传输媒体类型
		requestHeaders.setContentType(contentType);

//		requestHeaders.add("Content-Type", "charset=utf-8");
		requestHeaders.setContentEncoding(ContentCodingType.valueOf("utf-8"));

		//设置接收媒体类型
		List<MediaType> listtype = new ArrayList<MediaType>();
		listtype.add(MediaType.TEXT_PLAIN);
		listtype.add(acceptType);
		requestHeaders.setAccept(listtype);

		//设置接收字符集
		List<Charset> charset = new ArrayList<Charset>();
		charset.add(Charset.forName("utf-8"));
		requestHeaders.setAcceptCharset(charset);

		return requestHeaders;
	}

	private static synchronized RestTemplate getRestTemplate(){
		if(restTemplate == null){
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setReadTimeout(30000);
			requestFactory.setConnectTimeout(30000);
			restTemplate = new RestTemplate(true);
			restTemplate.setRequestFactory(requestFactory);
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
		}
		return restTemplate;
	}
}
