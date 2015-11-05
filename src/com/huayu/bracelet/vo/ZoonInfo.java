package com.huayu.bracelet.vo;

import java.util.List;

public class ZoonInfo extends MessageVo<ZoonInfo>{
	private int id;
	private int uid;
	private List<String> img_url;
	private String text;
	private String datatime;
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
	public List<String> getImg_url() {
		return img_url;
	}
	public void setImg_url(List<String> img_url) {
		this.img_url = img_url;
	}
	
	
}
