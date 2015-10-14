package com.poomoo.edao.application;

import org.litepal.LitePalApplication;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.poomoo.edao.R;
import com.poomoo.edao.crashhandler.CrashHandler;

public class eDaoClientApplication extends LitePalApplication {
	// 定位
	private String curProvince = "";
	private String curCity = "";
	private String curArea = "";

	// 提现
	private String handlingFee = "";// 提现手续费
	private String covertMinFee = "";// 提现下限
	private String corvertMaxFee = "";// 提现上限
	private String canCovertFee = "";// 可提现金额

	// 用户信息
	private String userId = "";
	private String realName = "";
	private String tel = "";
	private String type = "";// 用户类型 1普通会员，2：加盟会员
	private String realNameAuth = "";// 0默认未认证，1认证通过，2认证未通过
	private String idCardNum = "";// 身份证编号
	private String quickmarkPic = "";// 二维码
	private String bankProvince = "";// 开户省
	private String bankCity = "";// 开户市
	private String bankName = "";// 开户行名称
	private String bankCardId = "";// 银行卡号
	private String payPwdValue = "";// 是否设置过支付密码
	private String joinType = "";// 加盟类型 1-加盟商 2-经销商 3-合作商
	private String joinStatus = "";// 加盟审核 0,"默认值，未审核" 1,"审核通过" 2,"审核未通过"
	private String shopIsExists = "";// true-店铺已经添加
	private int totalEb = 0;// 总的意币
	private int totalGold = 0;// 总金币
	private int totalIntegral = 0;// 总的积分
	private double curlongitude = 0;
	private double curlatitude = 0;

	public String getCurProvince() {
		return this.curProvince;
	}

	public void setCurProvince(String curProvince) {
		this.curProvince = curProvince;
	}

	public String getCurCity() {
		return this.curCity;
	}

	public void setCurCity(String curCity) {
		this.curCity = curCity;
	}

	public String getCurArea() {
		return this.curArea;
	}

	public void setCurArea(String curArea) {
		this.curArea = curArea;
	}

	public String getHandlingFee() {
		return this.handlingFee;
	}

	public void setHandlingFee(String handlingFee) {
		this.handlingFee = handlingFee;
	}

	public String getCovertMinFee() {
		return this.covertMinFee;
	}

	public void setCovertMinFee(String covertMinFee) {
		this.covertMinFee = covertMinFee;
	}

	public String getCorvertMaxFee() {
		return this.corvertMaxFee;
	}

	public void setCorvertMaxFee(String corvertMaxFee) {
		this.corvertMaxFee = corvertMaxFee;
	}

	public String getCanCovertFee() {
		return canCovertFee;
	}

	public void setCanCovertFee(String canCovertFee) {
		this.canCovertFee = canCovertFee;
	}

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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRealNameAuth() {
		return this.realNameAuth;
	}

	public void setRealNameAuth(String realNameAuth) {
		this.realNameAuth = realNameAuth;
	}

	public String getIdCardNum() {
		return this.idCardNum;
	}

	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}

	public String getQuickmarkPic() {
		return this.quickmarkPic;
	}

	public void setQuickmarkPic(String quickmarkPic) {
		this.quickmarkPic = quickmarkPic;
	}

	public String getBankProvince() {
		return this.bankProvince;
	}

	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public String getBankCity() {
		return this.bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCardId() {
		return this.bankCardId;
	}

	public void setBankCardId(String bankCardId) {
		this.bankCardId = bankCardId;
	}

	public String getPayPwdValue() {
		return this.payPwdValue;
	}

	public void setPayPwdValue(String payPwdValue) {
		this.payPwdValue = payPwdValue;
	}

	public String getJoinType() {
		return this.joinType;
	}

	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}

	public String getJoinStatus() {
		return this.joinStatus;
	}

	public void setJoinStatus(String joinStatus) {
		this.joinStatus = joinStatus;
	}

	public String getShopIsExists() {
		return shopIsExists;
	}

	public void setShopIsExists(String shopIsExists) {
		this.shopIsExists = shopIsExists;
	}

	public int getTotalEb() {
		return this.totalEb;
	}

	public void setTotalEb(int totalEb) {
		this.totalEb = totalEb;
	}

	public int getTotalGold() {
		return this.totalGold;
	}

	public void setTotalGold(int totalGold) {
		this.totalGold = totalGold;
	}

	public int getTotalIntegral() {
		return this.totalIntegral;
	}

	public void setTotalIntegral(int totalIntegral) {
		this.totalIntegral = totalIntegral;
	}

	public double getCurlongitude() {
		return this.curlongitude;
	}

	public void setCurlongitude(double curlongitude) {
		this.curlongitude = curlongitude;
	}

	public double getCurlatitude() {
		return this.curlatitude;
	}

	public void setCurlatitude(double curlatitude) {
		this.curlatitude = curlatitude;
	}

	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();

		// 百度地图初始化
		SDKInitializer.initialize(getApplicationContext());

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher)
				.cacheInMemory(true).cacheOnDisk(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions).memoryCache(new WeakMemoryCache())
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	 
		 CrashHandler crashHandler = CrashHandler.getInstance();    
		 // 注册crashHandler    
		 crashHandler.init(getApplicationContext());
	}

}
