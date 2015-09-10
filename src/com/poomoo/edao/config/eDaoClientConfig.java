package com.poomoo.edao.config;

/**
 * 
 * @ClassName eDaoClientConfig
 * @Description TODO 配置文件
 * @author 李苜菲
 * @date 2015年9月10日 上午10:37:55
 */
public class eDaoClientConfig {
	// IP
	public static final String BaseLocalUrl = "http://192.168.0.112:8080/yidao/app/";// 本地
	public static final String BaseRemoteUrl = "http://www.yidao7.com/yidao/app/";// 远程
	public static final String url = BaseRemoteUrl + "call.htm";
	public static final String wxReUrl = BaseRemoteUrl + "orders/wxPayCallBack.json";
	public static final String imageurl = BaseRemoteUrl + "user/uploadUserImage.json";
	public static final String uploadStoreurl = BaseRemoteUrl + "shop/add.json";

	// 时间
	public static final int timeout = 1 * 30 * 1000;// 网络通讯超时
	public static final int advTime = 1 * 5 * 1000;// 广告轮播时间

	/* 提示信息 */
	public static final String checkNet = "请检查网络后重试";
	public static final String certificate = "请进行实名认证";
	public static final String notInstallWX = "请先安装微信,目前只支持微信支付";
	public static final String notDevelop = "开发中,敬请期待...";
	public static final String balanceIsNotEnough = "余额不足";
	public static final String less500 = "不能低于500";
	public static final String more5000 = "不能超过5000";
	public static final String timeIllegal = "结束日期不能小于起始日期";

	// 更新用户信息
	public static final int freshFlag = 1;// 刷新UI标志

	// 关于我们
	public static String tel = "", web = "", weibo = "", weixin = "";
}
