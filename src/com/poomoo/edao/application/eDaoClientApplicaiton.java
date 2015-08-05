package com.poomoo.edao.application;

import org.litepal.LitePalApplication;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.poomoo.edao.R;

public class eDaoClientApplicaiton extends LitePalApplication {

	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();
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
