package com.poomoo.edao.model;

/**
 * 
 * @ClassName LoginResData
 * @Description TODO 登录返回信息
 * @author 李苜菲
 * @date 2015-8-12 上午11:39:34
 */
public class LoginResData {

	private String userId = "";
	private String realName = "";
	private String tel = "";
	private String type = "";
	private String realNameAuth = "";

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

	public String getRealNameAuth() {
		return this.realNameAuth;
	}

	public void setRealNameAuth(String realNameAuth) {
		this.realNameAuth = realNameAuth;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
