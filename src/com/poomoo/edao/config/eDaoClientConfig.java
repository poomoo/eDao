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
	public static final String BaseRemoteUrl = "http://www.leyidao.com/yidao/app/";// 远程
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
	public static final String moreBalance = "可提现金额不足";
	public static final String timeIllegal = "结束日期不能小于起始日期";
	public static final String save2code = "二维码已经保存到:";

	// 更新用户信息
	public static final int freshFlag = 1;// 刷新UI标志

	// 加解密公钥
	public static final String key = "leyidaoB1AF"; // B1AF本机MD5值最后两位

	// 关于我们
	public static String tel = "", web = "", weibo = "", weixin = "";

	public static final String[] store_class = { "金银首饰", "酒店娱乐", "餐饮美食", "服装鞋类", "生活超市", "旅游度假", "美容保健", "广告印刷", "数码电器",
			"皮具箱包", "酒类服务", "户外摄影", "汽车服务", "教育培训", "农林牧副", "医药服务", "交通运输", "办公家居", "房产建材", "机械五金" };
	// 1金银首饰 2 酒店娱乐 3 餐饮美食 4 服装鞋类 5 生活超市 6 旅游度假 7 美容保健 8 宣传广告 9 数码电器
	// 10 皮具箱包 11 酒店服务 12 户外休闲 13 汽车服务 14 教育培训 15 农副产品 16 医药服务 17 交通运输 18 办公家具
	// 19 建房建材 20 机械设备
	public static String status = "2";// 订单类型 1-未支付 2-已支付 3-已删除
}
