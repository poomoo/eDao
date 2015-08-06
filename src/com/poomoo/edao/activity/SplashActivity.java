package com.poomoo.edao.activity;

import android.content.Context;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		setImmerseLayout();
		init();
		sp = getSharedPreferences("index", Context.MODE_PRIVATE);
		editor = sp.edit();
		guide = sp.getString("guide", "");
		index = sp.getString("index", "");

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
			}

		}, SPLASH_DISPLAY_LENGHT);
	}

	private void init() {
		// TODO 自动生成的方法存根
		imageView = (ImageView)findViewById(R.id.splash_loading_item);
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
//				overridePendingTransition(R.anim.push_left_in,
//						R.anim.push_left_out);
//				SplashActivity.this.finish();
			}
		});
		imageView.setAnimation(translate);
	}
}
