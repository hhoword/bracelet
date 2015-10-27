package com.huayu.bracelet.vo;

import java.util.List;

public class UserZoonInfo {

	private int id;	//Long	
	private int uid;//	Long	
	private List<String> img_url;//	String[]	动态图片地址
	private String text;	//String	动态内容
	private String datatime;//	Datetime	发表时间
	private String username; //	string	用户名（登陆那个）
	private String user_screenname;//	string	昵称
	private String user_img_url	;//string	用户头像url
	private int user_level;//	int	用户级别
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public List<String> getImg_url() {
		return img_url;
	}
	public void setImg_url(List<String> img_url) {
		this.img_url = img_url;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getDatatime() {
		return datatime;
	}
	public void setDatatime(String datatime) {
		this.datatime = datatime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUser_screenname() {
		return user_screenname;
	}
	public void setUser_screenname(String user_screenname) {
		this.user_screenname = user_screenname;
	}
	public String getUser_img_url() {
		return user_img_url;
	}
	public void setUser_img_url(String user_img_url) {
		this.user_img_url = user_img_url;
	}
	public int getUser_level() {
		return user_level;
	}
	public void setUser_level(int user_level) {
		this.user_level = user_level;
	}
	
	
}
