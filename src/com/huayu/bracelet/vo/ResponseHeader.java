/**
 * 文件名称 : ResponseHandler.java
 * <p>
 * 作者信息 : LIJUNJIE
 * <p>
 * 创建时间 : 2014年5月6日, 上午11:49:15
 * <p>
 * 版权声明 : Copyright (c) Hooray Ltd. All rights reserved
 * <p>
 * http返回包头类
 **/
package com.huayu.bracelet.vo;

public class ResponseHeader {
	int rc;
	String rm;
	String time;
	
    public int getRc() {
        return rc;
    }
    public void setRc(int rc) {
        this.rc = rc;
    }
    public String getRm() {
        return rm;
    }
    public void setRm(String rm) {
        this.rm = rm;
    }
    
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
    
    
}
