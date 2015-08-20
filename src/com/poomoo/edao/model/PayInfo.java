package com.poomoo.edao.model;

/**
 * 
 * @ClassName PayInfo
 * @Description TODO 支付信息
 * @author 李苜菲
 * @date 2015-8-20 上午11:13:26
 */
public class PayInfo {
	private String userId = "";
	private String realName = "";
	private String tel = "";

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
