package com.huayu.bracelet.vo;

public class WeeKAvgStep extends MessageVo<WeeKAvgStep>{

	private boolean status;//同步状态
	private String stepavg;//上周平均步数
	public String getStepavg() {
		return stepavg;
	}
	public void setStepavg(String stepavg) {
		this.stepavg = stepavg;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
