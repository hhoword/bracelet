package com.huayu.bracelet.vo;

import java.util.List;

public class ListUserZoonInfo extends MessageVo<List<ListUserZoonInfo>>{

	private UserZoonInfo zooninfo;
	private int praisecount;
	private int commentcount;



	public UserZoonInfo getZooninfo() {
		return zooninfo;
	}

	public void setZooninfo(UserZoonInfo zooninfo) {
		this.zooninfo = zooninfo;
	}

	public int getPraisecount() {
		return praisecount;
	}

	public void setPraisecount(int praisecount) {
		this.praisecount = praisecount;
	}

	public int getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(int commentcount) {
		this.commentcount = commentcount;
	}
	
}
