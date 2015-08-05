package com.poomoo.edao.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;

import com.poomoo.edao.R;

public class SplashActivity extends BaseActivity {
	private final int SPLASH_DISPLAY_LENGHT = 3000;

	private SharedPreferences sp = null;
	private Editor editor = null;
	private String guide = "", index = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		setImmerseLayout();

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
}
