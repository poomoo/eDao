package com.poomoo.edao.application;

import org.litepal.LitePalApplication;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.poomoo.edao.R;

public class eDaoClientApplicaiton extends LitePalApplication {
	private String realName = "";
	private String tel = "";
	private String userId = "";
	private String type = "";
	private String curProvince = "";
	private String curCity = "";

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
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

}
