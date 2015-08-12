package com.poomoo.edao.model;

/**
 * 
 * @ClassName Response
 * @Description TODO 请求返回数据
 * @author 李苜菲
 * @date 2015-8-12 上午11:37:13
 */
public class ResponseData {
	// "rsCode":"1" 1:成功，-1：失败，-2：必要参数为空
	// "msg":"请求成功",
	// "jsonData":"请求的结果集，主要正对查询功能"
	private String jsonData = "";
	private String msg = "";
	private int rsCode = 0;

	public String getJsonData() {
		return this.jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getRsCode() {
		return this.rsCode;
	}

	public void setRsCode(int rsCode) {
		this.rsCode = rsCode;
	}
}
