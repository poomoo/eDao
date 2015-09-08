package com.poomoo.edao.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.service.Get_UserInfo_Service;

public class SplashActivity extends BaseActivity {
	private final int SPLASH_DISPLAY_LENGHT = 3000;

	private ImageView imageView;

	private static String DB_PATH = "/data/data/com.poomoo.edao/databases/";
	private static String DB_NAME = "eDao.db";

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	private SharedPreferences loginsp;
	private eDaoClientApplication application = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		setImmerseLayout();
		application = (eDaoClientApplication) getApplication();
		init();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
			}

		}, SPLASH_DISPLAY_LENGHT);
	}

	private void init() {
		// TODO 自动生成的方法存根
		// 导入数据库文件
		importDB();
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener);
		initLocation();
		mLocationClient.start();
		imageView = (ImageView) findViewById(R.id.splash_loading_item);
		Animation translate = AnimationUtils.loadAnimation(this,
				R.anim.splash_loading);
		translate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				loginsp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
				if (loginsp.getBoolean("isLogin", false)) {
					application.setRealName(loginsp.getString("realName", ""));
					application.setTel(loginsp.getString("tel", ""));
					application.setUserId(loginsp.getString("userId", ""));
					application.setType(loginsp.getString("type", ""));
					application.setRealNameAuth(loginsp.getString(
							"realNameAuth", ""));
					application.setPayPwdValue(loginsp.getString("payPwdValue",
							""));
					application.setJoinType(loginsp.getString("joinType", ""));
					application.setJoinStatus(loginsp.getString("joinStatus",
							""));
					openActivity(NavigationActivity.class);
					startService(new Intent(SplashActivity.this,
							Get_UserInfo_Service.class));
				} else
					openActivity(LoginActivity.class);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
				SplashActivity.this.finish();
			}
		});
		imageView.setAnimation(translate);
	}

	private void importDB() {
		// TODO 自动生成的方法存根
		try {
			// 获得.db文件的绝对路径
			String databaseFilename = DB_PATH + DB_NAME;
			File dir = new File(DB_PATH);
			// 如果目录不存在，创建这个目录
			if (!dir.exists())
				dir.mkdir();
			System.out.println();
			boolean isExists = (new File(databaseFilename)).exists();
			System.out.println("isExists:" + isExists);
			// 如果在目录中不存在 .db文件，则从res\assets目录中复制这个文件到该目录
			if (!isExists) {
				System.out.println("文件不存在");
				// 获得封装.db文件的InputStream对象
				InputStream is = getAssets().open(DB_NAME);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[7168];
				int count = 0;
				// 开始复制.db文件
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			System.out.println("导入数据库文件结束");
		} catch (Exception e) {
		}
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系，
		int span = 1 * 10 * 1000;

		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		mLocationClient.setLocOption(option);
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			application.setCurProvince(location.getProvince());
			application.setCurCity(location.getCity());
			application.setCurArea(location.getDistrict());
			application.setCurlongitude(location.getLongitude());
			application.setCurlatitude(location.getLatitude());
			mLocationClient.unRegisterLocationListener(myListener);
		}
	}
}
