package com.huayu.bracelet.vo;

public class UserData extends MessageVo<UserData> {

	private UserInfo userinfo;
	private int followerCount;
	private int concernedCount;
	public UserInfo getUserinfo() {
		return userinfo;
	}
	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
	}
	public int getFollowerCount() {
		return followerCount;
	}
	public void setFollowerCount(int followerCount) {
		this.followerCount = followerCount;
	}
	public int getConcernedCount() {
		return concernedCount;
	}
	public void setConcernedCount(int concernedCount) {
		this.concernedCount = concernedCount;
	}
	
	
}
