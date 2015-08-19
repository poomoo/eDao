package com.poomoo.edao.config;

public class eDaoClientConfig {
	/**
	 * 网络相关
	 */

	// IP
	public static final String url = "http://192.168.0.112:8080/yidao/app/call.htm";// 本地
	public static final String imageurl = "http://192.168.0.112:8080/yidao/app/user/uploadUserImage.json";

	public static final int timeout = 1 * 30 * 1000;
	// public static final String url =
	// "";//远程
	public static final String checkNet = "请检查网络后重试";
	public static final String certificate = "请进行实名认证";
}
