package com.poomoo.edao.application;

import org.litepal.LitePalApplication;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.poomoo.edao.R;

public class eDaoClientApplication extends LitePalApplication {
	private String realName = "";
	private String tel = "";
	private String userId = "";
	private String type = "";
	private String curProvince = "";
	private String curCity = "";
	private String curArea = "";
	private String realNameAuth = "";
	private String handlingFee = "";
	private String covertMinFee = "";
	private String corvertMaxFee = "";
	private String totalEb = "";
	private String bankName = "";
	private String bankCardId = "";

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

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public String getRealNameAuth() {
		return this.realNameAuth;
	}

	public void setRealNameAuth(String realNameAuth) {
		this.realNameAuth = realNameAuth;
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

	public String getTotalEb() {
		return this.totalEb;
	}

	public void setTotalEb(String totalEb) {
		this.totalEb = totalEb;
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

	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();

		// 百度地图初始化
		SDKInitializer.initialize(getApplicationContext());

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisk(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new WeakMemoryCache())
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

}
