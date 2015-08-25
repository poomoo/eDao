package com.poomoo.edao.model;

/**
 * 
 * @ClassName UserRebateData
 * @Description TODO 用户返利字段
 * @author 李苜菲
 * @date 2015-8-24 下午3:12:34
 */
public class UserRebateData {
	String tel = "";
	String realName = "";
	String rebateGold = "";
	String payDt = "";

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRebateGold() {
		return this.rebateGold;
	}

	public void setRebateGold(String rebateGold) {
		this.rebateGold = rebateGold;
	}

	public String getPayDt() {
		return this.payDt;
	}

	public void setPayDt(String payDt) {
		this.payDt = payDt;
	}
}
