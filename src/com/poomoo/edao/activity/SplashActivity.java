package com.poomoo.edao.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.poomoo.edao.R;

public class SplashActivity extends BaseActivity {
	private final int SPLASH_DISPLAY_LENGHT = 3000;

	private ImageView imageView;

	private SharedPreferences sp = null;
	private Editor editor = null;
	private String guide = "", index = "";

	private static String DB_PATH = "/data/data/com.poomoo.edao/databases/";
	private static String DB_NAME = "eDao.db";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		setImmerseLayout();
		init();
		// sp = getSharedPreferences("index", Context.MODE_PRIVATE);
		// editor = sp.edit();
		// guide = sp.getString("guide", "");
		// index = sp.getString("index", "");

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
			boolean isExists=(new File(databaseFilename)).exists();
			System.out.println("isExists:"+isExists);
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
}
