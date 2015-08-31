package com.poomoo.edao.config;

public class eDaoClientConfig {
	/**
	 * 网络相关
	 */

	// IP http://www.yidao7.com/app/orders/wxPayCallBack.json
	public static final String BaseUrl = "http://192.168.0.112:8080/yidao/app/";
	public static final String url = BaseUrl + "call.htm";// 本地
	// public static final String wxReUrl = BaseUrl +
	// "orders/wxPayCallBack.json";
	public static final String wxReUrl = "http://www.yidao7.com/app/orders/wxPayCallBack.json";
	public static final String imageurl = BaseUrl + "user/uploadUserImage.json";

	public static final int timeout = 1 * 30 * 1000;// 网络通讯超时
	public static final int advTime = 1 * 5 * 1000;// 广告轮播时间
	// public static final String url =
	// "";//远程
	/* 提示信息 */
	public static final String checkNet = "请检查网络后重试";
	public static final String certificate = "请进行实名认证";
	public static final String notInstallWX = "请先安装微信,目前只支持微信支付";
	public static final String notDevelop = "开发中,敬请期待...";

	public static String tel = "", web = "", weibo = "", weixin = "";
}
