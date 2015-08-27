package com.poomoo.edao.model;

/**
 * 
 * @ClassName Detail
 * @Description TODO 用户领取明细
 * @author 李苜菲
 * @date 2015-8-27 上午10:15:31
 */
public class DetailData {
	private String payDt = "";
	private String gotNum = "";
	private String totalNum = "";
	private String gotTime = "";

	public String getPayDt() {
		return this.payDt;
	}

	public void setPayDt(String payDt) {
		this.payDt = payDt;
	}

	public String getGotNum() {
		return this.gotNum;
	}

	public void setGotNum(String gotNum) {
		this.gotNum = gotNum;
	}

	public String getTotalNum() {
		return this.totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public String getGotTime() {
		return this.gotTime;
	}

	public void setGotTime(String gotTime) {
		this.gotTime = gotTime;
	}
}
